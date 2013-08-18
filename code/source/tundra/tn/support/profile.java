package tundra.tn.support;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2013-08-17 21:54:21 EST
// -----( ON-HOST: 172.16.189.189

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




	public static final void _ (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(_)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	// returns the partner profile associated with the given ID (either an 
	// external ID with the given type, or an internal ID)
	public static IData get(String id, String type) throws com.wm.app.tn.profile.LookupStoreException, com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  if (type != null) {
	    int i = com.wm.app.tn.profile.LookupStore.getExternalIDType(type);
	    id = com.wm.app.tn.profile.ProfileStore.getInternalID(id, i);
	  }
	  return get(id);
	}
	
	// returns the partner profile associated with the given internal ID
	public static IData get(String id) throws com.wm.app.tn.profile.LookupStoreException, com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
	  com.wm.app.tn.profile.ProfileSummary summary = com.wm.app.tn.profile.ProfileStore.getProfileSummary(id);
	  if (summary == null) return null;
	
	  com.wm.app.tn.profile.Profile profile = com.wm.app.tn.profile.ProfileStore.getProfile(id);
	
	  IData output = normalize(profile, true);
	
	  IDataCursor cursor = output.getCursor();
	  IDataUtil.put(cursor, "ExternalID", getExternalIDs(profile));
	  IDataUtil.put(cursor, "ExtendedFields", getExtendedFields(id));
	  IDataUtil.put(cursor, "DeliveryMethods", getDelivery(profile));
	
	  cursor.destroy();
	
	  IDataUtil.merge(normalize(summary, true), output);
	
	  return output;
	}
	
	// returns the my enterprise profile
	public static IData self() throws com.wm.app.tn.profile.LookupStoreException, com.wm.app.tn.profile.ProfileStoreException, java.io.IOException {
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
	public static IData getExtendedFields(String id) throws com.wm.app.tn.profile.LookupStoreException, com.wm.app.tn.profile.ProfileStoreException {
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

