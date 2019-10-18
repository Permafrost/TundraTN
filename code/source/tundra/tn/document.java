package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-10-18T16:38:32.694
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocContentPart;
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.doc.BizDocType;
import com.wm.app.tn.err.ActivityLogEntry;
import java.io.InputStream;
import java.io.IOException;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.lang.UnrecoverableException;
import permafrost.tundra.tn.document.attribute.transform.Transformer;
import permafrost.tundra.tn.document.attribute.transform.number.ImminentPrioritizer;
import permafrost.tundra.tn.document.BizDocContentHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
import permafrost.tundra.tn.document.BizDocTypeHelper;
import permafrost.tundra.tn.profile.ProfileCache;
// --- <<IS-END-IMPORTS>> ---

public final class document

{
	// ---( internal utility methods )---

	final static document _instance = new document();

	static document _newInstance() { return new document(); }

	static document _cast(Object o) { return (document)o; }

	// ---( server methods )---




	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $id
		// [i] field:0:optional $content? {"false","true"}
		// [i] field:0:optional $raise? {"false","true"}
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] record:0:optional $sender
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		// [o] record:0:optional $receiver
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String id = IDataHelper.get(cursor, "$id", String.class);
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, false);

		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.get(id, content);

		    if (bizdoc == null) {
		        if (raise) throw new UnrecoverableException("Trading Networks document does not exist: " + id);
		    } else {
		        IDataHelper.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataHelper.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void normalize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(normalize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [i] field:0:optional $content? {"false","true"}
		// [i] field:0:optional $sender? {"false","true"}
		// [i] field:0:optional $receiver? {"false","true"}
		// [i] field:0:optional $raise? {"false","true"}
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] record:0:optional $sender
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		// [o] record:0:optional $receiver
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IData input = IDataHelper.get(cursor, "$bizdoc", IData.class);
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		    boolean sender = IDataHelper.getOrDefault(cursor, "$sender?", Boolean.class, false);
		    boolean receiver = IDataHelper.getOrDefault(cursor, "$receiver?", Boolean.class, false);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, false);

		    BizDocEnvelope output = BizDocEnvelopeHelper.normalize(input, content);

		    if (output == null) {
		        if (raise && input != null) {
		            IDataCursor inputCursor = input.getCursor();
		            String id = IDataUtil.getString(inputCursor, "InternalID");
		            inputCursor.destroy();

		            throw new UnrecoverableException("Trading Networks document does not exist: " + id);
		        }
		    } else {
		        IDataHelper.put(cursor, "$bizdoc", output);
		        if (sender || receiver) {
		            ProfileCache cache = ProfileCache.getInstance();
		            if (sender) IDataHelper.put(cursor, "$sender", cache.get(output.getSenderId()));
		            if (receiver) IDataHelper.put(cursor, "$receiver", cache.get(output.getReceiverId()));
		        }
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	public static class Content {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
	            String partName = IDataHelper.get(cursor, "$part", String.class);
	            String mode = IDataHelper.get(cursor, "$mode", String.class);

	            if (document != null) {
	                BizDocContentPart contentPart = BizDocContentHelper.getContentPart(document, partName);
	                InputStream content = BizDocContentHelper.getContent(document, contentPart);

	                if (content != null) {
	                    if (mode != null && !mode.equals("stream")) {
	                        IDataHelper.put(cursor, "$content", ObjectHelper.convert(content, mode));
	                    } else {
	                        IDataHelper.put(cursor, "$content", content);
	                    }
	                    IDataHelper.put(cursor, "$content.type", BizDocContentHelper.getContentType(contentPart));
	                    IDataHelper.put(cursor, "$part", contentPart.getPartName(), false);
	                }
	            }
	        } catch(IOException ex) {
	            ExceptionHelper.raise(ex);
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void remove(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            String partName = IDataHelper.get(cursor, "$part", String.class);

	            BizDocContentHelper.removeContentPart(document, partName);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class Duplicate {
	    public static void check(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "bizdoc", IData.class));
	            IDataHelper.put(cursor, "duplicate", BizDocEnvelopeHelper.isDuplicate(bizdoc), String.class);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class Error {
	    public static void exists(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            IData bizdoc = IDataHelper.get(cursor, "$bizdoc", IData.class);
	            IData classes = IDataHelper.get(cursor, "$class", IData.class);

	            ActivityLogEntry[] errors = BizDocEnvelopeHelper.getErrors(bizdoc, classes);

	            IDataHelper.put(cursor, "$exists?", errors != null && errors.length > 0, String.class);
	            IDataHelper.put(cursor, "$errors", IDataHelper.normalize(errors), false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class Type {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            String id = IDataHelper.get(cursor, "$id", String.class);
	            String name = IDataHelper.get(cursor, "$name", String.class);

	            BizDocType type = null;

	            if (id != null) {
	                type = BizDocTypeHelper.get(id);
	            } else if (name != null) {
	                type = BizDocTypeHelper.getByName(name);
	            }

	            if (type != null) {
	                IDataHelper.put(cursor, "$type", IDataHelper.normalize((IData)type));
	            }
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void normalize(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocType type = BizDocTypeHelper.normalize(IDataHelper.get(cursor, "$type", IData.class));

	            if (type != null) {
	                IDataHelper.put(cursor, "$type", IDataHelper.normalize((IData)type));
	            }
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class AttributeNumberTransformerPriority {
	    public static void imminence(IData pipeline) throws ServiceException {
	        Transformer.transform(pipeline, new ImminentPrioritizer());
	    }
	}
	// --- <<IS-END-SHARED>> ---
}

