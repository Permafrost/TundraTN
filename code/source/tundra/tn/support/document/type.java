package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-02-25 22:27:08 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocType;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocTypeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class type

{
	// ---( internal utility methods )---

	final static type _instance = new type();

	static type _newInstance() { return new type(); }

	static type _cast(Object o) { return (type)o; }

	// ---( server methods )---




	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $id
		// [i] field:0:optional $name
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String id = IDataUtil.getString(cursor, "$id");
		    String name = IDataUtil.getString(cursor, "$name");
		
		    BizDocType type = null;
		
		    if (id != null) {
		        type = BizDocTypeHelper.get(id);
		    } else if (name != null) {
		        type = BizDocTypeHelper.getByName(name);
		    }
		
		    if (type != null) {
		        IDataUtil.put(cursor, "$type", IDataHelper.normalize(type.getIData()));
		    }
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void normalize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(normalize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocType type = BizDocTypeHelper.normalize(IDataUtil.getIData(cursor, "$type"));
		
		    if (type != null) {
		        IDataUtil.put(cursor, "$type", IDataHelper.normalize(type.getIData()));
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

