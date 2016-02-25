package tundra.tn.support.document;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-02-25 22:28:21 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.sql.SQLException;
import com.wm.app.tn.doc.BizDocEnvelope;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.tn.delivery.GuaranteedJobHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
// --- <<IS-END-IMPORTS>> ---

public final class task

{
	// ---( internal utility methods )---

	final static task _instance = new task();

	static task _newInstance() { return new task(); }

	static task _cast(Object o) { return (task)o; }

	// ---( server methods )---




	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc
		// [i] - field:0:required InternalID
		// [o] field:0:required $tasks.length
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataUtil.getIData(cursor, "$bizdoc"));
		    IData[] tasks = IDataHelper.normalize(GuaranteedJobHelper.list(bizdoc));
		
		    if (tasks != null && tasks.length > 0) {
		        IDataUtil.put(cursor, "$tasks", tasks);
		        IDataUtil.put(cursor, "$tasks.length", "" + tasks.length);
		    } else {
		        IDataUtil.put(cursor, "$tasks.length", "0");
		    }
		} catch(SQLException ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

