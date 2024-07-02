# 0.0.38 (2024-07-03)

* update `./code/jars/Tundra.jar` library with same version used by Tundra 0.0.38 (2024-07-03), required for compatibility between the two packages Tundra and TundraTN

# 0.0.37 (2023-10-12)

* add `tundra.tn.document.duplicate:content` for checking for duplicates using a SHA-512 message digest of the document content
* add `tundra.tn.document.duplicate:key` service to calculate a unique key for duplicate detection for a given document
* add `tundra.tn.queue:expedite` to deliver queued documents on a given queue immediately rather than waiting for the next scheduled delivery time
* add `tundra.tn.support.queue:restart` for restarting queue processing
* add `tundra.tn.support.system:export` for exporting all the document attribute, document type, profile, delivery queue, and routing rule components as a stable and therefore comparable JSON string
* change `permafrost.tundra.lang.BaseException` and sub-classes to no longer implement `Serializable` interface as it is not required
* change `tundra.tn.content:recognize` to work around issue with Trading Networks suppressing loss of network or database connection errors at time of resolving the extracted sender and/or receiver and instead defaulting to the Unknown profile, the work around checks if the recognized bizdoc is an XML document type and has been recognized with an Unknown sender and/or receiver, and if so it then attempts to re-resolve the sender and/or receiver using the TundraTN in-memory profile cache and hence removing the reliance on the network / database at the time of resolution
* change `tundra.tn.content:route` to automatically log the transport info as a content part when top-level service is invoked externally
* change `tundra.tn.document.attribute.string.transformer.find:all`,`tundra.tn.document.attribute.string.transformer.find:any`, and `tundra.tn.document.attribute.string.transformer.find:none` to be implemented in Java to improve performance, and fix to correctly treat the provided `arg` input argument as a regular expression pattern rather than a literal as per the existing service documentation
* change `tundra.tn.document.attribute.string.transformer.match:all`, `tundra.tn.document.attribute.string.transformer.match:any`, and `tundra.tn.document.attribute.string.transformer.match:none` to be implemented in Java to improve performance, and fix to correctly treat the provided `arg` input argument as a regular expression pattern rather than a literal as per the existing service documentation
* change `tundra.tn.document.attribute:merge` to attempt to automatically parse datetime and number strings when setting `DATETIME`, `DATETIME LIST`, `NUMBER`, and `NUMBER LIST` attribute values using strings
* change `tundra.tn.document.attribute:merge` to try parsing number attribute string values first using `java.math.BigDecimal` and if that fails then try using `java.text.DecimalFormat`
* change `tundra.tn.document:route` to normalize attributes before persisting by removing any attributes which are not defined or not active in Trading Networks, and by reformatting any datetime or number attributes to have the expected format required by Trading Networks
* change `tundra.tn.exception:handle` to prefix error message summary with "Document processing failed"
* change `tundra.tn.profile:get`, `tundra.tn.profile:list`, and `tundra.tn.profile:self` to include `DisplayName` in output document
* change `tundra.tn.queue:each` and `tundra.tn.content:route` to set and clear the start time on worker threads when using a thread pool to process tasks and routes
* change `tundra.tn.queue:each` and dependent services to log when a retry has been requested for the task against the related bizdoc
* change `tundra.tn.queue:each` and dependent services to support new input parameter `$expedite?` which when `true` will run the queue's associated scheduled task immediately when new queued tasks are  detected
* change `tundra.tn.queue:each` and dependent services to support optionally invoking a `$service.exhausted` service when and if all task retries are exhausted at the time of exhaustion
* change `tundra.tn.queue:each` and dependent services to support the delegate bizdoc processing service requesting a task to be retried without throwing an exception which allows a task to be retried for reasons other than errors, and for the retry to be requested by the service processing the task
* change `tundra.tn.queue:each` to reduce contention on DeliveryJob table: regularly cache the set of queues with currently `QUEUED` tasks to replace the need for queues to individually poll the `DeliveryJob` table to determine whether there are tasks to be processed
* change `tundra.tn.support:shutdown` to also clear TN profile cache
* change `tundra.tn:deliver` to use a content part name for delivery responses consistent with other diagnostic content parts such as the `tundra.tn:receive` transport info content part
* change `tundra.tn:process` to include `$content.part` and `$content.length` in pipeline when `$parse?` is `false` and therefore the bizdoc content part is included in the pipeline as `$content` as an input stream when invoking `$service`
* change `tundra.tn:receive` to support `xmlStream` and `xmlBytes` inputs if the XML format is different to the default node format
* change `tundra.tn:receive` to use `tundra.tn.content:recognize`  instead of `WmTN/wm.tn.doc:recognize`, so that it benefits from the previously commited work around to the issue with Trading Networks suppressing loss of network or database connection errors at time of resolving the extracted sender and/or receiver and defaulting to the Unknown profile, with this change messages received via `tundra.tn:receive` will more robustly recognize the sender and/or receiver in the face of network or database connectivity loss
* change `tundra.tn:receive`, `tundra.tn.content:route`, and `tundra.tn.document:route` to correct the charset parameter on the `BizDocEnvelope` content part content type from the incorrect Trading Networks default of "UTF8" to the correct canonical name of "UTF-8" where applicable
* change thread names used by TundraTN thread pools for clarity, simplicity, and consistency with Tundra
* change TundraTN profile cache to be non-lazy and cache all profiles at startup
* fix `tundra.tn.content:recognize` to not throw `ClassCastException`, work around `java.lang.ClassCastException` thrown by `wm.tn.doc:recognize` when attempting to recognize an XML document while the pipeline contains a `$document` variable whose value is NOT an instance of the class `org.w3c.dom.Node` by removing all `$document` variables from the pipeline before calling `wm.tn.doc:recognize`
* fix `tundra.tn.content:recognize` to to support content encoding not being specified for an XML content stream, rather than throwing a `java.lang.NullPointerException`
* fix `tundra.tn.content:recognize` to use the pipeline when serializing `IData` document content so that additional variables can be specified to influence the serialization
* fix `tundra.tn.content:route` to always log which service routed the document rather than only logging when the route is successful (does not throw an exception)
* fix `tundra.tn.content:route` to correctly format `DATETIME` attribute values using the `java.sql.Timestamp.toString()` format
* fix `tundra.tn.content:route` to correctly handle routing a `com.wm.lang.xml.Document` node object that uses `CRLF` (carriage return followed by line feed) line endings to not encode the `CR` character as the entity `&#13;`
* fix `tundra.tn.content:route` to correctly run route on current thread when deferred routing is enabled but for whatever reason the route was unable to be submitted to the deferred routing thread executor, rather than waiting forever for the route to finish
* fix `tundra.tn.document.attribute:merge` to reformat all existing as well as new attributes that are being saved against the `BizDoc` object to ensure attribute values are in the format expected by the method `BizDocStore.updateAttributes()`, this works around an issue with webMethods 10.11 where an MWS user-initiated `BizDoc` route, such as reprocessing a document in MWS, incorrectly formats `DATETIME` attribute values as Unix milliseconds string, rather than the expected JDBC datetime string format, which then causes the following exception to be thrown while routing if the processing calls `tundra.tn.document.attribute:merge`, which then calls the method `BizDocStore.updateAttributes()` to save the merged attribute changes to the database: `com.wm.app.tn.db.DatastoreException: BizDocStore.updateAttributes (0) java.lang.IllegalArgumentException: Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]`
* fix `tundra.tn.queue:each` and dependent services to not interrupt processing thread when queue processor is using a concurrency of 1 (or in other words is executing tasks with the processing thread) when queue processing is being stopped / shutdown, to avoid interrupting in-flight tasks as most tasks do not support interruption gracefully
* fix `tundra.tn:process` to no longer include `$content.part` in  pipeline when `$parse?` is `false` to avoid clashing with existing userland code that calls `tundra.tn.document.content:add` using the deprecated input argument name `$part`
* fix compatibility with webMethods 10.7 by removing log4j dependency
* rename `tundra.tn.document.duplicate:check` to `tundra.tn.document.duplicate:identity` (note that the old name can still be used and is redirected to the new name for backwards compatibility)

# 0.0.36 (2021-07-01)

* add `tundra.tn.document.attribute.datetime.transformer:add` services for parsing a datetime string and then adding a specified duration
* add `tundra.tn.document.attribute.datetime.transformer:subtract` services for parsing a datetime string and then subtracting a specified duration
* change `README.md` to recommend using git reset rather than git checkout
* change `tundra.tn.content:route` to correct the charset parameter on the content part's content type from the incorrect Trading Networks default of `UTF8` to the correct canonical name of `UTF-8` where applicable
* change `tundra.tn.profile.delivery:get` implementation to Java for improved performance
* change `tundra.tn.queue:each` `$error.threshold` backoff strategy to use an exponential backoff up to the specified `$retry.wait` to reduce the number of retries
* change `tundra.tn.queue:each` `$error.threshold` to be used to successively back off/slow task processing if the number of continuously failing tasks exceeds the threshold
* change `tundra.tn.queue:each` to improve performance when ordered is true and the queue contains a large number of queued tasks
* change `tundra.tn.queue:each` to monitor queue and scheduler status of all queues currently being processed from a dedicated thread, and if disabled/suspended to automatically stop the associated queue processing
* change `tundra.tn.support.deliver.destination:normalize` to throw exception when a named delivery method is specifed but does not exist on the receiver's partner profile
* change `tundra.tn.support:amend` to use new input variable names when calling `tundra.content:amend`
* change `tundra.tn:deliver` to include `$destination.unresolved` in the input pipeline when `$service` is called containing the original value of `$destination` prior to it being resolved to a URI
* change `tundra.tn:deliver` to include the delivery result returned by the call to `Tundra/tundra.content:deliver` in the pipeline when invoking the `$finally` service (the `tundra.content:deliver` protocol implementation services can now return a protocol-specific `$response` and `$response.context`, which this change makes available for further processing by the `$finally` service if required; for example, an HTTP delivery is implemented with the `Tundra/tundra.content.deliver:http` service, which now returns the HTTP response body in `$response` and the HTTP return status and headers in the `$response.context` document)
* change `tundra.tn:process` and dependent services to support optional variable substitution on the pipeline prior to invoking the given service
* change `tundra.tn:split` to not include double quotes in relationship description when relating original to translated documents
* change `tundra.tn:translate` to not include double quotes in relationship description when relating original to translated documents
* change route and queue related logging to support being written to different configured log target files, which can be configured in the `Tundra` package configuration section `feature/log/target`
* fix `tundra.tn.document:parse` to infer default namespace prefix by inspecting the root node prefix on the document type's `recordBlueprint` (previously the prefix `ns` was assumed for the default namespace)
* fix `tundra.tn.document:parse` to provide derived namespace prefixes from the default Trading Networks `prefix0` prefix in the following order of precedence: (1) declared root node prefix on the document type's `recordBlueprint`, (2) default Integration Server `ns` prefix, or (3) default Trading Networks `prefix0` prefix
* fix `tundra.tn.queue:each` and `tundra.tn.document:route` to use current thread for service invocations (previously these services were using a separate thread for service invocation to work around `java.lang.NullPointerException` being thrown due to an `InvokeState` race condition, which has now been fixed)
* fix `tundra.tn:deliver` to handle `$destination` URI variable substitution using URI template or percent-delimited variable formats more consistently by only attempting to parse the URI after substitution has been attempted
* fix `tundra.tn:derive` amendment keys to support normalizing namespace prefixes against namespace declarations
* fix `tundra.tn:enqueue` log message when queuing condition evaluates to false to correctly substitute variables in the logged message

