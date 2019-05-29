package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-05-29T13:08:15.706
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.route.RoutingRule;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.route.Deferrer;
import permafrost.tundra.tn.route.DeferredRoute;
// --- <<IS-END-IMPORTS>> ---

public final class defer

{
	// ---( internal utility methods )---

	final static defer _instance = new defer();

	static defer _newInstance() { return new defer(); }

	static defer _cast(Object o) { return (defer)o; }

	// ---( server methods )---




	public static final void route (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(route)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required bizdoc
		// [i] object:0:required rule
		// [i] record:0:optional TN_parms
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = IDataHelper.get(cursor, "bizdoc", BizDocEnvelope.class);
		    RoutingRule rule = IDataHelper.get(cursor, "rule", RoutingRule.class);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    Deferrer.getInstance().defer(new DeferredRoute(bizdoc, rule, parameters));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void seed (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(seed)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		Deferrer.getInstance().seed();
		// --- <<IS-END>> ---


	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $bizdoc.defer.concurrency
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Integer concurrency = IDataHelper.get(cursor, "$bizdoc.defer.concurrency", Integer.class);
		    Deferrer deferrer = Deferrer.getInstance();
		    if (concurrency != null) deferrer.setConcurrency(concurrency);
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
		// [o] field:0:required $bizdoc.defer.started?
		// [o] object:0:required $bizdoc.defer.concurrency
		// [o] object:0:required $bizdoc.defer.queue.length
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Deferrer deferrer = Deferrer.getInstance();

		    IDataHelper.put(cursor, "$bizdoc.defer.started?", deferrer.isStarted(), String.class);
		    IDataHelper.put(cursor, "$bizdoc.defer.concurrency", deferrer.getConcurrency());
		    IDataHelper.put(cursor, "$bizdoc.defer.queue.length", deferrer.size());
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

