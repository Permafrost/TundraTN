package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2023-06-13 17:17:56 EST
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.io.InputStreamHelper;
import permafrost.tundra.lang.CharsetHelper;
import permafrost.tundra.server.ServiceHelper;
import permafrost.tundra.tn.util.SystemHelper;
// --- <<IS-END-IMPORTS>> ---

public final class system

{
	// ---( internal utility methods )---

	final static system _instance = new system();

	static system _newInstance() { return new system(); }

	static system _cast(Object o) { return (system)o; }

	// ---( server methods )---




	public static final void export (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(export)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required $system.tn.export.json
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String export = SystemHelper.export();
		    IDataUtil.put(cursor, "$system.tn.export.json", export);
		    ServiceHelper.respond(200, null, null, InputStreamHelper.normalize(export), "application/json", CharsetHelper.DEFAULT_CHARSET);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

