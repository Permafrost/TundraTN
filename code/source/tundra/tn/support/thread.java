package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:11:13 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
// --- <<IS-END-IMPORTS>> ---

public final class thread

{
	// ---( internal utility methods )---

	final static thread _instance = new thread();

	static thread _newInstance() { return new thread(); }

	static thread _cast(Object o) { return (thread)o; }

	// ---( server methods )---




	public static final void current (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(current)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required $thread
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  Thread thread = Thread.currentThread();
		  String description = thread.getClass().getName() + " ID#" + thread.getId() + " " + thread.getName();
		  IDataHelper.put(cursor, "$thread", description);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

