package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-11-27T15:46:07.901
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class duplicate

{
	// ---( internal utility methods )---

	final static duplicate _instance = new duplicate();

	static duplicate _newInstance() { return new duplicate(); }

	static duplicate _cast(Object o) { return (duplicate)o; }

	// ---( server methods )---




	public static final void check (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(check)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [o] field:0:required $duplicate? {&quot;false&quot;,&quot;true&quot;}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
		    IDataHelper.put(cursor, "$duplicate?", BizDocEnvelopeHelper.isDuplicate(bizdoc), String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

