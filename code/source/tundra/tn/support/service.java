package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-12-04 16:33:26.162
// -----( ON-HOST: EBZDEVWAP37.ebiztest.qr.com.au

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class service

{
	// ---( internal utility methods )---

	final static service _instance = new service();

	static service _newInstance() { return new service(); }

	static service _cast(Object o) { return (service)o; }

	// ---( server methods )---




	public static final void response (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(response)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required $code
		// [i] field:0:required $message
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  int code = Integer.parseInt(IDataUtil.getString(cursor, "$code"));
		  String message = IDataUtil.getString(cursor, "$message");
		
		  com.wm.net.HttpHeader response = com.wm.app.b2b.server.Service.getHttpResponseHeader();
		  response.setResponse(code, message);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

