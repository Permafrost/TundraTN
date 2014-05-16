package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2014-05-16 15:54:02.616
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class profile

{
	// ---( internal utility methods )---

	final static profile _instance = new profile();

	static profile _newInstance() { return new profile(); }

	static profile _cast(Object o) { return (profile)o; }

	// ---( server methods )---




	public static final void clear (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(clear)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		cache.clear();
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
		  IDataUtil.put(cursor, "$cache", cache.list());
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
		try {
		  cache.refresh();
		} catch (com.wm.app.tn.profile.ProfileStoreException ex) {
		  raise(ex);
		} catch (java.io.IOException ex) {
		  raise(ex);
		}
		// --- <<IS-END>> ---


	}



	public static final void seed (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(seed)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		try {
		  cache.seed();
		} catch (com.wm.app.tn.profile.ProfileStoreException ex) {
		  raise(ex);
		} catch (java.io.IOException ex) {
		  raise(ex);
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	public static class cache {
	  // local partner profile cache
	  protected static java.util.Map<String, IData> profiles = new java.util.concurrent.ConcurrentHashMap<String, IData>();

	  // refreshes the local partner profile cache from the TN database
	  public static void refresh() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    java.util.Iterator<String> iterator = profiles.keySet().iterator();
	    while(iterator.hasNext()) {
	      tundra.tn.support.profile.get(iterator.next(), true);
	    }
	  }

	  // clears the local partner profile cache
	  public static void clear() {
	    profiles.clear();
	  }

	  // seeds the local partner profile cache with all partners
	  public static void seed() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    tundra.tn.support.profile.list(true);
	  }

	  // returns a list of all the locally cached partner profiles
	  public static IData[] list() {
	    return (IData[])profiles.values().toArray(new IData[0]);
	  }
	}

	// returns a list of all partner profiles
	public static IData[] list(boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  java.util.List summaries = com.wm.app.tn.profile.ProfileStore.getProfileSummaryList(false, false);
	  java.util.List<IData> output = new java.util.ArrayList<IData>(summaries.size());
	  java.util.Iterator iterator = summaries.iterator();

	  while(iterator.hasNext()) {
	    com.wm.app.tn.profile.ProfileSummary summary = (com.wm.app.tn.profile.ProfileSummary)iterator.next();
	    String id = summary.getProfileID();
	    output.add(get(id, reload));
	  }

	  return output.toArray(new IData[0]);
	}

	// returns a list of all partner profiles
	public static IData[] list() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return list(false);
	}

	// returns the partner profile associated with the given ID (either an
	// external ID with the given type, or an internal ID)
	public static IData get(String id, String type) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  if (type != null) {
	    Integer typeID = com.wm.app.tn.profile.LookupStore.getExternalIDType(type);
	    if (typeID == null) throw new com.wm.app.tn.profile.LookupStoreException("Trading Networks partner profile external ID type does not exist: " + type);
	    id = com.wm.app.tn.profile.ProfileStore.getInternalID(id, typeID);
	  }
	  return get(id);
	}

	// returns the partner profile associated with the given internal ID
	public static IData get(String id, boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  IData output = null;

	  if (!reload) output = cache.profiles.get(id);

	  if (reload || output == null) {
	    com.wm.app.tn.profile.ProfileSummary summary = com.wm.app.tn.profile.ProfileStore.getProfileSummary(id); // if id is null or doesn't exist, this call returns null

	    if (summary != null) {
	      com.wm.app.tn.profile.Profile profile = com.wm.app.tn.profile.ProfileStore.getProfile(id);

	      output = normalize(profile, true);

	      IDataCursor cursor = output.getCursor();
	      IDataUtil.put(cursor, "ExternalID", getExternalIDs(profile));
	      IDataUtil.put(cursor, "ExtendedFields", getExtendedFields(id));
	      IDataUtil.put(cursor, "DeliveryMethods", getDelivery(profile));

	      cursor.destroy();

	      IDataUtil.merge(normalize(summary, true), output);

	      // lazily cache the profiles when they are requested
	      cache.profiles.put(id, output);
	    }
	  }

	  return output;
	}

	// returns the partner profile associated with the given internal ID
	public static IData get(String id) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return get(id, false);
	}

	// returns the my enterprise profile
	public static IData self() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return get(com.wm.app.tn.profile.ProfileStore.getMyID());
	}

	// returns all external IDs associated with the given profile as an IData
	public static IData getExternalIDs(com.wm.app.tn.profile.Profile profile) throws com.wm.app.tn.profile.LookupStoreException {
	  IData output = IDataFactory.create();
	  IDataCursor cursor = output.getCursor();

	  java.util.Map<Integer, String> types = getExternalIDTypes();
	  java.util.Enumeration externalIDs = profile.getExternalIDs();

	  while(externalIDs.hasMoreElements()) {
	    com.wm.app.tn.profile.ID externalID = (com.wm.app.tn.profile.ID)externalIDs.nextElement();
	    if (externalID != null) {
	      IDataUtil.put(cursor, types.get(externalID.getIDType()), externalID.getExternalID());
	    }
	  }

	  cursor.destroy();

	  return output;
	}

	// returns all delivery methods associated with the given profile as an IData
	public static IData getDelivery(com.wm.app.tn.profile.Profile profile) {
	  java.util.Enumeration destinations = profile.getDestinations();
	  if (destinations == null) return null;

	  IData output = IDataFactory.create();
	  IDataCursor cursor = output.getCursor();

	  com.wm.app.tn.profile.Destination preferred = profile.getPreferredDestination();
	  if (preferred != null) IDataUtil.put(cursor, "Preferred Protocol", normalize(preferred, true));

	  while(destinations.hasMoreElements()) {
	    com.wm.app.tn.profile.Destination destination = (com.wm.app.tn.profile.Destination)destinations.nextElement();
	    if (destination != null) {
	      IDataUtil.put(cursor, destination.getProtocolDisplayName(), normalize(destination, true));
	    }
	  }
	  cursor.destroy();

	  return output;
	}

	// remaps external ID type hashtable so the ID is the key
	public static java.util.Map<Integer, String> getExternalIDTypes() throws com.wm.app.tn.profile.LookupStoreException {
	  java.util.Hashtable types = com.wm.app.tn.profile.LookupStore.getExternalIDTypes();
	  java.util.Map<Integer, String> output = new java.util.TreeMap<Integer, String>();

	  java.util.Enumeration keys = types.keys();
	  while(keys.hasMoreElements()) {
	    String key = (String)keys.nextElement();
	    Integer value = (Integer)types.get(key);
	    output.put(value, key);
	  }

	  return output;
	}

	// returns the extended fields for a given partner profile
	public static IData getExtendedFields(String id) throws com.wm.app.tn.profile.ProfileStoreException {
	  IData output = IDataFactory.create();
	  IDataCursor cursor = output.getCursor();

	  java.util.Hashtable groups = com.wm.app.tn.profile.LookupStore.getFieldGroups();
	  if (groups != null) {
	    java.util.Enumeration keys = groups.keys();

	    while(keys.hasMoreElements()) {
	      String groupName = (String)keys.nextElement();
	      int groupID = (Short)groups.get(groupName);

	      IData group = IDataFactory.create();
	      IDataCursor gc = group.getCursor();

	      java.util.Vector fields = com.wm.app.tn.profile.ProfileStore.getExtendedFields(id, groupID);
	      int size = fields.size();

	      for (int i = 0; i < size; i++) {
	        com.wm.app.tn.profile.ExtendedProfileField field = (com.wm.app.tn.profile.ExtendedProfileField)fields.get(i);
	        if (field != null) {
	          String name = field.getName();
	          Object value = field.getValue();
	          if (value != null) IDataUtil.put(gc, name, value);
	        }
	      }
	      gc.destroy();

	      if (size > 0) IDataUtil.put(cursor, groupName, group);
	    }
	  }
	  cursor.destroy();

	  return output;
	}

	// throws a new ServiceException with the class and message from the given Throwable, which
	// is useful because java services are hard-wired to only throw ServiceExceptions
	public static void raise(Throwable exception) throws ServiceException {
	  if (exception != null) {
	    if (exception instanceof ServiceException) {
	      throw (ServiceException)exception;
	    } else {
	      raise(message(exception));
	    }
	  }
	}

	// throws a new ServiceException with the given message
	public static void raise(String message) throws ServiceException {
	  throw new ServiceException(message == null ? "" : message);
	}

	// returns an exception as a string
	public static String message(Throwable exception) {
	  String message = "";

	  if (exception != null) {
	    if (exception instanceof ServiceException) {
	      message = exception.getMessage();
	    } else {
	      message = exception.getClass().getName() + ": " + exception.getMessage();
	    }
	  }

	  return message;
	}

	// returns a new IData document, where all IDatas are implemented with the same class, and all
	// fully qualified keys are replaced with a nested structure
	public static IData normalize(IData input, boolean recurse) {
	  if (input == null) return null;

	  IData output = IDataFactory.create();
	  IDataCursor ic = input.getCursor();
	  IDataCursor oc = output.getCursor();

	  try {
	    while(ic.next()) {
	      String key = ic.getKey();
	      Object value = ic.getValue();

	      if (value != null) {
	        if (value instanceof com.wm.util.coder.IDataCodable) {
	          value = ((com.wm.util.coder.IDataCodable)value).getIData();
	        } else if (value instanceof com.wm.util.coder.ValuesCodable) {
	          value = ((com.wm.util.coder.ValuesCodable)value).getValues();
	        } else if (value instanceof com.wm.util.coder.IDataCodable[]) {
	          value = normalize((com.wm.util.coder.IDataCodable[])value, recurse);
	        } else if (value instanceof com.wm.util.coder.ValuesCodable[]) {
	          value = normalize((com.wm.util.coder.ValuesCodable[])value, recurse);
	        } else if (value instanceof IData[]) {
	          value = normalize((IData[])value, recurse);
	        } else if (value instanceof com.wm.util.Table) {
	          value = normalize(((com.wm.util.Table)value).getValues(), recurse);
	        } else if (recurse && value instanceof IData) {
	          value = normalize((IData)value, recurse);
	        }
	        IDataUtil.put(oc, key, value);
	      }
	    }
	  } finally {
	    ic.destroy();
	  }

	  return output;
	}

	// normalizes an IData[], where all IDatas are implemented with the same class, and all
	// fully qualified keys are replaced with a nested structure
	public static IData[] normalize(IData[] input, boolean recurse) {
	  if (input == null) return null;

	  IData[] output = new IData[input.length];
	  for (int i = 0; i < input.length; i++) {
	    output[i] = normalize(input[i], recurse);
	  }

	  return output;
	}

	// converts an IDataCodable[] to an IData[] and normalizes, where all IDatas are implemented
	// with the same class, and all fully qualified keys are replaced with a nested structure
	public static IData[] normalize(com.wm.util.coder.IDataCodable[] input, boolean recurse) {
	  if (input == null) return null;

	  IData[] output = new IData[input.length];
	  for (int i = 0; i < input.length; i++) {
	    output[i] = normalize(input[i].getIData(), recurse);
	  }

	  return output;
	}

	// converts a ValuesCodable[] to an IData[] and normalizes, where all IDatas are implemented
	// with the same class, and all fully qualified keys are replaced with a nested structure
	public static IData[] normalize(com.wm.util.coder.ValuesCodable[] input, boolean recurse) {
	  if (input == null) return null;

	  IData[] output = new IData[input.length];
	  for (int i = 0; i < input.length; i++) {
	    output[i] = normalize(input[i].getValues(), recurse);
	  }

	  return output;
	}
	// --- <<IS-END-SHARED>> ---
}

