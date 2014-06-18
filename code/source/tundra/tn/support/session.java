package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-06-19 09:05:14.899
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class session

{
	// ---( internal utility methods )---

	final static session _instance = new session();

	static session _newInstance() { return new session(); }

	static session _cast(Object o) { return (session)o; }

	// ---( server methods )---




	public static final void current (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(current)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required $session
		IDataCursor cursor = pipeline.getCursor();

		try {
		  com.wm.app.b2b.server.Session session = com.wm.app.b2b.server.Service.getSession();
		  IDataUtil.put(cursor, "$session", session.getSessionID());
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

