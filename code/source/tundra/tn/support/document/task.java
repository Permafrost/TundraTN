package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-11-03 14:41:15.513
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.delivery.GuaranteedJobHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class task

{
	// ---( internal utility methods )---

	final static task _instance = new task();

	static task _newInstance() { return new task(); }

	static task _cast(Object o) { return (task)o; }

	// ---( server methods )---




	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc
		// [i] - field:0:required InternalID
		// [o] field:0:required $tasks.length
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"));
		    IData[] tasks = IDataHelper.normalize(GuaranteedJobHelper.list(bizdoc));

		    if (tasks != null && tasks.length > 0) {
		        IDataUtil.put(cursor, "$tasks", tasks);
		        IDataUtil.put(cursor, "$tasks.length", "" + tasks.length);
		    } else {
		        IDataUtil.put(cursor, "$tasks.length", "0");
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

