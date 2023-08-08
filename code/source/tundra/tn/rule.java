package tundra.tn;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2023-08-09 05:54:06 EST
// -----( ON-HOST: -

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.tn.doc.BizDocEnvelope;
import com.wm.app.tn.route.RoutingRule;
import permafrost.tundra.collection.CollectionHelper;
import permafrost.tundra.data.IDataHelper;
import permafrost.tundra.tn.cache.ProfileCache;
import permafrost.tundra.tn.document.BizDocEnvelopeHelper;
import permafrost.tundra.tn.route.RoutingRuleHelper;
// --- <<IS-END-IMPORTS>> ---

public final class rule

{
	// ---( internal utility methods )---

	final static rule _instance = new rule();

	static rule _newInstance() { return new rule(); }

	static rule _cast(Object o) { return (rule)o; }

	// ---( server methods )---




	public static final void disable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(disable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $rule
		// [i] - field:0:optional RuleID
		// [i] - field:0:optional RuleName
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    RoutingRule rule = RoutingRuleHelper.normalize(IDataHelper.get(cursor, "$rule", IData.class));
		    RoutingRuleHelper.disable(rule);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void enable (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(enable)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $rule
		// [i] - field:0:optional RuleID
		// [i] - field:0:optional RuleName
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    RoutingRule rule = RoutingRuleHelper.normalize(IDataHelper.get(cursor, "$rule", IData.class));
		    RoutingRuleHelper.enable(rule);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void execute (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(execute)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:optional $bizdoc
		// [i] - field:0:required InternalID
		// [i] record:0:optional $rule
		// [i] - field:0:optional RuleID
		// [i] - field:0:optional RuleName
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional $bypassRouting {&quot;false&quot;,&quot;true&quot;}
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
		// [o] --- field:0:optional validate? {&quot;yes&quot;,&quot;no&quot;,&quot;dont care&quot;}
		// [o] --- field:0:optional verify? {&quot;yes&quot;,&quot;no&quot;,&quot;dont care&quot;}
		// [o] --- field:0:optional persist? {&quot;yes&quot;,&quot;no&quot;,&quot;dont care&quot;,&quot;only if unique&quot;}
		// [o] --- field:0:optional unique? {&quot;dont care&quot;,&quot;Document ID only&quot;,&quot;Document ID and sender&quot;}
		// [o] --- field:0:optional persistOption? {&quot;don't care&quot;,&quot;content, attributes and activity log&quot;,&quot;content only&quot;,&quot;attributes only&quot;,&quot;activity log only&quot;,&quot;content and attributes&quot;,&quot;content and activity log&quot;,&quot;attributes and activity log&quot;}
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
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
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
		// [o] record:0:optional $receiver
		// [o] - field:1:required ProfileGroups
		// [o] - field:1:required users
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
		// [o] record:0:optional $rule
		// [o] - field:0:required RuleName
		// [o] - field:0:required RuleDescription
		// [o] - field:0:required Disabled?
		// [o] - field:1:required SenderID
		// [o] - field:1:required ReceiverID
		// [o] - field:1:required DocTypeID
		// [o] - field:1:optional UserStatus
		// [o] - field:0:required HasErrors
		// [o] - record:0:required PreRoutingFlags
		// [o] -- field:0:required verify?
		// [o] -- field:0:required validate?
		// [o] -- field:0:required persist?
		// [o] -- field:0:required unique?
		// [o] -- field:0:required persistOption?
		// [o] -- field:0:optional dupCheckSvc
		// [o] - field:0:optional AlertID
		// [o] - field:0:optional AlertType
		// [o] - field:0:optional AlertSubject
		// [o] - field:0:optional AlertMessage
		// [o] - field:1:optional ResponseMessage
		// [o] - field:0:optional IntendedUserStatus
		// [o] - field:0:optional ServiceName
		// [o] - record:0:optional ServiceInput
		// [o] - object:0:optional ServiceAsync?
		// [o] - object:0:optional SendTo
		// [o] - field:0:optional LastModified
		// [o] - field:2:optional AttributeConditions
		// [o] - field:0:optional DeliveryQueue
		// [o] - field:0:required RuleID
		// [o] - field:0:required LastChangeID
		// [o] - field:0:required LastChangeUser
		// [o] - field:0:required LastChangeSession
		// [o] - object:0:required RuleIndex
		// [o] record:0:optional TN_parms
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), true);
		    RoutingRule rule = RoutingRuleHelper.normalize(IDataHelper.get(cursor, "$rule", IData.class));
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);
		
