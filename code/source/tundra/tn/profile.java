package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-09-13 16:34:19 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.tn.profile.ProfileCache;
// --- <<IS-END-IMPORTS>> ---

public final class profile

{
	// ---( internal utility methods )---

	final static profile _instance = new profile();

	static profile _newInstance() { return new profile(); }

	static profile _cast(Object o) { return (profile)o; }

	// ---( server methods )---




	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $id
		// [i] field:0:optional $type
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String id = IDataUtil.getString(cursor, "$id");
		    String type = IDataUtil.getString(cursor, "$type");
		
		    if (id != null) {
		        IData profile = ProfileCache.getInstance().get(id, type);
		        if (profile != null) IDataUtil.put(cursor, "$profile", profile);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData[] list = ProfileCache.getInstance().list(true);
		    if (list != null) IDataUtil.put(cursor, "$profiles", list);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void self (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(self)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData profile = ProfileCache.getInstance().self();
		    if (profile != null) IDataUtil.put(cursor, "$profile", profile);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

