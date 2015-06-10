package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2015-06-10 16:29:33.927
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
		} catch(java.io.IOException ex) {
		  tundra.tn.exception.raise(ex);
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
		} catch (java.io.IOException ex) {
		  tundra.tn.exception.raise(ex);
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
		} catch (java.io.IOException ex) {
		  tundra.tn.exception.raise(ex);
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	// represents both internal and external profile identities
	public static class ProfileID {
	  protected String type = null;
	  protected String value = null;

	  public ProfileID(String value) {
	    this(value, null);
	  }

	  public ProfileID(String value, String type) {
	    this.value = value;
	    this.type = type;
	  }

	  public boolean equals(Object obj) {
	    boolean result = false;
	    if (obj != null) {
	      if (obj.getClass() == this.getClass()) {
	        ProfileID other = (ProfileID)obj;
	        result = ((this.getType() == null && other.getType() == null) || (this.getType().equals(other.getType()))) && (this.getValue().equals(other.getValue()));
	      }
	    }
	    return result;
	  }

	  public int hashCode() {
	    String type = this.getType();
	    String value = this.getValue();

	    int hash = value.hashCode();
	    if (type != null) hash = hash ^ type.hashCode(); // xor the two hashes

	    return hash;
	  }

	  // returns the ID's type if it is an external ID, or null if it is an internal ID
	  public String getType() {
	    return type;
	  }

	  // returns the ID's value
	  public String getValue() {
	    return value;
	  }

	  // returns true if this is an internal ID
	  public boolean isInternal() {
	    return type == null;
	  }

	  // returns true if this is an external ID
	  public boolean isExternal() {
	    return !isInternal();
	  }

	  // returns an internal ID representation of this ID, if it exists
	  public ProfileID toInternalID() throws com.wm.app.tn.profile.ProfileStoreException {
	    ProfileID output = null;

	    if (this.isExternal()) {
	      Integer typeID = com.wm.app.tn.profile.LookupStore.getExternalIDType(this.getType());
	      if (typeID == null) throw new com.wm.app.tn.profile.LookupStoreException("Trading Networks partner profile external ID type does not exist: " + this.getType());

	      if (this.getValue() != null) {
	        String internalID = com.wm.app.tn.profile.ProfileStore.getInternalID(this.getValue(), typeID);
	        if (internalID != null) output = new ProfileID(internalID);
	      }
	    } else {
	      output = this;
	    }

	    return output;
	  }
	}

	// returns the partner profile associated with the given internal or external ID from the Trading Networks database
	public static IData get(ProfileID id) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  IData output = null;

	  if (id != null) id = id.toInternalID(); // normalize to internal ID

	  if (id != null) {
	    com.wm.app.tn.profile.ProfileSummary summary = com.wm.app.tn.profile.ProfileStore.getProfileSummary(id.getValue()); // if id is null or doesn't exist, this call returns null
	    if (summary != null) {
	      com.wm.app.tn.profile.Profile profile = com.wm.app.tn.profile.ProfileStore.getProfile(id.getValue());
	      output = normalize(profile, true);

	      IDataCursor cursor = output.getCursor();
	      IDataUtil.put(cursor, "ExternalID", getExternalIDs(profile));
	      IDataUtil.put(cursor, "ExtendedFields", getExtendedFields(id.getValue()));
	      IDataUtil.put(cursor, "DeliveryMethods", getDelivery(profile));
	      cursor.destroy();

	      IDataUtil.merge(normalize(summary, true), output);
	    }
	  }

	  return output;
	}

	// returns the partner profile associated with the given internal ID from the Trading Networks database
	public static IData get(String id) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return get(new ProfileID(id));
	}

	// returns the partner profile associated with the given internal or external ID from the Trading Networks database
	public static IData get(String id, String type) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return get(new ProfileID(id, type));
	}

	// returns the my enterprise profile from the Trading Networks database
	public static IData self() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  return get(new ProfileID(com.wm.app.tn.profile.ProfileStore.getMyID()));
	}

	// returns a list of all partner profiles
	public static IData[] list() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  java.util.List summaries = com.wm.app.tn.profile.ProfileStore.getProfileSummaryList(false, false);
	  java.util.List<IData> output = new java.util.ArrayList<IData>(summaries.size());
	  java.util.Iterator iterator = summaries.iterator();

	  while(iterator.hasNext()) {
	    com.wm.app.tn.profile.ProfileSummary summary = (com.wm.app.tn.profile.ProfileSummary)iterator.next();
	    output.add(get(new ProfileID(summary.getProfileID())));
	  }

	  return output.toArray(new IData[0]);
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


	public static class cache {
	  // local partner profile cache
	  protected static java.util.Map<ProfileID, IData> profiles = new java.util.concurrent.ConcurrentHashMap<ProfileID, IData>();

	  // refreshes the local partner profile cache from the TN database
	  public static void refresh() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    java.util.Iterator<ProfileID> iterator = profiles.keySet().iterator();
	    while(iterator.hasNext()) get(iterator.next(), true);
	  }

	  // clears the local partner profile cache
	  public static void clear() {
	    profiles.clear();
	  }

	  // seeds the local partner profile cache with all partners
	  public static void seed() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    java.util.List summaries = com.wm.app.tn.profile.ProfileStore.getProfileSummaryList(false, false);
	    java.util.Iterator iterator = summaries.iterator();

	    while(iterator.hasNext()) {
	      com.wm.app.tn.profile.ProfileSummary summary = (com.wm.app.tn.profile.ProfileSummary)iterator.next();
	      get(new ProfileID(summary.getProfileID()));
	    }
	  }

	  // returns a list of all the locally cached partner profiles, optionally seeded from the Trading Networks database
	  public static IData[] list(boolean seed) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    if (seed) seed();

	    java.util.List<IData> list = new java.util.ArrayList(profiles.size());
	    java.util.Iterator<ProfileID> iterator = profiles.keySet().iterator();

	    while(iterator.hasNext()) {
	      ProfileID id = iterator.next();
	      // only return the profiles associated with an internal ID
	      if (id.isInternal()) list.add(normalize(profiles.get(id), true));
	    }

	    return (IData[])list.toArray(new IData[0]);
	  }

	  // returns a list of all the locally cached partner profiles
	  public static IData[] list() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return list(false);
	  }

	  // returns the given profile from the cache, if it exists
	  public static IData get(ProfileID id, boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    IData profile = null;

	    if (id != null) {
	      if (!reload && profiles.containsKey(id)) {
	        profile = profiles.get(id);
	      } else {
	        profile = tundra.tn.support.profile.get(id);
	        if (profile != null) {
	          if (id.isExternal()) {
	            put(id, profile); // cache profile against external ID
	            id = id.toInternalID();
	          }

	          if (id != null) put(id, profile); // cache profile against internal ID
	        }
	      }
	    }

	    return normalize(profile, true);
	  }

	  // returns the given profile from the cache, if it exists
	  public static IData get(ProfileID id) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return get(id, false);
	  }

	  // returns the partner profile associated with the given internal ID
	  public static IData get(String id) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return get(new ProfileID(id), false);
	  }

	  // returns the partner profile associated with the given internal ID
	  public static IData get(String id, boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return get(new ProfileID(id), reload);
	  }

	  // returns the partner profile associated with the given internal or external ID
	  public static IData get(String id, String type) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return get(id, type, false);
	  }

	  // returns the partner profile associated with the given internal or external ID
	  public static IData get(String id, String type, boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return get(new ProfileID(id, type), reload);
	  }

	  // adds the given profile to the cache
	  public static IData put(ProfileID id, IData profile) {
	    return profiles.put(id, profile);
	  }

	  // removes the given profile to the cache
	  public static IData remove(ProfileID id) {
	    return profiles.remove(id);
	  }

	  // returns the my enterprise profile from the profile cache if cached, or from the Trading Networks database if
	  // not cached (at which time it will be lazily cached)
	  public static IData self(boolean reload) throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    IData output = null;

	    String id = com.wm.app.tn.profile.ProfileStore.getMyID();
	    if (id != null) {
	      output = get(new ProfileID(id), reload);
	    }

	    return output;
	  }

	  // returns the my enterprise profile from the profile cache if cached, or from the Trading Networks database if
	  // not cached (at which time it will be lazily cached)
	  public static IData self() throws com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	    return self(false);
	  }
	}
	// --- <<IS-END-SHARED>> ---
}

