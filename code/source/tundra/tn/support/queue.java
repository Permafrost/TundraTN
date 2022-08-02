package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2022-07-15 05:35:26 EST
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
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.math.FloatHelper;
import permafrost.tundra.math.IntegerHelper;
import permafrost.tundra.time.DurationHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueManager;
// --- <<IS-END-IMPORTS>> ---

public final class queue

{
	// ---( internal utility methods )---

	final static queue _instance = new queue();

	static queue _newInstance() { return new queue(); }

	static queue _cast(Object o) { return (queue)o; }

	// ---( server methods )---




	public static final void reflect (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reflect)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] record:0:required $queue.processing
		// [o] - object:0:required supervisor.started?
		// [o] - record:1:optional workers
		// [o] - object:0:required workers.length
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IDataHelper.put(cursor, "$queue.processing", DeliveryQueueManager.getInstance().getIData());
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void restart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(restart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		DeliveryQueueManager.getInstance().restart();
		// --- <<IS-END>> ---

                
	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		DeliveryQueueManager.getInstance().start();
		// --- <<IS-END>> ---

                
	}



	public static final void stop (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stop)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		DeliveryQueueManager.getInstance().stop();
		// --- <<IS-END>> ---

                
	}
}

