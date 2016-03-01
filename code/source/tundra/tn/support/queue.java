package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-03-01 10:22:50 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.sql.SQLException;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.math.IntegerHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueProcessor;
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
		// [i] field:0:optional $suspend? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $retry.limit
		// [i] field:0:optional $retry.wait
		// [i] field:0:optional $retry.factor
		// [i] field:0:optional $thread.priority
		// [i] field:0:optional $daemonize? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $status.exhausted
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queue = IDataUtil.getString(cursor, "queue");
		    String service = IDataUtil.getString(cursor, "$service");
		    IData scope = IDataUtil.getIData(cursor, "$pipeline");
		    int concurrency = IntegerHelper.parse(IDataUtil.getString(cursor, "$concurrency"), 1);
		    boolean ordered = BooleanHelper.parse(IDataUtil.getString(cursor, "$ordered?"));
		    boolean suspend = BooleanHelper.parse(IDataUtil.getString(cursor, "$suspend?"));
		    // support $retries for backwards-compatibility
		    int retryLimit = IntegerHelper.parse(ObjectHelper.coalesce(IDataUtil.getString(cursor, "$retry.limit"), IDataUtil.getString(cursor, "$retries")));
		    int retryWait = IntegerHelper.parse(IDataUtil.getString(cursor, "$retry.wait"));
		    int retryFactor = IntegerHelper.parse(IDataUtil.getString(cursor, "$retry.factor"), 1);
		    int threadPriority = IntegerHelper.parse(IDataUtil.getString(cursor, "$thread.priority"), Thread.NORM_PRIORITY);
		    boolean threadDaemon = BooleanHelper.parse(IDataUtil.getString(cursor, "$daemonize?"));
		    String exhaustedStatus = IDataUtil.getString(cursor, "$status.exhausted");
		
		    DeliveryQueueProcessor.each(queue, service, scope == null? pipeline : scope, concurrency, retryLimit, retryFactor, retryWait, threadPriority, threadDaemon, ordered, suspend, exhaustedStatus);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void interrupt (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(interrupt)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $queue
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queueName = IDataUtil.getString(cursor, "$queue");
		    DeliveryQueueProcessor.interrupt(queueName);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void reflect (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reflect)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IDataUtil.put(cursor, "$processing.started?", BooleanHelper.emit(DeliveryQueueProcessor.isStarted()));
		    IData[] processes = DeliveryQueueProcessor.list();
		    IDataUtil.put(cursor, "$processing.threads", processes);
		    IDataUtil.put(cursor, "$processing.threads.length", "" + processes.length);
		} catch(IOException ex) {
		    ExceptionHelper.raise(ex);
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		DeliveryQueueProcessor.start();
		// --- <<IS-END>> ---

                
	}



	public static final void stop (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stop)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		DeliveryQueueProcessor.stop();
		// --- <<IS-END>> ---

                
	}
}

