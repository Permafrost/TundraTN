package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2023-08-09 05:52:38 EST
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
import permafrost.tundra.tn.cache.ProfileCache;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
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
		// [i] field:0:optional $content.identity {&quot;UUID&quot;,&quot;ULID&quot;,&quot;SHA-512&quot;,&quot;SHA-384&quot;,&quot;SHA-256&quot;,&quot;SHA&quot;,&quot;MD5&quot;,&quot;MD2&quot;}
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
		
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.recognize(content, contentIdentity, contentNamespace, parameters, pipeline);
		
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
		// [i] field:0:optional $content.identity {&quot;UUID&quot;,&quot;ULID&quot;,&quot;SHA-512&quot;,&quot;SHA-384&quot;,&quot;SHA-256&quot;,&quot;SHA&quot;,&quot;MD5&quot;,&quot;MD2&quot;}
		// [i] record:0:optional $attributes
		// [i] field:0:optional $strict? {&quot;true&quot;,&quot;false&quot;}
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
		// [o] record:0:optional $sender
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
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
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
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

