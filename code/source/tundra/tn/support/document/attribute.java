package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-02-28 16:35:17 GMT+10:00
// -----( ON-HOST:

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocAttributeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class attribute

{
	// ---( internal utility methods )---

	final static attribute _instance = new attribute();

	static attribute _newInstance() { return new attribute(); }

	static attribute _cast(Object o) { return (attribute)o; }

	// ---( server methods )---




	public static final void sanitize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sanitize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $attributes
		// [o] record:0:optional $attributes
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IData attributes = IDataHelper.get(cursor, "$attributes", IData.class);
		    IDataHelper.put(cursor, "$attributes", BizDocAttributeHelper.sanitize(attributes), false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

