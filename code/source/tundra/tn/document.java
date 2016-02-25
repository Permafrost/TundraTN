package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-02-25 22:22:57 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
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
		// [i] field:0:optional $content? {&quot;false&quot;,&quot;true&quot;}
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String id = IDataUtil.getString(cursor, "$id");
		    boolean content = BooleanHelper.parse(IDataUtil.getString(cursor, "$content?"));
		
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.get(id, content);
		
		    if (bizdoc != null) {
		        IDataUtil.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataUtil.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataUtil.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
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
		// [i] field:0:optional $content? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $sender? {&quot;false&quot;,&quot;true&quot;}
		// [i] field:0:optional $receiver? {&quot;false&quot;,&quot;true&quot;}
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData input = IDataUtil.getIData(cursor, "$bizdoc");
		    boolean content = BooleanHelper.parse(IDataUtil.getString(cursor, "$content?"));
		    boolean sender = BooleanHelper.parse(IDataUtil.getString(cursor, "$sender?"));
		    boolean receiver = BooleanHelper.parse(IDataUtil.getString(cursor, "$receiver?"));
		
		    BizDocEnvelope output = BizDocEnvelopeHelper.normalize(input, content);
		
		    if (output != null) {
		        IDataUtil.put(cursor, "$bizdoc", output);
		        if (sender || receiver) {
		            ProfileCache cache = ProfileCache.getInstance();
		            if (sender) IDataUtil.put(cursor, "$sender", cache.get(output.getSenderId()));
		            if (receiver) IDataUtil.put(cursor, "$receiver", cache.get(output.getReceiverId()));
		        }
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