# 0.0.35 (2020-07-03)

* add `tundra.tn.document.attribute.string.transformer.profile:find` to lookup a partner profile internal ID given an external ID (this is a replacement for the built-in `FN_PARTNER_LOOKUP` function for extracting sender and receiver profiles for a document which is more resilient to short database outages by using an in-memory cache of external IDs)
* add `tundra.tn.document.attribute:merge` for creating or updating attributes on a given bizdoc
* add `tundra.tn.document:enqueue` for enqueuing a `BizDocEnvelope` to a given Trading Networks queue
* add `tundra.tn.document:route` for routing a `BizDocEnvelope` to Trading Networks
* add `tundra.tn.queue:interrupt` for interrupting currently running TundraTN queue processing
* add `tundra.tn.rule:disable` for disabling a given rule
* add `tundra.tn.rule:enable` for enabling a given rule
* add `tundra.tn.rule:execute` for executing the given rule against a given bizdoc
* add `tundra.tn.rule:get` for returning a rule associated with a given identity or name
* add `tundra.tn.rule:list` for returning the list of all defined rules
* add `tundra.tn.rule:match` for returning the first rule whose criteria matches the given bizdoc
* add `tundra.tn.rule:normalize` for normalizing a given rule to be a `RoutingRule` object
* change `tundra.tn.content:recognize` implementation to Java for easier reuse by other components and better performance
* change `tundra.tn.content:route` implementation to Java for easier reuse by other components and better performance
* change `tundra.tn.document.attribute.number.transformer.priority:imminence` implementation to Java for better performance
* change `tundra.tn.document.attribute.number.transformer.priority:imminence` implementation to use double-based maths instead of BigDecimal to improve performance
* change `tundra.tn.document.attribute.string.transformer.uri:decode` to use new `tundra.uri:decode` input and output parameters
* change `tundra.tn.document.attribute.string.transformer.uri:encode` to use new `tundra.uri:encode` input and output parameters
* change `tundra.tn.document.attribute` services related to datetimes to support new `seconds` named pattern which is the seconds since the unix epoch
* change `tundra.tn.document.content:add` to use consistent parameter names
* change `tundra.tn.document.content:exists` to use consistent parameter names
* change `tundra.tn.document.content:get` implementation to Java for better performance
* change `tundra.tn.document.content:get` to return `$content.length`
* change `tundra.tn.document.content:remove` implementation to Java for better performance
* change `tundra.tn.document.content:remove` to use consistent parameter names
* change `tundra.tn.document.derivative:exists` implementation to Java for better performance
* change `tundra.tn.document.duplicate:check` implementation to Java for better performance
* change `tundra.tn.document.error:exists` implementation to Java for better performance
* change `tundra.tn.document.namespace:get` implementation to Java for better performance
* change `tundra.tn.document.schema:get` implementation to Java for better performance
* change `tundra.tn.document.status:set` implementation to Java for better performance
* change `tundra.tn.document.status:set` to support setting the system status as well as the user status
* change `tundra.tn.document.type.schema:get` implementation to Java for better performance
* change `tundra.tn.document.type:get` implementation to Java for better performance
* change `tundra.tn.document.type:normalize` implementation to Java for better performance
* change `tundra.tn.document:add` implementation to Java for better performance
* change `tundra.tn.document:exists` implementation to Java for better performance
* change `tundra.tn.document:normalize` to not use `BizDocEnvelope` document reference for bizdoc input and output to make it clearer that the input bizdoc can be a subset only containing an `InternalID`, and so the output bizdoc does not appear duplicated in a caller's pipeline
* change `tundra.tn.document:parse` implementation to Java for better performance
* change `tundra.tn.document:relate` implementation to Java for better performance
* change `tundra.tn.document:reroute` implementation to Java for better performance
* change `tundra.tn.document:reroute` to support the dedicated deferred routing thread pool feature when enabled
* change `tundra.tn.exception:handle` to include a partial stack trace in activity log full message to help with error diagnosis
* change `tundra.tn.exception:handle` to include class name in activity log message summary
* change `tundra.tn.exception:handle` to set the exception message summary to the string representation of the exception if the exception message itself is empty
* change `tundra.tn.profile.cache:clear` implementation to Java for better performance
* change `tundra.tn.profile.cache:list` implementation to Java for better performance
* change `tundra.tn.profile.cache:refresh` implementation to Java for better performance
* change `tundra.tn.profile.cache:seed` implementation to Java for better performance
* change `tundra.tn.queue.each` to only start processing if the current size of the delivery queue is greater than 0
* change `tundra.tn.queue:each` and dependent services to support specifying `$error.threshold` input which will terminate queue processing if the number of tasks that continuously fail is equal to or greater than the given threshold
* change `tundra.tn.queue:each` implementation to Java for better performance
* change `tundra.tn.queue:each` to improve performance when retrieving queued tasks and `$concurrency` > 1 by not fetching tasks that are already submitted to the queue processing thread pool for execution
* change `tundra.tn.queue:each` to only check the delivery queue and scheduler status once a second to improve performance
* change `tundra.tn.queue:each` to support task prioritization using `Message Priority` bizdoc attribute
* change `tundra.tn.support.document.transport:log` implementation to Java for better performance
* change `tundra.tn.support.document:parse` to remove double quotes from activity log statements
* change `tundra.tn.support:receive` to use new `tundra.gzip:decompress` and `tundra.zip:decompress` inputs
* change `tundra.tn:connect` to use new `tundra.service:respond` input argument names
* change `tundra.tn:deliver` `file` delivery to set the last modified datetime to the `Content Updated` attribute value if it exists
* change `tundra.tn:deliver` response content part name to be consistent with `tundra.tn:receive` transport content part name
* change `tundra.tn:deliver` to include `tundra.system:reflect` `$system` document in the pipeline when variable substition is performed
* change `tundra.tn:discard` to use new `tundra.service:respond` input argument names
* change `tundra.tn:enqueue` to use `tundra.tn.document:enqueue` for its implementation
* change `tundra.tn:log` implementation to Java for better performance
* change `tundra.tn:receive` to merge `JMSMessage/header` and `JMSMessage/properties` if they exist into `TN_parms`
* change `tundra.tn:receive` to support receiving content as a `JMSMessage`
* change `tundra.tn:reject` to use new `tundra.service:respond` input argument names
* change `tundra.tn:retrieve` to set the `Content Created` and `Content Updated` attributes on the resulting bizdoc when the retrieval protocol supports providing these values
* change tundra.tn.exception:handle to include HTTP request context including the headers and a reference to the added response content part in the Trading Networks activity log
* deprecate `tundra.tn.queue:clear`, use `tundra.tn.queue:status` instead
* fix `BizDocEnvelope` processing services to work correctly when content, attributes, or activity log is not persisted for the document being processed
* fix `tundra.tn.content:recogniz` to handle non-UTF-8 character encodings correctly by providing the encoding in the current invoke state's content info which is used by `wm.tn.doc:handleLargeDoc`
* fix `tundra.tn.content:recognize` to ignore current invoke state content info as it relates to the intially received content rather than subsequent content being recognized
* fix `tundra.tn.document.namespace:get` to insert the `ns` prefix before the `prefix0` prefix in the returned `$namespace` document so that the `WmPublic/pub.xml:*` services use the `ns` prefix (because it appears first in the namespace declarations) when parsing or serializing XML content
* fix `tundra.tn.document:parse` to allow pipeline to contain additional settings for the parser
* fix `tundra.tn.document:route` to log the transport content part when required
* fix `tundra.tn.profile:get` to allow unspecified fields in the `DeliveryMethods` document in the returned profile structure
* fix `tundra.tn.profile:list` to allow unspecified fields in the `DeliveryMethods` document in the returned profile structure
* fix `tundra.tn.profile:self` to allow unspecified fields in the `DeliveryMethods` document in the returned profile structure
* fix `tundra.tn.queue:each` `$daemonize?` to work correctly when `true` on Integration Server 9.x and higher
* fix `tundra.tn.queue:each` to ensure tasks are only processed once when `$concurrency` > 1, and ordering by creation time works correctly
* fix `tundra.tn.queue:each` to not throw `NullPointerException` caused by race condition by invoking service on separate `ServiceThread` allocated from the Integration Server thread pool
* fix `tundra.tn.queue:each` to respect explicitly setting `$retry.limit` to zero as an override of the receiver profile's retry settings to provide a per-queue way of disabling retries
* fix `tundra.tn.reliable:deliver` to allow `$destination` to be optional
* fix `tundra.tn.support.document:route` to correctly respect `TN_parms` that overwrite the sender/receiver/doctype
* fix `tundra.tn:chain` to work correctly with new `tundra.tn.document.content:get` output parameters
* fix `tundra.tn:derive` race condition with setting the `Derivative?` attribute
* fix `tundra.tn:derive` to work correctly with new `tundra.tn.document.content:get` output parameters
* fix `tundra.tn:enqueue` log statement when queuing is disabled to correctly perform variable substitution
* fix `tundra.tn:enqueue` to only set `Queued?` attribute to `true` if document was actually queued
* fix `tundra.tn:log` to not throw `NullPointerException` when bizdoc is not found
* fix `tundra.tn:process` to work correctly with new `tundra.tn.document.content:get` output parameters
* fix `tundra.tn:retrieve` to drop `$content.identity` after use
* fix `tundra.tn:split` to work correctly with new `tundra.tn.document.content:get` output parameters
* fix `tundra.tn:translate` to work correctly with new `tundra.tn.document.content:get` output parameters
* fix deferred routing feature to treat reliable service execution correctly rather than treating it the same as asynchronous execution

