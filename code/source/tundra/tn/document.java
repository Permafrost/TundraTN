package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2020-02-12T06:13:04.022
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.delivery.DeliveryQueue;
import com.wm.app.tn.doc.BizDocContentPart;
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.doc.BizDocType;
import com.wm.app.tn.err.ActivityLogEntry;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.io.InputStreamHelper;
import permafrost.tundra.lang.BooleanHelper;
import permafrost.tundra.lang.ExceptionHelper;
import permafrost.tundra.lang.ObjectHelper;
import permafrost.tundra.lang.UnrecoverableException;
import permafrost.tundra.tn.delivery.DeliveryQueueHelper;
import permafrost.tundra.tn.document.attribute.transform.Transformer;
import permafrost.tundra.tn.document.attribute.transform.number.ImminentPrioritizer;
import permafrost.tundra.tn.document.attribute.transform.string.ProfileFinder;
import permafrost.tundra.tn.document.BizDocAttributeHelper;
import permafrost.tundra.tn.document.BizDocContentHelper;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
import permafrost.tundra.tn.document.BizDocTypeHelper;
import permafrost.tundra.tn.log.ActivityLogHelper;
import permafrost.tundra.tn.log.EntryType;
import permafrost.tundra.tn.profile.ProfileCache;
import permafrost.tundra.tn.profile.ProfileHelper;
// --- <<IS-END-IMPORTS>> ---

public final class document

{
	// ---( internal utility methods )---

	final static document _instance = new document();

	static document _newInstance() { return new document(); }

	static document _cast(Object o) { return (document)o; }

	// ---( server methods )---




