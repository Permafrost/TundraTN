package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-04-01 06:45:37 EST
// -----( ON-HOST: WIN-34RAS9HJLBT

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
		// [i] field:0:optional $ordered? {"false","true"}
		// [i] field:0:optional $suspend? {"false","true"}
		// [i] field:0:optional $retry.limit
		// [i] field:0:optional $retry.wait
		// [i] field:0:optional $retry.factor
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  String queue = IDataUtil.getString(cursor, "queue");
		  String service = IDataUtil.getString(cursor, "$service");
		  IData scope = IDataUtil.getIData(cursor, "$pipeline");
		  String sConcurrency = IDataUtil.getString(cursor, "$concurrency");
		  String sOrdered = IDataUtil.getString(cursor, "$ordered?");
		  String sSuspend = IDataUtil.getString(cursor, "$suspend?");
		  String sRetryLimit = IDataUtil.getString(cursor, "$retry.limit");
		  // support $retries for backwards-compatibility
		  if (sRetryLimit == null) sRetryLimit = IDataUtil.getString(cursor, "$retries");
		  String sRetryFactor = IDataUtil.getString(cursor, "$retry.factor");
		  String sRetryWait = IDataUtil.getString(cursor, "$retry.wait");
		
		  int concurrency = 1;
		  if (sConcurrency != null) concurrency = Integer.parseInt(sConcurrency);
		
		  boolean ordered = false;
		  if (sOrdered != null) ordered = Boolean.parseBoolean(sOrdered);
		
		  boolean suspend = false;
		  if (sSuspend != null) suspend = Boolean.parseBoolean(sSuspend);
		
		  int retryLimit = 0;
		  if (sRetryLimit != null) retryLimit = Integer.parseInt(sRetryLimit);
		
		  int retryFactor = 1;
		  if (sRetryFactor != null) retryFactor = Integer.parseInt(sRetryFactor);
		
		  int retryWait = 0;
		  if (sRetryWait != null) retryWait = Integer.parseInt(sRetryWait);
		
		  each(queue, service, scope == null? pipeline : scope, concurrency, retryLimit, retryFactor, retryWait, ordered, suspend);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	protected static final String EXECUTE_TASK_SERVICE_NAME = "tundra.tn.support.queue.task:execute";
	protected static final com.wm.lang.ns.NSName EXECUTE_TASK_SERVICE = com.wm.lang.ns.NSName.create(EXECUTE_TASK_SERVICE_NAME);
	protected static final String DELIVER_BATCH_SERVICE_NAME = "wm.tn.queuing:deliverBatch";
	protected static final String UPDATE_DELIVER_JOB = "deliver.job.update";
	protected static final String UPDATE_DELIVER_JOB_DELIVERING = "deliver.job.update.delivering";
	protected static final String UPDATE_DELIVER_JOB_RETRY_STRATEGY = "UPDATE DeliveryJob SET RetryLimit = ?, RetryFactor = ?, TimeToWait = ? WHERE JobID = ?";
	protected static final String SELECT_NEXT_DELIVER_JOB_ORDERED = "SELECT JobID FROM DeliveryJob WHERE QueueName = ? AND JobStatus = 'QUEUED' AND TimeCreated = (SELECT MIN(TimeCreated) FROM DeliveryJob WHERE QueueName = ? AND JobStatus = 'QUEUED') AND TimeUpdated <= ?";
	protected static final String SELECT_NEXT_DELIVER_JOB_UNORDERED = "SELECT JobID FROM DeliveryJob WHERE QueueName = ? AND JobStatus = 'QUEUED' AND TimeCreated = (SELECT MIN(TimeCreated) FROM DeliveryJob WHERE QueueName = ? AND JobStatus = 'QUEUED' AND TimeUpdated <= ?)";
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// if concurrency > 1, tasks will be processed by a thread pool whose size is equal to the desired concurrency,
	// otherwise they will be processed on the current thread
	public static void each(String queueName, String service, IData pipeline, int concurrency, int retryLimit, int retryFactor, int ttw, boolean ordered, boolean suspend) throws ServiceException {
	  try {
	    com.wm.app.tn.delivery.DeliveryQueue queue = com.wm.app.tn.db.QueueOperations.selectByName(queueName);
	    if (queue == null) throw new ServiceException("Queue '" + queueName + "' does not exist");
	
	    if (concurrency <= 1) {
	      eachDirect(queue, EXECUTE_TASK_SERVICE, service, pipeline, retryLimit, retryFactor, ttw, ordered, suspend);
	    } else {
	      eachConcurrent(queue, EXECUTE_TASK_SERVICE, service, pipeline, concurrency, retryLimit, retryFactor, ttw, ordered, suspend);
	    }
	  } catch (java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline
	// on the current thread
	protected static void eachDirect(com.wm.app.tn.delivery.DeliveryQueue queue, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int retryLimit, int retryFactor, int ttw, boolean ordered, boolean suspend) throws ServiceException {
	  boolean invokedByTradingNetworks = invokedByTradingNetworks();
	
	  try {
	    while(true) {
	      if (!invokedByTradingNetworks || queue.isEnabled() || queue.isDraining()) {
	        com.wm.app.tn.delivery.GuaranteedJob task = pop(queue.getQueueName(), ordered);
	        if (task == null) {
	          break; // if there are no more tasks, then exit
	        } else {
	        	updateRetryStrategy(task, retryLimit, retryFactor, ttw);
	          IData output = com.wm.app.b2b.server.Service.doInvoke(executeTaskService, createTaskInputPipeline(task, service, pipeline, queue.getQueueName(), queue.getQueueType()));
	        	retry(task, suspend);
	        }
	        if (invokedByTradingNetworks) queue = com.wm.app.tn.db.QueueOperations.selectByName(queue.getQueueName());
	      } else {
	        break; // if invoked by TN and queue is disabled or suspended, then exit
	      }
	    }
	  } catch (Exception ex) {
	  	tundra.tn.exception.raise(ex);
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// tasks will be processed by a thread pool whose size is equal to the desired concurrency
	protected static void eachConcurrent(com.wm.app.tn.delivery.DeliveryQueue queue, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int concurrency, int retryLimit, int retryFactor, int ttw, boolean ordered, boolean suspend) throws ServiceException {
	  boolean invokedByTradingNetworks = invokedByTradingNetworks();
	
	  com.wm.app.b2b.server.Session session = com.wm.app.b2b.server.Service.getSession();
	  com.wm.app.b2b.server.InvokeState state = com.wm.app.b2b.server.InvokeState.getCurrentState();
	  java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(concurrency, new ServerThreadFactory(queue.getQueueName(), state));
	  java.util.Queue<java.util.concurrent.Future<IData>> futures = new java.util.LinkedList<java.util.concurrent.Future<IData>>();
	
	  try {
	    while(true) {
	      if (!invokedByTradingNetworks || queue.isEnabled() || queue.isDraining()) {
	        int size = futures.size();
	        if (size < concurrency) {
	          // submit another queued task
	          com.wm.app.tn.delivery.GuaranteedJob task = pop(queue.getQueueName(), ordered);
	          if (task == null) {
	            if (size > 0) {
	              // wait for first thread to finish; once finished we'll loop again and see if there are now tasks on the queue
	              awaitOldest(futures, suspend);
	            } else {
	              // if all threads have finished and there are no more tasks, then exit
	              break;
	            }
	          } else {
		        	updateRetryStrategy(task, retryLimit, retryFactor, ttw);
	            futures.add(executor.submit(new CallableService(executeTaskService, session, createTaskInputPipeline(task, service, pipeline, queue.getQueueName(), queue.getQueueType()))));
	          }
	        } else {
	          // wait for first thread to finish
	          awaitOldest(futures, suspend);
	        }
	        if (invokedByTradingNetworks) queue = com.wm.app.tn.db.QueueOperations.selectByName(queue.getQueueName());
	      } else {
	        break; // if invoked by TN and queue is disabled or suspended, then exit
	      }
	    }
	  } catch (Exception ex) {
	  	tundra.tn.exception.raise(ex);
	  } finally {
	    executor.shutdown();
	    awaitAll(futures, suspend);
	  }
	}
	
	// waits for all futures in the given queue to complete
	protected static java.util.List<IData> awaitAll(java.util.Queue<java.util.concurrent.Future<IData>> futures, boolean suspend) {
	  java.util.List<IData> results = new java.util.ArrayList<IData>(futures.size());
	  while(futures.size() > 0) {
	    try {
	      results.add(awaitOldest(futures, suspend));
	    } catch (Exception ex) {
	      // ignore exceptions
	    }
	  }
	  return results;
	}
	
	// waits for the first/head future in the given queue to complete
	protected static IData awaitOldest(java.util.Queue<java.util.concurrent.Future<IData>> futures, boolean suspend) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  return await(futures.poll(), suspend);
	}
	
	// waits for the given future to complete
	protected static IData await(java.util.concurrent.Future<IData> future, boolean suspend) throws java.sql.SQLException, java.io.IOException, ServiceException {
	  IData output = null;
	  try {
	    output = future.get();
	
	    if (output != null) {
	      IDataCursor cursor = output.getCursor();
	      Object task = IDataUtil.get(cursor, "task");
	      cursor.destroy();
	
	      if (task != null && task instanceof com.wm.app.tn.delivery.GuaranteedJob) retry((com.wm.app.tn.delivery.GuaranteedJob)task, suspend);
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
	
	protected static final java.text.SimpleDateFormat DATE_FORMATTER = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	// re-enqueues the given job for delivery, unless it has reached its retry limit
	protected static void retry(com.wm.app.tn.delivery.GuaranteedJob job, boolean suspend) throws ServiceException {
	  job = com.wm.app.tn.db.DeliveryStore.getAnyJob(job.getJobId(), true); // refetch job from DB
	  com.wm.app.tn.doc.BizDocEnvelope bizdoc = job.getBizDocEnvelope();
	
	  int retryLimit = job.getRetryLimit();
	  int retries = job.getRetries();
	  String status = job.getStatus();
	  String queueName = job.getQueueName();
	  com.wm.app.tn.delivery.DeliveryQueue queue = tundra.tn.queue.get(queueName);
	
	  boolean exhausted = retries >= retryLimit;
	  boolean failed = (retries > 0 && status.equals("QUEUED")) || (exhausted && status.equals("FAILED"));
	
	  if (failed) {
	    if (exhausted) {
	      if (suspend) {
	        // reset retries back to 0
	        job.setRetries(0);
	        job.setStatus(com.wm.app.tn.delivery.GuaranteedJob.QUEUED);
	        job.setDefaultServerId();
	        job.setTimeUpdated(waitForNextRetry(job));
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
	    } else {
	    	long nextRetry = waitForNextRetry(job);
	      job.setTimeUpdated(nextRetry); // force this job to wait for its next retry
	      update(job);
	
	      if (bizdoc != null) {
	      	com.wm.app.tn.db.BizDocStore.changeStatus(bizdoc, "QUEUED", "REQUEUED");
	      	String message = "Next retry scheduled no earlier than " + DATE_FORMATTER.format(new java.util.Date(nextRetry));
	      	log(bizdoc, "MESSAGE", "Delivery", "Next retry scheduled", message);
	      }
	    }
	  }
	}
	
	// calculates the next time the given task should be retried using the retry settings in the task
	protected static long waitForNextRetry(com.wm.app.tn.delivery.GuaranteedJob job) {
		long now = new java.util.Date().getTime();
		long nextRetry = now;
	
		int retryCount = job.getRetries();
		int retryFactor = job.getRetryFactor();
		int ttw = (int)job.getTTW();
	
		if (ttw > 0) {
			if (retryFactor > 1 && retryCount > 1) {
				nextRetry = now + (ttw * (long)Math.pow(retryFactor, retryCount - 1));
			} else {
				nextRetry = now + ttw;
			}
		}
	
		return nextRetry;
	}
	
	// saves the given job to the Trading Networks database
	protected static void update(com.wm.app.tn.delivery.GuaranteedJob job) throws ServiceException {
	  java.sql.Connection connection = null;
	  java.sql.PreparedStatement statement = null;
	
	  try {
	    connection = com.wm.app.tn.db.Datastore.getConnection();
	    statement = com.wm.app.tn.db.SQLStatements.prepareStatement(connection, UPDATE_DELIVER_JOB);
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
	  	tundra.tn.exception.raise(ex);
	  } catch (java.io.IOException ex) {
	  	tundra.tn.exception.raise(ex);
	  } finally {
	    com.wm.app.tn.db.SQLStatements.releaseStatement(statement);
	    com.wm.app.tn.db.Datastore.releaseConnection(connection);     
	  }
	}
	
	// returns the head of the given queue without dequeuing it
	public static com.wm.app.tn.delivery.GuaranteedJob peek(String queueName, boolean ordered) throws ServiceException {
	  java.sql.Connection connection = null;
	  java.sql.PreparedStatement statement = null;
	  java.sql.ResultSet results = null;
	  com.wm.app.tn.delivery.GuaranteedJob task = null;
	
	  try {
	    connection = com.wm.app.tn.db.Datastore.getConnection();
	    statement = connection.prepareStatement(ordered ? SELECT_NEXT_DELIVER_JOB_ORDERED : SELECT_NEXT_DELIVER_JOB_UNORDERED);
	    statement.clearParameters();
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 1, queueName, "DeliveryQueue.QueueName");
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 2, queueName, "DeliveryQueue.QueueName");
	    com.wm.app.tn.db.SQLWrappers.setTimestamp(statement, 3, new java.sql.Timestamp(new java.util.Date().getTime()));
	    results = statement.executeQuery();
	    if (results.next()) {
	    	String id = results.getString(1);
	    	task = com.wm.app.tn.db.DeliveryOperations.getAnyJob(id, false);
	    }
	    connection.commit();
	  } catch (java.sql.SQLException ex) {
	    connection = com.wm.app.tn.db.Datastore.handleSQLException(connection, ex);
	  	tundra.tn.exception.raise(ex);
	  } catch (java.io.IOException ex) {
	  	tundra.tn.exception.raise(ex);
	  } finally {
	  	com.wm.app.tn.db.SQLWrappers.close(results);
	    com.wm.app.tn.db.SQLWrappers.close(statement);
	    com.wm.app.tn.db.Datastore.releaseConnection(connection);     
	  }
	
	  return task;
	}
	
	
	// dequeues a task from the given queue
	public static com.wm.app.tn.delivery.GuaranteedJob pop(String queueName, boolean ordered) throws ServiceException {
		com.wm.app.tn.delivery.GuaranteedJob task = peek(queueName, ordered);
		setDelivering(task);
		return task;
	}
	
	// update the given task status to DELIVERING
	protected static void setDelivering(com.wm.app.tn.delivery.GuaranteedJob task) throws ServiceException {
		if (task == null) return;
	
	  java.sql.Connection connection = null;
	  java.sql.PreparedStatement statement = null;
	
	  try {
	    connection = com.wm.app.tn.db.Datastore.getConnection();
	    statement = com.wm.app.tn.db.SQLStatements.prepareStatement(connection, UPDATE_DELIVER_JOB_DELIVERING);
	    statement.clearParameters();
	    com.wm.app.tn.db.SQLWrappers.setChoppedString(statement, 1, com.wm.app.tn.delivery.JobMgr.getJobMgr().getServerId(), "DeliveryJob.ServerID");
	    com.wm.app.tn.db.SQLWrappers.setCharString(statement, 2, task.getJobId());
	    statement.executeUpdate();
	    task.delivering();
	    connection.commit();
	  } catch (java.sql.SQLException ex) {
	    connection = com.wm.app.tn.db.Datastore.handleSQLException(connection, ex);
	    tundra.tn.exception.raise(ex);
	  } finally {
	    com.wm.app.tn.db.SQLStatements.releaseStatement(statement);
	    com.wm.app.tn.db.Datastore.releaseConnection(connection);     
	  }
	}
	
	// update the retry settings on the given task to be the retry settings on the receiver's profile, or the 
	// given default settings if the receiver profile has no retry settings configured
	protected static void updateRetryStrategy(com.wm.app.tn.delivery.GuaranteedJob task, int retryLimit, int retryFactor, int timeToWait) throws ServiceException {
		if (task == null) return;
	
		java.sql.Connection connection = null;
	  java.sql.PreparedStatement statement = null;
	
		try {
			int taskRetryLimit = task.getRetryLimit();
			int taskRetryFactor = task.getRetryFactor();
			int taskTTW = (int)task.getTTW();
	
			com.wm.app.tn.doc.BizDocEnvelope bizdoc = task.getBizDocEnvelope();
		  com.wm.app.tn.profile.ProfileSummary receiver = com.wm.app.tn.profile.ProfileStore.getProfileSummary(bizdoc.getReceiverId());
	
			if (receiver.getDeliveryRetries() > 0) {
				retryLimit = receiver.getDeliveryRetries();
				retryFactor = receiver.getRetryFactor();
				timeToWait = receiver.getDeliveryWait();
			}
	
			if (taskRetryLimit != retryLimit || taskRetryFactor != retryFactor || taskTTW != timeToWait) {
				task.setRetryLimit(retryLimit);
				task.setRetryFactor(retryFactor);
				task.setTTW(timeToWait);
	
		    connection = com.wm.app.tn.db.Datastore.getConnection();
		    statement = connection.prepareStatement(UPDATE_DELIVER_JOB_RETRY_STRATEGY);
		    statement.clearParameters();
		    statement.setInt(1, task.getRetryLimit());
		    statement.setInt(2, task.getRetryFactor());
		    statement.setInt(3, (int)task.getTTW());
		    com.wm.app.tn.db.SQLWrappers.setCharString(statement, 4, task.getJobId());
		    statement.executeUpdate();
		    connection.commit();
			}
	  } catch (java.sql.SQLException ex) {
	    connection = com.wm.app.tn.db.Datastore.handleSQLException(connection, ex);
	  	tundra.tn.exception.raise(ex);
	  } finally {
	    com.wm.app.tn.db.SQLWrappers.close(statement);
	    com.wm.app.tn.db.Datastore.releaseConnection(connection);     
	  }
	}
	
	protected static final String LOG_SERVICE_NAME = "tundra.tn:log";
	protected static final com.wm.lang.ns.NSName LOG_SERVICE = com.wm.lang.ns.NSName.create(LOG_SERVICE_NAME);
	
	// add an activity log statement to the given bizdoc
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

