package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:23:35 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.datatype.Duration;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.math.FloatHelper;
import permafrost.tundra.math.IntegerHelper;
import permafrost.tundra.time.DurationHelper;
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
		// [i] field:0:optional $ordered? {"false","true"}
		// [i] field:0:optional $suspend? {"false","true"}
		// [i] field:0:optional $task.age
		// [i] field:0:optional $retry.limit
		// [i] field:0:optional $retry.wait
		// [i] field:0:optional $retry.factor
		// [i] field:0:optional $thread.priority
		// [i] field:0:optional $daemonize? {"false","true"}
		// [i] field:0:optional $status.exhausted
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queue = IDataHelper.get(cursor, "queue", String.class);
		    String service = IDataHelper.get(cursor, "$service", String.class);
		    IData scope = IDataHelper.get(cursor, "$pipeline", IData.class);
		    int concurrency = IDataHelper.getOrDefault(cursor, "$concurrency", Integer.class, 1);
		    boolean ordered = IDataHelper.getOrDefault(cursor, "$ordered?", Boolean.class, false);
		    boolean suspend = IDataHelper.getOrDefault(cursor, "$suspend?", Boolean.class, false);
		    Duration age = IDataHelper.get(cursor, "$task.age", Duration.class);
		    // support $retries for backwards-compatibility
		    int retryLimit = IDataHelper.getOrDefault(cursor, "$retry.limit", Integer.class, IDataHelper.getOrDefault(cursor, "$retries", Integer.class, 0));
		    Duration retryWait = IDataHelper.get(cursor, "$retry.wait", Duration.class);
		    float retryFactor = IDataHelper.getOrDefault(cursor, "$retry.factor", Float.class, 1.0f);
		    int threadPriority = IDataHelper.getOrDefault(cursor, "$thread.priority", Integer.class, Thread.NORM_PRIORITY);
		    boolean threadDaemon = IDataHelper.getOrDefault(cursor, "$daemonize?", Boolean.class, false);
		    String exhaustedStatus = IDataHelper.get(cursor, "$status.exhausted", String.class);
		
		    DeliveryQueueProcessor.each(queue, service, scope == null? pipeline : scope, age, concurrency, retryLimit, retryFactor, retryWait, threadPriority, threadDaemon, ordered, suspend, exhaustedStatus);
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
		    String queueName = IDataHelper.get(cursor, "$queue", String.class);
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
		// [o] field:0:required $processing.started?
		// [o] record:1:required $processing.threads
		// [o] - record:0:required queue
		// [o] -- field:0:required name
		// [o] -- field:0:required type
		// [o] -- field:0:required status
		// [o] -- field:0:required length
		// [o] -- object:0:required queue
		// [o] - recref:0:required thread tundra.support.schema:thread
		// [o] field:0:required $processing.threads.length
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IDataHelper.put(cursor, "$processing.started?", DeliveryQueueProcessor.isStarted(), String.class);
		    IData[] processes = DeliveryQueueProcessor.list();
		    IDataHelper.put(cursor, "$processing.threads", processes);
		    IDataHelper.put(cursor, "$processing.threads.length", processes.length, String.class);
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

