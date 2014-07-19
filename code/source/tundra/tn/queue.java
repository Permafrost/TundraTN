package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-07-19 13:05:36 EST
// -----( ON-HOST: 172.16.189.136

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




	public static final void disable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(disable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  disable(IDataUtil.getString(cursor, "$queue"));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void drain (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(drain)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  drain(IDataUtil.getString(cursor, "$queue"));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void enable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(enable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  enable(IDataUtil.getString(cursor, "$queue"));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void suspend (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(suspend)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  suspend(IDataUtil.getString(cursor, "$queue"));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// return the delivery queue associated with the given name
	public static com.wm.app.tn.delivery.DeliveryQueue get(String queueName) throws ServiceException {
	  if (queueName == null) return null;
	  
	  com.wm.app.tn.delivery.DeliveryQueue queue = null;
	
	  try {
	    queue = com.wm.app.tn.db.QueueOperations.selectByName(queueName);
	  } catch(java.sql.SQLException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  } catch(java.io.IOException ex) {
	    throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	  }
	
	  return queue;
	}
	
	// enables the delivery of the queue associated with the given name
	public static void enable(String queueName) throws com.wm.app.b2b.server.ServiceException {
	  enable(get(queueName));
	}
	
	// enables the delivery of the given queue
	public static void enable(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;
	  queue.setState(com.wm.app.tn.delivery.DeliveryQueue.STATE_ENABLED);
	  update(queue);
	}
	
	// disables the delivery of the queue associated with the given name
	public static void disable(String queueName) throws ServiceException {
	  disable(get(queueName));
	}
	
	// disables the delivery of the given queue
	public static void disable(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;
	  queue.setState(com.wm.app.tn.delivery.DeliveryQueue.STATE_DISABLED);
	  update(queue);
	}
	
	// drains the delivery of the queue associated with the given name
	public static void drain(String queueName) throws ServiceException {
	  drain(get(queueName));
	}
	
	// drains the delivery of the given queue
	public static void drain(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;
	  queue.setState(com.wm.app.tn.delivery.DeliveryQueue.STATE_DRAINING);
	  update(queue);
	}
	
	// suspends the delivery of the queue associated with the given name
	public static void suspend(String queueName) throws ServiceException {
	  suspend(get(queueName));
	}
	
	// suspends the delivery of the given queue
	public static void suspend(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;
	  queue.setState(com.wm.app.tn.delivery.DeliveryQueue.STATE_SUSPENDED);
	  update(queue);
	}
	
	// updates the given queue with any changes that may have occurred
	protected static void update(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;
	
	  IData pipeline = IDataFactory.create();
	  IDataCursor cursor = pipeline.getCursor();
	  IDataUtil.put(cursor, "queue", queue);
	  cursor.destroy();
	
	  wm.tn.queuing.updateQueue(pipeline);
	}
	// --- <<IS-END-SHARED>> ---
}

