package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-02-26 08:41:34 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.lang.ns.NSName;
import com.wm.util.coder.IDataCodable;
import permafrost.tundra.data.Content;
import permafrost.tundra.data.ContentAttached;
import permafrost.tundra.data.IDataMap;
import permafrost.tundra.lang.BaseException;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.SecurityException;
import permafrost.tundra.lang.StrictException;
import permafrost.tundra.lang.UnrecoverableException;
import permafrost.tundra.lang.ValidationException;
import permafrost.tundra.time.DateTimeHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class exception

{
	// ---( internal utility methods )---

	final static exception _instance = new exception();

	static exception _newInstance() { return new exception(); }

	static exception _cast(Object o) { return (exception)o; }

	// ---( server methods )---




	public static final void handle (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(handle)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] object:0:required $exception
		// [i] field:0:required $exception.class
		// [i] field:0:required $exception.message
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"));
		    Throwable exception = (Throwable)IDataUtil.get(cursor, "$exception");
		    boolean statusSilence = BooleanHelper.parse(IDataUtil.getString(cursor, "$status.silence?"));
		
		    String messageClass = "Processing", 
		           messageSummary = exception.getMessage(), 
		           messageDetail = exception.toString(), 
		           userStatus = "ERROR";
		    
		    if (exception instanceof IDataCodable) {
		        IDataMap exceptionDocument = IDataMap.of(((IDataCodable)exception).getIData());
		        if (!BooleanHelper.parse((String)exceptionDocument.get("$exception.recoverable?"), true)) {
		            messageClass = "Unrecoverable";
		            userStatus = "ABORTED";
		        }
		
		        IDataMap exceptionContent = IDataMap.of((IData)exceptionDocument.get("$exception.content"));
		
		        if (exceptionContent.size() > 0) {
		            try {
		                byte[] content = (byte[])exceptionContent.get("$content");
		                String contentType = (String)exceptionContent.get("$content.type");
		                
		                if (content != null && content.length > 0) {
		                    String partName = "tundra.tn.exception:handle/" + exception.getClass().getSimpleName() + "/" + DateTimeHelper.now(null);
		
		                    IDataMap scope = new IDataMap();
		                    scope.put("$bizdoc", bizdoc);
		                    scope.put("$part", partName);
		                    scope.put("$content", content);
		                    scope.put("$content.type", contentType);
		                    scope.put("$overwrite?", "true");
		
		                    Service.doInvoke(NSName.create("tundra.tn.document.content:add"), scope);
		                }
		            } catch(Exception ex) {
		                // suppress/ignore exceptions adding content part
		            }
		        }
		    }
		
		    BizDocEnvelopeHelper.log(bizdoc, "ERROR", messageClass, messageSummary, messageDetail);
		    BizDocEnvelopeHelper.setStatus(bizdoc, null, userStatus, statusSilence);
		
		    ExceptionHelper.raise(exception);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



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

