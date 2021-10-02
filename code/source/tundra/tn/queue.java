package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2021-10-02 13:25:01 EST
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.IOException;
import java.sql.SQLException;
import javax.xml.datatype.Duration;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueManager;
import permafrost.tundra.tn.delivery.DeliveryQueueProcessor;
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



	public static final void each (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(each)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required queue
		// [i] field:0:required $service
		// [i] record:0:optional $pipeline
		// [i] field:0:optional $task.age
		// [i] field:0:optional $retry.limit
		// [i] field:0:optional $retry.wait
		// [i] field:0:optional $retry.factor
		// [i] field:0:optional $ordered? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $suspend? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $concurrency
		// [i] field:0:optional $thread.priority
		// [i] field:0:optional $error.threshold
		// [i] field:0:optional $status.exhausted
		// [i] field:0:optional $status.silence? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $daemonize? {&quot;false&quot;,&quot;true&quot;}
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String queue = IDataHelper.get(cursor, "queue", String.class);
		    String service = IDataHelper.get(cursor, "$service", String.class);
		    IData scope = IDataHelper.normalize(IDataHelper.get(cursor, "$pipeline", IData.class));
		    int concurrency = IDataHelper.getOrDefault(cursor, "$concurrency", Integer.class, 1);
		    boolean ordered = IDataHelper.getOrDefault(cursor, "$ordered?", Boolean.class, false);
		    boolean suspend = IDataHelper.getOrDefault(cursor, "$suspend?", Boolean.class, false);
		    Duration age = IDataHelper.get(cursor, "$task.age", Duration.class);
		    int retryLimit = IDataHelper.firstOrDefault(cursor, Integer.class, -1, "$retry.limit", "$retries");
		    Duration retryWait = IDataHelper.get(cursor, "$retry.wait", Duration.class);
		    float retryFactor = IDataHelper.getOrDefault(cursor, "$retry.factor", Float.class, 1.0f);
		    int threadPriority = IDataHelper.getOrDefault(cursor, "$thread.priority", Integer.class, Thread.NORM_PRIORITY);
		    boolean threadDaemon = IDataHelper.getOrDefault(cursor, "$daemonize?", Boolean.class, false);
		    String exhaustedStatus = IDataHelper.get(cursor, "$status.exhausted", String.class);
		    long errorThreshold = IDataHelper.getOrDefault(cursor, "$error.threshold", Long.class, 0L);

		    DeliveryQueueManager.getInstance().process(queue, service, scope == null? pipeline : scope, age, concurrency, retryLimit, retryFactor, retryWait, threadPriority, threadDaemon, ordered, suspend, exhaustedStatus, errorThreshold);
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
		    DeliveryQueueProcessor processor = DeliveryQueueManager.getInstance().get(queueName);
		    if (processor != null) processor.stop();
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