# 0.0.34 (2019-08-06)

* add `tundra.tn.document.attribute.datetime.transformer:constant` for setting a datetime attribute to a constant value via a document type attribute transformation
* add `tundra.tn.document.attribute.number.transformer.priority:imminence` for assigning a message priority based on imminence of the extracted datetime values to current time
* add `tundra.tn.document.attribute.number.transformer:constant` for setting a number attribute to a constant value via a document type attribute transformation
* add `tundra.tn.document.attribute.string.transformer:constant` for setting a string attribute to a constant value via a document type attribute transformation
* add new experimental deferred routing feature to `tundra.tn:receive` and `tundra.tn.content:route` for deferring execution of asynchronous processing rules to a dedicated fixed size thread pool, thereby constraining the compute resources required for bizdoc processing; disabled by default, this feature can be enabled via the package configuration `./config/package.hjson` file
* change `tundra.tn.content:route` to not include any `$attributes` specified that do not exist or are not active in Trading Networks on the routed document
* change `tundra.tn.document.error:exists` to be implemented in Java for 10x performance improvement
* change `tundra.tn.queue:each` and dependent queue processing services to exit their processing loops if the task scheduler is paused or stopped on the server instance on which it is running
* change `tundra.tn.queue:each` and dependent queue processing services to reduce contention on the DeliveryJob table of Trading Networks delivery queues by caching in memory which queues have queued tasks for 500ms and first checking this cache to determine whether to continue whenever a queue's scheduled task runs, which improves TN queue performance and reduces the impact on the database when there are many queues scheduled with short polling intervals such as every 1 second
* change `tundra.tn.support.document.strict:check` to be implemented in Java for 2x performance improvement
* change `tundra.tn.support.service:ensure` to set thread priority to value in `$bizdoc/Attributes/Thread Priority`, so that bizdocs with this attribute set can be processed at a higher or lower priority than normal if desired
* change `tundra.tn:amend`, `tundra.tn:derive`, and dependent services to support document amendments which can `merge`, `create`, `update`, or `delete` key value pairs
* change `tundra.tn:deliver` to perform variable substitution both before and after the invocation of `$service` if specified
* change `tundra.tn:enqueue` to set bizdoc attribute `Queued?` to `true`
* change `tundra.tn:process` to not refetch bizdoc after service invocation, as it is no longer required for checking if the user status was changed by the invoked service
* change `tundra.tn:receive` to return the HTTP header `X-Response-ID` set to the value of the internal ID of the resulting bizdoc
* change `tundra.tn:retrieve` to include the following extracted attributes on routed documents: `Content Archive`, `Content Name`, `Content Source`
* fix `tundra.tn.support.document.strict:check` to support duplicate document activity log message on v9.9+
* fix `tundra.tn:branch` handling of `enqueue` action to work correctly when `$branches/action/enqueue/$queues` is specified
* fix `tundra.tn:branch` to correctly map `$content.type.input` and `$content.type.output` input arguments for the `split` and `translate` actions
* fix `tundra.tn:branch` to not call `pub.flow:savePipelineToFile`
* fix `tundra.tn:receive` so that it responds with an HTTP 409 Conflict response when a duplicate is detected by Trading Networks on v9.9+
* fix `tundra.tn:receive` to not overwrite the extracted `DocumentID` on XML documents with the value in the URI query string parameter `id` nor the HTTP header `Message-ID`. If no `DocumentID` is extracted when the message is recognized, it will default to the following values in order of precedence: (1) the URL query string parameter `id` if specified, (2) the HTTP header `Message-ID` if specified, (3) a newly generated UUID

# 0.0.33 (2018-08-30)

* add `tundra.tn.document:reroute` for reprocessing a given bizdoc, and change `tundra.tn:reroute` to use this new service for its implementation
* change `tundra.tn.document:get` to support optionally throwing an exception if no bizdoc exists with the specified identity
* change `tundra.tn.document:normalize` to support optionally throwing an exception if the given bizdoc does not exist
* fix `tundra.tn.document:get` service comment to remove incorrect statement about thrown an exception if the bizdoc is not found
* fix `tundra.tn:derive` to include `OriginalSenderID` and `OriginalReceiverID` values in derived copies

# 0.0.32 (2018-04-20)

* fix `tundra.tn.schema:splitter` service comment typo
* fix `tundra.tn.support.document.transport:log` to correctly create a content part name which is also a valid file name on all major operating systems
* fix `tundra.tn:chain` service comment typo
* fix `tundra.tn:derive` to correctly set the user status to done on the processed bizdoc if it was reprocessed and not previously ignored
* fix `tundra.tn:process` service comment typo
* fix `tundra.tn:receive` to log transport info when receiving a non-Unknown document from a disallowed/spoofing sender

# 0.0.31 (2018-01-09)

* add `tundra.tn.document.duplicate:check` for checking if a document is a duplicate where the uniqueness criteria is the document type, sender, receiver, and document ID
* change `tundra.tn.content:route` to include route duration in activity log
* change `tundra.tn.document.attribute.number.transformer` services to use `tundra.document:uncase` rather than the deprecated `tundra.document.key:lowercase` service
* change `tundra.tn.queue:each` to always wait at least 10ms between successive polls of a queue
* change `tundra.tn.queue:each` to not throw a `java.util.ConcurrentModificationException` when competing with another thread or process for the head of the queue to support horizontal scaling of queue processing across multiple processes and servers
* change `tundra.tn.queue:each` to retry polling an empty queue after 500ms once if the immediately previous poll contained tasks to be processed, to improve performance when processing a queue at the same time tasks are being enqueued
* change `tundra.tn.queue:each` to truncate the transport status message logged against queued tasks to 512 characters to reduce `server.log` noise
* change `tundra.tn.schema:translator` to declare optional `$attributes` output parameter for setting attribute values on the resulting translated document
* change `tundra.tn.support.document:derive` to include derive duration in activity log
* change `tundra.tn.support.document:route` to include route duration in activity log when route fails strictness check
* change `tundra.tn.support.document:route` to not use a ULID for transport info content name suffix as current datetime provides sufficient uniqueness
* change `tundra.tn.support.service:reliable` to use `tundra.service:invoke` rather than `tundra.service:ensure` for simpler implementation and better performance when the invocation of `$reliable.service` throws an error
* change `tundra.tn.support:deliver` to not use a ULID for deliver response content name suffix as current datetime provides sufficient uniqueness
* change `tundra.tn.support:derive` to use `tundra.service:invoke` rather than `tundra.service:ensure` for better performance when an error occurs
* change `tundra.tn.support:receive` to include route duration in activity log
* change `tundra.tn:log` to truncate `$summary` to 240 characters, and `$message` to 1024 characters, before logging to reduce `server.log` noise
* change `tundra.tn:receive` to support sender, receiver, id, and type query string values for XML as well as flat file content
* fix `tundra.tn.queue:each` use of `java.text.SimpleDateFormat` to be thread safe
* fix `tundra.tn.queue:each` wait between retries to work correctly when `$concurrency > 1`
* fix `tundra.tn.support.deliver.destination:normalize` to correctly support query strings in non-mailto URIs

# 0.0.30 (2017-11-03)

* change `tundra.tn.document.attribute.number.transformer.duration:age` to use new `tundra.datetime:format` input and output arguments
* change `tundra.tn.document.attribute.number.transformer.duration:effective` to use new `tundra.datetime:format` input and output arguments
* change `tundra.tn.profile.cache:refresh` to remove deleted profiles from the cache
* change `tundra.tn.schema:processor` to reflect support for `$status.done` being returned by a processing service
* change `tundra.tn:deliver` delivery logs to include the `InternalID` and `DocumentID` in the log context
* change `tundra.tn:deliver` to support defaulting file names for `file`, `ftp`, `ftps`, and `sftp` protocols for destinations that use variable substitution
* change `tundra.tn:deliver` to support `sftp` delivery on Integration Server versions 9.0 and higher
* change `tundra.tn:receive` to support the sender ID, receiver ID, document ID, and document type name for flat file content being provided via the URL query string parameters `sender`, `receiver`, `id`, and `type` respectively
* change `tundra.tn:receive` when receiving content that is not recognized to route as an `Unknown` document for diagnostics and return an HTTP 406 Not Acceptable response to the client
* change `tundra.tn:retrieve` to support `sftp` retrieval on Integration Server versions 9.0 and higher
* change services dependent on `tundra.uri:decode` and `tundra.uri:encode` to reflect changes in their input and output arguments
* fix `tundra.tn.support.deliver.destination:normalize` to work correctly with change to `tundra.uri:emit` to support unadorned path strings and files
* fix `tundra.tn.support.document:route` to defend against `$type` variable in the pipeline when calling `tundra.tn.profile:get`
* fix `tundra.tn.support:access` to drop all variables before exiting
* fix `tundra.tn.support:receive` to drop unnecessary `$type` and `$string` variables from the pipeline
* fix `tundra.tn.support:shutdown` to not remove scheduled tasks for nodes other than the executing node
* fix `tundra.tn:deliver` to support URI variable substitution specified with either literal unadorned `%` delimiters, or the URI-encoded `%25` delimiters
* fix `tundra.tn:log` `$summary` and `$message` inputs to be text fields rather than pick lists
* fix `tundra.tn:log` to not attempt to log against a bizdoc that is not persisted which causes a foreign key exception to be logged
* fix `tundra.tn:receive` to correctly return an HTTP 403 Forbidden response when a partner attempts to route a document posing as another partner
* fix `tundra.tn:translate` service comment to display correctly in Designer

