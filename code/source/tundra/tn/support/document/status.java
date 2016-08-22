package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-08-22 15:27:46.001
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class status

{
	// ---( internal utility methods )---

	final static status _instance = new status();

	static status _newInstance() { return new status(); }

	static status _cast(Object o) { return (status)o; }

	// ---( server methods )---




	public static final void set (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(set)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:optional $status.system
		// [i] field:0:optional $status.system.previous
		// [i] field:0:optional $status.user
		// [i] field:0:optional $status.user.previous
		// [i] field:0:optional $status.silence? {&quot;false&quot;,&quot;true&quot;}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"));
		    String systemStatus = IDataUtil.getString(cursor, "$status.system");
		    String previousSystemStatus = IDataUtil.getString(cursor, "$status.system.previous");
		    String userStatus = IDataUtil.getString(cursor, "$status.user");
		    String previousUserStatus = IDataUtil.getString(cursor, "$status.user.previous");
		    boolean silence = BooleanHelper.parse(IDataUtil.getString(cursor, "$status.silence?"));

		    BizDocEnvelopeHelper.setStatus(bizdoc, systemStatus, previousSystemStatus, userStatus, previousUserStatus, silence);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

