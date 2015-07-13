package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-07-13 10:24:30.711
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
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
		  boolean content = Boolean.parseBoolean(IDataUtil.getString(cursor, "$content?"));

		  com.wm.app.tn.doc.BizDocEnvelope bizdoc = get(id, content);

		  if (bizdoc != null) {
		    IDataUtil.put(cursor, "$bizdoc", bizdoc);
		    IDataUtil.put(cursor, "$sender", tundra.tn.support.profile.cache.get(bizdoc.getSenderId()));
		    IDataUtil.put(cursor, "$receiver", tundra.tn.support.profile.cache.get(bizdoc.getReceiverId()));
		  }
		} catch (java.io.IOException ex) {
		  throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
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
		  boolean content = Boolean.parseBoolean(IDataUtil.getString(cursor, "$content?"));
		  boolean sender = Boolean.parseBoolean(IDataUtil.getString(cursor, "$sender?"));
		  boolean receiver = Boolean.parseBoolean(IDataUtil.getString(cursor, "$receiver?"));

		  com.wm.app.tn.doc.BizDocEnvelope output = normalize(input, content);

		  if (output != null) {
		    IDataUtil.put(cursor, "$bizdoc", output);
		    if (sender) IDataUtil.put(cursor, "$sender", tundra.tn.support.profile.cache.get(output.getSenderId()));
		    if (receiver) IDataUtil.put(cursor, "$receiver", tundra.tn.support.profile.cache.get(output.getReceiverId()));
		  }
		} catch (java.io.IOException ex) {
		  throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	// returns a full bizdoc if given either a subset or full bizdoc
	public static com.wm.app.tn.doc.BizDocEnvelope normalize(IData input, boolean content) throws ServiceException {
	  com.wm.app.tn.doc.BizDocEnvelope output = null;

	  if (input != null) {
	    if (input instanceof com.wm.app.tn.doc.BizDocEnvelope) {
	      output = (com.wm.app.tn.doc.BizDocEnvelope)input;
	      if (content && output.getContent() == null) output = get(output.getInternalId(), content);
	    } else {
	      IDataCursor cursor = input.getCursor();
	      String id = IDataUtil.getString(cursor, "InternalID");
	      cursor.destroy();

	      if (id == null) throw new IllegalArgumentException("InternalID is required");

	      output = get(id, content);
	    }
	  }

	  return output;
	}

	// returns the bizdoc associated with the given id
	public static com.wm.app.tn.doc.BizDocEnvelope get(String id) throws ServiceException {
	  return get(id, false);
	}

	// returns the bizdoc and optionally its content parts associated with the given id
	public static com.wm.app.tn.doc.BizDocEnvelope get(String id, boolean content) throws ServiceException {
	  com.wm.app.tn.doc.BizDocEnvelope bizdoc = null;

	  if (id != null) {
	    try {
	      bizdoc = com.wm.app.tn.db.BizDocStore.getDocument(id, content);
	    } catch(com.wm.app.tn.db.DatastoreException ex) {
	      throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	    }
	  }

	  return bizdoc;
	}
	// --- <<IS-END-SHARED>> ---
}