		    RoutingRuleHelper.execute(rule, bizdoc, parameters);
		
		    if (bizdoc != null) {
		        IDataHelper.put(cursor, "$bizdoc", bizdoc);
		        ProfileCache cache = ProfileCache.getInstance();
		        IDataHelper.put(cursor, "$sender", cache.get(bizdoc.getSenderId()));
		        IDataHelper.put(cursor, "$receiver", cache.get(bizdoc.getReceiverId()));
		        IDataHelper.put(cursor, "$rule", rule);
		    }
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
		// [i] field:0:optional $rule.id
		// [i] field:0:optional $rule.name
		// [o] record:0:optional $rule
		// [o] - field:0:required RuleName
		// [o] - field:0:required RuleDescription
		// [o] - field:0:required Disabled?
		// [o] - field:1:required SenderID
		// [o] - field:1:required ReceiverID
		// [o] - field:1:required DocTypeID
		// [o] - field:1:optional UserStatus
		// [o] - field:0:required HasErrors
		// [o] - record:0:required PreRoutingFlags
		// [o] -- field:0:required verify?
		// [o] -- field:0:required validate?
		// [o] -- field:0:required persist?
		// [o] -- field:0:required unique?
		// [o] -- field:0:required persistOption?
		// [o] -- field:0:optional dupCheckSvc
		// [o] - field:0:optional AlertID
		// [o] - field:0:optional AlertType
		// [o] - field:0:optional AlertSubject
		// [o] - field:0:optional AlertMessage
		// [o] - field:1:optional ResponseMessage
		// [o] - field:0:optional IntendedUserStatus
		// [o] - field:0:optional ServiceName
		// [o] - record:0:optional ServiceInput
		// [o] - object:0:optional ServiceAsync?
		// [o] - object:0:optional SendTo
		// [o] - field:0:optional LastModified
		// [o] - field:2:optional AttributeConditions
		// [o] - field:0:optional DeliveryQueue
		// [o] - field:0:required RuleID
		// [o] - field:0:required LastChangeID
		// [o] - field:0:required LastChangeUser
		// [o] - field:0:required LastChangeSession
		// [o] - object:0:required RuleIndex
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    String ruleID = IDataHelper.get(cursor, "$rule.id", String.class);
		    String ruleName = IDataHelper.get(cursor, "$rule.name", String.class);
		
		    RoutingRule rule = RoutingRuleHelper.getByIdentityOrName(ruleID, ruleName);
		
		    IDataHelper.put(cursor, "$rule", rule, false);
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
		// [o] record:1:optional $rule.list
		// [o] - field:0:required RuleName
		// [o] - field:0:required RuleDescription
		// [o] - field:0:required Disabled?
		// [o] - field:1:required SenderID
		// [o] - field:1:required ReceiverID
		// [o] - field:1:required DocTypeID
		// [o] - field:1:optional UserStatus
		// [o] - field:0:required HasErrors
		// [o] - record:0:required PreRoutingFlags
		// [o] -- field:0:required verify?
		// [o] -- field:0:required validate?
		// [o] -- field:0:required persist?
		// [o] -- field:0:required unique?
		// [o] -- field:0:required persistOption?
		// [o] -- field:0:optional dupCheckSvc
		// [o] - field:0:optional AlertID
		// [o] - field:0:optional AlertType
		// [o] - field:0:optional AlertSubject
		// [o] - field:0:optional AlertMessage
		// [o] - field:1:optional ResponseMessage
		// [o] - field:0:optional IntendedUserStatus
		// [o] - field:0:optional ServiceName
		// [o] - record:0:optional ServiceInput
		// [o] - object:0:optional ServiceAsync?
		// [o] - object:0:optional SendTo
		// [o] - field:0:optional LastModified
		// [o] - field:2:optional AttributeConditions
		// [o] - field:0:optional DeliveryQueue
		// [o] - field:0:required RuleID
		// [o] - field:0:required LastChangeID
		// [o] - field:0:required LastChangeUser
		// [o] - field:0:required LastChangeSession
		// [o] - object:0:required RuleIndex
		// [o] field:0:required $rule.list.length
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData[] ruleList = CollectionHelper.arrayify(RoutingRuleHelper.list(), IData.class);
		    int length = ruleList == null ? 0 : ruleList.length;
		
