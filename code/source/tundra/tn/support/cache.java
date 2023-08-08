package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2023-08-08 05:50:06 EST
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.cache.CacheManager;
// --- <<IS-END-IMPORTS>> ---

public final class cache

{
	// ---( internal utility methods )---

	final static cache _instance = new cache();

	static cache _newInstance() { return new cache(); }

	static cache _cast(Object o) { return (cache)o; }

	// ---( server methods )---




	public static final void reflect (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reflect)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IDataHelper.put(cursor, "$cache.manager", CacheManager.getInstance().getIData());
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void refresh (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(refresh)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		CacheManager.getInstance().refresh();
		// --- <<IS-END>> ---

                
	}



	public static final void reset (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reset)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		CacheManager.getInstance().reset();
		// --- <<IS-END>> ---

                
	}



	public static final void restart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(restart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		CacheManager.getInstance().restart();
		// --- <<IS-END>> ---

                
	}



	public static final void start (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(start)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		CacheManager.getInstance().start();
		// --- <<IS-END>> ---

                
	}



	public static final void stop (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stop)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		CacheManager.getInstance().stop();
		// --- <<IS-END>> ---

                
	}
}

