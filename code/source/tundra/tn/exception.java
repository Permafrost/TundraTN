package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-11-20 18:32:12 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.lang.BaseException;
import permafrost.tundra.lang.SecurityException;
import permafrost.tundra.lang.StrictException;
import permafrost.tundra.lang.ValidationException;
// --- <<IS-END-IMPORTS>> ---

public final class exception

{
	// ---( internal utility methods )---

	final static exception _instance = new exception();

	static exception _newInstance() { return new exception(); }

	static exception _cast(Object o) { return (exception)o; }

	// ---( server methods )---




	public static final void raise (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(raise)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $message
		// [i] field:0:optional $type {&quot;security&quot;,&quot;strict&quot;,&quot;validation&quot;}
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  String message = IDataUtil.getString(cursor, "$message");
		  String type = IDataUtil.getString(cursor, "$type");
		
		  raise(message, type);
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// throws a new security exception 
	public static void raise(String message, String type) throws ServiceException {
	  if (message == null) message = "";
	
	  ServiceException ex = null;
	
	  if (type != null && type.equals("security")) {
	    ex = new SecurityException(message);
	  } else if (type != null && type.equals("strict")) {
	    ex = new StrictException(message);
	  } else if (type != null && type.equals("validation")) {
	    ex = new ValidationException(message);
	  } else {
	    ex = new BaseException(message);
	  }
	
	  throw ex;
	}
	// --- <<IS-END-SHARED>> ---
}

