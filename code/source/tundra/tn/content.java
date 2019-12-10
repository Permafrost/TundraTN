package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-12-10T09:37:58.467
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
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
}

