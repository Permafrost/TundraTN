package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-07-19T16:44:11.089
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.lang.ns.NSName;
import com.wm.util.coder.IDataCodable;
import permafrost.tundra.content.DuplicateException;
import permafrost.tundra.content.MalformedException;
import permafrost.tundra.content.StrictException;
import permafrost.tundra.content.ValidationException;
import permafrost.tundra.content.UnsupportedException;
import permafrost.tundra.content.Content;
import permafrost.tundra.content.ContentAttached;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.data.IDataMap;
import permafrost.tundra.id.ULID;
import permafrost.tundra.lang.BaseException;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.SecurityException;
import permafrost.tundra.lang.UnrecoverableException;
import permafrost.tundra.server.ServiceHelper;
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
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class, true));
		    Throwable exception = IDataHelper.get(cursor, "$exception", Throwable.class, true);
		    boolean statusSilence = IDataHelper.getOrDefault(cursor, "$status.silence?", Boolean.class, false);
		    String service = IDataHelper.getOrDefault(cursor, "$exception.service", String.class, "tundra.tn.exception:handle");

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
		                    String partName = service.replaceAll("\\W", ".") + "_exception_" + DateTimeHelper.now("yyyy-MM-dd'T'HH.mm.ss.SSS") + "_" + ULID.generate();

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
		// [i] field:0:optional $type {&quot;security&quot;,&quot;strict&quot;,&quot;malformed&quot;,&quot;validation&quot;,&quot;duplicate&quot;,&quot;unsupported&quot;}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String message = IDataHelper.get(cursor, "$message", String.class);
		    String type = IDataHelper.get(cursor, "$type", String.class);

		    raise(message, type);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	/**
	 * Throws a new exception of the given type with the given message.
	 *
	 * @param message           The error message to use.
	 * @param type              The type of exception to throw.
	 * @throws ServiceException Always throws a new exception of the given type with the given message.
	 */
	public static void raise(String message, String type) throws ServiceException {
	    if (message == null) message = "";
	    if (type == null) type = "";

	    ServiceException ex = null;

	    if (type.equals("security")) {
	        ex = new SecurityException(message);
	    } else if (type.equals("strict")) {
	        ex = new StrictException(message);
	    } else if (type.equals("malformed")) {
	        ex = new MalformedException(message);
	    } else if (type.equals("validation")) {
	        ex = new ValidationException(message);
	    } else if (type.equals("duplicate")) {
	        ex = new DuplicateException(message);
	    } else if (type.equals("unsupported")) {
	        ex = new UnsupportedException(message);
	    } else {
	        ex = new BaseException(message);
	    }

	    throw ex;
	}
	// --- <<IS-END-SHARED>> ---
}

