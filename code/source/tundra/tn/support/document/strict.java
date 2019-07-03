package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-07-03T12:22:16.912
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class strict

{
	// ---( internal utility methods )---

	final static strict _instance = new strict();

	static strict _newInstance() { return new strict(); }

	static strict _cast(Object o) { return (strict)o; }

	// ---( server methods )---




	public static final void check (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(check)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] record:0:optional $strict
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
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IData bizdoc = IDataHelper.get(cursor, "$bizdoc", IData.class);
		    IData classes = IDataHelper.get(cursor, "$strict", IData.class);

		    if (classes != null) BizDocEnvelopeHelper.raiseIfErrors(bizdoc, classes);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

