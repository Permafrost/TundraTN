package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-06-14T15:40:25.912
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.route.RoutingRule;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.route.RoutingRuleHelper;
// --- <<IS-END-IMPORTS>> ---

public final class rule

{
	// ---( internal utility methods )---

	final static rule _instance = new rule();

	static rule _newInstance() { return new rule(); }

	static rule _cast(Object o) { return (rule)o; }

	// ---( server methods )---




	public static final void execute (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(execute)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] recref:0:optional bizdoc wm.tn.rec:BizDocEnvelope
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional $bypassRouting {"false","true"}
		// [i] object:0:optional rule
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = IDataHelper.get(cursor, "bizdoc", BizDocEnvelope.class);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);
		    RoutingRule rule = IDataHelper.get(cursor, "rule", RoutingRule.class);

		    RoutingRuleHelper.execute(rule, bizdoc, parameters);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void select (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(select)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] recref:0:optional bizdoc wm.tn.rec:BizDocEnvelope
		// [i] record:0:optional TN_parms
		// [i] - field:0:required processingRuleID
		// [i] - field:0:required processingRuleName
		// [o] object:0:optional rule
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = IDataHelper.get(cursor, "bizdoc", BizDocEnvelope.class);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    IDataHelper.put(cursor, "rule", RoutingRuleHelper.select(bizdoc, parameters, true));
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}
}

