package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2024-11-26 17:10:00 EST
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.profile.Destination;
import com.wm.app.tn.profile.Profile;
import com.wm.app.tn.profile.ProfileStore;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.cache.CacheManager;
import permafrost.tundra.tn.cache.ProfileCache;
import permafrost.tundra.tn.profile.DestinationHelper;
import permafrost.tundra.tn.profile.ExternalID;
import permafrost.tundra.tn.profile.InternalID;
import permafrost.tundra.tn.profile.ProfileHelper;
import permafrost.tundra.tn.profile.ProfileID;
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
		// [i] field:0:optional $refresh? {&quot;false&quot;,&quot;true&quot;}
		// [o] record:0:required $profile
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:required DisplayName
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
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
		    String identity = IDataHelper.get(cursor, "$id", String.class);
		    String type = IDataHelper.get(cursor, "$type", String.class);
		    boolean refresh = IDataHelper.getOrDefault(cursor, "$refresh?", Boolean.class, false);
		
		    if (identity != null) {
		        ProfileID profileID;
		        if (type == null) {
		            profileID = new InternalID(identity);
		        } else {
		            profileID = new ExternalID(identity, type);
		        }
		        IData profile = ProfileCache.getInstance().get(profileID, refresh);
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
		// [i] field:0:optional $refresh? {&quot;false&quot;,&quot;true&quot;}
		// [o] record:1:required $profiles
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:required DisplayName
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
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
		
		    ProfileCache.getInstance().seed(refresh);
		    IData[] profiles = ProfileCache.getInstance().list();
		
		    IDataHelper.put(cursor, "$profiles", profiles, false);
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
		// [i] field:0:optional $refresh? {&quot;false&quot;,&quot;true&quot;}
		// [o] record:0:required $profile
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
		// [o] - field:0:required DisplayName
		// [o] - field:0:optional DefaultID
		// [o] - record:0:optional ExternalID
		// [o] - record:0:optional ExtendedFields
		// [o] - record:0:optional DeliveryMethods
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
	        CacheManager.getInstance().start();
	    }
	}
	
	public static class Delivery {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();
	
	        try {
	            IData profile = IDataHelper.get(cursor, "$profile", IData.class);
	            String destinationName = IDataHelper.get(cursor, "$method", String.class);
	            IData destination = ProfileHelper.getDestination(profile, destinationName);
	
	            IDataHelper.put(cursor, "$delivery", destination, false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}
	// --- <<IS-END-SHARED>> ---
}

