package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-06-23 10:06:34 EST
// -----( ON-HOST: 172.16.189.129

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
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  String queue = IDataUtil.getString(cursor, "queue");
		  String service = IDataUtil.getString(cursor, "$service");
		  IData scope = IDataUtil.getIData(cursor, "$pipeline");
		  String sConcurrency = IDataUtil.getString(cursor, "$concurrency");
		
		  int concurrency = 1;
		  if (sConcurrency != null) concurrency = Integer.parseInt(sConcurrency);
		
		  each(queue, service, scope == null? pipeline : scope, concurrency, -1);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	protected final static String EXECUTE_TASK_SERVICE_NAME = "tundra.tn.support.queue.task:execute";
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// if concurrency > 1, tasks will be processed by a thread pool whose size is equal to the desired concurrency,
	// otherwise they will be processed on the current thread
	public static void each(String queueName, String service, IData pipeline, int concurrency, int limit) throws ServiceException {
	  try {
	    if (limit <= 0) limit = Integer.MAX_VALUE;
	
	    com.wm.app.tn.delivery.DeliveryQueue queue = com.wm.app.tn.db.QueueOperations.selectByName(queueName);
	    if (queue == null) throw new ServiceException("Queue '" + queueName + "' does not exist");
	
	    com.wm.lang.ns.NSName executeTaskService = com.wm.lang.ns.NSName.create(EXECUTE_TASK_SERVICE_NAME);
	
	    if (concurrency <= 1) {
	      eachDirect(queueName, queue.getQueueType(), executeTaskService, service, pipeline, limit);
	    } else {
	      eachConcurrent(queueName, queue.getQueueType(), executeTaskService, service, pipeline, concurrency, limit);
	    }
	  } catch (java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline
	// on the current thread
	protected static void eachDirect(String queueName, String queueType, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int limit) throws ServiceException {
	  try {
	    int total = 0;
	    while(total < limit) {
	      com.wm.app.tn.delivery.GuaranteedJob task = com.wm.app.tn.db.DeliveryStore.dequeueOldestJob(queueName);
	      if (task == null) {
	        break; // if all threads have finished and there are no more tasks, then exit
	      } else {
	        try {
	          IData output = com.wm.app.b2b.server.Service.doInvoke(executeTaskService, createTaskInputPipeline(task, service, pipeline, queueName, queueType));
	        } catch (Exception ex) {}
	        total++;
	      }
	    }
	  } catch (Exception ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// tasks will be processed by a thread pool whose size is equal to the desired concurrency
	protected static void eachConcurrent(String queueName, String queueType, com.wm.lang.ns.NSName executeTaskService, String service, IData pipeline, int concurrency, int limit) throws ServiceException {
	  try {
	    // upper bound on number of tasks submitted to thread pool at any one time
	    int backlog = concurrency * 2;
	    int total = 0;
	
	    com.wm.app.b2b.server.Session session = com.wm.app.b2b.server.Service.getSession();
	    com.wm.app.b2b.server.InvokeState state = com.wm.app.b2b.server.InvokeState.getCurrentState();
	
	    java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(concurrency, new ServerThreadFactory(queueName, state));
	
	    java.util.Queue<java.util.concurrent.Future<IData>> futures = new java.util.LinkedList<java.util.concurrent.Future<IData>>();
	
	    while(total < limit) {
	      int size = futures.size();
	
	      if (size < backlog) {
	        // submit another queued task
	        com.wm.app.tn.delivery.GuaranteedJob task = com.wm.app.tn.db.DeliveryStore.dequeueOldestJob(queueName);
	        if (task == null) {
	          if (size > 0) {
	            // wait for first thread to finish; once finished we'll loop again and see if there are now tasks on the queue
	            awaitOldest(futures);
	          } else {
	            // if all threads have finished and there are no more tasks, then exit
	            break;
	          }
	        } else {
	          futures.add(executor.submit(new CallableService(executeTaskService, session, createTaskInputPipeline(task, service, pipeline, queueName, queueType))));
	          total++;
	        }
	      } else {
	        // wait for first thread to finish
	        awaitOldest(futures);
	      }
	    }
	    executor.shutdown();
	    awaitAll(futures);
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (com.wm.app.tn.delivery.DeliveryException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.lang.InterruptedException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	}
	
	// waits for all futures in the given queue to complete
	protected static <T> void awaitAll(java.util.Queue<java.util.concurrent.Future<T>> futures) throws InterruptedException {
	  while(futures.size() > 0) {
	    awaitOldest(futures);
	  }
	}
	
	// waits for the first/head future in the given queue to complete
	protected static <T> void awaitOldest(java.util.Queue<java.util.concurrent.Future<T>> futures) throws InterruptedException {
	  await(futures.poll());
	}
	
	// waits for the given future to complete
	protected static <T> void await(java.util.concurrent.Future<T> future) throws InterruptedException {
	  try { future.get(); } catch (java.util.concurrent.ExecutionException ex) {}
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
	// --- <<IS-END-SHARED>> ---
}

