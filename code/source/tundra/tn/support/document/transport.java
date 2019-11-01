package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-11-01T14:33:45.988
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocContentHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class transport

{
	// ---( internal utility methods )---

	final static transport _instance = new transport();

	static transport _newInstance() { return new transport(); }

	static transport _cast(Object o) { return (transport)o; }

	// ---( server methods )---




	public static final void log (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(log)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:optional $transport.log? {"false","true"}
		// [i] field:0:optional $transport.log.part
		IDataCursor cursor = pipeline.getCursor();

		try {
		    boolean shouldLog = IDataHelper.getOrDefault(cursor, "$transport.log?", Boolean.class, false);
		    if (shouldLog) {
		        BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
		        String partName = IDataHelper.get(cursor, "$transport.log.part", String.class);
		        BizDocContentHelper.addTransportContentPart(document, partName);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

