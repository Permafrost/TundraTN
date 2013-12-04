package tundra.tn.support.security;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-12-04 15:48:54.085
// -----( ON-HOST: EBZDEVWAP37.ebiztest.qr.com.au

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
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
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		  raise(IDataUtil.getString(cursor, "$message"));
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// throws a new document strictness check exception 
	public static void raise(String message) throws Exception {
	  throw new Exception(message);
	}
	
	// document strictness check exception, thrown when a strictness check fails
	public static class Exception extends com.wm.app.b2b.server.ServiceException {
	  public Exception() {
	    super();
	  }
	
	  public Exception(String message) {
	    super(message);
	  }
	
	  public Exception(java.lang.Throwable exception) {
	    super(exception);
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