# 0.0.29 (2017-09-08)

* add `tundra.tn.document.attribute.string.transformer.datetime.threshold:after` for comparing extracted datetime strings with a threshold datetime calculated from the current datetime plus a specified duration
* add `tundra.tn.document.attribute.string.transformer.datetime.threshold:before` for comparing extracted datetime strings with a threshold datetime calculated from the current datetime plus a specified duration
* add `tundra.tn.document.attribute.string.transformer.datetime.threshold:equal` for comparing extracted datetime strings with a threshold datetime calculated from the current datetime plus a specified duration
* add `tundra.tn:connect` to allow a client to test connecting to and authenticating with Integration Server
* add `tundra.tn.document.attribute.string.transformer.uuid:generate` for assigning a newly generated `UUID` to an extracted attribute
* change `tundra.tn.content:recognize` to always use the default `UTF-8` encoding when serializing the provided `$content` to text to work around an issue where `wm.tn.doc:handleLargeDoc` does not correctly handle other character sets
* change `tundra.tn.content:recognize` to be more memory efficient when `TN_parms/$contentLength` is specified
* change `tundra.tn.content:recognize` to not recognize and instead return early if the given `$content` is empty (has zero length)
* change `tundra.tn.content:recognize` to support `jsonStream` objects
* change `tundra.tn.content:recognize` to support new `$content.identity` input argument which determines how a `DocumentID` is assigned when it is not extracted
* change `tundra.tn.content:route` to not route and instead return early if the given `$content` is empty (has zero length)
* change `tundra.tn.content:route` to not use a document reference for  sender and receiver for more flexibility when working with these structures in Developer/Designer
* change `tundra.tn.content:route` to silently support content handler inputs such as `node` or `ffdata` or `jsonStream`, and `iDocList` for SAP adapter listeners: if `$content` is not specified, the first of these other arguments found will be used instead for routing
* change `tundra.tn.content:route` to support new `$content.identity` input argument which determines how a `DocumentID` is assigned when it is not extracted
* change `tundra.tn.document.content:get` to return `$part` content part name for the content returned
* change `tundra.tn.document:get` to not use a document reference for sender and receiver for more flexibility when working with these structures in Developer/Designer
* change `tundra.tn.document:normalize` to not use a document reference for sender and receiver for more flexibility when working with these structures in Developer/Designer
* change `tundra.tn.profile:get` to support `$refresh?` input argument for optionally refreshing profiles from the database
* change `tundra.tn.profile:list` to support `$refresh?` input argument for optionally refreshing profiles from the database
* change `tundra.tn.profile:self` to support `$refresh?` input argument for optionally refreshing profiles from the database
* change `tundra.tn.support.queue:each` to not restart retrying job completion when completing a delivery job fails after the maximum number of retries
* change `tundra.tn.support.queue:registration` to not remove existing registrations if they already exist
* change `tundra.tn:chain`, `tundra.tn:deliver`, `tundra.tn:derive`, `tundra.tn:process`, `tundra.tn:split`, and `tundra.tn:translate` to specify the previous user status required when updating user status to `DONE` to ensure if the status is updated by another service or thread it is not overwritten
* change `tundra.tn:enqueue` `$queue` input to be a pick list that includes the value `Receiver's Queue` for convenience
* change `tundra.tn:enqueue` service comment to clarify that when `$queues/force?` input argument is false, the document will not be requeued if there is already an existing task with a status of `DELIVERING` or `DONE` for the same queue
* change `tundra.tn:enqueue` success activity log message to be consistent with the other Tundra bizdoc processing services
* change `tundra.tn:log` to drop unused variables
* change `tundra.tn:log` to label the call stack as `Service` in logged diagnostics
* change `tundra.tn:receive` to use `tundra.service:invoke` instead of `tundra.service:ensure` for a simpler implementation
* change `tundra.tn:receive` to use `tundra.support.receive:respond` for handling the response to the request
* change `tundra.tn:retrieve` to support FTP and FTPS file retrieval
* change `tundra.tn:retrieve` to support new `$content.identity` input argument which determines how a `DocumentID` is assigned when it is not extracted
* change `tundra.tn:retrieve` to use `$content.length` when provided by content processing service for `TN_parms/$contentLength` to allow for more memory efficient routing of large content
* change activity log messages to only use double quotes where necessary, such as when including a description that could include whitespace in a message; service names and internal document IDs are no longer quoted
* change tundra.tn.exception:handle attached content part names to all be consistent using the format `<service>_<content>_<datetime>_<ulid>`, which ensures the content part names are legal filenames, which then supports exporting the transaction content via My webMethods correctly
* change `tundra.tn:deliver` attached content part names to all be consistent using the format `<service>_<content>_<datetime>_<ulid>`, which ensures the content part names are legal filenames, which then supports exporting the transaction content via My webMethods correctly
* change `tundra.tn:receive` attached content part names to all be consistent using the format `<service>_<content>_<datetime>_<ulid>`, which ensures the content part names are legal filenames, which then supports exporting the transaction content via My webMethods correctly
* deprecate `tundra.tn.document.attribute.string.transformer.id:generate`, use `tundra.tn.document.attribute.string.transformer.uuid:generate` instead
* fix `tundra.tn.content:recognize` to drop rather than return undeclared flags output argument
* fix `tundra.tn.document.attribute` transformer services to always return `newValues` even when the input values list is null or does not exist; this fixes the following exception from being logged by Trading Networks: `java.lang.Exception: Custom transformation service <service> for the attribute <attribute> did not return "newValues" in the pipeline.`
* fix `tundra.tn:amend` to include a `$pipeline` input and to drop `$status.silence?`
* fix `tundra.tn:deliver` to convert Trading Network's default use of the non-canonical `UTF8` character set name to be `UTF-8`
* fix `tundra.tn:receive` service comment formatting in Designer
* fix `tundra.tn:receive` to correctly handle large XML documents
* fix exception handling to not include recursive object references to avoid stack overflow errors when serializing

# 0.0.28 (2017-06-05)

* change `tundra.tn.content:recognize`  to use new `Tundra/tundra.mime.type:classify` service
* change `tundra.tn.content:recognize` to support `$content` being provided as a `com.sap.conn.idoc.IDocDocumentList` object
* change `tundra.tn.content:recognize` to use `TN_parms/$contentSchema` when specified as the document reference used when serializing an `IData` document or `com.sap.conn.idoc.IDocDocumentList` object
* change `tundra.tn.content:route` to support `$content` being provided as a `com.sap.conn.idoc.IDocDocumentList` object
* change `tundra.tn.content:route` to use `TN_parms/$contentSchema` when specified as the document reference used when serializing an `IData` document or `com.sap.conn.idoc.IDocDocumentList` object
* change `tundra.tn.document:parse` to not log start and end datetimes against the bizdoc being parsed
* change `tundra.tn.profile:get` to not use a document reference for the output argument so that it is easier to map values from, for example, the ExternalID section
* change `tundra.tn.profile:get` to support multiple instances of an ExternalID, and include the default external ID as DefaultID
* change `tundra.tn.profile:list` to not use a document reference for the output argument so that it is easier to map values from, for example, the ExternalID section
* change `tundra.tn.profile:self` to not use a document reference for the output argument so that it is easier to map values from, for example, the ExternalID section
* change `tundra.tn.profile:self` to support multiple instances of an ExternalID, and include the default external ID as DefaultID
* change `tundra.tn.support.deliver.destination:normalize` to default the file name for `file:`, `ftp:`, `sftp:`, and `ftps:` URLs when no file is specified: the file name is defaulted to the value of the `Content Name` attribute if it exists on the bizdoc, otherwise the internal ID
* change `tundra.tn.support.document:derive` to use new `Tundra/tundra.mime.type:classify` service
* change `tundra.tn.support.queue:reflect` to use `tundra.schema:thread` instead of `tundra.support.schema:thread`, which has been deleted
* change `tundra.tn:log` to include current datetime in logged diagnostics
* change `tundra.tn:receive` to support `Content-Encoding` header values of `base64`, `gzip`, and `zip`
* fix `tundra.exception` services to correctly declare input arguments
* fix `tundra.tn.queue:each` to not throw `java.lang.NullPointerException` when delivery job is deleted while running
* fix `tundra.tn.support.document.attribute:merge` to correctly handle `$content` being provided as an `IData`, `Node`, `byte[]`, `InputStream`, or `String`

# 0.0.27 (2017-02-24)

* add `tundra.tn.support:startup` and `tundra.tn.support:shutdown` services for providing all package startup and shutdown tasks
* change `tundra.tn.support.document.attribute:merge` use of `tundra.document:merge` to use new `$operands` input argument
* change `tundra.tn.support.document:derive` use of `tundra.document:merge` to use new `$operands` input argument
* change `tundra.tn.support.queue:reflect` to declare its output arguments
* change `tundra.tn.support:deliver` use of `tundra.document:merge` to use new `$operands` input argument
* change `tundra.tn.support` service comments to include service name
* change `tundra.tn:log` to use `tundra.document:trim` rather than the deprecated `tundra.document.value:trim` service
* fix `tundra.tn.profile.cache:clear` and `tundra.tn.profile.cache:refresh` to first refresh the internal Trading Networks profile cache to ensure the latest Trading Networks changes are loaded from the database prior to caching
* fix `tundra.tn.queue` services when `$concurrency` > 1 to not leak webMethods Adapter Runtime (ART) connections by not cloning the current call stack when initializing the thread pool

