package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-12-13T16:17:18.873
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import java.nio.charset.Charset;
import javax.activation.MimeType;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
import permafrost.tundra.tn.profile.ProfileCache;
// --- <<IS-END-IMPORTS>> ---

public final class content

{
	// ---( internal utility methods )---

	final static content _instance = new content();

	static content _newInstance() { return new content(); }

	static content _cast(Object o) { return (content)o; }

	// ---( server methods )---




	public static final void recognize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(recognize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $content
		// [i] field:0:optional $content.identity {"UUID","ULID","SHA-512","SHA-384","SHA-256","SHA","MD5","MD2"}
		// [i] record:0:optional $content.namespace
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional $contentType
		// [i] - field:0:optional $contentEncoding
		// [i] - field:0:optional $contentName
		// [i] - field:0:optional $contentLength
		// [i] - field:0:optional $contentSchema
		// [i] - field:0:optional SenderID
		// [i] - field:0:optional ReceiverID
		// [i] - field:0:optional DocumentID
		// [i] - field:0:optional DoctypeID
		// [i] - field:0:optional DoctypeName
		// [i] - field:0:optional GroupID
		// [i] - field:0:optional ConversationID
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] record:0:optional TN_parms
		// [o] - field:0:optional $contentType
		// [o] - field:0:optional $contentEncoding
		// [o] - field:0:optional $contentName
		// [o] - field:0:optional $contentLength
		// [o] - field:0:optional $contentSchema
		// [o] - field:0:optional SenderID
		// [o] - field:0:optional ReceiverID
		// [o] - field:0:optional DocumentID
		// [o] - field:0:optional DoctypeID
		// [o] - field:0:optional DoctypeName
		// [o] - field:0:optional GroupID
		// [o] - field:0:optional ConversationID
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Object content = IDataHelper.get(cursor, "$content");
		    String contentIdentity = IDataHelper.get(cursor, "$content.identity", String.class);
		    IData contentNamespace = IDataHelper.first(cursor, IData.class, "$content.namespace", "$namespace");
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.recognize(content, contentIdentity, contentNamespace, parameters);

		    IDataHelper.put(cursor, "$bizdoc", bizdoc, false);
		    IDataHelper.put(cursor, "TN_parms", parameters, false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void route (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(route)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional $content
		// [i] field:0:optional $content.type
		// [i] field:0:optional $content.encoding
		// [i] field:0:optional $content.schema
		// [i] record:0:optional $content.namespace
		// [i] field:0:optional $content.identity {"UUID","ULID","SHA-512","SHA-384","SHA-256","SHA","MD5","MD2"}
		// [i] record:0:optional $attributes
		// [i] field:0:optional $strict? {"true","false"}
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional $contentType
		// [i] - field:0:optional $contentEncoding
		// [i] - field:0:optional $contentName
		// [i] - field:0:optional $contentLength
		// [i] - field:0:optional $contentSchema
		// [i] - field:0:optional SenderID
		// [i] - field:0:optional ReceiverID
		// [i] - field:0:optional DocumentID
		// [i] - field:0:optional DoctypeID
		// [i] - field:0:optional DoctypeName
		// [i] - field:0:optional GroupID
		// [i] - field:0:optional ConversationID
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
		// [o] record:0:optional TN_parms
		// [o] - field:0:optional $contentType
		// [o] - field:0:optional $contentEncoding
		// [o] - field:0:optional $contentName
		// [o] - field:0:optional $contentLength
		// [o] - field:0:optional $contentSchema
		// [o] - field:0:optional SenderID
		// [o] - field:0:optional ReceiverID
		// [o] - field:0:optional DocumentID
		// [o] - field:0:optional DoctypeID
		// [o] - field:0:optional DoctypeName
		// [o] - field:0:optional GroupID
		// [o] - field:0:optional ConversationID
		IDataCursor cursor = pipeline.getCursor();

		try {
		    Object content = IDataHelper.first(cursor, Object.class, "$content", "stream", "node", "ffdata", "content", "contentStream", "jsonStream", "iDocList", "JMSMessage");
		    MimeType contentType = IDataHelper.get(cursor, "$content.type", MimeType.class);
		    Charset contentEncoding = IDataHelper.first(cursor, Charset.class, "$content.encoding", "$encoding");
		    String contentSchema = IDataHelper.first(cursor, String.class, "$content.schema", "$schema");
		    IData contentNamespace = IDataHelper.first(cursor, IData.class, "$content.namespace", "$namespace");
		    String contentIdentity = IDataHelper.get(cursor, "$content.identity", String.class);
		    IData attributes = IDataHelper.get(cursor, "$attributes", IData.class);
		    boolean strict = IDataHelper.getOrDefault(cursor, "$strict?", Boolean.class, true);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    BizDocEnvelope document = BizDocEnvelopeHelper.route(content, contentIdentity, contentType, contentEncoding, contentNamespace, contentSchema, attributes, parameters, pipeline, strict);

		    if (document != null) {
		        IDataHelper.put(cursor, "$bizdoc", document);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(document.getSenderId()), false);
		        IDataHelper.put(cursor, "$receiver", cache.get(document.getReceiverId()), false);
		        IDataHelper.put(cursor, "TN_parms", parameters, false);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

