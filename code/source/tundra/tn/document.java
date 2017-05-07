package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:01:57 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
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
		// [i] field:0:optional $content? {"false","true"}
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] recref:0:optional $sender tundra.tn.schema:profile
		// [o] recref:0:optional $receiver tundra.tn.schema:profile
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String id = IDataHelper.get(cursor, "$id", String.class);
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.get(id, content);
		
		    if (bizdoc != null) {
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
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] recref:0:optional $sender tundra.tn.schema:profile
		// [o] recref:0:optional $receiver tundra.tn.schema:profile
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData input = IDataHelper.get(cursor, "$bizdoc", IData.class);
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		    boolean sender = IDataHelper.getOrDefault(cursor, "$sender?", Boolean.class, false);
		    boolean receiver = IDataHelper.getOrDefault(cursor, "$receiver?", Boolean.class, false);
		
		    BizDocEnvelope output = BizDocEnvelopeHelper.normalize(input, content);
		
		    if (output != null) {
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
}

