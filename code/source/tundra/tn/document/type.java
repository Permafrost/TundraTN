package tundra.tn.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-08-23 16:22:53.226
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
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
		  if (id != null || name != null) {
		    com.wm.app.tn.doc.BizDocType type = get(id, name);
		    IDataUtil.put(cursor, "$type", type.getIData());
		  }
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// returns the Trading Networks document type given the ID
	public static com.wm.app.tn.doc.BizDocType get(String id, String name) throws ServiceException {
	  com.wm.app.tn.doc.BizDocType type = null;
	  if (id != null) {
	    type = com.wm.app.tn.db.BizDocTypeStore.get(id, true, true);
	  } else if (name != null) {
	    type = com.wm.app.tn.db.BizDocTypeStore.getByName(name, true, true);
	  }
	  return type;
	}
	// --- <<IS-END-SHARED>> ---
}

