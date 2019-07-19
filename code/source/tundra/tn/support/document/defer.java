package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-07-23T11:26:32.128
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.route.RoutingRule;
import javax.xml.datatype.Duration;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.tn.route.CallableRoute;
import permafrost.tundra.tn.route.Deferrer;
// --- <<IS-END-IMPORTS>> ---

public final class defer

{
	// ---( internal utility methods )---

	final static defer _instance = new defer();

	static defer _newInstance() { return new defer(); }

	static defer _cast(Object o) { return (defer)o; }

	// ---( server methods )---




	public static final void restart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(restart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		Deferrer.getInstance().restart();
		// --- <<IS-END>> ---


	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $bizdoc.defer.concurrency
		// [i] object:0:optional $bizdoc.defer.capacity
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Integer concurrency = IDataHelper.get(cursor, "$bizdoc.defer.concurrency", Integer.class);
		    Integer capacity = IDataHelper.get(cursor, "$bizdoc.defer.capacity", Integer.class);

		    Deferrer deferrer = Deferrer.getInstance();

		    if (concurrency != null && concurrency >= 1) deferrer.setConcurrency(concurrency);
		    if (capacity != null && capacity >= 1) deferrer.setCapacity(capacity);

		    deferrer.start();
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void status (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(status)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] object:0:required $bizdoc.defer.started?
		// [o] object:0:required $bizdoc.defer.concurrency
		// [o] object:0:required $bizdoc.defer.capacity
		// [o] object:0:optional $bizdoc.defer.idle?
		// [o] object:0:optional $bizdoc.defer.saturated?
		// [o] object:0:optional $bizdoc.defer.pending
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Deferrer deferrer = Deferrer.getInstance();

		    boolean started = deferrer.isStarted();

		    IDataHelper.put(cursor, "$bizdoc.defer.started?", started);
		    IDataHelper.put(cursor, "$bizdoc.defer.concurrency", deferrer.getConcurrency());
		    IDataHelper.put(cursor, "$bizdoc.defer.capacity", deferrer.getCapacity());

		    if (started) {
		        IDataHelper.put(cursor, "$bizdoc.defer.idle?", deferrer.isIdle());
		        IDataHelper.put(cursor, "$bizdoc.defer.saturated?", deferrer.isSaturated());
		        IDataHelper.put(cursor, "$bizdoc.defer.pending", deferrer.size());
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void stop (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stop)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		Deferrer.getInstance().stop();
		// --- <<IS-END>> ---


	}
}

