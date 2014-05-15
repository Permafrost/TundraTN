package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-05-15 13:25:29.932
// -----( ON-HOST: EBZDEVWAP37.ebiztest.qr.com.au

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
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
		    IData profile = tundra.tn.support.profile.get(id, type);
		    if (profile != null) IDataUtil.put(cursor, "$profile", profile);
		  }
		} catch (java.io.IOException ex) {
		  tundra.tn.support.profile.raise(ex);
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
		  IData[] list = tundra.tn.support.profile.list();
		  if (list != null) IDataUtil.put(cursor, "$profiles", list);
		} catch (java.io.IOException ex) {
		  tundra.tn.support.profile.raise(ex);
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
		  IData profile = tundra.tn.support.profile.self();
		  if (profile != null) IDataUtil.put(cursor, "$profile", profile);
		} catch (java.io.IOException ex) {
		  tundra.tn.support.profile.raise(ex);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

