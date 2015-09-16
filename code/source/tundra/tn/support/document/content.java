package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-09-16 20:47:00 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocContentPart;
import com.wm.app.tn.doc.BizDocEnvelope;
import java.io.InputStream;
import permafrost.tundra.tn.document.BizDocContentHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class content

{
	// ---( internal utility methods )---

	final static content _instance = new content();

	static content _newInstance() { return new content(); }

	static content _cast(Object o) { return (content)o; }

	// ---( server methods )---




	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:optional $part
		// [o] object:0:optional $content
		// [o] field:0:optional $content.type
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"), true);
		    String partName = IDataUtil.getString(cursor, "$part");
		
		    if (document != null) {
		        BizDocContentPart contentPart = BizDocContentHelper.getContentPart(document, partName);
		        InputStream content = BizDocContentHelper.getContent(document, contentPart);
		
		        if (content != null) {
		            IDataUtil.put(cursor, "$content", content);
		            IDataUtil.put(cursor, "$content.type", BizDocContentHelper.getContentType(contentPart));
		        }
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void remove (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(remove)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:required $part
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"));
		    String partName = IDataUtil.getString(cursor, "$part");
		
		    BizDocContentHelper.removeContentPart(document, partName);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

