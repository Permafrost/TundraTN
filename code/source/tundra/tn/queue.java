package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-01-19 12:46:13.297
// -----( ON-HOST: -

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



	public static final void length (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(length)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $queue
		// [o] field:0:required $length
		IDataCursor cursor = pipeline.getCursor();

		try {
		  String queueName = IDataUtil.getString(cursor, "$queue");
		  IDataUtil.put(cursor, "$length", "" + length(queueName));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] record:1:optional $queues
		// [o] - field:0:required name
		// [o] - field:0:required type
		// [o] - field:0:required status
		// [o] - field:0:required length
		IDataCursor cursor = pipeline.getCursor();

		try {
		  IData[] list = list();
		  if (list != null) IDataUtil.put(cursor, "$queues", list);
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
	    tundra.tn.exception.raise(ex);
	  } catch(java.io.IOException ex) {
	    tundra.tn.exception.raise(ex);
	  }

	  return queue;
	}

	// returns a list of all registered queues
	public static IData[] list() throws ServiceException {
	  IData[] output = null;
	  try {
	    output = toIData(com.wm.app.tn.db.QueueOperations.select(null));
	  } catch(java.sql.SQLException ex) {
	    tundra.tn.exception.raise(ex);
	  } catch(java.io.IOException ex) {
	    tundra.tn.exception.raise(ex);
	  }
	  return output;
	}

	// converts the given delivery queue object to an IData object
	public static IData toIData(com.wm.app.tn.delivery.DeliveryQueue input) throws ServiceException {
	  if (input == null) return null;

	  IData output = IDataFactory.create();
	  IDataCursor cursor = output.getCursor();

	  IDataUtil.put(cursor, "name", input.getQueueName());
	  IDataUtil.put(cursor, "type", input.getQueueType());
	  IDataUtil.put(cursor, "status", input.getState());
	  IDataUtil.put(cursor, "length", "" + length(input));

	  cursor.destroy();

	  return output;
	}

	// converts the given delivery queue object to an IData object
	public static IData[] toIData(com.wm.app.tn.delivery.DeliveryQueue[] input) throws ServiceException {
	  if (input == null) return null;

	  IData[] output = new IData[input.length];

	  for (int i = 0; i < input.length; i++) {
	    output[i] = toIData(input[i]);
	  }

	  return output;
	}

	// enables the delivery of the queue associated with the given name
	public static void enable(String queueName) throws ServiceException {
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

	// returns the number of jobs currently queued in the given queue
	public static int length(String queueName) throws ServiceException {
	  return length(get(queueName));
	}

	// returns the number of jobs currently queued in the given queue
	public static int length(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  int length = 0;

	  if (queue != null) {
	    try {
	      String[] jobs = com.wm.app.tn.db.QueueOperations.getQueuedJobs(queue.getQueueName());
	      if (jobs != null) length = jobs.length;
	    } catch(java.sql.SQLException ex) {
	      tundra.tn.exception.raise(ex);
	    }
	  }

	  return length;
	}

	// updates the given queue with any changes that may have occurred
	protected static void update(com.wm.app.tn.delivery.DeliveryQueue queue) throws ServiceException {
	  if (queue == null) return;

	  IData pipeline = IDataFactory.create();
	  IDataCursor cursor = pipeline.getCursor();
	  IDataUtil.put(cursor, "queue", queue);
	  cursor.destroy();

	  try {
	    Service.doInvoke("wm.tn.queuing", "updateQueue", pipeline);
	  } catch(Exception ex) {
	    tundra.tn.exception.raise(ex);
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

