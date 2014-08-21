package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-08-21 15:16:26.915
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
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
		  IData bizdoc = IDataUtil.getIData(cursor, "$bizdoc");
		  String partName = IDataUtil.getString(cursor, "$part");
		
		  if (bizdoc != null) {
		    ContentPart part = new ContentPart(bizdoc, partName);
		    java.io.InputStream content = part.getContent();
		
		    if (content != null) {
		      IDataUtil.put(cursor, "$content", content);
		      IDataUtil.put(cursor, "$content.type", part.getMimeType());
		    }
		  }
		} finally {
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// wrapper class for a bizdoc content part 
	public static class ContentPart {
	  protected com.wm.app.tn.doc.BizDocEnvelope bizdoc = null;
	  protected com.wm.app.tn.doc.BizDocContentPart part = null;
	
	  public ContentPart(IData bizdoc) throws ServiceException {
	    this(bizdoc, null);
	  }
	
	  public ContentPart(com.wm.app.tn.doc.BizDocEnvelope bizdoc) throws ServiceException {
	    this(bizdoc, null);
	  }
	
	  public ContentPart(IData bizdoc, String partName) throws ServiceException {
	    this(tundra.tn.document.normalize(bizdoc, true), partName);
	  }
	
	  public ContentPart(com.wm.app.tn.doc.BizDocEnvelope bizdoc, String partName) throws ServiceException {
	    this.bizdoc = bizdoc;
	
	    if (bizdoc != null) {
	      if (partName != null) {
	        this.part = bizdoc.getContentPart(partName);
	      } else {
	        com.wm.app.tn.doc.BizDocType type = bizdoc.getDocType();
	
	        if (type != null && type instanceof com.wm.app.tn.doc.XMLDocType) {
	          this.part = bizdoc.getContentPart("xmldata");
	        } else if (type != null && type instanceof com.wm.app.tn.doc.FFDocType) {
	          this.part = bizdoc.getContentPart("ffdata");
	        } else {
	          this.part = bizdoc.getContentPart("xmldata");
	          if (this.part == null) this.part = bizdoc.getContentPart("ffdata");
	          if (this.part == null) this.part = bizdoc.getContentPart("bytes");
	        }
	      }
	    }
	  }
	
	  // returns the name of this content part
	  public String getName() {
	    return part == null ? null : part.getPartName();
	  }
	
	  // returns the content associated with this content part
	  public java.io.InputStream getContent() throws ServiceException {
	    java.io.InputStream stream = null;
	    if (part != null) {
	      try {
	        Object content = part.getContent(bizdoc.getInternalId());
	        if (content != null) {
	          if (content instanceof java.io.InputStream) {
	            stream = (java.io.InputStream)content;
	          } else {
	            stream = new java.io.ByteArrayInputStream((byte[])content);
	          }
	        }
	      } catch(java.io.IOException ex) {
	        throw new ServiceException(ex.getClass().getName() + ": " + ex.getMessage());
	      }  
	    }
	    return stream;
	  }
	
	  // returns the mimeType associated with this content part
	  public String getMimeType() {
	    String mimeType = "application/octet-stream";
	    if (part != null) {
	      mimeType = part.getMimeType();
	    }
	    return mimeType;
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

