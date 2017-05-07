package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:15:49 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
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
		// [i] field:0:optional $status.silence? {"false","true"}
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
		    String systemStatus = IDataHelper.get(cursor, "$status.system", String.class);
		    String previousSystemStatus = IDataHelper.get(cursor, "$status.system.previous", String.class);
		    String userStatus = IDataHelper.get(cursor, "$status.user", String.class);
		    String previousUserStatus = IDataHelper.get(cursor, "$status.user.previous", String.class);
		    boolean silence = IDataHelper.getOrDefault(cursor, "$status.silence?", Boolean.class, false);
		
		    BizDocEnvelopeHelper.setStatus(bizdoc, systemStatus, previousSystemStatus, userStatus, previousUserStatus, silence);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

