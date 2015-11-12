package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-11-12 15:31:40.509
// -----( ON-HOST: -

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
		GuaranteedJobHelper.restart(GuaranteedJobHelper.get((String)IDataHelper.get(pipeline, "$task/TaskId")));
		// --- <<IS-END>> ---


	}
}

