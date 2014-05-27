package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-05-27 18:50:40 EST
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
		  String s = IDataUtil.getString(cursor, "$concurrency");
		
		  int concurrency = 1;
		  if (s != null) concurrency = Integer.parseInt(s);
		
		  each(queue, service, scope == null? pipeline : scope, concurrency);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// dequeues each task on the given TN queue, and processes the task using the given service and input pipeline;
	// if concurrency > 1, tasks will be processed by a thread pool whose size is equal to the desired concurrency
	public static void each(String queue, String service, IData pipeline, int concurrency) throws ServiceException {
	  try {
	    com.wm.lang.ns.NSName executeTaskService = com.wm.lang.ns.NSName.create("tundra.tn.support.queue.task:execute");
	    com.wm.app.b2b.server.Session session = com.wm.app.b2b.server.Service.getSession();
	    com.wm.app.b2b.server.InvokeState state = com.wm.app.b2b.server.InvokeState.getCurrentState();
	
	    java.util.concurrent.ExecutorService executor = createExecutorService(queue, state, concurrency);
	
	    java.util.List<java.util.concurrent.Future<IData>> futures = new java.util.ArrayList<java.util.concurrent.Future<IData>>();
	
	    com.wm.app.tn.delivery.DeliveryQueue q = com.wm.app.tn.db.QueueOperations.selectByName(queue);
	    if (q == null) throw new ServiceException("Queue '" + queue + "' does not exist");
	    String type = q.getQueueType();
	
	    while(true) {
	      com.wm.app.tn.delivery.GuaranteedJob task = com.wm.app.tn.db.DeliveryStore.dequeueOldestJob(queue);
	      if (task == null) {
	        if (futures.size() > 0) {
	          // wait for first thread to finish; once finished we'll loop again and see if there are now tasks on the queue
	          java.util.concurrent.Future future = futures.remove(0);
	          try { future.get(); } catch (java.util.concurrent.ExecutionException ex) {}
	        } else {
	          // if all threads have finished and there are no more tasks, then exit
	          break;          
	        }
	      } else {
	        IData input = IDataFactory.create();
	        IDataCursor cursor = input.getCursor();
	        IDataUtil.put(cursor, "task", task);
	        IDataUtil.put(cursor, "timeDequeued", System.currentTimeMillis());
	        IDataUtil.put(cursor, "$service", service);
	        IDataUtil.put(cursor, "$pipeline", IDataUtil.deepClone(pipeline));
	        IDataUtil.put(cursor, "queue", queue);
	        IDataUtil.put(cursor, "queue.type", type);
	        cursor.destroy();
	
	        futures.add(executor.submit(new CallableService(executeTaskService, session, input)));
	      }
	    }
	    executor.shutdown();
	  } catch (java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (com.wm.app.tn.delivery.DeliveryException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch (java.lang.InterruptedException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
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
	
	// a direct executor service which executes tasks on the current thread (therefore blocking it)
	public static class DirectExecutorService extends java.util.concurrent.AbstractExecutorService {
	  private volatile boolean shutdown = false;
	
	  public DirectExecutorService() {}
	
	  public boolean awaitTermination(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
	    return true;
	  }
	
	  public void execute(Runnable r) {
	    r.run();
	  }
	
	  public boolean isShutdown() {
	    return shutdown;
	  }
	
	  public boolean isTerminated() {
	    return shutdown;
	  }
	
	  public void shutdown() {
	    this.shutdown = true;
	  }
	
	  public java.util.List<Runnable> shutdownNow() {
	    shutdown();
	    return new java.util.ArrayList<Runnable>(0);
	  }
	}
	
	// return an appropriate executor service: a direct executor when concurrency is <= 1 is returned which executes tasks on the current thread;
	// otherwise a thread pool executor is returned configured with a thread pool size equal to the desired concurrency
	protected static java.util.concurrent.ExecutorService createExecutorService(String queue, com.wm.app.b2b.server.InvokeState state, int concurrency) {
	  java.util.concurrent.ExecutorService service = null;
	  if (concurrency <= 1) {
	    service = new DirectExecutorService();
	  } else {
	    service = java.util.concurrent.Executors.newFixedThreadPool(concurrency, new ServerThreadFactory(queue, state));
	  }
	  return service;
	}
	// --- <<IS-END-SHARED>> ---
}

