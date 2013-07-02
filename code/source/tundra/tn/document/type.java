package tundra.tn.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-07-03 09:46:11 EST
// -----( ON-HOST: 172.16.189.216

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
	  IData pipeline = IDataFactory.create();
	  IDataCursor cursor = pipeline.getCursor();
	  IDataUtil.put(cursor, "typeId", id);
	  IDataUtil.put(cursor, "typeName", name);
	  cursor.destroy();
	
	  pipeline = tundra.service.invoke("wm.tn.doctype:view", pipeline);
	
	  cursor = pipeline.getCursor();
	  return (com.wm.app.tn.doc.BizDocType)IDataUtil.get(cursor, "type");
	}
	// --- <<IS-END-SHARED>> ---
}

