package tundra.tn.support.document.attribute;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-06-08 14:56:46 EST
// -----( ON-HOST:-

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocAttributeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class any

{
	// ---( internal utility methods )---

	final static any _instance = new any();

	static any _newInstance() { return new any(); }

	static any _cast(Object o) { return (any)o; }

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

