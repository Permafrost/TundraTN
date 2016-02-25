package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-02-25 22:29:42 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.Session;
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
		// [o] field:0:optional $session
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  IDataUtil.put(cursor, "$session", Service.getSession().getSessionID());
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

