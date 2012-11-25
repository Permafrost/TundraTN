package tundra.tn.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2012-11-20 08:17:08.595
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
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  String id = IDataUtil.getString(cursor, "$id");
		  if (id != null) {
		    com.wm.app.tn.doc.BizDocType type = get(id);
		    IDataUtil.put(cursor, "$type", type.getIData());
		  }
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// returns the Trading Networks document type given the ID
	public static com.wm.app.tn.doc.BizDocType get(String id) throws ServiceException {
	  IData pipeline = IDataFactory.create();
	  IDataCursor cursor = pipeline.getCursor();
	  IDataUtil.put(cursor, "typeId", id);
	  cursor.destroy();
	
	  pipeline = tundra.service.invoke("wm.tn.doctype:view", pipeline);
	
	  cursor = pipeline.getCursor();
	  return (com.wm.app.tn.doc.BizDocType)IDataUtil.get(cursor, "type");
	}
	// --- <<IS-END-SHARED>> ---
}