# 0.0.26 (2016-10-30)

* add `tundra.tn:publish` and related services for publishing a bizdoc's content to the webMethods messages subsystem via `pub.publish:publish`
* change `tundra.tn.content:recognize` service comment and README entry to reflect support for `$content` being specified as an `org.w3c.dom.Node` object
* change `tundra.tn.content:recognize` to support providing `$content` as an `org.w3c.dom.Node` object
* change `tundra.tn.content:route` service comment and README entry to reflect support for `$content` being specified as an `org.w3c.dom.Node` object
* change `tundra.tn.document.status:set` to support optimistic concurrency when updating status
* change `tundra.tn.document.type:*` services to convert Trading Networks `FixedData` document type objects to flow service compatible `IData` objects
* change `tundra.tn.queue:each` thread naming to clarify parent start time from task start time, and use [ULID](https://github.com/alizain/ulid) instead of [UUID](http://docs.oracle.com/javase/6/docs/api/java/util/UUID.html) for parent and thread contexts
* change `tundra.tn:deliver` to use new `$system/property` output from `tundra.system:reflect`, rather than the deprecated `$system/properties`
* change `tundra.tn:enqueue` to use optimistic concurrency when updating the bizdoc user status so that it does not overwrite status changes made between the bizdoc being queued and the attempt to update the bizdoc user status
* change `tundra.tn:receive` to use new `$system/property` output from `tundra.system:reflect`, rather than the deprecated `$system/properties`
* change `tundra.tn:retrieve` to enable service auditing on error when top-level service
* change `tundra.tn:retrieve` to ensure `$service.input` does not exist in pipeline when calling `tundra.content:retrieve`
* fix `tundra.tn.document:parse` service comment and README entry typo
* fix `tundra.tn.profile:get` and related services to treat all delivery destinations consistently in the returned profile structure
* fix `tundra.tn.schema:processor` comment and README entry typo
* fix `tundra.tn:deliver` and related services, and `tundra.tn.profile.delivery:get` to support custom delivery destinations
* fix `tundra.tn:deliver` service comment to clarify that variables provided in `$pipeline` take precedence and override their corresponding values in the `$destination` URI
* fix `tundra.tn:receive` service comment typo and README entry typo

# 0.0.25 (2016-07-05)

* add `tundra.tn.document.attribute.string.transformer.find:all` for transforming a list of string values into a single boolean value by returning whether all values include a given regular expression
* add `tundra.tn.document.attribute.string.transformer.find:any` for transforming a list of string values into a single boolean value by returning whether any values include a given regular expression
* add `tundra.tn.document.attribute.string.transformer.find:none` for transforming a list of string values into a single boolean value by returning whether no values include a given regular expression
* add `tundra.tn.document.attribute.string.transformer.match:all` for transforming a list of string values into a single boolean value by returning whether all values match a given regular expression
* add `tundra.tn.document.attribute.string.transformer.match:any` for transforming a list of string values into a single boolean value by returning whether any values match a given regular expression
* add `tundra.tn.document.attribute.string.transformer.match:none` for transforming a list of string values into a single boolean value by returning whether no values match a given regular expression
* add `tundra.tn.support.queue` services for reflecting and starting, stopping, and interrupting queue processing threads
* change `tundra.tn.document:get` to only fetch the profile cache instance once rather than each time it is used
* change `tundra.tn.document:normalize` to only fetch the profile cache instance once rather than each time it is used
* change `tundra.tn.queue:each` and dependent services to correctly handle thread interruption by exiting the queue polling loop
* change `tundra.tn.queue:each` and dependent services to improve performance of job processing by sleeping the supervisor thread only 5ms while all worker threads are currently busy before the supervisor checks worker thread status again, and by scheduling the first refresh of the queue status from the TN database +5 seconds in the future rather than immediately
* change `tundra.tn.queue:each` and dependent services to set an exhaustion user status on the related bizdoc when all retries of a task fails
* change `tundra.tn.queue:each` and dependent services to support new thread daemon mode where the service does not exit until the queue is disabled or suspended, or the server is shutdown
* change `tundra.tn.queue:each` and dependent services to support specifying `$retry.factor` as a float
* change `tundra.tn.queue:each` and dependent services to support specifying the minimum age a task must be before it is eligible to be dequeued and processed
* change `tundra.tn.queue:each` to support being stopped and started, and to defend against multiple threads attempting to process the same queue at the same time
* change `tundra.tn.queue:get` output argument to include `com.wm.app.tn.delivery.DeliveryQueue` object in queue structure
* change `tundra.tn.queue:list` output argument to include `com.wm.app.tn.delivery.DeliveryQueue` object in queue structure
* change `tundra.tn.support.profile.schedule:create` to schedule the task on the node the service runs on instead of `$all`
* change `tundra.tn.support.profile.schedule:remove` to only remove profile cache schedules from current Integration Server node
* change `tundra.tn:derive` to not copy the `Translation?` attribute from the source bizdoc to the derivative bizdocs
* change `tundra.tn:receive` to return more appropriate HTTP response codes for different processing results, as follows:

| Response | Reason |
| --- | --- |
| 202 Accepted | Received content was routed successfully |
| 400 Bad Request | Received content was malformed |
| 403 Forbidden | Sender was denied access to route the received content |
| 406 Not Accepable | Received content was not recognized (Unknown) |
| 409 Conflict | Received content was detected as a duplicate |
| 422 Unprocessable Entity | Received content failed validation |
| 500 Internal Server Error | All other errors that occur while processing |
* fix `tundra.tn.exception:handle` to default recoverability of an exception to true

# 0.0.24 (2016-01-19)

* add `tundra.tn.document.attribute.number.transformer.duration:age` for returning the number of milliseconds from an extracted datetime to the current datetime
* add `tundra.tn.document.attribute.number.transformer.duration:effective` for returning the number of milliseconds from the current datetime to an extracted datetime
* add `tundra.tn.document.attribute.number.transformer:average` for calculating the average of a list of extracted number values
* add `tundra.tn.document.attribute.number.transformer:maximum` for returning the maximum value from a list of extracted numbers
* add `tundra.tn.document.attribute.number.transformer:minimum` for returning the minimum value from a list of extracted numbers
* add `tundra.tn.document.attribute.number.transformer:add` for transforming a list of extracted numbers into a total sum; unlike the Trading Networks built-in sum transformer, this service handles null values without throwing a NullPointerException
* change `tundra.tn.document.attribute.string.transformer.dns:self` to always return a single value regardless of the input values provided
* change `tundra.tn.document.attribute.string.transformer.id:generate` to support the input values being null by returning a single newly-generated UUID
* change `tundra.tn.document.attribute.string.transformer.profile:get` to support input values being null by returning a single result containing the internal ID of the partner profile associated with the given arg value
* change `tundra.tn.document.attribute.string.transformer.profile:self` to support input values being null by returning a single item for the internal ID of the My Enterprise profile
* change `tundra.tn.document.attribute.string.transformer.user:current` to always return a single value containing the current user regardless of the input values
* change `tundra.tn.document.content:get` to support returning `$content` as either an input stream, array of bytes, or string
* change `tundra.tn.exception:handle` implementation to java and to automatically attach a new content part to the associated bizdoc for content related to the exception being handled: this is used to attach the response body of a failed HTTP request to ease diagnosis
* change `tundra.tn.queue:each` and dependent services to fail fast when concurrent modification to a queued job is detected while dequeuing to defend against other threads, processes, or servers processing the same job at the same time
* change `tundra.tn.queue:each` and related services to only process delivery tasks if they are older than 750ms to reduce likelihood of race condition between routing thread and queue processing thread when updating the bizdoc status
* change `tundra.tn.queue:each` to not thrash the Trading Networks database when either there are no jobs waiting to be processed or when all worker threads are busy
* change `tundra.tn:deliver` to support setting the destination URI dynamically using a value associated with a specified pipeline key by specifying an opaque URI `pipeline:key`, for example a destination of `pipeline:foo/bar[0]/baz` will resolve the key `foo/bar[0]/baz` against the pipeline and use the associated value as the destination URI to be delivered to
* change `tundra.tn:deliver` to support specifying the filename for file deliveries in `$pipeline/$filename`, which if provided will override the file name in the `$destination` URI
* change `tundra.tn:enqueue` to default `$force` to `true` when the deprecated `$queue` input is used to preserve the original behaviour for this input; when using `$queues` input, `$force?` now always defaults to `false`, even when only one queue is specified
* change `tundra.tn:enqueue` to support restarting tasks with status `DELIVERING` that have exceeded a maximum allowed delivery duration of 10 minutes
* change `tundra.tn:receive` spoofing security exception message to quote username and sending profile name and ID to ease diagnosis
* change `tundra.tn:reject` to use double-quotes to delimit service name in exception message/response body
* fix `tundra.tn.schema.document.attribute` transformer schemas to allow null input values
* fix `tundra.tn:enqueue` to correctly set user status when reprocessing a bizdoc
* move `tundra.tn.schema.attribute` schemas to `tundra.tn.schema.document.attribute` folder

# 0.0.23 (2015-12-02)

* add `tundra.tn.content:recognize` service for recognizing content and returning an unrouted bizdoc
* add `tundra.tn.task:restart` for restarting a delivery task regardless of status
* change `tundra.tn.content:route` to support variable substitution on attribute values
* change `tundra.tn.document.status:set` to automatically suffix user status `DONE` with `W/ ERRORS` when the bizdoc has errors
* change `tundra.tn.document:parse` to record parse duration in the bizdoc's activity log
* change `tundra.tn.document:parse` to support new input argument `$validate?` which when `true` will validate the parsed content against the specified schema
* change `tundra.tn.queue:each` and related services to support specifying the thread priority used to process tasks
* change `tundra.tn:derive` and related services to support an option to always create a derivative even if one already exists
* change `tundra.tn:enqueue` and related services to support an option to always enqueue document even if a task for the specified queue already exists
* change `tundra.tn:enqueue` to declare backwards-compatible input argument `$queue` to ensure existing uses of this argument can be maintained in TN Console
* change `tundra.tn:enqueue` to restart existing queued tasks when the bizdoc is reprocessed and only one queue is specified in `$queues` list
* change `tundra.tn:split` and related services to optionally validate input and output content
* change `tundra.tn:translate` and related services to optionally validate input and output content
* fix `tundra.tn:enqueue` log message to not incorrectly suggest that `DONE` tasks can be restarted manually
* fix the way `tundra.tn.queue:each` and related services handle not retrying when unrecoverable errors exist no the related bizdoc to ensure the delivery task failure status is correctly reflected in Trading Networks

# 0.0.22 (2015-11-04)

* add `tundra.tn.document.attribute.string.transformer.id:generate` for converting an extracted attribute to a newly generated UUID
* add `tundra.tn.document.attribute.string.transformer:first` for returning the first item in the given list of extracted string attributes
* add `tundra.tn.document.attribute.string.transformer:last` for returning the last item from the given list of extracted string values
* add `tundra.tn.document.attribute.string.transformer:length` for returning the length of a list of extracted attribute values
* add `tundra.tn.document.attribute.string.transformer:unique` for returning only the unique values from an extracted attribute list
* add support for silencing status changes to all the `tundra.tn:*` bizdoc processing services and their related services
* change `tundra.tn.content:route` to support specifying a list of attributes to be set on the resulting bizdoc
* change `tundra.tn.document:derive` input to use a derivative rule instead of all the constituent parts of a derivative rule
* change `tundra.tn.profile.cache:list` to return an array of duplicated `IData` profiles rather than the cached profiles themselves
* change `tundra.tn.queue:each` and related services to not retry when unrecoverable errors exist no the related bizdoc
* change `tundra.tn:derive` and related services to set bizdoc attribute `Derivative?` to `true` on the derived documents
* change `tundra.tn:enqueue` and related services to support conditional
* change `tundra.tn:enqueue` to correctly support reprocessing documents by checking for pre-existing delivery tasks related to the document:
  1. if there are pre-existing failed/stopped tasks, these are restarted
  2. else if there are pre-existing in-progress or completed tasks, the document is not requeued to the related delivery queue as it was already successfully delivered or delivery is still in-progress
  3. else the document is requeued to the related delivery queue
* change `tundra.tn:split` and related services to set bizdoc attribute `Translation?` to `true` on the split documents
* change `tundra.tn:translate` and related services to set bizdoc attribute `Translation?` to `true` on the translated document
* change TundraTN profile cache to return profiles as a duplicated `IData` for thread safety
* fix `tundra.tn.document:relate` to not update `$pipeline` variable if it already exists in the pipeline
* fix `tundra.tn:derive` and related services to work correctly when amending XML documents that include namespaces

# 0.0.21 (2015-09-10)

* change `tundra.tn.queue:each` and dependent services to only suspend queue delivery when `$suspend?` is `true` and the queue is not already suspended; this ensures that only the first task to reach exhaustion suspends the queue and has a user status of `SUSPENDED`
* change `tundra.tn:derive` and related services to set the `GroupID` and `ConversationID` on the derived documents using the source document's values for these attributes
* change `tundra.tn:split` and related services to set the `GroupID` and `ConversationID` on the split documents using the source document's values for these attributes
* Change `tundra.tn:translate` and related services to set the `GroupID` and `ConversationID` on the translateed documents using the source document's values for these attributes
* change `tundra.tn:retrieve` to provide the source location and archive location in `TN_parms` when routing each retrieved content

# 0.0.20 (2015-08-31)

* add `tundra.tn.document.attribute.string.transformer.dns:self` for transforming a bizdoc's extracted attribute to have the value of the localhost's domain name, host name, or IP address
* add `tundra.tn.queue:get` for returning the properties for a Trading Networks delivery queue with the given name
* change `tundra.tn.document.content:add` to support an optional input flag `$overwrite?` which if true will overwrite an existing content part if one exists with the same name
* change `tundra.tn.document:get` to improve performance by using the TundraTN profile cache when returning the sender and receiver profiles
* change `tundra.tn.document:normalize` to improve performance by using the TundraTN profile cache when returning sender and receiver profiles
* change `tundra.tn.queue:each` and dependent services input argument from `$retries` to `$retry.limit` on, in preparation for adding other retry related input arguments; `$retries` is still supported silently for backwards compatibility
* change `tundra.tn.queue:each` and dependent services support for ordered queue processing to allow for the queue suspension on task exhaustion to be configurable via new input variable `$suspend?`
* change `tundra.tn.queue:each` and dependent services to add the task being processed to the input pipeline of the processing service as a variable named `$task`
* change `tundra.tn.queue:each` and dependent services to support using the `ordered?` and `suspend?` flags for both single threaded and concurrent processing
* change `tundra.tn.queue:each` to support specifying a wait between retries and a factor to use to increase the wait between each subsequent retries; also now supports using the retry wait and factor settings on the receiver's profile, if configured
* change `tundra.tn.queue:length` to support the input argument `$queue` being null
* change `tundra.tn.support.document:route` to first check if `WmPRT/pub.prt.tn:handleBizDoc` exists before calling it, thus removing unstated dependency of TundraTN on the WmPRT package
* change `tundra.tn:process` to support `$service` returning `$status.done` in its output pipeline; if returned, this value will be used to update the user status of the bizdoc
* change `tundra.tn:receive` to not include email content when logging transport info as it is potentially a large amount of data
* fix `tundra.tn.profile:self` to not throw a NullPointerException when the My Enterprise profile does not exist
* fix `tundra.tn:deliver` to redact delivery password even when `$subsitute` is false

# 0.0.19 (2015-03-19)

* add `tundra.tn.document.content:remove` for deleting the bizdoc content part with the given name
* add `tundra.tn.document.namespace:get` for retrieving the namespace declarations from a given bizdoc in a useful format, and automatically copy `$namespace/prefix0` if it exists to `$namespace/ns`, so that parsing a document with a default namespace using `WmPublic/pub.xml:*` services works correctly
* add `tundra.tn.queue:length` for returning the number of tasks with a status of `QUEUED` or `DELIVERING` in the given queue
* add `tundra.tn.queue:list` for returning a list of all registered TN delivery queues
* change `tundra.tn.queue` class to invoke `wm.tn.queuing:updateQueue` using the `Service.doInvoke` method, rather than a direct java invocation
* change `tundra.tn:deliver` and related services to add an input argument `$substitute?` which can be set to false to turn off variable substitution when it is not required
* change `tundra.tn:deliver` to support using configured partner profile certificates for authentication for HTTPS and FTPS transports
* fix `tundra.tn.profile:get`, `tundra.tn.profile:list`, and `tundra.tn.profile:self` to return a copy of the cached profile, rather than the cached profile itself, so that inadvertent changes made to the profile are not persisted in the cache
* fix `tundra.tn:deliver` to correctly support when the invocation of `$service` returns values in `$pipeline` by merging them into the actual pipeline
* fix `tundra.tn:deliver` to support mailto destinations that include unencoded '%' characters used to delineate variable substitution statements.
* fix `tundra.tn:translate` to not route the translated document in strict mode as it is the processing rule's responsiblity for checking the validity or otherwise of the translated document

# 0.0.18 (2014-11-17)

* change `tundra.tn.content:route` to default `$strict?` to `true`, and to use the `$content.type` and `$encoding` returned by `tundra.content:emit` when appropriate
* change `tundra.tn.support:receive` to drop unused `$content.type` variable from the pipeline after calling `tundra.content:emit`
* fix `tundra.tn.support.content:route` to correctly use the specified `$encoding` when sniffing for XML content using `tundra.xml:validate`
* fix `tundra.tn:process` to not include `$prefix?` and `$status.done` arguments in the input pipeline for `$service`

# 0.0.17 (2014-10-29)

* change `tundra.tn:chain` and `tundra.tn:process` and related queue and reliable services to add the bizdoc content part input stream to the input pipeline of the processing service/s when `$parse` is `false`

# 0.0.16 (2014-10-27)

* add `tundra.tn.document.attribute.string.transformer.profile:get` for extracting the internal ID of a specific partner profile, which lets you fix the sender or receiver of a document to that profile in the document type
* add `tundra.tn.document.attribute.string.transformer.user:current` for extracting the currently logged on username as a bizdoc attribute
* change `tundra.tn.document.content:add` to use the following precedence rules for determining the encoding of the content: (1) use `$encoding` if specified, (2) use `$content.type` charset parameter if specified, or (3) use `UTF-8`
* change `tundra.tn:deliver` to include username and redacted password in the logged URL to provide evidence of the authority used
* change `tundra.tn:derive` to process all derivatives, even if when an error is encountered; only after all derivatives have been processed will any errors encountered be rethrown
* change `tundra.tn:receive` to redact the Authorization header for HTTP requests when adding the transport info content part; also change the format of the content part from XML to YAML so it is smaller and easier to read
* fix `tundra.tn.derive derivative` success log statement to use variable substitution for the derivative index
* fix `tundra.tn.profile:get` to not throw a `NullPointerException` when the given profile ID does not exist
* fix missing substitution variable in missing sender/receiver error message in `tundra.tn.support.document:derive`

# 0.0.15 (2014-08-27)

* add `tundra.tn.document.attribute.string.transformer:find` for transforming a string attribute to a boolean based on whether the given regular expression pattern is found in the attribute value
* add `tundra.tn.document:normalize` for a more efficient way of handling both full bizdocs and bizdoc subsets than using `tundra.tn.document:get` to refetch the bizdoc
* add `tundra.tn.document.type:normalize` for a more efficient way of handling both full document types and document type subsets than using `tundra.tn.document.type:get` to refetch the type
* add `tundra.tn.profile.cache:*` services to the TundraTN public API
* add `tundra.tn.queue:disable` for disabling a given Trading Networks queue
* add `tundra.tn.queue:drain` for draining a given Trading Networks queue
* add `tundra.tn.queue:enable` for enabling a given Trading Networks queue
* add `tundra.tn.queue:suspend` for suspending a given Trading Networks queue
* add `tundra.tn:enqueue` action to `tundra.tn:branch` and related services
* add `tundra.tn:enqueue` for queueing documents to a Trading Networks queue
* add `tundra.tn:status` and related services for changing the status on a bizdoc from a processing rule, reliable service execution task, or queue
* change the local in-memory profile cache to also lazily cache profiles by external ID
* change `tundra.tn:deliver` to support sap+idoc URI scheme
* change `tundra.tn.content:route` to add `$encoding` input
* change `tundra.tn.content:route` to default to using UTF-8 character encoding
* change `tundra.tn.content:route` to not overwrite `TN_parms/$contentEncoding` with the MIME type charset parameter if it was already set by the caller
* change `tundra.tn.content:route` to support JSON
* change `tundra.tn.content:route` to support XML namespaces
* change `tundra.tn.document.attribute.string.transformer:match` to set the `tundra.string:match` `$literal` input to always be false
* change `tundra.tn.document.content:add` logging to not include call stack, as `tundra.tn:log` now provides this automatically
* change `tundra.tn.document.content:add` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document.content:exists` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document.content:get` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document.content:get` to return the correct content type even for content types not known by Trading Networks
* change `tundra.tn.document.error:exists` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document.schema:get` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document.status:set` logging to not include call stack, as `tundra.tn:log` now provides this automatically
* change `tundra.tn.document:derive` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document:get` to not throw an exception when no bizdoc with the given ID exists
* change `tundra.tn.document:parse` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.document:parse` to support parsing JSON content parts
* change `tundra.tn.document:parse` to support XML namespaces
* change `tundra.tn.document:relate` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.profile:get` to not throw an exception when there is no matching profile for given profile ID
* change `tundra.tn.queue:each` and dependent processing services to support processing tasks in ascending task creation order even when task is retried, and suspend queue on failed task exhaustion (no more retries)
* change `tundra.tn.queue:each` and dependent processing services to support specifying the number of retries for failed tasks when no retry settings are present on the receiver profile, and to support specifying the maximum number of tasks to process in one invocation
* change `tundra.tn.queue:each` and dependent services to check the queue state if invoked by Trading Networks inside the processing loop, and stop processing tasks if the queue state is disabled or suspended
* change `tundra.tn.queue:each` to dequeue at most 2 \* `$concurrency` tasks at once when `$concurrency` > 1
* change `tundra.tn.queue:each` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn.queue:each` to process queue items on the current thread when `$concurrency` = 1
* change `tundra.tn.support.document:derive` logging to not include call stack, as `tundra.tn:log` now provides this automatically
* change `tundra.tn.support.document:relate` logging to not include call stack, as `tundra.tn:log` now provides this automatically
* change `tundra.tn.support.document:route` to use `$caller` output from `tundra.service:callstack,` instead of calculating it itself
* change `tundra.tn.support.queue:each` to exit from its processing loop if an exception is thrown by the `tundra.tn.support.queue.task:execute` service
* change `tundra.tn.support.stream:route` logging to not include call stack, as `tundra.tn:log` now provides this automatically
* change `tundra.tn.support.stream:route` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:*` services to support JSON
* change `tundra.tn:amend` to include delivery duration in log statement
* change `tundra.tn:branch` and related services to support `tundra.tn:status` as an action
* change `tundra.tn:branch` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:branch` to include delivery duration in log statement
* change `tundra.tn:branch` to support XML namespaces
* change `tundra.tn:chain` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:chain` to include delivery duration in log statement
* change `tundra.tn:deliver` and related services to ignore users and passwords set to empty strings on receiver profile delivery destination
* change `tundra.tn:deliver` to ignore deliveries where no destination is specified
* change `tundra.tn:deliver` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:deliver` to include delivery duration in log statement
* change `tundra.tn:deliver` to log processing attempt and success when `$service` is invoked
* change `tundra.tn:deliver` to not attach a delivery response content part to bizdoc if there is no response or if it is empty
* change `tundra.tn:deliver` to redact user and password from activity log statements
* change `tundra.tn:deliver` to support ftp URI scheme
* change `tundra.tn:deliver` to support jms URI scheme
* change `tundra.tn:deliver` to support variable substitution on both the `$destination` URI string, and the parsed `$destination` URI document
* change `tundra.tn:derive` and related services so TN_parms take precedence over document recognition: this means you can now change the document type as part of a derivative if you set TN_parms/DoctypeName to a different Document Type in the derivative rule
* change `tundra.tn:derive` derivative successful log statement to use `tundra.yaml:emit` on the derivative rule, instead of hand-crafting the message
* change `tundra.tn:derive` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:derive` to include delivery duration in log statement
* change `tundra.tn:discard` to be stateless
* change `tundra.tn:enqueue` to include delivery duration in log statement
* change `tundra.tn:log` to automatically append diagnostics (host, session, thread, call stack) to log message
* change `tundra.tn:log` to support `$context` input for specifying arbitrary structured information to be appended to the log message with other diagnostics
* change `tundra.tn:log` to trim leading and trailing whitespace from `$type`, `$class`, `$summary`, `$message`, and `$context` inputs
* change `tundra.tn:process` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:process` to include delivery duration in log statement
* change `tundra.tn:receive` to be stateless
* change `tundra.tn:receive` to scope implementation so query string parameters are not leaked into subsequent processing rule action
* change `tundra.tn:receive` to support Integration Server v9.5 JSON content handler
* change `tundra.tn:reject` to be stateless
* change `tundra.tn:retrieve` to optionally clean up archived files older than a given age
* change `tundra.tn:split` and related services to incrementally serialize then route the translated documents to improve the perceived performance of the service
* change `tundra.tn:split` and related services to support disabling the creation of a relationship between the original document and the split documents
* change `tundra.tn:split` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:split` to include delivery duration in log statement
* change `tundra.tn:split` to support XML namespaces
* change `tundra.tn:split` to use the following TN_parms returned by the call to `$service` when routing split documents to TN when `$content.type.output`, `$encoding.output`, and `$schema.output` are not specified: `TN_parms/$contentType`, `TN_parms/$contentEncoding`, `TN_parms/$schema`
* change `tundra.tn:translate` to improve performance by using `tundra.tn.document:normalize` instead of `tundra.tn.document:get`
* change `tundra.tn:translate` to include delivery duration in log statement
* change `tundra.tn:translate` to support XML namespaces
* fix `tundra.tn.content:get` NullPointerException
* fix `tundra.tn.document.content:get` to not throw an exception when no content part with the given name exists
* fix `tundra.tn:derive` to use the same content encoding as the original content part when amending the derivative content part, unless overriden by the derivative rule itself

# 0.0.14 (2014-05-28)

* add a local lazy in-memory cache of partner profiles to improve the performance of TundraTN services
* add `tundra.tn.queue:clear` for clearing all items on a Trading Networks queue

# 0.0.13 (2014-05-14)

* change `tundra.tn.profile:get` to not return anything when profile is not found
* change `tundra.tn.profile:get` to throw a more descriptive error when the External ID type does not exist
* change `tundra.tn.profile:self` to not return anything when profile is not found
* change `tundra.tn.profile:self` to throw a more descriptive error when the External ID type does not exist
* change `tundra.tn:derive` and related services to throw an exception with a more descriptive error message when the sender or receiver profile for the derivative is not found
* change `tundra.tn:retrieve` to default the values of the following TN_parms, when not specified by the caller: DocumentID, GroupID, $contentType, $contentEncoding, $contentName, $user.
* change `tundra.tn:retrieve` to support strict routing of documents, which aborts routing/ processing rule execution of the document if any any errors (such as validation errors) are encountered prior to processing

# 0.0.12 (2014-02-14)

* change `tundra.tn:receive` to drop unused variables from pipeline
* change `tundra.tn:receive` to simplify HTTP response handling to make it easier to both wrap, and use as a template for custom receive services
* change `tundra.tn:discard` to simplify HTTP response handling
* change `tundra.tn:reject` to simplify HTTP response handling
* change all top-level services and their related queue and reliable counterparts to allow customisation of bizdoc completion status

# 0.0.11 (2013-12-19)

* change `tundra.tn.document:relate` to disable logging step for source document, as `WmTN/wm.tn.doc:relateDocuments` already logs the relationship on the source document
* change `tundra.tn:split` to reduce the number of log statements written per output document
* change `tundra.tn:receive` and `tundra.tn.content:route` strict routing to check all error classes, with malformed documents resulting in a routing exception (HTTP 500 response)

# 0.0.10 (2013-12-17)

* change `tundra.tn:deliver` to fix a bug with file delivery, where `$mode` variable was incorrectly being overwritten and dropped when a `$service` was invoked

# 0.0.9 (2013-12-13)

* change the following `tundra.tn:branch`, `:chain`, `:deliver`, `:derive`, `:process`, `:split`, `:translate` and related services to add `$parse?` and `$prefix?` input parameters which allow for document parsing to be enabled/disabled, and to support calling normal WmTN style bizdoc processing services
* change tundra.tn.content:route to simplify implementation by calling `tundra.tn.support.content:route` directly
* change `tundra.tn.document.error:exists` to add Security error class to `$class` `IData` input argument
* change `tundra.tn:receive` to correctly handle when the security check of whether the logged on user is allowed to route documents as the sending partner fails

# 0.0.8 (2013-11-25)

* change tundra.tn.exception:handle to declare `$exception.class` in the input pipeline, since it's now being used in the service logic
* change `tundra.tn:process` and related services to add `$prefix?` flag to allow disabling of '$' prefixing of bizdoc, sender, and receiver input arguments (so that services implementing the `WmTN/wm.tn.rec:ProcessingService` specification are now supported), and `$parse?` flag to allow the disabling of bizdoc content parsing
* change `tundra.tn:branch` and related services to fix TN set inputs bug by making the process action `$service` input optional
* change `tundra.tn:derive` to merge source bizdoc attributes with recognized attributes when amending content (the latter takes precedence)
* change `tundra.tn:derive` and related services to support setting attributes on the derived bizdoc
* change `tundra.tn.schema:derivative` to include condition statement on amendments and attributes
* change `tundra.tn.document:derive` to include condition statement on `$amendments` and `$attributes`
* change `tundra.tn:amend` and `tundra.tn:derive` and related services to add support for conditional statements on amendments and attributes
* change `tundra.tn:translate` and related services to only set the bizdoc user status to 'DONE' if the translation/mapping service did not change the status itself
* change `tundra.tn:split` and related services to only set the bizdoc user status to 'DONE' if the translation/mapping service did not change the status itself
* change `tundra.tn.content:route` to improve performance at the expense of more memory use by eliminating the file IO from the routing logic

# 0.0.7 (2013-11-05)

* change `tundra.tn.queue:*` to add support for multi-threaded task processing
* change `tundra.tn:derive` to fix a class cast exception bug when deriving and amending more than one copy of a bizdoc
* change `tundra.tn:derive` log statements to use a consistent format
* change `tundra.tn:process` to log output `$summary` and `$message` strings from processing service against bizdoc, if specified
* change `tundra.tn.queue:each` to clarify the type of queue (private vs public) in dequeue log statement
* remove TN queue delivery service deregistration on package shutdown to avoid missing delivery service errors as a result of TN cluster node synchronization
* change tundra.tn:receive handling of flat files: now defaults `TN_parms/DocumentID` to an automatically generated UUID, and `TN_parms/ReceiverID` to the required External ID of the My Enterprise profile

# 0.0.6 (2013-10-11)

* change `tundra.tn.queue:deliver` and `tundra.tn.reliable:deliver` to add `$parse?` option to allow bizdoc content to be parsed prior to delivery, which is useful in conjunction with variable substitution on the delivery URI
* change `tundra.tn.queue:derive` and `tundra.tn.reliable:derive` to add `$part` input for specifying which bizdoc content part should be copied to the derivatives

# 0.0.5 (2013-10-11)

* change `tundra.tn:discard`, `:receive`, and `:reject` now support being wrapped by another service, and being executed via the `/soap/rpc` handlers
* change `tundra.tn:receive` to always add transport info content part to resulting bizdoc, even when aborted by a failed strictness check
* change `tundra.tn:deliver` to add `$parse?` option to allow bizdoc content to be parsed prior to delivery, which is useful in conjunction with variable substition on the delivery URI
* change `tundra.tn:derive` and related services to add `$part` input to specify which bizdoc content part to be copied to the derivative; derivatives now only include either the specified part or the default content part, other parts are not copied to derivatives
* change `tundra.tn:derive` to preserve a user status of 'DONE' when bizdoc is reprocessed, even if no derivatives are created on the reprocess
* change `tundra.tn.document:parse` to return the type of schema used to parse the bizdoc content (either Flat File or XML)
* change `tundra.tn:derive` to re-extract attributes after amending content, when applicable

# 0.0.4 (2013-10-01)

* add support for 'strict' mode processing of bizdocs to top-level services and their queue and reliable counterparts; 'strict' mode aborts further processing if any errors (of a given class) are found on the bizdoc being processed
* add `tundra.tn.document.attribute.datetime.transformer:parse` to parse ISO8601/XML dates, times and datetimes correctly when extracting a document attribute in TN
* add `tundra.tn.document.error:exists` which returns true if any errors (of a given class, if specified) are found on the given bizdoc
* add `tundra.tn.reliable:amend`, a reliable service execution version of `tundra.tn:amend`
* add `tundra.tn.schema:processor` specification describing the inputs and outputs required of a processing service called by `tundra.tn:process`
* add `tundra.tn.schema:splitter` specification describing the inputs and outputs required of a splitting service called by `tundra.tn:split`
* add `tundra.tn.schema:translator` specification describing the inputs and outputs required of a service called by `tundra.tn:translate`
* add `tundra.tn:branch` and related queue and reliable services to allow the execution of different actions under different conditions
* add `tundra.tn:discard`, the Trading Networks equivalent of Unix's `/dev/null`
* add `tundra.tn:reject` - to receive arbitrary content and always return an error to the client
* change `tundra.tn.document.schema:get` to improve performance by not refetching the entire bizdoc when the provided bizdoc already contains the information required
* change `tundra.tn.document.type:get` to fetch the bizdoc type directly from the `BizDocTypeStore` object, rather than via a call to `wm.tn.doctype:view`
* change `tundra.tn.document.type:get` to fix null pointer exception when the doctype is not found
* change `tundra.tn.profile:get` and `:self` to java services that now return a superset of both `wm.tn.rec:Profile` and `wm.tn.rec:ProfileSummary`
* change `tundra.tn.queue:each` to fetch the bizdoc content as part of the bizdoc envelope before calling the given queue processing service
* change `tundra.tn.reliable:*` services to not drop inputs, as this causes problems when TN retries to run the task after failure
* change `tundra.tn:deliver` to add a content part for the delivery response body, when applicable
* change `tundra.tn:deliver` to fix a mailto: URI regex match bug
* change `tundra.tn:deliver` to perform variable substitution on $destination URI by parsing the URI, running the substitution, then re-emitting the URI as a string
* change `tundra.tn:split` and related services to support either setting bizdoc to 'ERROR' or 'IGNORED' when the splitting service returns no content, controlled by the optional `$required?` input flag
* change `tundra.tn:translate` and related services to support either setting bizdoc to 'ERROR' or 'IGNORED' when the translation service returns no content, controlled by the optional `$required?` input flag
* delete all unit test cases from TundraTN to prevent tests being inadvertently run in production; also, as the test cases were dependent on an unspecified configuration being set up in Trading Networks, their usefulness were limited

# 0.0.3 (2013-07-20)

* add `tundra.tn.profile.delivery:get` returns a named delivery method for the given profile
* add `tundra.tn.queue:each` which allows the use of any normal bizdoc processing service to process items in a TN delivery queue
* add `tundra.tn:receive` gateway service which can be invoked by HTTP and FTP clients for routing arbitrary (XML or flat file) content to TN
* add `tundra.tn:retrieve` to fetch content from a source URI and route it to TN
* change `tundra.tn.content:route` to add a 'strict' mode where an exception is thrown if the content fails validation while routing to TN
* change `tundra.tn.document.content:add` to correctly includes `$encoding` as charset parameter the bizdoc part content type
* change `tundra.tn.support:startup` to set appropriate ACL permissions on `tundra.tn` and `tundra.tn:receive` nodes
* change `tundra.tn:deliver` to support delivering to mailto: URIs
* change `tundra.tn:deliver` to support profile delivery methods (including preferred protocol)
* rename `tundra.tn.document.attribute.*` services to `tundra.tn.document.attribute.string.transformer.*` to clarify their use as string transformers [BACKWARDS INCOMPATIBLE]
* rename `tundra.tn.schema.*` elements to be consistent with Tundra package [BACKWARDS INCOMPATIBLE]

# 0.0.2 (2013-07-03)

* add `tundra.tn:amend` for making precise inline edits to bizdoc content; the edited content is added as a new content part to the original bizdoc
* add `tundra.tn:chain` for executing a list of processing services sequentially against a bizdoc
* add `tundra.tn.document.attribute.profile:self` to force a TN document type sender and/or receiver to always be the My Enterprise profile
* add `tundra.tn:queue` versions of the top-level `tundra.tn:*` services to be called from TN delivery queues
* add `tundra.tn:reliable` service execution versions of the top-level `tundra.tn:*` services
* add `tundra.tn:split`, which is like `tundra.tn:translate`, but for one-to-many bizdoc content mappings
* change `tundra.tn.content:route` to support forcing sender, receiver, document ID, group ID, conversation ID, and document type to specific values specified in `TN_parms` (even for XML documents)
* change `tundra.tn:derive` to add support for inline filtering (via `tundra.condition:evaluate`) and filter services, which, if specified, the filter condition must evaluate to true for the bizdoc copy in question to be made
* change `tundra.tn:derive` to be reentrant so that reprocessing of a bizdoc already related to derivatives will no longer create the same derivatives (they should be reprocessed directly if required)
* change `tundra.tn:derive` to support amendments: each derived copy can now include inline edits to the content (for example, this can be used to update the receiver element in XML)

# 0.0.1 (2013-02-25)

* add `tundra.tn:deliver` for delivering bizdoc content to a destination URI from a TN processing rule
* add `tundra.tn:derive` for making copies of a bizdoc with an updated sender and/or receiver on each copy
* add `tundra.tn:log` for logging messages to the TN activity log
* add `tundra.tn:process` which takes care of all the usual boilerplate associated with bizdoc processing services (updating user status, parsing content, handling errors) and defers the actual processing to a named service
* add `tundra.tn:translate` for one-to-one mappings of bizdoc content from one format to another; takes care of the usual boilerplate associated with mapping services (updating user status, parsing content, handling errors, and routing mapped content back to TN) and defers the actual mapping to a named service
* add `tundra.tn.content:route` for routing arbitrary content (string, bytes, stream, `IData` document) to TN in a consistent and uniform manner -- one service regardless of content type, rather than the WmTN approach of a different service and API for each content type
* add `tundra.tn.document` services for working with bizdocs
* add `tundra.tn.exception:handle` as a standard error handler for bizdoc processing services
* add `tundra.tn.profile` services for working with TN profiles
