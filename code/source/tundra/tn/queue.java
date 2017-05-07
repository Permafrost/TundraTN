package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:08:46 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.sql.SQLException;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueHelper;
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
		    DeliveryQueueHelper.disable(DeliveryQueueHelper.get(IDataHelper.get(cursor, "$queue", String.class)));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		    DeliveryQueueHelper.drain(DeliveryQueueHelper.get(IDataHelper.get(cursor, "$queue", String.class)));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		    DeliveryQueueHelper.enable(DeliveryQueueHelper.get(IDataHelper.get(cursor, "$queue", String.class)));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		// [o] - object:0:required queue
		// [o] field:0:required $queue.exists?
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queueName = IDataHelper.get(cursor, "$queue", String.class);
		
		    IData properties = DeliveryQueueHelper.toIData(DeliveryQueueHelper.get(queueName));
		    
		    IDataHelper.put(cursor, "$queue.properties", properties, false);
		    IDataHelper.put(cursor, "$queue.exists?", properties != null, String.class);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		    String queueName = IDataHelper.get(cursor, "$queue", String.class);
		    IDataHelper.put(cursor, "$length", DeliveryQueueHelper.length(DeliveryQueueHelper.get(queueName)), String.class);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		// [o] - object:0:required queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData[] list = DeliveryQueueHelper.toIDataArray(DeliveryQueueHelper.list());
		    IDataHelper.put(cursor, "$queues", list, false);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
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
		    DeliveryQueueHelper.suspend(DeliveryQueueHelper.get(IDataHelper.get(cursor, "$queue", String.class)));
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