	public static final void enqueue (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(enqueue)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:required $queue
		// [i] field:0:optional $force? {"false","true"}
		// [o] record:0:optional $task
		// [o] - field:0:required TaskId
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
		    DeliveryQueue queue = DeliveryQueueHelper.get(IDataHelper.get(cursor, "$queue", String.class));
		    boolean force = IDataHelper.getOrDefault(cursor, "$force?", Boolean.class, false);
		    String summary = IDataHelper.get(cursor, "$tundra.tn.document.enqueue.log.message.summary.prefix", String.class);
		    IData context = IDataHelper.get(cursor, "$tundra.tn.document.enqueue.log.context", IData.class);

		    IDataHelper.put(cursor, "$task", BizDocEnvelopeHelper.enqueue(document, queue, force, summary, context), false);
		} catch(Exception ex) {
		    ExceptionHelper.raise(ex);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional $id
		// [i] field:0:optional $content? {"false","true"}
		// [i] field:0:optional $raise? {"false","true"}
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] record:0:optional $sender
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
		// [o] record:0:optional $receiver
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
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, false);

		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.get(id, content);

		    if (bizdoc == null) {
		        if (raise) throw new UnrecoverableException("Trading Networks document does not exist: " + id);
		    } else {
		        IDataHelper.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataHelper.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void normalize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(normalize)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:optional $content? {"false","true"}
		// [i] field:0:optional $sender? {"false","true"}
		// [i] field:0:optional $receiver? {"false","true"}
		// [i] field:0:optional $raise? {"false","true"}
		// [o] record:0:optional $bizdoc
		// [o] - field:0:required InternalID
		// [o] - record:0:required DocType
		// [o] -- field:0:required TypeID
		// [o] -- field:0:required TypeName
		// [o] -- field:0:optional TypeDescription
		// [o] -- record:0:optional Deleted
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional Hidden
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional SenderFromSession
		// [o] --- field:0:required MBoolean
		// [o] -- field:0:required LastModified
		// [o] -- field:0:optional ValidationSvc
		// [o] -- field:0:optional VerificationSvc
		// [o] -- field:0:optional SigningSvc
		// [o] -- record:0:optional PreProcessingFlags
		// [o] --- field:0:optional validate? {"yes","no","dont care"}
		// [o] --- field:0:optional verify? {"yes","no","dont care"}
		// [o] --- field:0:optional persist? {"yes","no","dont care","only if unique"}
		// [o] --- field:0:optional unique? {"dont care","Document ID only","Document ID and sender"}
		// [o] --- field:0:optional persistOption? {"don't care","content, attributes and activity log","content only","attributes only","activity log only","content and attributes","content and activity log","attributes and activity log"}
		// [o] --- field:0:optional dupCheckSvc
		// [o] -- record:0:optional Attributes
		// [o] -- record:0:optional RequiredAttributes
		// [o] -- record:0:optional Routing
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional attribQueries
		// [o] -- record:0:optional PipelineMatchIData
		// [o] -- record:0:optional envelopeIData
		// [o] -- field:2:optional nsDecls
		// [o] -- field:0:optional Type
		// [o] -- field:1:optional queries
		// [o] -- field:1:optional qryEvals
		// [o] -- field:0:optional docType
		// [o] -- field:0:optional ValidationSchema
		// [o] -- field:0:optional recordBlueprint
		// [o] - field:0:required DocTimestamp
		// [o] - field:0:required LastModified
		// [o] - field:0:required SenderID
		// [o] - field:0:required ReceiverID
		// [o] - field:0:optional DocumentID
		// [o] - field:0:optional GroupID
		// [o] - field:0:optional ConversationID
		// [o] - field:0:required SystemStatus
		// [o] - field:0:optional UserStatus
		// [o] - field:0:required Persisted?
		// [o] - field:0:required LargeDocument?
		// [o] - record:0:optional Attributes
		// [o] - object:0:optional Signature
		// [o] - object:0:optional SignedBody
		// [o] - record:1:optional ContentParts
		// [o] -- field:0:required PartName
		// [o] -- field:0:required MimeType
		// [o] -- object:0:required Length
		// [o] -- object:0:required Bytes
		// [o] -- object:0:required PartIndex
		// [o] -- field:0:required StorageType
		// [o] -- object:0:required StorageRef
		// [o] -- field:0:required LargePart?
		// [o] - object:0:optional Content
		// [o] - record:0:optional Errors
		// [o] -- record:1:optional Recognition
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Verification
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Validation
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Persistence
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Routing
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional General
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] - record:1:optional Relationships
		// [o] -- field:0:required from
		// [o] -- field:0:required to
		// [o] -- field:0:required relationship
		// [o] - field:0:optional ReceiveSvc
		// [o] - field:0:optional OriginalSenderID
		// [o] - field:0:optional OriginalReceiverID
		// [o] - field:0:optional Comments
		// [o] - object:0:optional RepeatNum
		// [o] - field:0:optional RoutingType
		// [o] - field:0:optional Duplicate
		// [o] record:0:optional $sender
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
		// [o] record:0:optional $receiver
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
		    IData input = IDataHelper.get(cursor, "$bizdoc", IData.class);
		    boolean content = IDataHelper.getOrDefault(cursor, "$content?", Boolean.class, false);
		    boolean sender = IDataHelper.getOrDefault(cursor, "$sender?", Boolean.class, false);
		    boolean receiver = IDataHelper.getOrDefault(cursor, "$receiver?", Boolean.class, false);
		    boolean raise = IDataHelper.getOrDefault(cursor, "$raise?", Boolean.class, false);

		    BizDocEnvelope output = BizDocEnvelopeHelper.normalize(input, content);

		    if (output == null) {
		        if (raise && input != null) {
		            IDataCursor inputCursor = input.getCursor();
		            String id = IDataUtil.getString(inputCursor, "InternalID");
		            inputCursor.destroy();

		            throw new UnrecoverableException("Trading Networks document does not exist: " + id);
		        }
		    } else {
		        IDataHelper.put(cursor, "$bizdoc", output);
		        if (sender || receiver) {
		            ProfileCache cache = ProfileCache.getInstance();
		            if (sender) IDataHelper.put(cursor, "$sender", cache.get(output.getSenderId()));
		            if (receiver) IDataHelper.put(cursor, "$receiver", cache.get(output.getReceiverId()));
		        }
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void parse (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(parse)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] field:0:optional $part
		// [i] field:0:optional $validate? {"false","true"}
		// [o] record:0:optional $document
		// [o] field:0:optional $content.type
		// [o] field:0:optional $schema
		// [o] field:0:optional $schema.type {"Flat File","XML"}
		// [o] record:0:optional $namespace
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
		    String partName = IDataHelper.get(cursor, "$part", String.class);
		    boolean validate = IDataHelper.getOrDefault(cursor, "$validate?", Boolean.class, false);

		    IData document = BizDocContentHelper.parse(bizdoc, partName, validate, true);

		    if (document != null) {
		        IDataHelper.put(cursor, "$document", document);

		        BizDocContentPart contentPart = BizDocContentHelper.getContentPart(bizdoc, partName);
		        if (contentPart != null) IDataHelper.put(cursor, "$content.type", contentPart.getMimeType(), false);
		        IDataHelper.put(cursor, "$schema", BizDocEnvelopeHelper.getContentSchema(bizdoc), false);
		        IDataHelper.put(cursor, "$schema.type", BizDocEnvelopeHelper.getContentSchemaType(bizdoc), false);
		        IDataHelper.put(cursor, "$namespace", BizDocEnvelopeHelper.getNamespaceDeclarations(bizdoc), false);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void relate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(relate)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc.source
		// [i] - field:0:required InternalID
		// [i] record:0:optional $bizdoc.target
		// [i] - field:0:required InternalID
		// [i] field:0:optional $relationship
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope source = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc.source", IData.class));
		    BizDocEnvelope target = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc.target", IData.class));
		    String relationship = IDataHelper.get(cursor, "$relationship", String.class);

		    BizDocEnvelopeHelper.relate(source, target, relationship);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void reroute (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reroute)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] record:0:optional TN_parms
		// [o] record:0:required $bizdoc
		// [o] - field:0:required InternalID
		// [o] - record:0:required DocType
		// [o] -- field:0:required TypeID
		// [o] -- field:0:required TypeName
		// [o] -- field:0:optional TypeDescription
		// [o] -- record:0:optional Deleted
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional Hidden
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional SenderFromSession
		// [o] --- field:0:required MBoolean
		// [o] -- field:0:required LastModified
		// [o] -- field:0:optional ValidationSvc
		// [o] -- field:0:optional VerificationSvc
		// [o] -- field:0:optional SigningSvc
		// [o] -- record:0:optional PreProcessingFlags
		// [o] --- field:0:optional validate? {"yes","no","dont care"}
		// [o] --- field:0:optional verify? {"yes","no","dont care"}
		// [o] --- field:0:optional persist? {"yes","no","dont care","only if unique"}
		// [o] --- field:0:optional unique? {"dont care","Document ID only","Document ID and sender"}
		// [o] --- field:0:optional persistOption? {"don't care","content, attributes and activity log","content only","attributes only","activity log only","content and attributes","content and activity log","attributes and activity log"}
		// [o] --- field:0:optional dupCheckSvc
		// [o] -- record:0:optional Attributes
		// [o] -- record:0:optional RequiredAttributes
		// [o] -- record:0:optional Routing
		// [o] --- field:0:required MBoolean
		// [o] -- record:0:optional attribQueries
		// [o] -- record:0:optional PipelineMatchIData
		// [o] -- record:0:optional envelopeIData
		// [o] -- field:2:optional nsDecls
		// [o] -- field:0:optional Type
		// [o] -- field:1:optional queries
		// [o] -- field:1:optional qryEvals
		// [o] -- field:0:optional docType
		// [o] -- field:0:optional ValidationSchema
		// [o] -- field:0:optional recordBlueprint
		// [o] - field:0:required DocTimestamp
		// [o] - field:0:required LastModified
		// [o] - field:0:required SenderID
		// [o] - field:0:required ReceiverID
		// [o] - field:0:optional DocumentID
		// [o] - field:0:optional GroupID
		// [o] - field:0:optional ConversationID
		// [o] - field:0:required SystemStatus
		// [o] - field:0:optional UserStatus
		// [o] - field:0:required Persisted?
		// [o] - field:0:required LargeDocument?
		// [o] - record:0:optional Attributes
		// [o] - object:0:optional Signature
		// [o] - object:0:optional SignedBody
		// [o] - record:1:optional ContentParts
		// [o] -- field:0:required PartName
		// [o] -- field:0:required MimeType
		// [o] -- object:0:required Length
		// [o] -- object:0:required Bytes
		// [o] -- object:0:required PartIndex
		// [o] -- field:0:required StorageType
		// [o] -- object:0:required StorageRef
		// [o] -- field:0:required LargePart?
		// [o] - object:0:optional Content
		// [o] - record:0:optional Errors
		// [o] -- record:1:optional Recognition
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Verification
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Validation
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Persistence
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional Routing
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] -- record:1:optional General
		// [o] --- field:0:required EntryTimestamp
		// [o] --- field:0:required EntryType
		// [o] --- field:0:required EntryClass
		// [o] --- field:0:required BriefMessage
		// [o] --- field:0:optional FullMessage
		// [o] --- field:0:optional RelatedDocID
		// [o] --- field:0:optional RelatedPartnerID
		// [o] --- field:0:optional RelatedConversationID
		// [o] --- field:0:required RelatedStepID
		// [o] --- field:0:optional B2BUser
		// [o] - record:1:optional Relationships
		// [o] -- field:0:required from
		// [o] -- field:0:required to
		// [o] -- field:0:required relationship
		// [o] - field:0:optional ReceiveSvc
		// [o] - field:0:optional OriginalSenderID
		// [o] - field:0:optional OriginalReceiverID
		// [o] - field:0:optional Comments
		// [o] - object:0:optional RepeatNum
		// [o] - field:0:optional RoutingType
		// [o] - field:0:optional Duplicate
		// [o] record:0:optional $sender
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
		// [o] record:0:required $receiver
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
		// [o] record:0:optional TN_parms
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    if (bizdoc != null) {
		        BizDocEnvelopeHelper.reroute(bizdoc, parameters);

		        IDataHelper.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataHelper.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
		        IDataHelper.put(cursor, "TN_parms", parameters, false);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}



	public static final void route (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(route)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [i] field:0:optional $transport.log? {"false","true"}
		// [i] field:0:optional $transport.log.part
		// [i] field:0:optional $strict? {"false","true"}
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional processingRuleID
		// [i] - field:0:optional processingRuleName
		// [i] - field:0:optional $bypassRouting {"false","true"}
		// [o] recref:0:optional $bizdoc wm.tn.rec:BizDocEnvelope
		// [o] record:0:optional $sender
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
		// [o] record:0:optional $receiver
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
		// [o] record:0:optional TN_parms
		// [o] - field:0:optional processingRuleID
		// [o] - field:0:optional processingRuleName
		// [o] - field:0:optional $bypassRouting {"false","true"}
		IDataCursor cursor = pipeline.getCursor();

		try {
		    BizDocEnvelope bizdoc = IDataHelper.get(cursor, "$bizdoc", BizDocEnvelope.class);
		    boolean transportLog = IDataHelper.getOrDefault(cursor, "$transport.log?", Boolean.class, false);
		    String transportLogPartName = IDataHelper.get(cursor, "$transport.log.part", String.class);
		    boolean strict = IDataHelper.getOrDefault(cursor, "$strict?", Boolean.class, true);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);

		    if (bizdoc != null) {
		        BizDocEnvelopeHelper.route(bizdoc, transportLog, transportLogPartName, parameters, strict);

		        IDataHelper.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataHelper.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
		        IDataHelper.put(cursor, "TN_parms", parameters, false);
		    }
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	public static class ActivityLog {
	    public static void log(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), false, false);
	            String entryType = IDataHelper.getOrDefault(cursor, "$type", String.class, "MESSAGE");
	            String entryClass = IDataHelper.getOrDefault(cursor, "$class", String.class, "General");
	            String messageSummary = IDataHelper.get(cursor, "$summary", String.class);
	            String messageDetail = IDataHelper.get(cursor, "$message", String.class);
	            IData context = IDataHelper.get(cursor, "$context", IData.class);

	            ActivityLogHelper.log(EntryType.normalize(entryType), entryClass, messageSummary, messageDetail, bizdoc, context);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentAttribute {
	    public static void merge(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            IData attributes = IDataHelper.get(cursor, "$attributes", IData.class);
	            boolean substitute = IDataHelper.getOrDefault(cursor, "$substitute?", Boolean.class, false);

	            BizDocAttributeHelper.merge(bizdoc, attributes, pipeline, substitute);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentAttributeNumberTransformerPriority {
	    public static void imminence(IData pipeline) throws ServiceException {
	        Transformer.transform(pipeline, new ImminentPrioritizer());
	    }
	}

	public static class DocumentAttributeStringTransformerProfile {
	    public static void find(IData pipeline) throws ServiceException {
	        Transformer.transform(pipeline, new ProfileFinder());
	    }
	}

	public static class DocumentContent {
	    public static void add(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
	            String partName = IDataHelper.get(cursor, "$part", String.class);
	            Object content = IDataHelper.get(cursor, "$content");
	            String contentType = IDataHelper.get(cursor, "$content.type", String.class);
	            Charset contentEncoding = IDataHelper.first(cursor, Charset.class, "$content.encoding", "$encoding");
	            boolean overwrite = IDataHelper.getOrDefault(cursor, "$overwrite?", Boolean.class, false);

	            BizDocContentHelper.addContentPart(document, partName, contentType, contentEncoding, InputStreamHelper.normalize(content, contentEncoding), overwrite);
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void exists(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
	            String partName = IDataHelper.get(cursor, "$part", String.class);

	            IDataHelper.put(cursor, "$exists?", BizDocContentHelper.exists(document, partName), String.class);
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
	            String partName = IDataHelper.get(cursor, "$part", String.class);
	            String mode = IDataHelper.get(cursor, "$mode", String.class);

	            if (document != null) {
	                BizDocContentPart contentPart = BizDocContentHelper.getContentPart(document, partName);
	                InputStream content = BizDocContentHelper.getContent(document, contentPart);

	                if (content != null) {
	                    if (mode != null && !mode.equals("stream")) {
	                        IDataHelper.put(cursor, "$content", ObjectHelper.convert(content, mode));
	                    } else {
	                        IDataHelper.put(cursor, "$content", content);
	                    }
	                    IDataHelper.put(cursor, "$content.type", BizDocContentHelper.getContentType(contentPart));
	                    IDataHelper.put(cursor, "$part", contentPart.getPartName(), false);
	                }
	            }
	        } catch(IOException ex) {
	            ExceptionHelper.raise(ex);
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void remove(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope document = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            String partName = IDataHelper.get(cursor, "$part", String.class);

	            BizDocContentHelper.removeContentPart(document, partName);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentDerivative {
	    public static void exists(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            String originalDocumentID = BizDocEnvelopeHelper.getIdentity(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            String derivedSenderID = ProfileHelper.getIdentity(IDataHelper.get(cursor, "$sender", IData.class));
	            String derivedReceiverID = ProfileHelper.getIdentity(IDataHelper.get(cursor, "$receiver", IData.class));

	            BizDocEnvelope derivedDocument = BizDocEnvelopeHelper.getDerivative(originalDocumentID, derivedSenderID, derivedReceiverID);

	            IDataHelper.put(cursor, "$exists?", derivedDocument != null, String.class);
	            IDataHelper.put(cursor, "$derivative", derivedDocument, false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentDuplicate {
	    public static void check(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "bizdoc", IData.class));
	            IDataHelper.put(cursor, "duplicate", BizDocEnvelopeHelper.isDuplicate(bizdoc), String.class);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentError {
	    public static void exists(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            IData bizdoc = IDataHelper.get(cursor, "$bizdoc", IData.class);
	            IData classes = IDataHelper.get(cursor, "$class", IData.class);

	            ActivityLogEntry[] errors = BizDocEnvelopeHelper.getErrors(bizdoc, classes);

	            IDataHelper.put(cursor, "$exists?", errors != null && errors.length > 0, String.class);
	            IDataHelper.put(cursor, "$errors", IDataHelper.normalize(errors), false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentNamespace {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            IDataHelper.put(cursor, "$namespace", BizDocEnvelopeHelper.getNamespaceDeclarations(bizdoc), false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentSchema {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            IDataHelper.put(cursor, "$schema", BizDocEnvelopeHelper.getContentSchema(bizdoc), false);
	            IDataHelper.put(cursor, "$schema.type", BizDocEnvelopeHelper.getContentSchemaType(bizdoc), false);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentStatus {
	    public static void set(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class));
	            String systemStatus = IDataHelper.get(cursor, "$status.system", String.class);
	            String previousSystemStatus = IDataHelper.get(cursor, "$status.system.previous", String.class);
	            String userStatus = IDataHelper.first(cursor, String.class, "$status.user", "$status");
	            String previousUserStatus = IDataHelper.get(cursor, "$status.user.previous", String.class);
	            boolean silence = IDataHelper.getOrDefault(cursor, "$status.silence?", Boolean.class, false);

	            BizDocEnvelopeHelper.setStatus(bizdoc, systemStatus, previousSystemStatus, userStatus, previousUserStatus, silence);
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentType {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            boolean backwardsCompatible = false;

	            String id = IDataHelper.get(cursor, "$document.type.id", String.class);
	            String name = IDataHelper.get(cursor, "$document.type.name", String.class);

	            if (id == null && name == null) {
	                id = IDataHelper.get(cursor, "$id", String.class);
	                name = IDataHelper.get(cursor, "$name", String.class);
	                backwardsCompatible = id != null || name != null;
	            }

	            BizDocType type = null;

	            if (id != null) {
	                type = BizDocTypeHelper.get(id);
	            } else if (name != null) {
	                type = BizDocTypeHelper.getByName(name);
	            }

	            if (type != null) {
	                IDataHelper.put(cursor, backwardsCompatible ? "$type" : "$document.type", IDataHelper.normalize((IData)type));
	            }
	        } finally {
	            cursor.destroy();
	        }
	    }

	    public static void normalize(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            boolean backwardsCompatible = false;

	            BizDocType type = BizDocTypeHelper.normalize(IDataHelper.get(cursor, "$document.type", IData.class));

	            if (type == null) {
	                type = BizDocTypeHelper.normalize(IDataHelper.get(cursor, "$type", IData.class));
	                backwardsCompatible = type != null;
	            }

	            if (type != null) {
	                IDataHelper.put(cursor, backwardsCompatible ? "$type" : "$document.type", IDataHelper.normalize((IData)type));
	            }
	        } finally {
	            cursor.destroy();
	        }
	    }
	}

	public static class DocumentTypeSchema {
	    public static void get(IData pipeline) throws ServiceException {
	        IDataCursor cursor = pipeline.getCursor();

	        try {
	            BizDocType type = BizDocTypeHelper.normalize(IDataHelper.get(cursor, "$document.type", IData.class));
	            if (type == null) {
	                type = BizDocTypeHelper.normalize(IDataHelper.get(cursor, "$type", IData.class));
	                IDataHelper.put(cursor, "$schema", BizDocTypeHelper.getContentSchema(type), false);
	                IDataHelper.put(cursor, "$schema.type", BizDocTypeHelper.getContentSchemaType(type), false);
	            } else {
	                IDataHelper.put(cursor, "$content.schema", BizDocTypeHelper.getContentSchema(type), false);
	                IDataHelper.put(cursor, "$content.schema.type", BizDocTypeHelper.getContentSchemaType(type), false);
	            }
	        } finally {
	            cursor.destroy();
	        }
	    }
	}
	// --- <<IS-END-SHARED>> ---
}

