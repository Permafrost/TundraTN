package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2019-10-18T16:44:18.308
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.profile.ProfileStore;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.profile.ProfileCache;
// --- <<IS-END-IMPORTS>> ---

public final class profile

{
	// ---( internal utility methods )---

	final static profile _instance = new profile();

	static profile _newInstance() { return new profile(); }

	static profile _cast(Object o) { return (profile)o; }

	// ---( server methods )---




	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $id
		// [i] field:0:optional $type
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] record:0:required $profile
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    String id = IDataHelper.get(cursor, "$id", String.class);
		    String type = IDataHelper.get(cursor, "$type", String.class);
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);

		    if (id != null) {
		        IData profile = ProfileCache.getInstance().get(id, type, refresh);
		        IDataHelper.put(cursor, "$profile", profile, false);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void list (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(list)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] record:1:required $profiles
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);
		    IDataHelper.put(cursor, "$profiles", ProfileCache.getInstance().list(true, refresh), false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void self (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(self)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $refresh? {"false","true"}
		// [o] record:0:required $profile
		// [o] - recref:0:required Corporate wm.tn.rec:Corporation
		// [o] - recref:1:required Contact wm.tn.rec:Contact
		// [o] - recref:1:required Delivery wm.tn.rec:Delivery
		// [o] - recref:1:required ID wm.tn.rec:ExternalID
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
		// [o] -- recref:0:optional Preferred Protocol wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Primary HTTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary E-mail wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary FTPS wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTP wm.tn.rec:Delivery
		// [o] -- recref:0:optional Secondary HTTPS wm.tn.rec:Delivery
		// [o] - field:0:required ProfileID
		// [o] - field:0:required CorporationName
		// [o] - field:0:required OrgUnit
		// [o] - field:0:required Type
		// [o] - object:0:required Self?
		// [o] - field:0:required Status
		// [o] - field:0:required RemoteStatus
		// [o] - field:0:required PreferredProtocol
		// [o] - field:0:required PollingProtocol
		// [o] - field:0:required TNVersion
		// [o] - object:0:required Deleted?
		// [o] - object:0:required TimeToWait
		// [o] - object:0:required RetryLimit
		// [o] - object:0:required RetryFactor
		// [o] - field:1:required ProfileGroups
		// [o] - object:0:required RoutingOffStatus?
		IDataCursor cursor = pipeline.getCursor();

		try {
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);
		    IDataHelper.put(cursor, "$profile", ProfileCache.getInstance().self(refresh), false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	public static class Cache {
	    public static void clear(IData pipeline) throws ServiceException {
	        // first refresh the internal TN profile cache
	        ProfileStore.getProfileStore(true);
	        // then clear the TundraTN profile cache
	        ProfileCache.getInstance().clear();
	    }

	    public static void list(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            IDataHelper.put(cursor, "$cache", ProfileCache.getInstance().list());
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void refresh(IData pipeline) throws ServiceException {
	        // first refresh the internal TN profile cache
	        ProfileStore.getProfileStore(true);
	        // then refresh the TundraTN profile cache
	        ProfileCache.getInstance().refresh();
	    }

	    public static void seed(IData pipeline) throws ServiceException {
	        ProfileCache.getInstance().seed();
	    }
	}
	// --- <<IS-END-SHARED>> ---
}

