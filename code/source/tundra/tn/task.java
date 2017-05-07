package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2017-05-07 20:10:38 EST
// -----( ON-HOST: 192.168.66.129

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.delivery.GuaranteedJobHelper;
// --- <<IS-END-IMPORTS>> ---

public final class task

{
	// ---( internal utility methods )---

	final static task _instance = new task();

	static task _newInstance() { return new task(); }

	static task _cast(Object o) { return (task)o; }

	// ---( server methods )---




	public static final void restart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(restart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $task
		// [i] - field:0:required TaskId
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData task = IDataHelper.get(cursor, "$task", IData.class);
		    GuaranteedJobHelper.restart(GuaranteedJobHelper.normalize(task));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

