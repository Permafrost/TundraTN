package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-08-24 18:08:52 EST
// -----( ON-HOST: 172.16.189.132

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class queue

{
	// ---( internal utility methods )---

	final static queue _instance = new queue();

	static queue _newInstance() { return new queue(); }

	static queue _cast(Object o) { return (queue)o; }

	// ---( server methods )---




	public static final void each (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(each)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required queue
		// [i] field:0:required $service
		// [i] record:0:optional $pipeline
		// [i] field:0:optional $concurrency
		// [i] field:0:optional $ordered? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $retries
		// [i] field:0:optional $limit
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  String queue = IDataUtil.getString(cursor, "queue");
		  String service = IDataUtil.getString(cursor, "$service");
		  IData scope = IDataUtil.getIData(cursor, "$pipeline");
		  String sConcurrency = IDataUtil.getString(cursor, "$concurrency");
		  String sOrdered = IDataUtil.getString(cursor, "$ordered?");
		  String sRetries = IDataUtil.getString(cursor, "$retries");
		  String sLimit = IDataUtil.getString(cursor, "$limit");
		
		  int concurrency = 1;
		  if (sConcurrency != null) concurrency = Integer.parseInt(sConcurrency);
		
		  boolean ordered = false;
		  if (sOrdered != null) ordered = Boolean.parseBoolean(sOrdered);
		
		  int limit = 0;
		  if (sLimit != null) limit = Integer.parseInt(sLimit);
		
		  int retries = 0;
		  if (sRetries != null) retries = Integer.parseInt(sRetries);
		
		  each(queue, service, scope == null? pipeline : scope, concurrency, limit, retries, ordered);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	protected final static String EXECUTE_TASK_SERVICE_NAME = "tundra.tn.support.queue.task:execute";
	protected final static com.wm.lang.ns.NSName EXECUTE_TASK_SERVICE = com.wm.lang.ns.NSName.create(EXECUTE_TASK_SERVICE_NAME);
	protected final static String DELIVER_BATCH_SERVICE_NAME = "wm.tn.queuing:deliverBatch";
	protected static final String DELIVERY_JOB_UPDATE_SQL_STATEMENT = "deliver.job.update";
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// if concurrency > 1, tasks will be processed by a thread pool whose size is equal to the desired concurrency,
	// otherwise they will be processed on the current thread
	public static void each(String queueName, String service, IData pipeline, int concurrency, int batchLimit, int retryLimit, boolean ordered) throws ServiceException {
	  try {
	    if (batchLimit <= 0) batchLimit = Integer.MAX_VALUE;
	
	    com.wm.app.tn.delivery.DeliveryQueue queue = com.wm.app.tn.db.QueueOperations.selectByName(queueName);
	    if (queue == null) throw new ServiceException("Queue '" + queueName + "' does not exist");
	
	    if (concurrency <= 1) {
	      eachDirect(queue, EXECUTE_TASK_SERVICE, service, pipeline, batchLimit, retryLimit, ordered);
	    } else {
	      eachConcurrent(queue, EXECUTE_TASK_SERVICE, service, pipeline, concurrency, batchLimit, retryLimit);
	    }
	  } catch (java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline
	// on the current thread
	protected static void eachDirect(com.wm.app.tn.delivery.DeliveryQueue queue, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int batchLimit, int retryLimit, boolean ordered) throws ServiceException {
	  boolean invokedByTradingNetworks = invokedByTradingNetworks();
	  int total = 0;
	
	  try {
	    while(total < batchLimit) {
	      if (!invokedByTradingNetworks || queue.isEnabled() || queue.isDraining()) {
	        com.wm.app.tn.delivery.GuaranteedJob task = com.wm.app.tn.db.DeliveryStore.dequeueOldestJob(queue.getQueueName());
	        if (task == null) {
	          break; // if there are no more tasks, then exit
	        } else {
	          IData output = com.wm.app.b2b.server.Service.doInvoke(executeTaskService, createTaskInputPipeline(task, service, pipeline, queue.getQueueName(), queue.getQueueType()));
	          total++;
	
	          if (retry(task, retryLimit, ordered)) { 
	            break; // if the task needs to be retried and the queue is being processed in-order, exit so as not to retry the same task immediately
	          }
	        }
	        if (invokedByTradingNetworks) queue = com.wm.app.tn.db.QueueOperations.selectByName(queue.getQueueName());
	      } else {
	        break; // if invoked by TN and queue is disabled or suspended, then exit
	      }
	    }
	  } catch (Exception ex) {
	    if (ex instanceof ServiceException) {
	      throw (ServiceException)ex;
	    } else {
	      throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	    }
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// tasks will be processed by a thread pool whose size is equal to the desired concurrency
	protected static void eachConcurrent(com.wm.app.tn.delivery.DeliveryQueue queue, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int concurrency, int batchLimit, int retryLimit) throws ServiceException {
	  boolean invokedByTradingNetworks = invokedByTradingNetworks();
	
	  // upper bound on number of tasks submitted to thread pool at any one time
	  int backlog = concurrency * 2;
	  int total = 0;
	
	  com.wm.app.b2b.server.Session session = com.wm.app.b2b.server.Service.getSession();
	  com.wm.app.b2b.server.InvokeState state = com.wm.app.b2b.server.InvokeState.getCurrentState();
	
	  java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(concurrency, new ServerThreadFactory(queue.getQueueName(), state));
	
	  java.util.Queue<java.util.concurrent.Future<IData>> futures = new java.util.LinkedList<java.util.concurrent.Future<IData>>();
	  java.util.List<Exception> exceptions = new java.util.ArrayList<Exception>();
	
	  try {
	    while(total < batchLimit) {
	      if (!invokedByTradingNetworks || queue.isEnabled() || queue.isDraining()) {
	        int size = futures.size();
	        if (size < backlog) {
	          // submit another queued task
	          com.wm.app.tn.delivery.GuaranteedJob task = com.wm.app.tn.db.DeliveryStore.dequeueOldestJob(queue.getQueueName());
	          if (task == null) {
	            if (size > 0) {
	              // wait for first thread to finish; once finished we'll loop again and see if there are now tasks on the queue
	              awaitOldest(futures, retryLimit);
	            } else {
	              // if all threads have finished and there are no more tasks, then exit
	              break;
	            }
	          } else {
	            futures.add(executor.submit(new CallableService(executeTaskService, session, createTaskInputPipeline(task, service, pipeline, queue.getQueueName(), queue.getQueueType()))));
	            total++;
	          }
	        } else {
	          // wait for first thread to finish
	          awaitOldest(futures, retryLimit);
	        }
	        if (invokedByTradingNetworks) queue = com.wm.app.tn.db.QueueOperations.selectByName(queue.getQueueName());
	      } else {
	        break; // if invoked by TN and queue is disabled or suspended, then exit
	      }
	    }
	  } catch (java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (com.wm.app.tn.delivery.DeliveryException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } finally {
	    executor.shutdown();
	    awaitAll(futures, retryLimit);
	  }
	}
	
	// waits for all futures in the given queue to complete
	protected static java.util.List<IData> awaitAll(java.util.Queue<java.util.concurrent.Future<IData>> futures, int retryLimit) {
	  java.util.List<IData> results = new java.util.ArrayList<IData>(futures.size());
	  while(futures.size() > 0) {
	    try {
	      results.add(awaitOldest(futures, retryLimit));
	    } catch (Exception ex) {
	      // ignore exceptions
	    }
	  }
	  return results;
	}
	
	// waits for the first/head future in the given queue to complete
	protected static IData awaitOldest(java.util.Queue<java.util.concurrent.Future<IData>> futures, int retryLimit) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  return await(futures.poll(), retryLimit);
	}
	
	// waits for the given future to complete
	protected static IData await(java.util.concurrent.Future<IData> future, int retryLimit) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  IData output = null;
	  try {
	    output = future.get();
	
	    if (output != null) {
	      IDataCursor cursor = output.getCursor();
	      Object task = IDataUtil.get(cursor, "task");
	      cursor.destroy();
	
	      if (task != null && task instanceof com.wm.app.tn.delivery.GuaranteedJob) retry((com.wm.app.tn.delivery.GuaranteedJob)task, retryLimit);
	    }
	  } catch (java.util.concurrent.ExecutionException ex) {
	    // ignore exceptions
	  } catch(InterruptedException ex) {
	    // ignore exceptions
	  }
	  return output;
	}
	
	// returns a new pipeline for the executeTaskService
	protected static IData createTaskInputPipeline(com.wm.app.tn.delivery.GuaranteedJob task, String service, IData pipeline, String queueName, String queueType) throws java.io.IOException {
	  IData output = IDataFactory.create();
	  IDataCursor cursor = output.getCursor();
	  IDataUtil.put(cursor, "task", task);
	  IDataUtil.put(cursor, "timeDequeued", System.currentTimeMillis());
	  IDataUtil.put(cursor, "$service", service);
	  IDataUtil.put(cursor, "$pipeline", IDataUtil.deepClone(pipeline));
	  IDataUtil.put(cursor, "queue", queueName);
	  IDataUtil.put(cursor, "queue.type", queueType);
	  cursor.destroy();
	
	  return output;
	}
	
	// returns true if the invocation callstack includes the WmTN/wm.tn.queuing:deliverBatch service
	protected static boolean invokedByTradingNetworks() {
	  java.util.Iterator iterator = com.wm.app.b2b.server.InvokeState.getCurrentState().getCallStack().iterator();
	  boolean result = false;
	  while(iterator.hasNext()) {
	    result = iterator.next().toString().equals(DELIVER_BATCH_SERVICE_NAME);
	    if (result) break;
	  }
	  return result;
	}
	
	// wraps a call to an IS service with a standard java.util.concurrent.callable interface, so that it can
	// be used by java.util.concurrent executors
	public static class CallableService implements java.util.concurrent.Callable<IData> {
	  protected com.wm.lang.ns.NSName service;
	  protected IData input;
	  protected com.wm.app.b2b.server.Session session;
	
	  public CallableService(String service, com.wm.app.b2b.server.Session session, IData input) {
	    this(com.wm.lang.ns.NSName.create(service), session, input);
	  }
	
	  public CallableService(com.wm.lang.ns.NSName service, com.wm.app.b2b.server.Session session, IData input) {
	    this.service = service;
	    this.input = input;
	    this.session = session;
	  }
	
	  public IData call() throws Exception {
	    return com.wm.app.b2b.server.Service.doInvoke(service, session, input);
	  }
	} 
	
	// a thread factory which creates IS threads with the given invoke state
	public static class ServerThreadFactory implements java.util.concurrent.ThreadFactory {
	  String queue;
	  protected com.wm.app.b2b.server.InvokeState state;
	  protected long count = 1;
	
	  public ServerThreadFactory(String queue, com.wm.app.b2b.server.InvokeState state) {
	    this.queue = queue;
	    this.state = state;
	  }
	
	  public Thread newThread(Runnable r) {
	    com.wm.app.b2b.server.ServerThread thread = new com.wm.app.b2b.server.ServerThread(r);
	    thread.setInvokeState(cloneInvokeStateWithStack());
	    thread.setName("TundraTN/Queue '" + queue + "' Thread#" + String.format("%03d", count++));
	    return thread;
	  }
	
	  protected com.wm.app.b2b.server.InvokeState cloneInvokeStateWithStack() {
	    com.wm.app.b2b.server.InvokeState outputState = (com.wm.app.b2b.server.InvokeState)state.clone();
	    java.util.Stack stack = (java.util.Stack)state.getCallStack().clone();
	    while(!stack.empty()) {
	      com.wm.lang.ns.NSService service = (com.wm.lang.ns.NSService)stack.remove(0);
	      outputState.pushService(service);
	    }
	    return outputState;
	  }
	}
	
	// re-enqueues the given job for delivery, unless it has reached its retry limit
	protected static boolean retry(com.wm.app.tn.delivery.GuaranteedJob job, int retryLimit) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  return retry(job, retryLimit, false);
	}
	
	// re-enqueues the given job for delivery, unless it has reached its retry limit
	protected static boolean retry(com.wm.app.tn.delivery.GuaranteedJob job, int retryLimit, boolean ordered) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  job = com.wm.app.tn.db.DeliveryStore.getAnyJob(job.getJobId(), true); // refetch job from DB
	  com.wm.app.tn.doc.BizDocEnvelope bizdoc = job.getBizDocEnvelope();
	
	  int jobLimit = job.getRetryLimit();
	  int retries = job.getRetries();
	  String status = job.getStatus();
	  String queueName = job.getQueueName();
	  com.wm.app.tn.delivery.DeliveryQueue queue = tundra.tn.queue.get(queueName);
	
	  boolean automaticRetry = jobLimit > 0;
	  if (automaticRetry) retryLimit = jobLimit; // honour the retry settings on the task
	  boolean exhausted = retries >= retryLimit;
	
	  boolean failed = (jobLimit > 0 && retries > 0 && status.equals("QUEUED")) || ((jobLimit <= 0 || exhausted) && status.equals("FAILED"));
	
	  if (failed) {
	    if (exhausted) {
	      if (ordered) {
	        // reset retries back to 0
	        job.setRetries(0);
	        job.setStatus(com.wm.app.tn.delivery.GuaranteedJob.QUEUED);
	        job.setDefaultServerId();
	        job.setTimeUpdated(job.getTimeCreated()); // force this job to the front of the queue
	        update(job);
	
	        // suspend the queue
	        tundra.tn.queue.suspend(queueName);
	
	        if (bizdoc != null) {
	          com.wm.app.tn.db.BizDocStore.changeStatus(bizdoc, "QUEUED", "SUSPENDED");
	          if (queue.getQueueType().equals("private")) {
	            log(bizdoc, "WARNING", "Delivery", "Suspended receiver's private queue '" + queueName + "'", "Ordered delivery of receiver's private queue '" + queueName + "' was suspended due to task exhaustion");
	          } else {
	            log(bizdoc, "WARNING", "Delivery", "Suspended public queue '" + queueName + "'", "Ordered delivery of public queue '" + queueName + "' was suspended due to task exhaustion");
	          }
	        }
	      }
	    } else if (automaticRetry) {
	      if (bizdoc != null) com.wm.app.tn.db.BizDocStore.changeStatus(bizdoc, "QUEUED", "REQUEUED");
	    } else {
	      job.retryFailed();
	      job.setStatus(com.wm.app.tn.delivery.GuaranteedJob.QUEUED);
	      job.setDefaultServerId();
	      if (ordered) job.setTimeUpdated(job.getTimeCreated()); // force this job to the front of the queue
	      update(job);
	
	      if (bizdoc != null) com.wm.app.tn.db.BizDocStore.changeStatus(bizdoc, "QUEUED", "REQUEUED");
	    }
	  }
	
	  return failed && ordered;
	}
	
	// saves the given job to the Trading Networks database
	protected static void update(com.wm.app.tn.delivery.GuaranteedJob job) throws java.sql.SQLException, java.io.IOException {
	  java.sql.Connection connection = null;
	  java.sql.PreparedStatement statement = null;
	
	  try {
	    connection = com.wm.app.tn.db.Datastore.getConnection();
	    statement = com.wm.app.tn.db.SQLStatements.prepareStatement(connection, DELIVERY_JOB_UPDATE_SQL_STATEMENT);
	    statement.clearParameters();
	
	    // instead of setting TimeUpdated to now, set it to the time in the job object
	    com.wm.app.tn.db.SQLWrappers.setTimestamp(statement, 1, new java.sql.Timestamp(job.getTimeUpdated()));
	
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 2, job.getStatus(), "DeliveryJob.JobStatus");
	    statement.setInt(3, job.getRetries());
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 4, job.getTransportStatus(), "DeliveryJob.TransportStatus");
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 5, job.getTransportStatusMessage(), "DeliveryJob.TransportStatusMessage");
	    statement.setInt(6, (int)job.getTransportTime());
	    com.wm.app.tn.db.SQLWrappers.setBinaryStream(statement, 7, job.getOutputData());
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 8, job.getServerId(), "DeliveryJob.ServerID");
	    com.wm.app.tn.db.SQLWrappers.setBinaryStream(statement, 9, job.getDBIData());
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 10, job.getQueueName(), "DeliveryQueue.QueueName");
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 11, job.getInvokeAsUser(), "DeliveryJob.UserName");
	    com.wm.app.tn.db.SQLWrappers.setCharString(statement, 12, job.getJobId());
	
	    statement.executeUpdate();
	    connection.commit();
	  } catch (java.sql.SQLException ex) {
	    connection = com.wm.app.tn.db.Datastore.handleSQLException(connection, ex);
	    throw ex;
	  } finally {
	    com.wm.app.tn.db.SQLStatements.releaseStatement(statement);
	    com.wm.app.tn.db.Datastore.releaseConnection(connection);     
	  }
	}
	
	protected static final String LOG_SERVICE_NAME = "tundra.tn:log";
	protected static final com.wm.lang.ns.NSName LOG_SERVICE = com.wm.lang.ns.NSName.create(LOG_SERVICE_NAME);
	
	protected static void log(com.wm.app.tn.doc.BizDocEnvelope bizdoc, String type, String klass, String summary, String message) throws ServiceException {
	  IData input = IDataFactory.create();
	  IDataCursor cursor = input.getCursor();
	  IDataUtil.put(cursor, "$bizdoc", bizdoc);
	  IDataUtil.put(cursor, "$type", type);
	  IDataUtil.put(cursor, "$class", klass);
	  IDataUtil.put(cursor, "$summary", summary);
	  IDataUtil.put(cursor, "$message", message);
	  cursor.destroy();
	
	  try {
	    Service.doInvoke(LOG_SERVICE, input);
	  } catch (ServiceException ex) {
	    throw ex;
	  } catch (Exception ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

