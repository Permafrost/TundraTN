package tundra.tn.support.document.attribute.number.transformer;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-06-10T08:36:03.390
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.tn.document.attribute.transform.Transformer;
import permafrost.tundra.tn.document.attribute.transform.number.ImminentPrioritizer;
// --- <<IS-END-IMPORTS>> ---

public final class priority

{
	// ---( internal utility methods )---

	final static priority _instance = new priority();

	static priority _newInstance() { return new priority(); }

	static priority _cast(Object o) { return (priority)o; }

	// ---( server methods )---




	public static final void imminence (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(imminence)>> ---
		// @specification tundra.tn.schema.document.attribute.number:transformer
		// @subtype unknown
		// @sigtype java 3.5
		Transformer.transform(pipeline, new ImminentPrioritizer());
		// --- <<IS-END>> ---


	}
}