		    IDataHelper.put(cursor, "$rule.list", ruleList, false);
		    IDataHelper.put(cursor, "$rule.list.length", length, String.class);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}



	public static final void match (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(match)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required $bizdoc
		// [i] - field:0:required InternalID
		// [i] record:0:optional TN_parms
		// [i] - field:0:optional processingRuleID
		// [i] - field:0:optional processingRuleName
		// [o] record:0:required $rule
		// [o] - field:0:required RuleName
		// [o] - field:0:required RuleDescription
		// [o] - field:0:required Disabled?
		// [o] - field:1:required SenderID
		// [o] - field:1:required ReceiverID
		// [o] - field:1:required DocTypeID
		// [o] - field:1:optional UserStatus
		// [o] - field:0:required HasErrors
		// [o] - record:0:required PreRoutingFlags
		// [o] -- field:0:required verify?
		// [o] -- field:0:required validate?
		// [o] -- field:0:required persist?
		// [o] -- field:0:required unique?
		// [o] -- field:0:required persistOption?
		// [o] -- field:0:optional dupCheckSvc
		// [o] - field:0:optional AlertID
		// [o] - field:0:optional AlertType
		// [o] - field:0:optional AlertSubject
		// [o] - field:0:optional AlertMessage
		// [o] - field:1:optional ResponseMessage
		// [o] - field:0:optional IntendedUserStatus
		// [o] - field:0:optional ServiceName
		// [o] - record:0:optional ServiceInput
		// [o] - object:0:optional ServiceAsync?
		// [o] - object:0:optional SendTo
		// [o] - field:0:optional LastModified
		// [o] - field:2:optional AttributeConditions
		// [o] - field:0:optional DeliveryQueue
		// [o] - field:0:required RuleID
		// [o] - field:0:required LastChangeID
		// [o] - field:0:required LastChangeUser
		// [o] - field:0:required LastChangeSession
		// [o] - object:0:required RuleIndex
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    BizDocEnvelope bizdoc = BizDocEnvelopeHelper.normalize(IDataHelper.get(cursor, "$bizdoc", IData.class), false);
		    IData parameters = IDataHelper.get(cursor, "TN_parms", IData.class);
		
		    IDataHelper.put(cursor, "$rule", RoutingRuleHelper.match(bizdoc, parameters, true));
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
		// [i] record:0:optional $rule
		// [i] - field:0:optional RuleID
		// [i] - field:0:optional RuleName
		// [o] record:0:optional $rule
		// [o] - field:0:required RuleName
		// [o] - field:0:required RuleDescription
		// [o] - field:0:required Disabled?
		// [o] - field:1:required SenderID
		// [o] - field:1:required ReceiverID
		// [o] - field:1:required DocTypeID
		// [o] - field:1:optional UserStatus
		// [o] - field:0:required HasErrors
		// [o] - record:0:required PreRoutingFlags
		// [o] -- field:0:required verify?
		// [o] -- field:0:required validate?
		// [o] -- field:0:required persist?
		// [o] -- field:0:required unique?
		// [o] -- field:0:required persistOption?
		// [o] -- field:0:optional dupCheckSvc
		// [o] - field:0:optional AlertID
		// [o] - field:0:optional AlertType
		// [o] - field:0:optional AlertSubject
		// [o] - field:0:optional AlertMessage
		// [o] - field:1:optional ResponseMessage
		// [o] - field:0:optional IntendedUserStatus
		// [o] - field:0:optional ServiceName
		// [o] - record:0:optional ServiceInput
		// [o] - object:0:optional ServiceAsync?
		// [o] - object:0:optional SendTo
		// [o] - field:0:optional LastModified
		// [o] - field:2:optional AttributeConditions
		// [o] - field:0:optional DeliveryQueue
		// [o] - field:0:required RuleID
		// [o] - field:0:required LastChangeID
		// [o] - field:0:required LastChangeUser
		// [o] - field:0:required LastChangeSession
		// [o] - object:0:required RuleIndex
		IDataCursor cursor = pipeline.getCursor();
		
		try {
		    IData rule = IDataHelper.get(cursor, "$rule", IData.class);
		    IDataHelper.put(cursor, "$rule", RoutingRuleHelper.normalize(rule), false);
		} finally {
		    cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}

