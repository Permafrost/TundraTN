package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-08-25 19:29:33 EST
// -----( ON-HOST: -

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
		// [i] field:0:optional $type {&quot;security&quot;,&quot;strict&quot;}
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
	  } else {
	    ex = new ServiceException(message);
	  }

	  throw ex;
	}

	// throws a new ServiceException with the given message
	public static void raise(String message) throws ServiceException {
	  throw new ServiceException(message == null ? "" : message);
	}

	// throws a new ServiceException with the class and message from the given Throwable, which
	// is useful because java services are hard-wired to only throw ServiceExceptions
	public static void raise(Throwable exception) throws ServiceException {
	  if (exception != null) {
	    if (exception instanceof ServiceException) {
	      throw (ServiceException)exception;
	    } else {
	      raise(message(exception));
	    }
	  }
	}

	// returns an exception as a string
	public static String message(Throwable exception) {
	  String message = "";

	  if (exception != null) {
	    if (exception instanceof ServiceException) {
	      message = exception.getMessage();
	    } else {
	      message = exception.getClass().getName() + ": " + exception.getMessage();
	    }
	  }

	  return message;
	}

	// security exception, thrown when a user doesn't have appropriate rights
	public static class SecurityException extends com.wm.app.b2b.server.ServiceException {
	  public SecurityException() {
	    super();
	  }

	  public SecurityException(String message) {
	    super(message);
	  }

	  public SecurityException(java.lang.Throwable exception) {
	    super(exception);
	  }
	}

	// document strictness check exception, thrown when a strictness check fails
	public static class StrictException extends com.wm.app.b2b.server.ServiceException {
	  public StrictException() {
	    super();
	  }

	  public StrictException(String message) {
	    super(message);
	  }

	  public StrictException(java.lang.Throwable exception) {
	    super(exception);
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

