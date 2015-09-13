package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-09-13 16:35:20 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.tn.queue.QueueHelper;
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
		    QueueHelper.disable(QueueHelper.get(IDataUtil.getString(cursor, "$queue")));
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
		    QueueHelper.drain(QueueHelper.get(IDataUtil.getString(cursor, "$queue")));
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
		    QueueHelper.enable(QueueHelper.get(IDataUtil.getString(cursor, "$queue")));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $queue
		// [o] record:0:optional $queue.properties
		// [o] - field:0:required name
		// [o] - field:0:required type
		// [o] - field:0:required status
		// [o] - field:0:required length
		// [o] field:0:required $queue.exists?
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queueName = IDataUtil.getString(cursor, "$queue");
		    IData properties = QueueHelper.toIData(QueueHelper.get(queueName));
		    if (properties != null) IDataUtil.put(cursor, "$queue.properties", properties);
		    IDataUtil.put(cursor, "$queue.exists?", "" + (properties != null));
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
		// [i] field:0:optional $queue
		// [o] field:0:required $length
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queueName = IDataUtil.getString(cursor, "$queue");
		    IDataUtil.put(cursor, "$length", "" + QueueHelper.length(QueueHelper.get(queueName)));
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
		    IData[] list = QueueHelper.toIDataArray(QueueHelper.list());
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
		    QueueHelper.suspend(QueueHelper.get(IDataUtil.getString(cursor, "$queue")));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

