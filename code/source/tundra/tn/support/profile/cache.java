package tundra.tn.support.profile;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2016-10-31 14:56:44.246
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.profile.ProfileStore;
import permafrost.tundra.tn.profile.ProfileCache;
// --- <<IS-END-IMPORTS>> ---

public final class cache

{
	// ---( internal utility methods )---

	final static cache _instance = new cache();

	static cache _newInstance() { return new cache(); }

	static cache _cast(Object o) { return (cache)o; }

	// ---( server methods )---




	public static final void clear (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(clear)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// first refresh the internal TN profile cache
		ProfileStore.getProfileStore(true);

		// then clear the TundraTN profile cache
		ProfileCache.getInstance().clear();
		// --- <<IS-END>> ---


	}



	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		IDataCursor cursor = pipeline.getCursor();

		try {
		    IDataUtil.put(cursor, "$cache", ProfileCache.getInstance().list());
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
		// first refresh the internal TN profile cache
		ProfileStore.getProfileStore(true);

		// then refresh the TundraTN profile cache
		ProfileCache.getInstance().refresh();
		// --- <<IS-END>> ---


	}



	public static final void seed (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(seed)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		ProfileCache.getInstance().seed();
		// --- <<IS-END>> ---


	}
}

