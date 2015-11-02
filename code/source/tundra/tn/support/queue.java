package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-11-03 09:30:23.107
// -----( ON-HOST: EBZDEVWAP37.ebiztest.qr.com.au

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.math.IntegerHelper;
import permafrost.tundra.tn.delivery.DeliveryQueueHelper;
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
		// [i] field:0:optional $status.silence? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $concurrency
		// [i] field:0:optional $ordered? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $suspend? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $retry.limit
		// [i] field:0:optional $retry.wait
		// [i] field:0:optional $retry.factor
		// [o] field:0:required queue
		// [o] field:0:optional logMsg
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String queue = IDataUtil.getString(cursor, "queue");
		    String service = IDataUtil.getString(cursor, "$service");
		    IData scope = IDataUtil.getIData(cursor, "$pipeline");
		    boolean statusSilence = BooleanHelper.parse(IDataUtil.getString(cursor, "$status.silence?"));
		    int concurrency = IntegerHelper.parse(IDataUtil.getString(cursor, "$concurrency"), 1);
		    boolean ordered = BooleanHelper.parse(IDataUtil.getString(cursor, "$ordered?"));
		    boolean suspend = BooleanHelper.parse(IDataUtil.getString(cursor, "$suspend?"));
		    // support $retries for backwards-compatibility
		    int retryLimit = IntegerHelper.parse(ObjectHelper.coalesce(IDataUtil.getString(cursor, "$retry.limit"), IDataUtil.getString(cursor, "$retries")));
		    int retryFactor = IntegerHelper.parse(IDataUtil.getString(cursor, "$retry.factor"), 1);
		    int retryWait = IntegerHelper.parse(IDataUtil.getString(cursor, "$retry.wait"));
		
		    DeliveryQueueHelper.each(queue, service, scope == null? pipeline : scope, concurrency, retryLimit, retryFactor, retryWait, ordered, suspend, statusSilence);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

