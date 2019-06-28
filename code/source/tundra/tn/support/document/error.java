package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-06-28T14:37:00.310
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.err.ActivityLogEntry;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class error

{
	// ---( internal utility methods )---

	final static error _instance = new error();

	static error _newInstance() { return new error(); }

	static error _cast(Object o) { return (error)o; }

	// ---( server methods )---




	public static final void exists (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(exists)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] record:0:optional $class
		// [i] - field:0:optional Security {"true","false"}
		// [i] - field:0:optional Recognition {"true","false"}
		// [i] - field:0:optional Verification {"true","false"}
		// [i] - field:0:optional Validation {"true","false"}
		// [i] - field:0:optional Persistence {"true","false"}
		// [i] - field:0:optional Saving {"true","false"}
		// [i] - field:0:optional Routing {"true","false"}
		// [i] - field:0:optional General {"true","false"}
		// [i] - field:0:optional Processing {"true","false"}
		// [i] - field:0:optional Delivery {"true","false"}
		// [i] - field:0:optional Transient {"true","false"}
		// [i] - field:0:optional Unrecoverable {"true","false"}
		// [o] field:0:required $exists?
		// [o] recref:1:optional $errors wm.tn.rec:ActivityLogEntry
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IData bizdoc = IDataHelper.get(cursor, "$bizdoc", IData.class);
		    IData classes = IDataHelper.get(cursor, "$class", IData.class);

		    ActivityLogEntry[] errors = BizDocEnvelopeHelper.getErrors(bizdoc, classes);

		    IDataHelper.put(cursor, "$exists?", errors != null && errors.length > 0, String.class);
		    IDataHelper.put(cursor, "$errors", IDataHelper.normalize(errors), false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

