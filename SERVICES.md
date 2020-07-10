### tundra.tn:amend

Edits the given XML or flat file bizdoc content part with the list of {key,
value} pairs specified in `$amendments`.

The keys in `$amendments` can be fully-qualified, for example `a/b/c[0]`, and
the values can include percent-delimited variable substitution strings which
will be substituted prior to being inserted in `$document`.

The bizdoc user status is first updated to 'AMENDING', then the content part
identified by `$part.input` (or the default content part if not specified) is
parsed to an `IData` document using the named `$schema` (or the schema
configured on the TN document type if not specified), the amendments are
applied via the `$amendments` {key, value} pairs, the amended `IData` document
is then emitted as stream then added to the bizdoc as a new content part
identified by `$part.output` (or 'amendment' if not specified), and the bizdoc
user status is updated to 'DONE'.

This service is designed to be used in conjunction with other TN processing
rule actions, such as the 'Deliver document by' action, which can use the
amended content part for delivery rather than the original content part.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

---

### tundra.tn:branch

Evaluates each given branch condition in the specified order against the
pipeline and executes the action for the first matching branch against the
bizdoc being processed.

Conditions can be any statement supported by
`Tundra/tundra.condition:evaluate`. The condition will be evaluated against a
pipeline containing `$bizdoc`, `$sender`, `$receiver`, the parsed bizdoc content
as an `IData` document called `$document`, and the parsing schema/document
blueprint used by the parser as a string called `$schema`.

A null condition will always evaluate to `true`, and can therefore be used as
a default branch statement to match any documents unmatched by more specific
conditions.

Supported actions include all the relevant TundraTN top-level processing
services:
* `TundraTN/tundra.tn:chain`
* `TundraTN/tundra.tn:deliver`
* `TundraTN/tundra.tn:derive`
* `TundraTN/tundra.tn:enqueue`
* `TundraTN/tundra.tn:process`
* `TundraTN/tundra.tn:split`
* `TundraTN/tundra.tn:translate`

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

---

### tundra.tn:chain

Processes a Trading Networks document sequentially via the given list of
processing services.

As this service provides logging, content parsing, error handling, and
bizdoc status updates, the processing services do not need to include any of
this common boilerplate code.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document to be processed.
* `$services` is a list of fully-qualified service names to be called
  sequentially to process the bizdoc. Refer to the
  `TundraTN/tundra.tn.schema:processor` specification as a guide to the
  inputs and outputs required of the processing service.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  process the bizdoc. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message`, and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$pipeline` is an optional `IData` document containing additional arbitrary
  input arguments for `$service` (or `WmPublic/pub.flatFile:convertToValues`,
  `WmPublic/pub.xml:xmlStringToXMLNode`, or `WmPublic/pub.xml:xmlNodeToDocument`
  via `Tundra/tundra.tn.document:parse`). Fully-qualified names will be
  handled correctly, for example an argument named `example/item[0]` will
  be converted to an `IData` document named `example` containing a String
  list named `item` with it's first value set accordingly.
* `$service.input` is an optional name used when adding the bizdoc
  content to the input pipeline of the call to `$service`. Defaults to
  `$document` when `$parse?` is `true`, and `$content` when `$parse` is `false`.
* `$status.done` is an optional user status to use for the bizdoc when
  processing has completed successfully. Defaults to DONE.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$parse?` is an optional boolean flag which when `true` will parse the
  bizdoc content part to an `IData` document which is added to the input
  pipeline of the call to `$service`, and when `false` will instead add the
  bizdoc content part as an input stream to the input pipeline. Defaults
  to `true`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When `true` `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when `false` `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to `true`.
* `$part` is an optional name identifying the bizdoc content part to be
  parsed and added to the input pipeline of the call to `$service`. Defaults
  to the default content part (xmldata for XML documents, ffdata for Flat
  File documents). Not used if `$parse?` is `false`.
* `$encoding` optional character encoding to be used when reading the bizdoc
  content part bytes. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:connect

Designed to be invoked via an [HTTP] request to test whether a client
can connect and authenticate to Integration Server for publishing
messages via the tundra.tn:receive service.

Returns an [HTTP] response body in [HTML] format describing a successful
connection to the server.

---

### tundra.tn:deliver

Delivers Trading Networks document (bizdoc) content to the given
destination [URI].

Variable substitution is performed on all variables specified in the
`$pipeline` document, and the `$destination` [URI], allowing for
dynamic generation of any of these values. Also, if `$service` is
specified, it will be called prior to variable substitution and thus
can be used to populate the pipeline with variables to be used by the
substitution.

When using variable substitution in a [URI], because it uses `%`
characters to delineate substitution statements but it is special
character in URIs (used for the escape sequence of [URL-encoded]
characters), it needs to be [URL-encoded] as `%25`. For example, to
include the variable substitution statement:

    %$bizdoc/DocType/TypeName%

in a `mailto` [URI], the `%` characters need to be encoded as the
`%25` escape sequence as follows:

    mailto:john.doe@example.com?subject=%25$bizdoc/DocType/TypeName%25

Uses `Tundra/tundra.content:deliver` for its implementation. Please
refer to that service's documentation for further details.

This service is designed to be called directly from a Trading
Networks bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document whose content is to be
  delivered.
* `$destination` is either a [URI], or a named destination (such as
  `Receiver's Preferred Protocol`), to which the bizdoc content will
  be delivered. If not specified, no delivery will be attempted.
  The supported delivery protocols ([URI] schemes) are as per
  `Tundra/tundra.content:deliver`. Please refer to this service's
  documentation for further details.
* `$service` is an optional fully-qualified service name which, when
  specified, will be invoked prior to delivery, thus allowing a
  service to perform processing to influence the delivery (such as
  populating the pipeline with configuration variables at runtime).
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while
  attempting delivery. The input pipeline will include the variables
  described in the specification
  `Tundra/tundra.schema.exception:handler`, as per a
  normal catch service invoked by `Tundra/tundra.service:ensure`.
  Defaults to `TundraTN/tundra.tn.exception:handle`, the standard
  TundraTN exception handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after delivery, and whether or not an
  exception is encountered during delivery.
* `$pipeline` is an optional `IData` document containing arbitrary
  variables which can be used to influence the delivery. Variables
  provided will take precedence and override their corresponding
  values in the `$destination` [URI] where applicable. See the
  `$destination` description above for transport-specific options
  which can be provided via this `IData` document.
* `$status.done` is an optional user status to use for the bizdoc
  when delivery has completed successfully. Defaults to `DONE`.
* `$status.ignored` is an optional user status to use for the bizdoc
  when no delivery destination is provided. Defaults to `IGNORED`.
* `$status.silence?` is an optional boolean which when `true` will
  cause this service not to change the status on the document.
  Defaults to `false`.
* `$substitute?` is an optional boolean flag which when `true` will
  perform variable substitution on all variables in the pipeline
  (after invoking `$service`, if applicable), which allows variables
  to be set dynamically using other values in the pipeline (or
  values returned by `$service`, if applicable). Defaults to `true`.
* `$part` is an optional name of the bizdoc content part to be
  delivered. Defaults to the default content part when not specified
  (`xmldata` for [XML] document types; `ffdata` for Flat File
  document types).
* `$parse?` is an optional boolean flag which when `true` parses the
  bizdoc content part identified by `$part` using the parsing schema
  configured on the Trading Networks document type, prior to both
  invoking `$service`, if specified, and content delivery. The parsed
  document content can then be used in conjunction with variable
  substitution for influencing the delivery [URI] based on the
  content of the document. Defaults to `false`.
* `$prefix?` is an optional boolean flag indicating whether to use
  the `$` prefix on the standard input arguments (`bizdoc`, `sender`,
  and `receiver`) when calling `$service`. When `true` `$service`
  should implement the `TundraTN/tundra.tn.schema:processor`
  specification; when `false` `$service` should implement the
  `WmTN/wm.tn.rec:ProcessingService` specification. Defaults to
  `true`.
* `$encoding` is an optional character set to use when encoding text
  content for delivery. Defaults to [UTF-8].
* `$strict` is an optional set of boolean flags that control strict
  mode processing of bizdocs: if any error classes are set to `true`
  and the bizdoc contains errors for those classes, the bizdoc will
  not be processed; instead an exception will be thrown and handled
  by the `$catch` service.

  For example, if you have enabled duplicate document checking on the
  Trading Networks document type and do not wish to deliver
  duplicates, set the `$strict/Saving` error class to `true` and
  duplicate documents will not be delivered, and will instead have
  their user status set to `ABORTED` (when using the standard
  `$catch` service).

  The following flags are supported, and all default to `true` if not
  specified:
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:derive

Derives new bizdocs from an existing bizdoc, updating the sender and/or
receiver on each derivative.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

Upon successful processing by this service, the bizdoc user status will be
either set to `$status.done` if one or more derivatives were created, or
`$status.ignored` if no derivatives are created, unless the `$service`
processing service has already changed the user status, in which case this
service will not change it again.

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document from which bizdoc copies will be
  derived.
* `$service` is an optional fully qualified service name which, when
  specified, will be invoked prior to deriving any bizdoc copies, thus
  allowing a service to perform processing to influence the derivative
  process (such as specifying additional derivative rules at runtime). The
  service is invoked with an input pipeline containing the following
  variables: `$bizdoc`, `$sender`, `$receiver`, `$document` (the parsed bizdoc
  default content part), `$schema`, `$schema.type`, and `$derivatives`. If any
  derivative rules are added, changed, or removed, the service must return
  the `$derivatives` rule list in its output pipeline.
* `$catch` is an optional fully qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  derive bizdoc copies. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message` and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully qualified service name which, when
  specified, will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$pipeline` is an optional `IData` document containing arbitrary variables
  which can be used to influence the derivative process.
* `$derivatives` is a list of rules describing when and what copies are to
  be made of this bizdoc:
  * `description` is an optional description of the derivative rule, used in
    all related activity log statements.
  * `sender` is an optional (internal or external) ID identifying the
    desired Trading Networks partner profile to be used as the sender on
    the derivative bizdoc. Defaults to the original bizdoc sender if not
    specified.
  * `receiver` is an optional (internal or external) ID identifying the
    desired Trading Networks partner profile to be used as the receiver on
    the derivative bizdoc. Defaults to the original bizdoc receiver if not
    specified.
  * `type` is an optional Trading Networks external ID type used to
    interpret the above `sender` and `receiver` values. If not specified, then
    the sender and receiver IDs are treated as internal partner profile
    IDs.
  * `filter` is an optional inline filter condition (as supported by
    `Tundra/tundra.condition:evaluate`) or fully qualified service name of a
    service implementing the `TundraTN/tundra.tn.schema.derivative:filter`
    specification. If specifying an inline filter condition, the input
    pipeline will be the same as described for a filter service by the
    `TundraTN/tundra.tn.schema.derivative:filter` specification. If not
    specified, then a copy will always be derived for this new sender/
    receiver.
  * `amendments` is an optional list of {key, value} pairs used to make
    small inline modifications to the derived bizdoc content:
    * `key` is a fully-qualified reference to a field in the parsed bizdoc
      content.
    * `value` is the new value to be assigned to the element associated with
      the given key.
    * `action` is an optional choice of `merge`, `create`, `update`, or `delete`.
      Defaults to `merge` if not specified.
      * `merge` will create the key and associate it with the given
        value if the key does not already exist, or update the the
        associated value if the key already exists.
      * `create` will only create the key and associate it with the given
        value if the key does not already exist.
      * `delete` will remove the key and previously associated value
        from the document. No value is required to be specified when
        using this action.
    * `condition` is an optional `Tundra/tundra.condition:evaluate`
      conditional statement can also be specified, which is evaluated
      against the pipeline containing `$bizdoc`, `$sender`, `$receiver`, and
      `$document` (the parsed bizdoc content), and only if the condition
      evaluates to `true` will the associated amended value be applied. If
      not specified, the amended value will always be applied.
  * `attributes` is an optional list of {key, value} pairs used to set
    attributes on the derived bizdoc:
    * `key` identifies the attribute that is set to the given `value`.
    * `value` is the new value to be assigned to the attribute associated
      with the given `key`.
    * `condition` is an optional `Tundra/tundra.condition:evaluate`
      conditional statement can also be specified, which is evaluated
      against the pipeline containing `$bizdoc`, `$sender`, `$receiver`, and
      `$document` (the parsed bizdoc content), and only if the condition
      evaluates to `true` will the attribute be added to the derived bizdoc.
      If not specified, the attribute will always be added to the derived
      bizdoc.
  * `TN_parms` is an optional `IData` document containing routing hints used
    when routing the derivative bizdoc.
  * `force?` is an optional boolean flag: when `true` a new derivative will
    always be created even if an existing derivative for the same sender
    and receiver already exists; when `false` a new derivative will only
    be created if there is no existing derivative with the same sender
    and receiver. Defaults to `false`, if not specified.
  * `enabled?` is an optional boolean flag, when `true` this derivative rule
    is active, when `false`, this derivative rule is inactive and ignored.
    Defaults to `true` when not specified.
* `$status.done` is an optional user status to use for the bizdoc when
  derivatives have been created successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  derivatives are created due to filtering. Defaults to IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (bizdoc, sender, and receiver)
  when calling `$service`. When `true` `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when `false` `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to `true`.
* `$part` is the optional name of the bizdoc content part to be copied to
  the resulting derivatives. Defaults to the default content part when not
  specified (xmldata for XML document types, ffdata for Flat File document
  types).
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:discard

Receives arbitrary (XML or flat file) content and then discards it
(does nothing with it). This is the Trading Networks equivalent of
Unix's [/dev/null], which is useful for successfully receiving
messages from a partner that do not need to be saved or processed.

This service is intended to be invoked by clients via HTTP or FTP.

---

### tundra.tn:enqueue

Enqueues a Trading Networks document to the given queue for deferred
processing by that queue.

Note that when this service is invoked for a Trading Networks document that
already has an existing task with a status of `FAILED` for the queue in question,
that task will be restarted rather than a new task being added.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* bizdoc is the Trading Networks document to be enqueued.
* `$queue` is an optional name of the Trading Networks delivery queue
  to which this document will be enqueued. This input argument is
  included for backwards-compatibility only, and is deprecated. Use
  the `$queues` input argument instead.
* `$queues` is a list of enqueuing rules which will be applied to this
  document.
  * `name` is is either the name of the Trading Networks public queue
    the document should be enqueued to, or the value "Receiver's Queue"
    in which case the document will be enqueued to the delivery queue
    associated with the receiver partner profile.
  * `condition` is an optional `Tundra/tundra.condition:evaluate` conditional
    statement, which is evaluated against a pipeline containing `$bizdoc`,
    `$sender`, `$receiver`, and `$document` (the parsed bizdoc content), and
    only if the condition evaluates to `true` will the document be enqueued
    to this queue. If not specified, the document will always be enqueued
    to this queue.
  * `force?` is an optional boolean flag: when `true` the document will always
    be enqueued even if an existing task for the same queue already exists;
    when `false` the document will only be enqueued if there is no existing
    task with a status of `DELIVERING` or `DONE` for the same queue. Note that
    this service prefers to restart existing `FAILED` tasks for the given
    queue, rather than enqueuing new tasks, and therefore this setting
    has no effect when a `FAILED` task for the queue already exists.
    Defaults to `false`, if not specified.
  * `enabled?` is an optional boolean flag indicating whether this
    enqueuing rule will be applied to the document. When `false`, this
    enqueuing rule is inactive and ignored. Defaults to `true` when not
    specified.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  process the bizdoc. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message`, and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$status.done` is an optional user status to use for the bizdoc when
  enqueue has completed successfully. Defaults to `QUEUED`.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  enqueuing occurs. Defaults to `IGNORED`.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:log

Logs a message in the Trading Networks activity log, appended with
information about the current execution context for better
diagnostics.

#### Inputs:

* `$bizdoc` is an optional Trading Networks document against which
  to log the message.
* `$type` is an optional choice of `MESSAGE`, `WARNING`, or `ERROR`, which
  describes the type of message being logged. Defaults to `MESSAGE`.
* `$class` is an optional message class or category.
* `$summary` is an optional short title or summary of the message
  being logged. If not specified, defaults to be the same as
  `$message`.
* `$message` is an optional message to be logged. If not specified,
  defaults to an empty string.
* `$context` is an optional `IData` document containing arbitrary
  information about the current execution context. The host name,
  session, thread, and call stack are added, and it is then
  serialized and appended to `$message` prior to logging.

---

### tundra.tn:process

Processes a Trading Networks document via the given `$service` processing
service.

As this service provides logging, content parsing, error handling, and
bizdoc status updates, the `$service` processing service does not need to
include any of this common boilerplate code.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document to be processed.
* `$service` is the fully-qualified service name to be called to process the
  bizdoc. Refer to the `TundraTN/tundra.tn.schema:processor` specification
  as a guide to the inputs and outputs required of the processing service.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  process the bizdoc. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message`, and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$pipeline` is an optional `IData` document containing additional arbitrary
  input arguments for `$service` (or `WmPublic/pub.flatFile:convertToValues`,
  `WmPublic/pub.xml:xmlStringToXMLNode`, or `WmPublic/pub.xml:xmlNodeToDocument` via
  `Tundra/tundra.tn.document:parse`). Fully-qualified names will be handled
  correctly, for example an argument named `example/item[0]` will be
  converted to an `IData` document named `example` containing a String[] list
  named `item` with it's first value set accordingly
* `$service.input` is an optional name used when adding the bizdoc
  content to the input pipeline of the call to `$service`. Defaults to
  `$document` when `$parse?` is `true`, and `$content` when `$parse` is `false`.
* `$status.done` is an optional user status to use for the bizdoc when
  processing has completed successfully. If `$service` returns an output
  pipeline argument named `$status.done` or it changes the status itself,
  this takes precedence over any value specified here. Defaults to `DONE`.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$parse?` is an optional boolean flag which when `true` will parse the
  bizdoc content part to an `IData` document which is added to the input
  pipeline of the call to `$service`, and when `false` will instead add the
  bizdoc content part as an input stream to the input pipeline. Defaults
  to `true`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When `true` `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when `false` `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to `true`.
* `$part` is an optional name identifying the bizdoc content part to be
  parsed and added to the input pipeline of the call to `$service`. Defaults
  to the default content part (xmldata for XML documents, ffdata for Flat
  File documents). Not used if `$parse?` is `false`.
* `$encoding` optional character encoding to be used when reading the bizdoc
  content part bytes. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition `
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:publish

Publishes a Trading Networks document to the webMethods Integration
Server messaging subsystem via `WmPublic/pub.publish:publish`.

Supports 'strict' mode processing of bizdocs: if any `$strict` error
classes are set to 'true' and the bizdoc contains errors for any of
these classes, the bizdoc will not be processed; instead an
exception will be thrown and handled by the `$catch` service. For
example, if you have enabled duplicate document checking on the
Trading Networks document type and do not wish to process
duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have
their user status set to 'ABORTED' (when using the standard `$catch`
service).

This service is designed to be called directly from a Trading
Networks bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document to be published.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while
  attempting to publish the bizdoc. The input pipeline will include
  the following variables, as per a normal catch service invoked by
  Tundra/tundra.service:ensure: `$exception`, `$exception?`,
  `$exception.class`, `$exception.message`, and `$exception.stack`. If not
  specified, defaults to `TundraTN/tundra.tn.exception:handle`, the
  standard TundraTN exception handler.
* `$finally` is an optional fully-qualified service name which when
  specified will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$schema.publish` is an publishable document reference used to
  publish the document. This is mandatory for flat file document
  types, and optional for XML document types. If not specified and
  the document type is XML, it defaults to the document reference
  specified on the document type. If not specified for a flat file
  document type, an exception will be thrown.
* `$local.publish?` is an optional boolean which when `true` will
  perform a local publish only. Defaults to `false`.
* `$status.done` is an optional user status to use for the bizdoc when
  the publish has completed successfully. Defaults to `DONE`.
* `$status.silence?` is an optional boolean which when `true` will cause
  this service not to change the status on the document. Defaults to
  `false`.
* `$part` is the optional name of the bizdoc content part to be
  published. Defaults to the default content part when not specified
  (`xmldata` for XML document types, `ffdata` for Flat File document
  types).
* `$strict` is an optional set of boolean flags which when `true` abort
  the processing of the bizdoc when it contains any errors with the
  associated class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`


---

### tundra.tn:receive

Receives arbitrary (XML or flat file) content and routes it to
Trading Networks.

The content can be specified as a string, byte array, JMSMessage,
`java.io.InputStream`, or `org.w3c.dom.Node` object.

This service is either intended to be invoked directly by clients via
HTTP or FTP, or it can be wrapped by another service which specifies
appropriate TN_parms to control the routing of the content (ie. a
one-line flat file gateway service).

When invoked via HTTP, the service returns a 'text/plain' response
body containing either the resulting Trading Networks bizdoc internal
ID on success, or a message describing the errors that occurred on
failure, and an appropriate HTTP response code according to the
following table:

Response                   | Reason
---------------------------|-------------------------------------------------------
202 Accepted               | Received content was routed successfully
400 Bad Request            | Received content was malformed
403 Forbidden              | Sender was denied access to route the received content
406 Not Acceptable         | Received content was not recognized (Unknown)
409 Conflict               | Received content was detected as a duplicate
422 Unprocessable Entity   | Received content failed validation
500 Internal Server Error  | All other errors that occur while processing

When invoked via transports other than HTTP, for example FTP, if the
content is received successfully the service invocation will succeed
and a response body containing the resulting Trading Networks bizdoc
internal ID is returned. If a security or any other exception is
encountered, the service invocation will fail by rethrowing the
exception.

When invoked by a wrapping service, any exceptions encountered will
be thrown to the calling service. It is then the calling service's
responsibility to set an appropriate response for the transport in
question. This is due to a limitation in Integration Server that
requires the invoked (top-level) service to set the HTTP response
to be returned (a child service cannot set the response on behalf of
its parent).

#### Inputs:

* `strict` is an optional boolean flag indicating whether `strict`
  mode routing should be used for the received content. Defaults to
  `true`. To disable `strict` mode when using HTTP, include
  `strict=false` in the query string of the receive URL:

        http://localhost:5555/invoke/tundra.tn/receive?strict=false

* `TN_parms` is an optional set of routing hints for Trading Networks
  to use when routing the received content. If not specified by the
  caller, the following TN_parms are set automatically as follows:
  * `$contentType` is an optional mime media type of the received
    content. Defaults to the following values in order of precedence:
    * The content type specified in the transport document returned
      by `WmPublic/pub.flow:getTransportInfo`.
    * The value `text/xml` if a node object exists in the pipeline.
    * The value `application/octet-stream`.
  * `$contentEncoding` is an optional character encoding used by the
    received content, for example UTF-16. Defaults to the following
    values in order of precedence:
    * The `charset` property in the specified content type.
    * The value [UTF-8].
  * `$contentName` is an optional logical label or name for the
    received content, typically the filename for flat files. Defaults
    to the following values in order of precedence:
    * For HTTP transports:
      * The filename specified in the `Content-Disposition` header.
      * The filename part of the request URI.
    * For non-HTTP transports:
      * The filename specified in the transport document returned
        by `WmPublic/pub.flow:getTransportInfo`.
  * `$contentSchema` is an optional Integration Server document
    reference or flat file schema the received content conforms to.
    Defaults to the value of the `$content.schema` variable, if
    specified in the pipeline.
  * `$user` is the user that sent the received content. Defaults to
    the currently logged on user.
  * `SenderID` is an optional Trading Networks profile external ID
    which identifies the sender of the content. Defaults to the
    following values in order of precedence:
    * The URL query string parameter `sender`.
    * The HTTP header `Message-Sender`.
    * The HTTP header `X-Sender`.
    * The currently logged on user name.
  * `ReceiverID` is an optional Trading Networks profile external ID
    which identifies the receiver of the content. Defaults to the
    following values in order of precedence:
    * The URL query string parameter `receiver`.
    * The HTTP header `Message-Receiver`.
    * The HTTP header `X-Receiver`.
    * The HTTP header `X-Recipient`.
    * The required External ID value of the My Enterprise profile.
  * `DocumentID` is an optional ID used to identify the content in
    Trading Networks. If not specified, and no `DocumentID` is
    extracted when the message is recognized, defaults to the
    following values in order of precedence:
    * The URL query string parameter `id`.
    * The HTTP header `Message-ID`.
    * A newly generated [UUID].
  * `DoctypeName` is an optional name of the Trading Networks
    Document Type which is the type of the received content.
    Specifying this value bypasses the normal Trading Networks
    document recognition logic. Defaults to the following values in
    order of precedence:
    * The URL query string parameter `type`.
    * The HTTP header `Message-Type`.
  * `GroupID` is an optional ID used to identify the group this
    content belongs to in Trading Networks. For flat files only,
    defaults to the value of `TN_parms/DocumentID`.

#### Outputs:

* `id` is the internal ID assigned by Trading Networks to the
  resulting bizdoc.

---

### tundra.tn:reject

Receives arbitrary (XML or flat file) content and then rejects it
by always returning an error to the client.

This service is intended to be invoked by clients via HTTP or FTP.

---

### tundra.tn:reroute

Reprocesses the given document in Trading Networks by rematching it
against the processing rule base and executing the first processing
rule that matches.

This service is designed to be called directly from a Trading
Networks bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document to be reprocessed.

#### Outputs:

* `bizdoc` is the Trading Networks document that was reprocessed.
* `sender` is the Trading Networks profile of the sender of the
  document.
* `receiver` is the Trading Networks profile of the receiver of the
  document.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

---

### tundra.tn:retrieve

Retrieves arbitrary content from the given `$source` URI, and routes
it to Trading Networks.

Uses `Tundra/tundra.content:retrieve` for its implementation. Please
refer to that service's documentation for further details.

#### Inputs:

* `$source` is a URI identifying the location from which content is
  to be retrieved. The retrieval protocols / URI schemes are as per
  `Tundra/tundra.content:retrieve`. Please refer to this service's
  documentation for further details.
* `$limit` is an optional maximum number of content matches to be
  processed in a single execution. Defaults to 1000.
* `$strict?` is an optional boolean, which if `true` will abort
  routing/processing rule execution of the document if any any errors
  (such as validation errors) are encountered prior to processing.
  Defaults to `false`.
* `$content.identity` is an optional choice of mode for assigning a
  value to the `DocumentID` of the resulting bizdoc if no
  `DocumentID` is extracted:
  * `UUID`: assigns a newly generated [UUID] in canonical.
  * `ULID`: assigns a newly generated [ULID].
  * `SHA-512`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-384`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-256`: the algorithm used to calculate a message digest from
    the content.
  * `SHA`: the algorithm used to calculate a message digest from the
    content.
  * `MD5`: the algorithm used to calculate a message digest from the
    content.
  * `MD2`: the algorithm used to calculate a message digest from the
    content.
* `TN_parms` is an optional set of routing hints for Trading Networks
  to use when routing the retrieved content. The following TN_parms
  are set automatically, if no other value is provided:
  * `DocumentID` is set using the provided `$content.identity`
    approach.
  * `GroupID` is set to a generated [UUID] and groups all content
    retrieved per invocation of this service.
  * `$contentType` is the MIME media type of the content.
  * `$contentEncoding` is the character set of the content.
  * `Content Name` is the name associated with the retrieved content,
    for example the file name for content retrieved from a file URI.
  * `Content Source` is the source location from which the content
    was retrieved.
  * `Content Archive` is the archive location to which the content
    was archived.
  * `User` is the user under which this service was invoked.

---

### tundra.tn:split

One-to-many conversion of an XML or flat file Trading Networks document
(bizdoc) to another format. Calls the given splitting service, passing the
parsed content as an input, and routing the split content back to Trading
Networks as new documents automatically.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document whose content is to be
  split.
* `$service` is the fully-qualified name of the service which will be
  invoked to split the parsed bizdoc content. The splitting service
  must accept a single `IData` document and return an `IData[]` document list,
  and optionally TN_parms. Refer to the `TundraTN/tundra.tn.schema:splitter`
  specification as a guide to the inputs and outputs required of the
  translation service.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  split the bizdoc content. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message` and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after translation, and whether or not an
  exception is encountered during translation.
* `$pipeline` is an optional `IData` document containing arbitrary variables
  which can be used to influence the splitting process.
* `$content.type.input` is the MIME media type that describes the format of
  the bizdoc content being split. For [JSON] content, a recognized [JSON]
  MIME media type, such as "application/json", must be specified. Defaults
  to the content type specified on the bizdoc content part.
* `$content.type.output` is the MIME media type that describes the format of
  all the resulting split contents. For [JSON] content, a recognized
  [JSON] MIME media type, such as "application/json", must be specified.
* `$namespace.input` is a list of namespace prefixes and the URIs they
  map to, used when parsing the bizdoc content as [XML] with elements
  in one or more namespaces. Defaults to using the namespace prefixes
  declared on the associated Trading Networks document type.
* `$namespace.output` is a list of namespace prefixes and the URIs they
  map to, used when serializing the split documents returned by
  `$service` to the same [XML] format with elements in one or more
  namespaces. If the list of split documents contain dissimilar formats,
  then `$service` should return a `IData` document list called `$namespaces`
  of the same length as the split document list, where `$namespaces[n]`
  will be used to serialize `$documents[n]`.
* `$schema.input` is the optional name of the Integration Server document
  reference or flat file schema to use to parse the bizdoc content into an
  `IData` structure. Defaults to the parsing schema specified on the
  associated Trading Networks document type.
* `$schema.output` is the optional name of the Integration Server document
  reference or flat file schema to use to serialize the split documents
  returned by `$service`, if all the documents returned are of the same
  format. If the list of split documents contain dissimilar formats, then
  `$service` should return a list of Integration Server document reference
  or flat file schema names called `$schemas` of the same length as the
  split document list, and `$schemas[n]` will be used to serialize
  `$documents[n]`.
* `$service.input` is the optional name of the input parameter used for the
  parsed bizdoc content in the input pipeline of the invocation of
  `$service`. Defaults to `$document`.
* `$service.output` is the optional name of the output parameter used by
  `$service` to return the translated documents in its output pipeline.
  Defaults to `$documents`.
* `$encoding.input` is an optional character set to use when decoding the
  content part data. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$encoding.output` is an optional character set to use when serializing
  the split documents. If not specified, defaults to [UTF-8].
* `$validate.input?` is an optional boolean flag which when `true` will
  validate the input content against the given `$schema.input`, or the
  parsing schema specified on the associated Trading Networks document
  type if `$schema.input` is not specified, and throw an exception to abort
  processing if the content is invalid. Defaults to `false`.
* `$validate.output?` is an optional boolean flag which when `true` will
  validate each output content against the appropriate schema (`$schema.output`
  if specified, otherwise `$schemas[n]` returned by `$service`), and throw
  an exception to abort processing if the content is invalid. Defaults to
  `false`.
* `$status.done` is an optional user status to use for the bizdoc when
  it has been split successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  split documents are returned by `$service` and `$required` is `false`.
  Defaults to IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$required?` is an optional boolean indicating whether `$service` is
  required to return a one or more split documents. If `true`, and no
  documents are returned by `$service`, an exception will be thrown and
  handled by the `$catch` service. Defaults to `false`.
* `$relate?` is an optional boolean indicating whether the original document
  should be related to each of the individual split documents. Defaults to
  `true`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When `true` `$service` should implement the
  `TundraTN/tundra.tn.schema:splitter` specification, when `false` `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to `true`.
* `$part` is the optional name of the bizdoc content part to be split.
  Defaults to the default content part when not specified (xmldata for XML
  document types, ffdata for Flat File document types).
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:status

Updates the user status on a Trading Networks document.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

# Inputs:

* `bizdoc` is the Trading Networks document whose status will be updated.
* `$status.user` is user status used to update the bizdoc.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  process the bizdoc. The input pipeline will include the following
  variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message`, and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after processing, and whether or not an
  exception is encountered during processing.
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:translate

One-to-one conversion of [CSV], [JSON], pipe separated values, [TSV], [XML],
[YAML], or Flat File content in a Trading Networks document (bizdoc) to
another format. Calls the given translation service, passing the parsed
content as an input, and routing the translated content back to Trading
Networks as a new document automatically.

Parsing (deserialization) of the bizdoc content is determined in order of
precedence by the input variable `$schema.input`, the parsing schema
configured on the bizdoc's associated document type (`recordBlueprint` or
`ParsingSchema` for XML and flat files respectively), and the input
variable `$content.type.input`. If `$schema.input` is specified, or if the
document type specifies a parsing schema, then that reference will be used
to parse the content, and the type of reference (document reference versus
flat file schema reference) determines the parser implementation used (see
below). However, if `$schema.input` is not specified, nor is a parsing
schema defined on the associated document type, then `$content.type.input`
will be used to determine the parser implementation to use for the
appropriate MIME media type. If `$schema.input` is not specified, and no
parsing schema is specified on the associated document type, and
`$content.type.input` is not specified, then the content will be parsed as
[XML] by default.

Emitting (serialization) of the translated content is determined in order
of precedence by the input variable `$schema.output`, the `TN_parms/$schema`
returned by `$service`, the input variable `$content.type.output`, and the
`TN_parms/$contentType` returned by `$service`. If `$schema.output` is
specified, or if `$service` returned `TN_parms/$schema`, then that
reference will be used to parse the content, and the type of reference
(document reference versus flat file schema reference) determines the
parser implementation used (see below). However, if `$schema.output` is
not specified, nor `TN_parms/$schema` returned by `$service`, but
`$content.type.output` is specified or `TN_parms/$contentType` is returned
by `$service` then it will be used to determine the parser implementation
to use for the appropriate MIME media type. If neither `$schema.output` is
specified, nor `TN_parms/$schema` is returned by `$service`, nor
`$content.type.output` is specified, nor `TN_parms/$contentType` is
returned by `$service`, then the content will be parsed as [XML] by
default.

Supports 'strict' mode processing of bizdocs: if any `$strict` error classes
are set to 'true' and the bizdoc contains errors for any of these classes,
the bizdoc will not be processed; instead an exception will be thrown and
handled by the `$catch` service. For example, if you have enabled duplicate
document checking on the Trading Networks document type and do not wish to
process duplicates, set the `$strict/Saving` error class to 'true' and
duplicate documents will not be processed and will instead have their user
status set to 'ABORTED' (when using the standard `$catch` service).

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document whose content is to be
  translated.
* `$service` is the fully-qualified name of the service which will be
  invoked to translate the parsed bizdoc content. The translation service
  must accept a single `IData` document and return a single `IData` document,
  and optionally TN_parms. Refer to the
  `TundraTN/tundra.tn.schema:translator` specification as a guide to the
  inputs and outputs required of the translation service.
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting to
  translate the bizdoc content. The input pipeline will include the
  following variables, as per a normal catch service invoked by
  `Tundra/tundra.service:ensure`: `$exception`, `$exception?`, `$exception.class`,
  `$exception.message` and `$exception.stack`. If not specified, defaults to
  `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after translation, and whether or not an
  exception is encountered during translation.
* `$pipeline` is an optional `IData` document containing arbitrary variables
  which can be used to influence the translation process.
* `$content.type.input` is the MIME media type that describes the format of
  the bizdoc content being translated. Defaults the the content type of the
  relevant bizdoc content part, if not specified.
* `$content.type.output` is the MIME media type that describes the format of
  the resulting translated content. Defaults to the value in
  `TN_parms/$contentType` returned by `$service`, if not specified.
* `$namespace.input` is a list of namespace prefixes and the URIs they
  map to, used when parsing the bizdoc content as [XML] with elements
  in one or more namespaces. Defaults to using the namespace prefixes
  declared on the associated Trading Networks document type.
* `$namespace.output` is a list of namespace prefixes and the URIs they
  map to, used when serializing the translated document returned by
  `$service` to [XML] content with elements in one or more namespaces.
* `$schema.input` is the optional name of the Integration Server document
  reference or flat file schema to use to parse the bizdoc content into an
  `IData` structure. Defaults to the parsing schema specified on the
  associated Trading Networks document type.
* `$schema.output` is the optional name of the Integration Server document
  reference or flat file schema to use to serialize the translated
  document returned by `$service`. Defaults to the value in `TN_parms/$schema`
  returned by `$service`, if not specified.
* `$service.input` is the optional name of the input parameter used for the
  parsed bizdoc content in the input pipeline of the invocation of `$service`.
  Defaults to `$document`.
* `$service.output` is the optional name of the output parameter used by
  `$service` to return the translated document in its output pipeline.
  Defaults to `$translation`.
* `$encoding.input` is an optional character set to use when decoding the
  content part data. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$encoding.output` is an optional character set to use when serializing
  the translated document. If not specified, defaults to [UTF-8].
* `$validate.input?` is an optional boolean flag which when `true` will
  validate the input content against the given `$schema.input`, or the
  parsing schema specified on the associated Trading Networks document
  type if `$schema.input` is not specified, and throw an exception to abort
  processing if the content is invalid. Defaults to `false`.
* `$validate.output?` is an optional boolean flag which when `true` will
  validate the output content against the given `$schema.output`, or the
  value in `TN_parms/$schema` returned by `$service` if `$schema.output` is
  not specified, and throw an exception to abort processing if the content
  is invalid. Defaults to `false`.
* `$status.done` is an optional user status to use for the bizdoc when
  it has been translated successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  translation is returned by `$service` and `$required` is `false`. Defaults to
  IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$required?` is an optional boolean indicating whether `$service` is
  required to return a translated document. If `true`, and no translation is
  returned by `$service`, an exception will be thrown and handled by the
  `$catch` service. Defaults to `false`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When `true` `$service` should implement the
  `TundraTN/tundra.tn.schema:translator` specification, when `false` `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to `true`.
* `$part` is the optional name of the bizdoc content part to be translated.
  Defaults to the default content part when not specified (xmldata for XML
  document types, ffdata for Flat File document types).
* `$strict` is an optional set of boolean flags which when `true` abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn.content:recognize

Recognizes the given content against the defined set of Trading
Networks document types, and returns a new Trading Networks document
(BizDocEnvelope) for that document type and the given content.

#### Inputs:

* `$content` is string, byte array, input stream, [org.w3c.dom.Node]
  object, `com.sap.conn.idoc.IDocDocumentList` object, or `IData`
  document content to be recognized by Trading Networks.
* `$content.identity` is an optional choice of mode for assigning a
  value to the `DocumentID` of the resulting bizdoc if no
  `DocumentID` is extracted:
  * `UUID`: assigns a newly generated [UUID].
  * `ULID`: assigns a newly generated [ULID].
  * `SHA-512`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-384`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-256`: the algorithm used to calculate a message digest from
    the content.
  * `SHA`: the algorithm used to calculate a message digest from the
    content.
  * `MD5`: the algorithm used to calculate a message digest from the
    content.
  * `MD2`: the algorithm used to calculate a message digest from the
    content.
* `$namespace` is an optional list of namespace prefixes and the URIs
  they map to, used when `$content` is provided as an `IData`
  document to be serialized to [XML] with elements in one or more
  namespaces.
* `TN_parms` is an optional set of routing hints for Trading Networks
  to use when recognizing `$content`.

#### Outputs:

* `$bizdoc` is a new Trading Networks document (BizDocEnvelope)
  representing the recognized content, but not yet routed to Trading
  Networks for processing.
* `TN_parms` is the set of routing hints Trading Networks used when
  recognizing the given content.

---

### tundra.tn.content:route

Routes arbitrary content specified as a string, byte array, input
stream, [org.w3c.dom.Node] object, `IData` document, or
`com.sap.conn.idoc.IDocDocumentList` object to Trading Networks.

Correctly supports large documents, so any document considered large
will be routed as a large document in TN, unlike the
`WmTN/wm.tn.doc.xml:routeXML` service which does not correctly use
tspace when routing large documents.

Also supports overriding the normally recognised document attributes,
unlike `WmTN/wm.tn.doc.xml:routeXML`, such as sender, receiver,
document ID, group ID, conversation ID, and document type with the
value specified in `TN_parms` for both XML and flat files documents.

#### Inputs:

* `$content` is string, byte array, input stream, [org.w3c.dom.Node]
  object, `com.sap.conn.idoc.IDocDocumentList` object, or `IData`
  document content to be routed to Trading Networks.

  If `$content` is provided as an `IData` document, it will be
  serialized using an parser determined in order of precedence by
  `$content.schema` and `$content.type`. If `$content.schema` is
  specified, the type of reference determines the parser to use: a
  document reference will use the XML parser, and a flat file schema
  reference will use the Flat File parser.

  If `$content.schema` is not specified, `$content.type` is used to
  determine the most appropriate parser for the MIME media type in
  question. If neither `$content.schema`, nor `$content.type` are
  specified, `$content` is serialized as XML by default.

  Parser implementions are as follows:
  * CSV: `Tundra/tundra.csv:emit`
  * Flat File: `WmFlatFile/pub.flatFile:convertToString`
  * JSON: `Tundra/tundra.json:emit`
  * Pipe separated values: `Tundra/tundra.csv:emit`
  * TSV: `Tundra/tundra.csv:emit`
  * XML: `WmPublic/pub.xml:documentToXMLString`
  * YAML: `Tundra/tundra.yaml:emit`
* `$content.type` is the MIME media type that describes the format of
  the  given content:
  * For [CSV] content, a recognized [CSV] MIME media type, such as
    "text/csv", "text/comma-separated-values", or a type that includes a
    "+csv" suffix, must be specified.
  * For [JSON] content, a recognized [JSON] MIME media type, such as
    "application/json", or a type that includes a "+json" suffix, must be
    specified.
  * For pipe separated values content, a MIME media type "text/psv",
    "text/pipe-separated-values", or a type that includes a "+psv" suffix,
    must be specified.
  * For [TSV] content, a recognized [TSV] MIME media type, such as
    "text/tsv", "text/tab-separated-values", or a type that includes a
    "+tsv" suffix, must be specified.
  * For [YAML] content, a recognized [YAML] MIME media type, such as
    "application/yaml", or a type that includes a "+yaml" suffix, must be
    specified.
  * For [XML] content, a recognized [XML] MIME media type, such as
    "text/xml" or "application/xml", or a type that includes a
    "+xml" suffix, must be specified.
* `$content.encoding` is an optional character set to use when
  encoding the resulting text data to a byte array or input stream.
  Defaults to [UTF-8].
* `$content.schema` is the fully-qualified name of the parsing schema
  to use to serialize `$content` when provided as an `IData` document
  to [XML] or Flat File content, and can have the following values:
  * For [XML] content, specify the fully-qualified name of the
    document reference that defines the [XML] format.
  * For Flat File content specify the fully-qualified name of the
    flat file schema that defines the Flat File format.
  Defaults to serializing `$content` as [XML], if neither
  `$content.type` nor `$content.schema` are specified.
* `$content.namespace` is an optional list of namespace prefixes and
  the URIs they map to, used when `$content` is provided as an
  `IData` document to be serialized to [XML] with elements in one or
  more namespaces.
* `$content.identity` is an optional choice of mode for assigning a
  value to the `DocumentID` of the resulting bizdoc if no
  `DocumentID` is extracted:
  * `UUID`: assigns a newly generated [UUID].
  * `ULID`: assigns a newly generated [ULID].
  * `SHA-512`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-384`: the algorithm used to calculate a message digest from
    the content.
  * `SHA-256`: the algorithm used to calculate a message digest from
    the content.
  * `SHA`: the algorithm used to calculate a message digest from the
    content.
  * `MD5`: the algorithm used to calculate a message digest from the
    content.
  * `MD2`: the algorithm used to calculate a message digest from the
    content.
* `$attributes` is a list of attributes to be set on the resulting
  Trading Networks document, and can be used to override the values
  of any extracted attributes, or to set the values of additional
  attributes not extracted by Trading Networks when it recognizes
  the type of document being routed. Attribute values that include
  percent-delimited variable substitutions will have their value
  resolved against the pipeline. If any variable substitution
  includes references to `$document`, the content will first be
  parsed and added to the pipeline as `$document` to support
  substituting attribute values based on the content being routed.
* `$strict?` is an optional boolean, which if `true` will abort
  routing (processing rule execution) of the document if any errors
  (such as validation errors) are encountered prior to processing,
  and result in an exception being thrown. Defaults to `true`.
* `TN_parms` is an optional set of routing hints for Trading Networks
  to use when routing `$content`. If specified, the following values
  will overwrite the normal bizdoc recognised values, allowing for
  sender, receiver, document ID, group ID, conversation ID, and
  document type to be forced to have the specified value (even for
  [XML] document types):
  * `TN_parms/SenderID`
  * `TN_parms/ReceiverID`
  * `TN_parms/DocumentID`
  * `TN_parms/DoctypeID`
  * `TN_parms/DoctypeName`
  * `TN_parms/GroupID`
  * `TN_parms/ConversationID`

#### Outputs:

* `$bizdoc` is the resulting Trading Networks document that was
  routed.
* `$sender` is the Trading Networks profile of the sender of the
  document.
* `$receiver` is the Trading Networks profile of the receiver of the
  document.
* `TN_parms` is the routing hints used to route the document in
  Trading Networks.

---

### tundra.tn.document.attribute.datetime.transformer:add

Trading Networks datetime attribute transformer which parses the
given Trading Networks document attribute values and adds the
given duration.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is either an [ISO8601] XML duration string to be added to the
  parsed datetime, or a [YAML] or [JSON] formatted string containing
  both the datetime pattern to use to parse the given values and an
  [ISO8601] XML duration to be added to the parsed datetime values.

  For example, the following [YAML] formatted value in `arg` will
  parse the datetime values using the pattern "dd/MM/yyyy HH:mm:ss",
  and then add a duration of 1 day:

        duration: P1D
        pattern: dd/MM/yyyy HH:mm:ss

  Similarly, the following [JSON] formatted value in `arg` will have
  the same results:

        { "duration": "P1D", "pattern": "dd/MM/yyyy HH:mm:ss" }

#### Outputs:

* `newValues` is a list containing the same number of items as the
  given `values` list, where `newValues[n]` is a [java.util.Date]
  object representing the parsed `values[n]` value plus the given
  duration.

---

### tundra.tn.document.attribute.datetime.transformer:constant

Trading Networks string transformer which returns the given
argument as the attribute value. This transformer can be used to
assign a constant value to an extracted attribute.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is the constant value to be returned as the attribute's value,
  and must be a [ISO8601] XML datetime string.

#### Outputs:

* `newValues` is a list the same length as the input values list if
  its length was greater than zero, or a list containing a single
  item, where each returned item has the given `arg` as its value.

---

### tundra.tn.document.attribute.datetime.transformer:parse

Trading Networks date attribute transformer which parses the given Trading
Networks document attribute value or list of values with the given
datetime pattern into [java.util.Date] object or list of objects.

Since the built-in TN date attribute parsing only supports
[java.text.SimpleDateFormat], versions prior to Java 7 are unable to parse
[ISO8601] dates, times, and datetimes when they include a timezone offset.
This service supports named patterns including [ISO8601] and custom
[java.text.SimpleDateFormat] patterns.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of datetime strings to be parsed.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is either a named pattern, or a custom [java.text.SimpleDateFormat]
  pattern used to parse the specified values. Defaults to an ISO8601/XML
  datetime, if not specified.

#### Outputs:

* `newValues` is a list of parsed [java.util.Date] objects representing
  the same instants in time as the given input datetime string `values`.

---

### tundra.tn.document.attribute.datetime.transformer:subtract

Trading Networks datetime attribute transformer which parses the
given Trading Networks document attribute values and subtracts the
given duration.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using
[java.text.SimpleDateFormat] compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is either an [ISO8601] XML duration string to be subtracted
  from the parsed datetime, or a [YAML] or [JSON] formatted string
  containing both the datetime pattern to use to parse the given
  values and an [ISO8601] XML duration to be added to the parsed
  datetime values.

  For example, the following [YAML] formatted value in `arg` will
  parse the datetime values using the pattern "dd/MM/yyyy HH:mm:ss",
  and then subtract a duration of 1 day:

          duration: P1D
          pattern: dd/MM/yyyy HH:mm:ss

  Similarly, the following [JSON] formatted value in `arg` will have
  the same results:

          { "duration": "P1D", "pattern": "dd/MM/yyyy HH:mm:ss" }

#### Outputs:

* `newValues` is a list containing the same number of items as the
  given `values` list, where `newValues[n]` is a [java.util.Date]
  object representing the parsed `values[n]` value minus the given
  duration.

---

### tundra.tn.document.attribute.number.transformer:add

Trading Networks number transformer which returns the sum of the
extracted numeric values.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of extracted numeric values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either the precision or number of decimal places used to
  calculate the result , or a [YAML] or [JSON] formatted string
  containing the precision and rounding algorithm to be used.
  The rounding algorithms available and how to identify the
  algorithm to be used is as per the `Tundra/tundra.decimal:add`
  `$rounding` input argument.

  For example, the following [YAML] formatted value in arg will
  calculate will round the result to 2 decimal places using the
  HALF_UP algorithm:

      precision: 2
      rounding: HALF_UP

  Similarly, the following [JSON] formatted value in arg will use
  the exact same values for precision and rounding:

      { "precision" : 2, "rounding": "HALF_UP" }

#### Outputs:

* `newValues` is a list containing a single value representing the
  total sum of all the given `values`.

---

### tundra.tn.document.attribute.number.transformer:average

Trading Networks number transformer which calculates the average
of the extracted numeric values.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of extracted numeric values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either the precision or number of decimal places used to
  calculate the result , or a [YAML] or [JSON] formatted string
  containing the precision and rounding algorithm to be used.
  The rounding algorithms available and how to identify the
  algorithm to be used is as per the `Tundra/tundra.decimal:add`
  `$rounding` input argument.

  For example, the following [YAML] formatted value in arg will
  calculate will round the result to 2 decimal places using the
  HALF_UP algorithm:

      precision: 2
      rounding: HALF_UP

  Similarly, the following [JSON] formatted value in arg will use
  the exact same values for precision and rounding:

      { "precision" : 2, "rounding": "HALF_UP" }

#### Outputs:

* `newValues` is a list containing a single value representing the
  average of all the given `values`.

---

### tundra.tn.document.attribute.number.transformer:constant

Trading Networks string transformer which returns the given
argument as the attribute value. This transformer can be used to
assign a constant value to an extracted attribute.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is the constant value to be returned as the attribute's value,
  and must be a decimal number specified as a string using the
  [java.math.BigDecimal grammar].

#### Outputs:

* `newValues` is a list the same length as the input values list if
  its length was greater than zero, or a list containing a single
  item, where each returned item has the given `arg` as its value.

---

### tundra.tn.document.attribute.number.transformer.duration:age

Trading Networks number transformer which returns the age in
milliseconds relative to the current datetime of the given
extracted datetime strings.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either a named pattern, or a custom [java.text.SimpleDateFormat]
  pattern used to parse the specified values. Defaults to an [ISO8601] XML
  datetime, if not specified.

#### Outputs:

* `newValues` is a list containing the same number of items as the given
  `values` list, where `newValues[n]` is the age in milliseconds from the
  datetime specified in `values[n]` to the current datetime.

---

### tundra.tn.document.attribute.number.transformer.duration:effective

Trading Networks number transformer which returns the the number of
milliseconds from the current datetime to the the given extracted
datetime strings.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either a named pattern, or a custom [java.text.SimpleDateFormat]
  pattern used to parse the specified values. Defaults to an [ISO8601] XML
  datetime, if not specified.

#### Outputs:

* `newValues` is a list containing the same number of items as the given
  `values` list, where `newValues[n]` is the distance in milliseconds from the
  current datetime to the datetime specified in `values[n]`.

---

### tundra.tn.document.attribute.number.transformer:maximum

Trading Networks number transformer which returns the maximum
of the extracted numeric values.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of extracted numeric values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either the precision or number of decimal places used to
  calculate the result , or a [YAML] or [JSON] formatted string
  containing the precision and rounding algorithm to be used.
  The rounding algorithms available and how to identify the
  algorithm to be used is as per the `Tundra/tundra.decimal:add`
  `$rounding` input argument.

  For example, the following [YAML] formatted value in arg will
  calculate will round the result to 2 decimal places using the
  HALF_UP algorithm:

      precision: 2
      rounding: HALF_UP

  Similarly, the following [JSON] formatted value in arg will use
  the exact same values for precision and rounding:

      { "precision" : 2, "rounding": "HALF_UP" }

#### Outputs:

* `newValues` is a list containing a single value representing the
  maximum of all the given `values`.

---

### tundra.tn.document.attribute.number.transformer:minimum

Trading Networks number transformer which returns the minimum
of the extracted numeric values.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of extracted numeric values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either the precision or number of decimal places used to
  calculate the result , or a [YAML] or [JSON] formatted string
  containing the precision and rounding algorithm to be used.
  The rounding algorithms available and how to identify the
  algorithm to be used is as per the `Tundra/tundra.decimal:add`
  `$rounding` input argument.

  For example, the following [YAML] formatted value in arg will
  calculate will round the result to 2 decimal places using the
  HALF_UP algorithm:

      precision: 2
      rounding: HALF_UP

  Similarly, the following [JSON] formatted value in arg will use
  the exact same values for precision and rounding:

      { "precision" : 2, "rounding": "HALF_UP" }

#### Outputs:

* `newValues` is a list containing a single value representing the
  minimum of all the given `values`.

---

### tundra.tn.document.attribute.number.transformer.priority:imminence

Trading Networks number transformer which returns a message priority
given one or more datetimes, a duration range, and a base priority.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a YAML or JSON string containing the following fields:
  * `pattern` is the datetime pattern the given values conform to.
  * `range` is an XML duration range, specified as `start..end`, and
    defaults to `-PT1H..P7D` if not specified. Datetime values within
    the range affect the priority, and the closer the datetime to the
    start of the range the higher the returned priority.
  * `priority` is the base priority as an integer.

#### Outputs:

* newValues is a list containing a message priority calculated as
  the base priority integer plus a decimal mantissa representing the
  most imminent of the given datetime values to the start of the given
  duration range relative to current time.

---

### tundra.tn.document.attribute.string.transformer:constant

Trading Networks string transformer which returns the given
argument as the attribute value. This transformer can be used to
assign a constant value to an extracted attribute.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is the constant value to be returned as the attribute's value.

#### Outputs:

* `newValues` is a list the same length as the input values list if
  its length was greater than zero, or a list containing a single
  item, where each returned item has the given `arg` as its value.

---

### tundra.tn.document.attribute.string.transformer.datetime.threshold:after

Trading Networks string transformer which returns whether the
extracted datetime strings are later than the current datetime
plus the specified duration.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is either an [ISO8601] XML duration string used to calculate
  the threshold by being added to the current datetime, or a [YAML]
  or [JSON] formatted string containing both the datetime pattern to
  use to parse the given values and an [ISO8601] XML duration used
  to calculate the threshold by being added to the current datetime.

  For example, the following [YAML] formatted value in arg will
  parse the datetime values using the given pattern and calculate
  the comparison threshold as the current datetime + 1 day:

      duration: P1D
      pattern: dd/MM/yyyy HH:mm:ss

  Similarly, the following [JSON] formatted value in arg will have
  the same results:

      { "duration": "P1D", "pattern": "dd/MM/yyyy HH:mm:ss" }

#### Outputs:

* `newValues` is a list containing the same number of items as the
  given values list, where `newValues[n]` is a boolean indicating if
  the datetime specified in `values[n]` was later than the
  threshold, calculated as the current datetime plus the given
  duration.

---

### tundra.tn.document.attribute.string.transformer.datetime.threshold:before

Trading Networks string transformer which returns whether the
extracted datetime strings are earlier than the current datetime
plus the specified duration.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is either an [ISO8601] XML duration string used to calculate
  the threshold by being added to the current datetime, or a [YAML]
  or [JSON] formatted string containing both the datetime pattern to
  use to parse the given values and an [ISO8601] XML duration used
  to calculate the threshold by being added to the current datetime.

  For example, the following [YAML] formatted value in arg will
  parse the datetime values using the given pattern and calculate
  the comparison threshold as the current datetime + 1 day:

      duration: P1D
      pattern: dd/MM/yyyy HH:mm:ss

  Similarly, the following [JSON] formatted value in arg will have
  the same results:

      { "duration": "P1D", "pattern": "dd/MM/yyyy HH:mm:ss" }

#### Outputs:

* `newValues` is a list containing the same number of items as the
  given values list, where `newValues[n]` is a boolean indicating if
  the datetime specified in `values[n]` was earlier than the
  threshold, calculated as the current datetime plus the given
  duration.

---

### tundra.tn.document.attribute.string.transformer.datetime.threshold:equal

Trading Networks string transformer which returns whether the
extracted datetime strings are equal to the current datetime plus
the specified duration.

Supports a handful of well-known named patterns:

Pattern Name  | Description
------------- | --------------------------------------------
datetime      | ISO8601 XML datetime
datetime.db2  | yyyy-MM-dd-HH.mm.ss.SSSSSS
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601 XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601 XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
seconds       | Number of seconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

Custom datetime patterns can be specified using [java.text.SimpleDateFormat]
compatible patterns.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of datetime strings all conforming to the same
  pattern.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is either an [ISO8601] XML duration string used to calculate
  the threshold by being added to the current datetime, or a [YAML]
  or [JSON] formatted string containing both the datetime pattern to
  use to parse the given values and an [ISO8601] XML duration used
  to calculate the threshold by being added to the current datetime.

  For example, the following [YAML] formatted value in arg will
  parse the datetime values using the given pattern and calculate
  the comparison threshold as the current datetime + 1 day:

      duration: P1D
      pattern: dd/MM/yyyy HH:mm:ss

  Similarly, the following [JSON] formatted value in arg will have
  the same results:

      { "duration": "P1D", "pattern": "dd/MM/yyyy HH:mm:ss" }

#### Outputs:

* `newValues` is a list containing the same number of items as the
  given values list, where `newValues[n]` is a boolean indicating if
  the datetime specified in `values[n]` was equal to the threshold,
  calculated as the current datetime plus the given duration.

---

### tundra.tn.document.attribute.string.transformer.dns:self

Trading Networks string transformer which returns the domain name,
host name, or IP address of the localhost on which it runs. This
transformer can be used to record the name or address of the server
on which the related bizdoc was received and routed in an extracted
attribute.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is an optional choice of the following values, and
  determines whether the domain name, host name, or IP address is
  returned respectively. If not specified, defaults to `domain`:
  * `domain`
  * `host`
  * `address`

#### Outputs:

* `newValues` is a list containing a single item whose value is
  the localhost's domain name, host name, or IP address, depending
  on the value of `arg` provided.

---

### tundra.tn.document.attribute.string.transformer:find

Trading Networks string transformer which returns whether the given
Trading Networks document attribute value or list of values includes
the given [regular expression pattern].

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be searched against the
  [regular expression pattern].
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list boolean values indicating if the given input
  string `values` were found to include the given [regular expression
  pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.find:all

Trading Networks string transformer which returns whether all of the
given Trading Networks document attribute value or list of values
includes the given [regular expression pattern], thereby transforming
a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be searched against for the
  [regular expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  all of the given input string values were found to include the
  given [regular expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.find:any

Trading Networks string transformer which returns whether any of the
given Trading Networks document attribute value or list of values
includes the given [regular expression pattern], thereby transforming
a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be searched against for the
  [regular expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  any of the given input string values were found to include the
  given [regular expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.find:none

Trading Networks string transformer which returns whether none of the
given Trading Networks document attribute value or list of values
includes the given [regular expression pattern], thereby transforming
a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be searched against for the
  [regular expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  none of the given input string values were found to include the
  given [regular expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer:first

Trading Networks string transformer which returns only the first item
in the given list of extracted string values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to transformed.
* `isArray` is a boolean indicating if the values argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a string list which contains only the first item from the
  given `values` list.

---

### tundra.tn.document.attribute.string.transformer:last

Trading Networks string transformer which returns only the last item
in the given list of extracted string values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to transformed.
* `isArray` is a boolean indicating if the values argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a string list which contains only the last item from the
  given `values` list.

---

### tundra.tn.document.attribute.string.transformer:length

Trading Networks string transformer which returns only the length of
the given list of extracted string values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to transformed.
* `isArray` is a boolean indicating if the values argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a string list which contains only a single item: the length
  of the given `values` list.

---

### tundra.tn.document.attribute.string.transformer:match

Trading Networks string transformer which returns whether the given
Trading Networks document attribute value or list of values match the
given [regular expression pattern].

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the
  [regular expression pattern].
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list boolean values indicating if the given input
  string `values` match the given [regular expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.match:all

Trading Networks string transformer which returns whether all of the
given Trading Networks document attribute value or list of values
match the given [regular expression pattern], thereby transforming
a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the [regular
  expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  all of the given input string values match the given [regular
  expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.match:any

Trading Networks string transformer which returns whether any of the
given Trading Networks document attribute value or list of values
match the given [regular expression pattern], thereby transforming
a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the [regular
  expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  any of the given input string values match the given [regular
  expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.match:none

Trading Networks string transformer which returns whether none of
the given Trading Networks document attribute value or list of
values match the given [regular expression pattern], thereby
transforming a list of string values into a single boolean value.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the [regular
  expression pattern].
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list containing a single boolean item indicating if
  none of the given input string values match the given [regular
  expression pattern] `arg`.

---

### tundra.tn.document.attribute.string.transformer.profile:find

Trading Networks string transformer which returns the internal ID
associated with a Trading Networks partner profile for a given
external ID.

This transformer replaces the built-in partner profile lookup
function `FN_PARTNER_LOOKUP`. Unlike the `FN_PARTNER_LOOKUP`, this
transformer caches the external IDs in memory, so that it is more
resilient and likely to survive short database outages gracefully. In
contrast, the `FN_PARTNER_LOOKUP` unexpectedly returns the `Unknown`
partner profile when performing lookups during database outages,
resulting in documents with an `Unknown` sender or receiver set
incorrectly.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of one or more Trading Networks partner profile
  external IDs.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is the type of external ID provided in `values`, such as
  `DUNS` or `User Defined 1`.

#### Outputs:

* `newValues` is a list containing the internal IDs of the partner
  profiles identified by the given external IDs in `values`.

---

### tundra.tn.document.attribute.string.transformer.profile:get

Trading Networks string transformer which returns the internal ID
associated with a Trading Networks partner profile. This transformer
can be used to force the sender or receiver of a document to always
be a specific partner profile, regardless of the value of the
extracted attribute.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is either the internal ID for a specific partner profile, or
  a [YAML] or [JSON] formatted string containing the external ID
  description and value to be used to look up a specific partner
  profile.

  For example, the following [YAML] formatted value in `arg` will
  look up a partner profile with an External ID "User Defined 1"
  equal to "ABCDEFG":

      User Defined 1: ABCDEFG

  Similarly, the following [JSON] formatted value in `arg` will look
  up the exact same partner profile:

      { "User Defined 1" : "ABCDEFG" }

#### Outputs:

* `newValues` is a list the same length as the input `values` list if
  its length was greater than zero, or a list containing a single
  item, where each returned item is the internal ID of the Trading
  Networks partner profile associated with the given `arg` value.

---

### tundra.tn.document.attribute.string.transformer.profile:self

Trading Networks string transformer which returns the Trading Networks My
Enterprise profile's internal ID. This transformer can be used to force the
sender or receiver of a document to always be the My Enterprise profile,
regardless of the value of the extracted attribute.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored by this
  service and are therefore irrelevant.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a list the same length as the input `values` list if
  its length was greater than zero, or a list containing a single
  item, where each returned item is the internal ID of the Trading
  Networks My Enterprise partner profile.

---

### tundra.tn.document.attribute.string.transformer:unique

Trading Networks string transformer which returns only the unique values
in the given list of extracted string values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to transformed.
* `isArray` is a boolean indicating if the values argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a list of only the unique strings from the given `values` list.

---

### tundra.tn.document.attribute.string.transformer.uri:decode

Trading Networks string transformer which URI decodes the given Trading
Networks document attribute value or list of values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be URI decoded.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a list of URI decoded items from the input values list.

---

### tundra.tn.document.attribute.string.transformer.uri:encode

Trading Networks string transformer which URI encodes the given Trading
Networks document attribute value or list of values.

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be URI encoded.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a list of URI encoded items from the input values list.

---

### tundra.tn.document.attribute.string.transformer.user:current

Trading Networks string transformer which returns the currently
logged on username. This transformer can be used to extract the user
that submitted the document as an attribute of the document.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the `values` argument contains
  multiple items.
* `arg` is not used by this service, and therefore not required.

#### Outputs:

* `newValues` is a list containing a single item whose value is
  the currently loggged on username.

---

### tundra.tn.document.attribute.string.transformer.uuid:generate

Trading Networks string transformer which returns newly generated
immutable universally unique identifiers ([UUID]). This transformer
can be used to assign a generated identifier to an extracted
attribute, such as GroupID.

This service is intended to be invoked by Trading Networks as a
custom document type attribute transformer.

#### Inputs:

* `values` is a list of arbitrary strings, their values are ignored
  by this service and are therefore irrelevant.
* `isArray` is a boolean indicating if the values argument contains
  multiple items.
* `arg` is an optional choice which determines how the [UUID] is
  converted to a string. Defaults to `string`, if not specified:
  * `string` returns the [UUID] in the string representation as per
    [RFC 4122], and is the default.
  * `base64` returns the [UUID] as a [Base64] encoded string.

#### Outputs:

* `newValues` is a list the same length as the input `values` list if
  its length was greater than zero, or a list containing a single
  item, where each returned item is a newly generated [UUID].

---

### tundra.tn.document.attribute:merge

Merges the given attributes into the given Trading Networks document.

#### Inputs:

* `$bizdoc` is the Trading Networks document (BizDocEnvelope) against
  which the given `$attributes` will be merged.
* `$attributes` are the attribute keys and values to be merged into
  the given `$bizdoc`. The attributes are first sanitized such that
  any keys that do match already defined Trading Networks document
  attributes are removed, then the attributes are
  merged into the `$bizdoc`. Existing attributes on the `$bizdoc`
  will be overwritten if `$attributes` contains the same attribute.
* `$substitute?` is an optional boolean indicating if variable
  substitution should be performed against the `$attributes` values.
  The pipeline used for substitution will include the parsed
  `$bizdoc` content as a variable named `$document`. Defaults to
  `false`.

---

### tundra.tn.document.content:add

Adds a content part with the given name and content to the given
Trading Networks document (bizdoc).

#### Inputs:

* `$bizdoc` is the Trading Networks document to add the content part
  to. For convenience, only the `InternalID` is required.
* `$content` is the content part data to be added, specified as a
  string, byte array, or input stream.
* `$content.part` is the name of the content part to be added.
* `$content.type` is an optional MIME media type describing the type
  of content being added. Defaults to `application/octet-stream`, the
  default MIME media type for arbitrary binary data, if not specified.
* `$content.encoding` is the optional character set used to encode
  `$content` when is specified as a string. Defaults to [UTF-8].
* `$overwrite?` is an optional boolean flag indicating whether to
  overwrite an existing content part with the same name, if one
  exists. Defaults to `false`.

---

### tundra.tn.document.content:exists

Returns `true` if the content part identified by the given part name
exists for the given Trading Networks document (bizdoc).

#### Inputs:

* `$bizdoc` is the Trading Networks document to check for the
  existence of the given content part. For convenience, only the
  `InternalID` is required.
* `$content.part` is the name of the content part check the existence
  of.

#### Outputs:

* `$exists?` is a boolean that when `true` indicates that the given
  `$bizdoc` has a content part with the given `$content.part` name.

---

### tundra.tn.document.content:get

Returns the given Trading Networks document's content associated with
the given part name.

#### Inputs:

* `$bizdoc` is the Trading Networks document to retrieve the content
  part from. For convenience, only the `InternalID` is required.
* `$content.part` is an optional name of the content part to be
  returned. If not specified, the default content part  is returned:
  `xmldata` for XML, or `ffdata` for Flat Files.
* `$content.mode` is an optional choice which determines the type of
  object `$content` is returned as. Defaults to `stream`.
  * `base64`: the content is returned as a base-64 encoded string.
  * `bytes`: the content is returned as a byte array.
  * `stream`: the content is returned as a `java.io.InputStream`.
  * `string`: the content is returned as a string.

#### Outputs:

* `$content` is the content part data.
* `$content.part` is the name of the content part.
* `$content.type` is the MIME media type of the content part.
* `$content.length` is the size in bytes of the content part.

---

### tundra.tn.document.content:remove

Deletes the Trading Networks document content part with the given name
from the Trading Networks database.

#### Inputs:

* `$bizdoc` is the Trading Networks document to delete the content
  part from. For convenience, only the `InternalID` is required.
* `$content.part` is the name of the content part to be deleted from
  the Trading Networks document, and uniquely identifies the part
  being deleted.

---

### tundra.tn.document.derivative:exists

Returns `true` if the given bizdoc is related to a derived bizdoc with the
given sender and receiver.

#### Inputs:

* `$bizdoc` is the Trading Networks document to check for the existence of a
  derivative with the given sender and receiver. Only the internal ID of
  the bizdoc must be specified, with the remainder of the
  `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$sender` is the Trading Networks partner profile of the derived sender of
  the bizdoc. The structure is a minimal version of the
  `TundraTN/tundra.tn.schema:profile` document, which support profiles
  provided in `WmTN/wm.tn.rec:ProfileSummary` or `WmTN/wm.tn.rec:Profile`
  format, or an ID provided in the PartnerID or ProfileID fields.
* `$receiver` is the Trading Networks partner profile of the derived
  receiver of the bizdoc. The structure is a minimal version of the
  `TundraTN/tundra.tn.schema:profile` document, which support profiles
  provided in `WmTN/wm.tn.rec:ProfileSummary` or `WmTN/wm.tn.rec:Profile`
  format, or an ID provided in the PartnerID or ProfileID fields.

#### Outputs:

* `$exists?` is a boolean that when `true` indicates the existence of a
  derivative of the given bizdoc with the given derived sender and
  receiver.
* `$derivative` is the optional derivative bizdoc, if it exists.

---

### tundra.tn.document:derive

Copies an existing Trading Networks document (bizdoc), optionally updating
the sender and/or receiver on the copy, and routes the copy as a new
document to Trading Networks.

#### Inputs:

* `$bizdoc` is the Trading Networks document from which a bizdoc copy will
  be derived. Only the internal ID of the bizdoc must be specified, with
  the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely
  optional. If the specified bizdoc does not exist, an exception will be
  thrown.
* `$derivative.rule` is a derivative rule describing what type of copy is
  to be made of this bizdoc:
  * `description` is an optional description of the derivative rule, used in
    all related activity log statements.
  * `sender` is an optional (internal or external) ID identifying the
    desired Trading Networks partner profile to be used as the sender on
    the derivative bizdoc. Defaults to the original bizdoc sender if not
    specified.
  * `receiver` is an optional (internal or external) ID identifying the
    desired Trading Networks partner profile to be used as the receiver on
    the derivative bizdoc. Defaults to the original bizdoc receiver if not
    specified.
  * `type` is an optional Trading Networks external ID type used to
    interpret the above `sender` and `receiver` values. If not specified, then
    the sender and receiver IDs are treated as internal partner profile
    IDs.
  * `filter` is an optional inline filter condition (as supported by
    `Tundra/tundra.condition:evaluate`) or fully qualified service name of a
    service implementing the `TundraTN/tundra.tn.schema.derivative:filter`
    specification. If specifying an inline filter condition, the input
    pipeline will be the same as described for a filter service by the
    `TundraTN/tundra.tn.schema.derivative:filter` specification. If not
    specified, then a copy will always be derived for this new sender/
    receiver.
  * `amendments` is an optional list of {key, value} pairs used to make
    small inline modifications to the derived bizdoc content:
    * `key` is a fully-qualified reference to a field in the parsed bizdoc
      content.
    * `value` is the new value to be assigned to the element associated with
      the given key.
    * `condition` is an optional `Tundra/tundra.condition:evaluate`
      conditional statement can also be specified, which is evaluated
      against the pipeline containing `$bizdoc`, `$sender`, `$receiver`, and
      `$document` (the parsed bizdoc content), and only if the condition
      evaluates to `true` will the associated amended value be applied. If
      not specified, the amended value will always be applied.
  * `attributes` is an optional list of {key, value} pairs used to set
    attributes on the derived bizdoc:
    * `key` identifies the attribute that is set to the given `value`.
    * `value` is the new value to be assigned to the attribute associated
      with the given `key`.
    * `condition` is an optional `Tundra/tundra.condition:evaluate`
      conditional statement can also be specified, which is evaluated
      against the pipeline containing `$bizdoc`, `$sender`, `$receiver`, and
      `$document` (the parsed bizdoc content), and only if the condition
      evaluates to `true` will the attribute be added to the derived bizdoc.
      If not specified, the attribute will always be added to the derived
      bizdoc.
  * `TN_parms` is an optional `IData` document containing routing hints used
    when routing the derivative bizdoc.
  * `force?` is an optional boolean flag: when `true` a new derivative will
    always be created even if an existing derivative for the same sender
    and receiver already exists; when `false` a new derivative will only
    be created if there is no existing derivative with the same sender
    and receiver. Defaults to `false`, if not specified.
  * `enabled?` is an optional boolean flag, when `true` this derivative rule
    is active, when `false`, this derivative rule is inactive and ignored.
    Defaults to `true` when not specified.
* `$part` is an optional name of the bizdoc content part to be copied to the
  resulting derivative. If not specified, the default content part is
  copied (xmldata for XML document types, ffdata for Flat File document
  types).

#### Outputs:

* `$derivative` is the resulting bizdoc copy after it has been routed to
  Trading Networks.

---

### tundra.tn.document.duplicate:check

Checks if the given document is a duplicate by checking if there
are other documents with the same document type, sender, receiver,
and document ID.

This service is designed to be called as a custom document duplicate
check service from Trading Networks, and is compatible with the the
`WmTN/wm.tn.rec:DupCheckService` specification.

#### Inputs:

* `bizdoc` is the Trading Networks document to be checked. Only
  the internal ID of the bizdoc must be specified, with the
  remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely
  optional.

#### Outputs:

* `duplicate` is a boolean flag which when `true` indicates that the
  given document was considered a duplicate.

---

### tundra.tn.document:enqueue

Enqueues the given Trading Networks document for delivery by the
given queue.

Creates a new delivery task if one does not already exist for the
given document on the given queue. However, if a task does already
exist, it will be restarted if it is failed or stopped or `$force?`
is `true`, otherwise the document will not be requeued to the queue
as an existing in-progress or completed task already exists for it.

#### Inputs:

* `$bizdoc` is the Trading Networks document to be enqueued.
* `$queue` is the name of the Trading Networks delivery queue to
  enqueue the given `$bizdoc` to for delivery.
* `$force?` is an optional boolean which if `true` will always
  restart an existing task regardless of its status. If `false`, only
  existing tasks that are failed or stopped will be restarted.
  Defaults to `false`.

#### Outputs:

* `$task` is the delivery task created or restarted to deliver the
  given `$bizdoc` via the given `$queue`.

---

### tundra.tn.document.error:exists

Returns `true` if any errors (of the given class, if specified) exist on the
given bizdoc.

#### Inputs:

* `$bizdoc` is the Trading Networks document to check for the existence of
  any errors (of the given class, if specified) exist. Only the internal
  ID of the bizdoc must be specified, with the remainder of the
  `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$class` is an optional set of boolean flags which indicate which error
  classes are of interest. If not specified, all error classes are
  considered to be of interest.
  * `Security`
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`
  * `General`
  * `Processing`
  * `Delivery`
  * `Transient`
  * `Unrecoverable`

#### Outputs:

* `$exists?` is a boolean that when `true` indicates the existence of one or
  more errors of the given classes (if specified) have been logged against
  the given bizdoc.
* `$errors` is the list of activity error logs of the given classes (if
  specified) that were found logged against the given bizdoc.

---

### tundra.tn.document:get

Returns the Trading Networks document (bizdoc) associated with the
given internal ID, optionally including the document's content parts.

#### Inputs:

* `$id` is the internal ID of the Trading Networks document (bizdoc)
  to be retrieved.
* `$content?` is an optional boolean indicating whether to also return
  the bizdoc's content parts. Defaults to `false`.
* `$raise?` is an optional boolean indicating whether to throw an
  exception if no bizdoc exists with the given `$id`. Defaults to
  `false`.

#### Outputs:

* `$bizdoc` is the Trading Networks document associated with the given
  `$id`, if found.
* `$sender` is the Trading Networks partner profile of the sender of
  the returned bizdoc.
* `$receiver` is the Trading Networks partner profile of the receiver
  of the returned bizdoc.

---

### tundra.tn.document.namespace:get

Returns the namespace declarations associated with the given Trading
Networks document (bizdoc) in a format useful when parsing the
content using `WmPublic/pub.xml:xmlStringToXMLNode` and
`WmPublic/pub.xml:xmlNodeToDocument`.

Note that the special Trading Networks namespace prefix `prefix0`,
which represents the default namespace, is automatically copied
to Integration Server's default namespace prefix `ns`.

#### Inputs:

* `$bizdoc` is the Trading Networks document to retrieve the
  namespace declarations from. Only the internal ID of the bizdoc
  must be specified, with the remainder of the
  `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.

#### Outputs:

* `$namespace` is an `IData` document containing the namespace
  declarations associated with the given `$bizdoc`, where the keys
  in the document are the namespace prefixes and the values are
  the namespace URIs.

---

### tundra.tn.document:normalize

Returns the given Trading Networks document (bizdoc) and its
associated sender and receiver profiles if desired. When the given
bizdoc is a subset (only the InternalID is required), the full
bizdoc will be returned.

#### Inputs:

* `$bizdoc` is the Trading Networks document (bizdoc) to be
  normalized, and can be specified as a subset containing at least
  the InternalID field.
* `$content?` is an optional boolean indicating whether to also return
  the bizdoc's content parts. Defaults to `false`.
* `$sender?` is an optional boolean indicating whether to also return
  the associated sender partner profile. Defaults to `false`.
* `$receiver?` is an optional boolean indicating whether to also
  return the associated receiver partner profile. Defaults to `false`.
* `$raise?` is an optional boolean indicating whether to throw an
  exception if the bizdoc does not exist. Defaults to `false`.

#### Outputs:

* `$bizdoc` is the normalized full Trading Networks document (bizdoc).
* `$sender` is the Trading Networks partner profile of the sender of
  the returned bizdoc, if requested.
* `$receiver` is the Trading Networks partner profile of the receiver
  of the returned bizdoc, if requested.

---

### tundra.tn.document:parse

Parses the Trading Networks document content part associated with the
given part name, or the default part if not provided, using the
parsing schema configured on the associated document type.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be
  parsed. Only the internal ID of the bizdoc must be specified, with
  the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure
  purely optional. If the specified bizdoc does not exist, an
  exception will be thrown.
* `$part` is an optional content part name to be parsed. If not
  specified, the default content part (xmldata for XML, ffdata for
  Flat Files) will be parsed. If the specified content part name does
  not exist, an exception will be thrown.
* `$encoding` is an optional character set to use when decoding the
  content part data. If not specified, defaults to the character set
  specified in the MIME content type of the content part being
  parsed, or failing that [UTF-8].
* `$validate?` is an optional boolean flag which when `true` will
  validate the parsed content against the given `$schema`, and throw
  an exception if the content is invalid. Defaults to `false`.

#### Outputs:

* `$document` is the parsed content part in an `IData` document
  representation.
* `$content.type` is the MIME media type that describes the format of
  the parsed content.
* `$namespace` is the list of XML namespace prefixes and URIs
  declared on the associated document type and used when parsing the
  content.
* `$schema` is an optional output that specifies the fully-qualified
  name of the document reference (for XML) or flat file schema (for
  Flat Files) declared on the associated document type.
* `$schema.type` is an optional output that specifies whether
  `$schema` is an XML document reference or flat file schema, and is
  a choice of one of the following values:
  * `Flat File`
  * `XML`

---

### tundra.tn.document:relate

Relates two Trading Networks documents (BizDocEnvelope) together.

Use this service in preference to `WmTN/wm.tn.doc:relateDocuments`,
as this service does not throw an exception if the relationship
already exists, and this service also logs the creation of the
relationship on the target bizdoc.

#### Inputs:

* `$bizdoc.source` is the source Trading Networks document in the
  relationship. Only the `InternalID` is required, with the remainder
  of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$bizdoc.target` is the target Trading Networks document in the
  relationship. Only the `InternalID` is required, with the remainder
  of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$relationship` is an optional string describing the relationship
  between the source and target bizdocs. Defaults to `Unknown`.

---

### tundra.tn.document:reroute

Reprocesses the given Trading Networks document.

#### Inputs:

* `$bizdoc` is the Trading Networks document (BizDocEnvelope) to be
  reprocessed, and can be specified as a subset containing at least
  the `InternalID` field.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

#### Outputs:

* `$bizdoc` is the Trading Networks document that was reprocessed.
* `$sender` is the Trading Networks profile of the sender of the
  document.
* `$receiver` is the Trading Networks profile of the receiver of the
  document.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

---

### tundra.tn.document:route

Routes the given `BizDocEnvelope` object to Trading Networks.

Reimplements `wm.tn.route:routeBizdoc`, but throws an exception and
aborts routing if any errors are detected prior to executing the
processing rule if `$strict?` is `true`.

#### Inputs:

* `$bizdoc` is the Trading Networks `BizDocEnvelope` object to be
  routed.
* `$transport.log?` is an optional boolean indicating if the current
  transport information from `pub.flow:getTransportInfo` should be
  added to the document as a new content part for diagnostics.
  Defaults to `false`.
* `$transport.log.part` is an optional content part name to use when
  adding the current transport information content part. Defaults
* `$strict?` is an optional boolean indicating if routing should be
  aborted if any errors are detected prior to executing the
  processing rule.
* `TN_parms` is the optional Trading Networks routing hints used when
  routing the document.

#### Outputs:

* `$bizdoc` is the Trading Networks BizDocEnvelope object that was
  routed.
* `$sender` is the sender profile associated with the document.
* `$receiver` is the reciever profile associated with the document.
* `TN_parms` is the Trading Networks routing hints that were used
  when the document was routed.

---

### tundra.tn.document.schema:get

Returns the parsing schema associated with the given Trading Networks
document.

#### Inputs:

* `$bizdoc` is the Trading Networks document to retrieve the parsing
  schema from. Only the internal ID of the bizdoc must be specified,
  with the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure
  purely optional.

#### Outputs:

* `$schema` is the fully-qualified name of the document reference
  (for XML) or flat file schema (for flat files) declared on the
  given bizdoc's document type.
* `$schema.type` specifies whether `$schema` is an XML document
  reference or flat file schema, and is a choice of one of the
  following values:
  * `Flat File`
  * `XML`

---

### tundra.tn.document.status:set

Sets user status on the given Trading Networks document.

#### Inputs:

* `$bizdoc` is the Trading Networks document to set the user status
  on. Only the internal ID of the bizdoc must be specified, with the
  remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely
  optional.
* `$status.system` is an optional system status string to be set on the
  given document. If not specified, the document's system status will
  not be changed.
* `$status.system.previous` can be optionally specified to ensure the
  system status is only updated if it previously equalled this value
  at the time of update.
* `$status.user` is an optional user status string to be set on the
  given document. If errors exist on the given Trading Networks
  document, and the specified user status is "DONE", then it is
  automatically suffixed with "W/ ERRORS". If not specified, the
  document's user status will not be changed.
* `$status.user.previous` can be optionally specified to ensure the
  user status is only updated if it previously equalled this value
  at the time of update.
* `$status.silence?` is an optional boolean which when `true` will cause
  this service not to change the status on the document. Defaults to
  `false`.

---

### tundra.tn.document.type:get

Returns the Trading Networks document type associated with the given
ID or name as an `IData` document.

Use this service in preference to `WmTN/wm.tn.doctype:view`, as the
WmTN service returns an object of type `com.wm.app.tn.doc.BizDocType`
which, despite looking like one, is not a normal `IData` document and
therefore causes problems in Flow services. For example, you cannot
branch on fields in the `com.wm.app.tn.doc.BizDocType` document.

Also, unlike `WmTN/wm.tn.doctype:view`, this service does not throw
an exception if the document type does not exist.

#### Inputs:

* `$document.type.id` is an optional internal ID that identifies the
  Trading Networks document type to be returned. If both
  `$document.type.id` and `$document.type.name` are specified,
  `$document.type.id` takes precedence.
* `$document.type.name` is an optional Trading Networks document type
  name that identifies the document type to be returned. If both
  `$document.type.id` and `$document.type.name` are specified,
  `$document.type.id` takes precedence.

#### Outputs:

* `$document.type` is the Trading Networks document type identified
  by either `$document.type.id` or `$document.type.name`, returned as
  an `IData` document with the `WmTN/wm.tn.rec:BizDocType` structure.
  If no document type exists with the given `$document.type.id` or
  `$document.type.name`, nothing will be returned by this service,
  nor will an exception be thrown.

---

### tundra.tn.document.type:normalize

Returns the given Trading Networks document type. When the given type
is a subset (only the `TypeID` is required), the full type will be
returned.

#### Inputs:

* `$document.type` is the Trading Networks document type to be
  normalized, and can be specified as a subset containing at least
  the `TypeID` field.

#### Outputs:

* `$document.type` is the normalized full Trading Networks document
  type.

---

### tundra.tn.document.type.schema:get

Returns the parsing schema associated with the given Trading Networks
document type.

#### Inputs:

* `$document.type` is the Trading Networks document type to retrieve
  the parsing schema from. Can be specified as a subset of the
  `WmTN/wm.tn.rec:BizDocType` structure containing at least the
  `TypeID` field.

#### Outputs:

* `$content.schema` is the fully-qualified name of the document
  reference (for XML) or flat file schema (for flat files) declared
  on the given document type.
* `$content.schema.type` specifies whether `$schema` is an XML
  document reference or flat file schema, and is a choice of one of
  the following values:
  * `Flat File`
  * `XML`

---

### tundra.tn.exception:handle

Handles a Trading Networks document processing error by logging the
error against the document in the activity log, and setting the user
status to either 'ABORTED' for security or strictness exceptions,
or 'ERROR' for any other exceptions.

This service can be used as a standard catch service for Trading
Networks document processing services in conjunction with
`Tundra/tundra.service:ensure`.

#### Inputs:

* `bizdoc` is the Trading Networks document being processed that
  caused the error.
* `$exception` is the Java exception object that was thrown by the
  processing service.
* `$exception.class` is the Java class name of the exception object
  that was thrown by the processing service.
* `$exception.message` is the error message related to the Java
  exception object that was thrown by the processing service.

---

### tundra.tn.exception:raise

Throws a new exception with the given message.

#### Inputs:

* `$message` is an optional error message to use when constructing the
  new exception object to be thrown. If not specified, an empty message
  will be used to construct the exception object.
* `$type` is an optional choice of 'security', 'strict', or 'validation',
  which if specified will throw one of the following subclasses of
  com.wm.app.b2b.server.ServiceException respectively:
  * permafrost.tundra.lang.SecurityException - used to indicate a security
    access error.
  * permafrost.tundra.lang.StrictException - used to indicate a document
    strictness error.
  * permafrost.tundra.lang.ValidationException - used to indicate a document
    validation error.

  If not specified, a com.wm.app.b2b.server.ServiceException exception
  object will be thrown.

---

### tundra.tn.profile.cache:clear

Clears the local in-memory partner profile cache by removing
all cached partner profiles.

---

### tundra.tn.profile.cache:list

Returns a list of all the partner profiles stored in the
local in-memory cache.

---

### tundra.tn.profile.cache:refresh

Reloads each partner profile stored in the local in-memory
partner profile cache from the Trading Networks database.

---

### tundra.tn.profile.cache:seed

Initializes the local in-memory partner profile cache with all
partner profiles from the Trading Networks database.

---

### tundra.tn.profile.delivery:get

Returns the named delivery method for the given Trading Networks partner
profile.

#### Inputs:

* `$profile` is the Trading Networks partner profile from which to retrieve
  the named delivery method. The structure is a minimal version of the
  `TundraTN/tundra.tn.schema:profile` document, which support profiles
  provided in `WmTN/wm.tn.rec:ProfileSummary` or `WmTN/wm.tn.rec:Profile`
  format, or an ID provided in the PartnerID or ProfileID fields.
* `$method` is the named delivery method to be retrieved, a choice of:
  * Preferred Protocol
  * Primary E-mail
  * Primary FTP
  * Primary FTPS
  * Primary HTTP
  * Primary HTTPS
  * Secondary E-mail
  * Secondary FTP
  * Secondary FTPS
  * Secondary HTTP
  * Secondary HTTPS

#### Outputs:

* `$delivery` is the named delivery method retrieved from the given partner
  profile, if it exists.

---

### tundra.tn.profile:get

Returns the Trading Networks partner profile associated with the given ID.

Executing this service has the side-effect of seeding the TundraTN local
in-memory cache with the returned partner profile, if it was not already
cached.

#### Inputs:

* `$id` is an optional identifier to use to look up the partner profile.
  If not specified, no profile is returned.
* `$type` is an optional External ID description which the given `$id`
  corresponds. If not specified, `$id` is assumed to be an internal partner
  ID.
* `$refresh?` is an optional boolean which when `true` will force a
  refresh of the cached profile from the Trading Networks database.
  Defaults to `false`.

#### Outputs:

* `$profile` is the Trading Networks partner profile associated with the
  given `$id`. The profile structure is a superset of both
  `wm.tn.rec:ProfileSummary` and `wm.tn.rec:Profile`, with additional
  convenient and usable structures added for External IDs, Delivery
  Methods, and Extended Fields.

---

### tundra.tn.profile:list

Returns a list of all the Trading Networks partner profiles stored in the
Trading Networks database.

Executing this service has the side-effect of seeding the TundraTN local
in-memory cache with all partner profiles, if they were not already
cached.

#### Inputs:

* `$refresh?` is an optional boolean which when `true` will force a
  refresh of all cached profiles from the Trading Networks database.
  Defaults to `false`.

#### Outputs:

* `$profiles` is a list of every Trading Networks partner profile. The
  profile structure is a superset of both `wm.tn.rec:ProfileSummary` and
  `wm.tn.rec:Profile`, with additional convenient and usable structures
  added for External IDs, Delivery Methods, and Extended Fields.

---

### tundra.tn.profile:self

Returns the Trading Networks My Enterprise partner profile.

Executing this service has the side-effect of seeding the TundraTN local
in-memory cache with the My Enterprise partner profile, if it was not
already cached.

#### Inputs:

* `$refresh?` is an optional boolean which when `true` will force a
  refresh of the cached profile from the Trading Networks database.
  Defaults to `false`.

#### Outputs:

* `$profile` is the Trading Networks My Enterprise partner profile. The
  profile structure is a superset of both `wm.tn.rec:ProfileSummary` and
  `wm.tn.rec:Profile`, with additional convenient and usable structures
  added for External IDs, Delivery Methods, and Extended Fields.

---

### tundra.tn.queue:branch

Invokes `TundraTN/tundra.tn:branch` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:chain`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:chain

Invokes `TundraTN/tundra.tn:chain` for each item in the given Trading
Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:chain`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:deliver

Invokes `TundraTN/tundra.tn:deliver` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:deliver`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:derive

Invokes `TundraTN/tundra.tn:derive` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:derive`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:disable

Disables the given Trading Networks queue for both queuing and delivery of
documents: no new documents can be added to the queue, nor will existing
queued documents be delivered.

When a queue is disabled or draining, delivery fails because Trading
Networks cannot place the delivery task in the queue. Trading Networks sets
the delivery task status to FAILED and logs a message to the activity log.

#### Inputs:

* `$queue` is the name of the queue to be disabled.

---

### tundra.tn.queue:drain

Drains the given Trading Networks queue: no new documents can be added to
the queue, but existing documents in the queue will still be delivered.

When a queue is disabled or draining, delivery fails because Trading
Networks cannot place the delivery task in the queue. Trading Networks sets
the delivery task status to FAILED and logs a message to the activity log.

#### Inputs:

* `$queue` is the name of the queue to be drained.

---

### tundra.tn.queue:each

Invokes the given bizdoc processing service for each item in the
given Trading Networks queue.

As the above implies, this service lets you use any normal bizdoc
processing service to process items in a Trading Networks delivery
queue.

#### Inputs:

* `queue` is the name of the Trading Networks queue from which tasks
  will be dequeued and processed.
* `$service` is the fully-qualified service name of the bizdoc
  processing service, which will be invoked for each task on the
  given queue. The service is required to implement the
  `WmTN/wm.tn.rec:ProcessingService` specification. Additionally,
  the queued task which is being processed will also be added to
  the input pipeline of `$service` as a variable named `$task` whose
  structure is as per `WmTN/wm.tn.rec:Task`, which allows `$service`
  to access properties on the task such as the current retry count.
* `$pipeline` is an optional input pipeline to be used when invoking
  the given `$service` bizdoc processing service. Defaults to using
  the pipeline itself as the input pipeline for `$service`, if not
  specified.
* `$task.age` is the optional age a task must be before it will be
  dequeued and processed, measured from its creation time. This
  argument can be used to defer the processing of tasks for the
  specified duration. If not specified, tasks are eligible to be
  dequeued and processed immediately upon creation.
* `$retry.limit` is the number of times a failed task will be
  retried. Defaults to the retry limit on the receiver's profile,
  or `0` (no retries) if the receiver's profile has no retry limit
  configured.
* `$retry.wait` is an optional time to wait between retries,
  specified as an XML duration string. Defaults to the retry wait
  on the receiver's profile, or a zero wait between retries if the
  receiver's profile has no retry wait configured.
* `$retry.factor` is an optional factor to use to increase the time
  to wait between each subsequent retry. The time to wait for a
  retry is calculated as:

      ($retry.factor ^ (retry count - 1)) * $retry.wait

  Defaults to the retry factor on the receiver's profile, or `1.0`
  if the receiver's profile has no retry factor configured.
* `$ordered?` is an optional boolean flag which when `true` processes
  tasks in strict ascending task creation order. Note that when
  processing tasks in creation order, if the oldest task fails no
  other tasks will be processed until all retries have been
  exhausted.

  When `false`, tasks will be ordered by the priority specified by
  the value of the Document Attribute `Message Priority` if it
  exists, where higher numeric values are treated as higher priority.
  If all documents have the same priority, or no priority is
  specified, tasks are generally processed in task creation order,
  however retries of older tasks do not halt processing of younger
  tasks.

  Defaults to `false` if not specified.
* `$suspend?` is an optional boolean flag which when `true` will
  automatically suspend the queue when all retries of a task are
  exhausted. At this point, the exhausted task's retry count is
  reset to `0`, the task is requeued to the front of the queue, and
  the queue is suspended, requiring manual intervention to resolve
  the queue processing error. Defaults to `false` if not specified.
* `$concurrency` is an optional number of threads to use when
  processing queue tasks. Defaults to `1` if not specified.

  If a `$concurrency > 1` is specified, a thread pool will be
  created with a size equal to the given value. These threads are
  not managed by the normal Integration Server thread pools, and
  therefore are not restricted by Integration Server thread pool
  settings. As such, please ensure that the sum of all the
  `$concurrency` values for all Trading Networks queues that specify
  a `$concurrency > 1`, plus the configured Integration Server
  thread pool maximum is supported by the amount of free memory
  available on your server:

      ((Q1 + .. + Qn) + IS thread pool max) * Java thread stack size < Server free memory

  If a `$concurrency <= 1` is specified, tasks will be processed
  sequentially on the main thread.
* `$thread.priority` is an optional priority used by the threads
  processing queued tasks. Defaults to [Thread.NORM_PRIORITY],
  if not specified.
* `$error.threshold` is an optional number of continuous task
  failures allowed before queue processing is successively backed
  off. If not specified, or set to `0`, queue processing will not
  back off regardless of the number of continuous task failures
  encountered.
* `$status.exhausted` is an optional user status to set on the bizdoc
  when all retries have been exhausted. Defaults to `EXHAUSTED`.
* `$status.silence?` is an optional boolean which when `true` will
  suppress updates to the bizdoc status. Defaults to `false`.
* `$daemonize?` is an optional boolean which when `true` daemonizes
  the threads used to process queued tasks, and the invocation of
  this service will continue (not exit) until either the queue is
  disabled or suspended, or the server is shutdown. Defaults to
  `false`.

#### Outputs:

* `queue` is the name of the Trading Networks queue from which tasks
  were dequeued and processed.
* `logMsg` is an optional message describing the processing of the
  queue. This output variable is not used by this service.

---

### tundra.tn.queue:enable

Enables the given Trading Networks queue for both queuing and delivery of
documents: new documents can be added to the queue, and queued documents
will be delivered. This is the normal state for a queue.

#### Inputs:

* `$queue` is the name of the queue to be enabled.

---

### tundra.tn.queue:get

Returns the properties for the Trading Networks delivery queue with
the given name.

#### Inputs:

* `$queue` is the name of the Trading Networks delivery queue whose
  properties are to be returned.

#### Outputs:

* `$queue.properties` is an `IData` document containing the properties
  of the Trading Networks delivery queue with the given name.
  * `name` is the unique name of the queue.
  * `type` is the type of queue, and is one of the following values:
    * `private`
    * `public`
  * `status` is the current status of the queue, and is one of the
    following values:
    * `disabled`
    * `draining`
    * `enabled`
    * `suspended`
  * `length` is the number of tasks with a status of `QUEUED` or
    `DELIVERING` currently in the queue.
  * `queue` is the `com.wm.app.tn.delivery.DeliveryQueue` object
    which represents this queue.

---

### tundra.tn.queue:interrupt

Interrupts and therefore shuts down the processing supervisor
associated with the given queue.

#### Inputs:

* `$queue` is the name of the queue to be interrupted.

---

### tundra.tn.queue:length

Returns the number of tasks with a status of `QUEUED` or `DELIVERING`
in the given Trading Networks delivery queue.

#### Inputs:

* `$queue` is the name of the queue whose length is to be returned.

#### Outputs:

* `$length` is the number of tasks with with a status of `QUEUED` or
  `DELIVERING` in the given queue.

---

### tundra.tn.queue:list

Returns a list of all registered Trading Networks delivery queues.

#### Outputs:

* `$queues` is an `IData[]` document list containing one item per
  registered Trading Networks delivery queue.
  * `name` is the unique name of the queue.
  * `type` is the type of queue, and is one of the following values:
    * `private`
    * `public`
  * `status` is the current status of the queue, and is one of the
    following values:
    * `disabled`
    * `draining`
    * `enabled`
    * `suspended`
  * `length` is the number of tasks with a status of `QUEUED` or
    `DELIVERING` currently in the queue.
  * `queue` is the `com.wm.app.tn.delivery.DeliveryQueue` object
    which represents this queue.

---

### tundra.tn.queue:process

Invokes `TundraTN/tundra.tn:process` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:process`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:publish

Invokes `TundraTN/tundra.tn:publish` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:publish`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:reroute

Invokes `TundraTN/tundra.tn:reroute` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:reroute`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:split

Invokes `TundraTN/tundra.tn:split` for each item in the given Trading
Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:split`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:status

Invokes `TundraTN/tundra.tn:status` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:status`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.queue:suspend

Suspends the delivery of documents in the given Trading Networks queue. New
documents can be added to the queue, but no documents will be delivered by
the queue.

Queues can be suspended to minimise delivery errors when the receiver is
temporarily unable to receive documents.

#### Inputs:

* `$queue` is the name of the queue to be suspended.

---

### tundra.tn.queue:translate

Invokes `TundraTN/tundra.tn:translate` for each item in the given
Trading Networks queue.

For details on inputs and outputs, refer to the following service
documentation:
* `TundraTN/tundra.tn:translate`
* `TundraTN/tundra.tn.queue:each`

---

### tundra.tn.reliable:amend

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:amend`.

Refer to `tundra.tn:amend` for further details.

---

### tundra.tn.reliable:branch

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:branch`.

Refer to `tundra.tn:branch` for further details.

---

### tundra.tn.reliable:chain

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:chain`.

Refer to `tundra.tn:chain` for further details.

---

### tundra.tn.reliable:deliver

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:deliver`.

Refer to `tundra.tn:deliver` for further details.

---

### tundra.tn.reliable:derive

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:derive`.

Refer to `tundra.tn:derive` for further details.

---

### tundra.tn.reliable:process

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:process`.

Refer to `tundra.tn:process` for further details.

---

### tundra.tn.reliable:publish

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:publish`.

Refer to `tundra.tn:publish` for further details.

---

### tundra.tn.reliable:reroute

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:reroute`.

Refer to `tundra.tn:reroute` for further details.

---

### tundra.tn.reliable:split

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:split`.

Refer to `tundra.tn:split` for further details.

---

### tundra.tn.reliable:translate

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:translate`.

Refer to `tundra.tn:translate` for further details.

---

### tundra.tn.rule:disable

Disable the given Trading Networks processing rule.

#### Inputs:

* `$rule` is the Trading Networks processing rule to be disabled, and
  can be specified as a subset containing at least the `RuleID` or
  `RuleName`.

---

### tundra.tn.rule:enable

Enable the given Trading Networks processing rule.

#### Inputs:

* `$rule` is the Trading Networks processing rule to be enabled, and
  can be specified as a subset containing at least the `RuleID` or
  `RuleName`.

---

### tundra.tn.rule:execute

Processes a Trading Networks document using the specified processing
rule.

#### Inputs:

* `$bizdoc` is the Trading Networks document (bizdoc) to be processed,
  and can be specified as a subset containing at least the `InternalID`
  field.
* `$rule` is the Trading Networks processing rule to be used when
  processing the given `$bizdoc`, and can be specified as a subset
  containing at least the `RuleID` or `RuleName`.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

#### Outputs:

* `$bizdoc` is the Trading Networks document that was processed.
* `$sender` is the Trading Networks profile of the sender of the
  document.
* `$receiver` is the Trading Networks profile of the receiver of the
  document.
* `$rule` is the Trading Networks processing rule used to process
  `$bizdoc`.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

---

### tundra.tn.rule:get

Returns the Trading Networks processing rule associated with the
given identity or name.

#### Inputs:

* `$rule.id` is the internal RuleID for the rule to be returned.
* `$rule.name` is the internal RuleName for the rule to be returned.

#### Outputs:

* `$rule` is the Trading Networks processing rule associated with the
  given identity or name, if it exists.

---

### tundra.tn.rule:list

Returns the list of all Trading Networks processing rules currently
defined.

#### Outputs:

* `$rule.list` is the list of all Trading Networks processing rules.
* `$rule.list.length` is the number of items in the returned `$rule.list`.

---

### tundra.tn.rule:match

Returns the first Trading Networks processing rule whose criteria
matches the given document.

#### Inputs:

* `$bizdoc` is the Trading Networks document (bizdoc), and can be
  specified as a subset containing at least the `InternalID` field.
* `TN_parms` contains the routing hints used to route the document in
  Trading Networks.

#### Outputs:

* `$rule` is the first Trading Networks processing rule whose criteria
  matches the given `$bizdoc`.

---

### tundra.tn.rule:normalize

Normalizes the given Trading Networks processing rule. When the given
rule is a subset (only `RuleID` or `RuleName` is required), the full
rule will be returned.

Throws an exception if the rule does not exist.

#### Inputs:

* $rule is the Trading Networks processing rule to be normalized, and
  can be specified as a subset containing at least the RuleID or
  RuleName.

#### Outputs:

* $rule is the normalized Trading Networks processing rule if it
  exists.

---

### tundra.tn.schema:derivative

This schema describes the structure for derivative rules used by
`TundraTN/tundra.tn:derive`.

---

### tundra.tn.schema.derivative:filter

Filter services used by `TundraTN/tundra.tn:derive` must implement this
specification.

#### Inputs:

* `$bizdoc` is the Trading Networks document being processed.
* `$sender` is the Trading Networks partner profile associated with the
  sender of the bizdoc.
* `$receiver` is the Trading Networks partner profile associated with the
  receiver of the bizdoc.
* `$document` is the parsed bizdoc content for processing.
* `$schema` is the name of the Integration Server document reference or flat
  file schema used to parse the content into an `IData` structure.
* `$derivative` is the derivative rule to be filtered. The filter service is
  allowed to edit the `$derivative` rule, so that it may, for example,
  disable the rule by setting `$derivative/enabled?` to `false`, or specify
  a different sender and/or receiver.

#### Outputs:

* `$derivative` is the derivative rule after filtering. The filter service
  is required to return the rule whether it makes changes to it or not.

---

### tundra.tn.schema.document.attribute.datetime:transformer

An improved version of the `WmTN/wm.tn.rec:DateAttributeTransformService`
specification with type constraints provided for the input and output
arguments.

#### Inputs:

* `values` is the list of extracted values to be transformed.
* `isArray` is a boolean indicating if the values argument contains multiple
  items.
* `arg` is an optional argument that can be used to influence the
  transformation.

#### Outputs:

* `newValues` is a list of transformed items from the input `values` list.

---

### tundra.tn.schema.attribute.number:transformer

An improved version of the WmTN/wm.tn.rec:NumberAttributeTransformService
specification with type constraints provided for the input and output
arguments.

#### Inputs:

* `values` is the list of extracted values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is an optional argument that can be used to influence the
  transformation.

#### Outputs:

* `newValues` is a list of transformed items from the input `values` list.

---

### tundra.tn.schema.document.attribute.string:transformer

An improved version of the `WmTN/wm.tn.rec:StringAttributeTransformService`
specification with type constraints provided for the input and output
arguments.

#### Inputs:

* `values` is the list of extracted values to be transformed.
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is an optional argument that can be used to influence the
  transformation.

#### Outputs:

* `newValues` is a list of transformed items from the input `values` list.

---

### tundra.tn.schema:processor

Processing services called by `TundraTN/tundra.tn:process` can implement this
specification.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be
  processed.
* `$sender` is the Trading Networks partner profile associated with the
  sender of the bizdoc.
* `$receiver` is the Trading Networks partner profile associated with the
  receiver of the bizdoc.
* `$document` is the parsed bizdoc content for processing. This is the
  default name for this input parameter. The actual name of the parameter
  can be changed using the `TundraTN/tundra.tn:process` `$service.input`
  parameter. This input is only provided when the `TundraTN/tundra.tn:process`
  `$parse?` parameter is `true`.
* `$content` is the raw bizdoc content as an input stream for processing.
  This is the default name for this input parameter. The actual name of
  the parameter can be changed using the `TundraTN/tundra.tn:process`
  `$service.input` parameter. This input is only provided when the
  `TundraTN/tundra.tn:process` `$parse?` parameter is `false`.
* `$content.type` is the MIME media type that describes the format of the
  bizdoc content.
* `$namespace` is the list of XML namespace prefixes and URIs declared on
  the associated document type and used when parsing the content.
* `$schema` is the name of the Integration Server document reference or flat
  file schema used to parse the content into an `IData` structure.
* `$schema.type` describes whether the schema used to parse the content was
  a Flat File or XML schema.

#### Outputs:

* `$summary` is an optional diagnostic summary message to be automatically
  logged in the Trading Networks Activity Log.
* `$message` is an optional diagnostic detailed message to be automatically
  logged in the Trading Networks Activity Log.
* `$status.done` is an optional user status to be set on the document after
  processing has completed successfully.

---

### tundra.tn.schema:profile

A superset of `wm.tn.rec:Profile` and `wm.tn.rec:ProfileSummary`, with
some additional useful and more developer friendly inclusions such
as `DefaultID`, `ExternalID`, `ExtendedFields`, and `DeliveryMethods`.

---

### tundra.tn.schema:splitter

Splitting services used by `TundraTN/tundra.tn:split` can implement this
specification.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be
  split.
* `$sender` is the Trading Networks partner profile associated with the
  sender of the bizdoc.
* `$receiver` is the Trading Networks partner profile associated with the
  receiver of the bizdoc.
* `$document` is the parsed bizdoc content for splitting. This is the
  default name for this input parameter. The actual name of the parameter
  can be changed using the `TundraTN/tundra.tn:split` `$service.input`
  parameter, which allows the use of `TundraTN/tundra.tn:split` with
  existing mapping services.
* `$schema` is the name of the Integration Server document reference or flat
  file schema used to parse the content into an `IData` structure.

#### Outputs:

* `$documents` is the split list of content with which each item of the
  list will be routed back to Trading Networks as individual new
  documents. This is the default name for this output parameter. The
  actual name of the parameter can be changed using the
  `TundraTN/tundra.tn:split` `$service.output` parameter, which allows the
  use of `TundraTN/tundra.tn:split` with existing mapping services.
* `$schemas` is the list of Integration Server document references or flat
  file schemas that each `$documents` item conforms to. The length of
  `$schemas` must match the length of `$documents`, and `$schema[n]` is used to
  serialize `$documents[n]` to an input stream for routing to Trading
  Networks.
* `TN_parms` provides routing hints for Trading Networks. It can be
  specified as either a singleton `IData` or an `IData` list. If specified as
  a singleton, it will be used when routing every item in the `$documents`
  list. If specified as a list, the length of `TN_parms` must match the
  length of `$documents`, and `TN_parms[n]` will be used when routing
  `$documents[n]` to Trading Networks.

---

### tundra.tn.schema:translator

Translation services invoked by `TundraTN/tundra.tn:translate` can
implement this specification.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be
  translated.
* `$sender` is the Trading Networks partner profile associated with the
  sender of the bizdoc.
* `$receiver` is the Trading Networks partner profile associated with
  the receiver of the bizdoc.
* `$document` is the parsed bizdoc content for translation. This is the
  default name for this input parameter. The actual name of the
  parameter can be changed using the `TundraTN/tundra.tn:translate`
  `$service.input` parameter, which allows the use of `TundraTN/
  tundra.tn:translate` with existing mapping services.
* `$schema` is the name of the Integration Server document reference
  or flat file schema used to parse the content into an `IData`
  structure.

#### Outputs:

* `$translation` is the translated content which will be routed back to
  Trading Networks as a new document. This is the default name for
  this output parameter. The actual name of the parameter can be
  changed using the `TundraTN/tundra.tn:translate` `$service.output`
  parameter, which allows the use of `TundraTN/tundra.tn:translate`
  with existing mapping services.
* `$attributes` is an optional `IData` document containing attribute
  values to be set on the resulting translated document. Atributes
  specified here will override values extracted from the document.
* `TN_parms` is an optional set of routing hints used when routing the
  translated document to Trading Networks.

---

### tundra.tn.task:restart

Restarts the given task. This service, unlike `wm.tn.task:restartTask`,
does not require the task status to be `STOPPED` or `FAILED`, and will
restart the given task regardless of its status.

#### Inputs:

* `$task` is the Trading Networks task to be restarted. Only the `TaskId`
  of the task must be specified, with the remainder of the
  `WmTN/wm.tn.rec:Task` structure purely optional.

[/dev/null]: <http://en.wikipedia.org/wiki//dev/null>
[Base64]: <http://en.wikipedia.org/wiki/Base64>
[CSV]: <http://en.wikipedia.org/wiki/Comma-separated_values>
[default charset]: <http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html#defaultCharset()>
[HTML]: <http://en.wikipedia.org/wiki/HTML>
[HTTP]: <http://tools.ietf.org/search/rfc2616>
[ISO8601]: <http://en.wikipedia.org/wiki/ISO_8601>
[JAR]: <http://en.wikipedia.org/wiki/JAR_(file_format)>
[java.text.SimpleDateFormat]: <http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html>
[java.util.Date]: <http://docs.oracle.com/javase/6/docs/api/java/util/Date.html>
[JSON]: <http://www.json.org>
[LICENSE]: <https://github.com/Permafrost/TundraTN/blob/master/LICENSE>
[org.w3c.dom.Node]: <http://docs.oracle.com/javase/6/docs/api/org/w3c/dom/Node.html>
[regular expression pattern]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html>
[releases]: <https://github.com/Permafrost/TundraTN/releases>
[RFC 4122]: <http://www.ietf.org/rfc/rfc4122.txt>
[Thread.NORM_PRIORITY]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Thread.html#NORM_PRIORITY>
[TSV]: <http://en.wikipedia.org/wiki/Tab-separated_values>
[Tundra]: <https://github.com/Permafrost/Tundra>
[Tundra.java]: <https://github.com/Permafrost/Tundra.java>
[TundraTN]: <https://github.com/Permafrost/TundraTN>
[TundraTN.java]: <https://github.com/Permafrost/TundraTN.java>
[URI]: <http://www.w3.org/Addressing/>
[URL-encoded]: <http://en.wikipedia.org/wiki/Percent-encoding>
[UTF-8]: <http://en.wikipedia.org/wiki/UTF-8>
[ULID]: <https://github.com/alizain/ulid>
[UUID]: <http://docs.oracle.com/javase/6/docs/api/java/util/UUID.html>
[webMethods Integration Server]: <http://www.softwareag.com/corporate/products/wm/integration/products/ai/overview/default.asp>
[webMethods Trading Networks]: <http://www.softwareag.com/corporate/products/wm/integration/products/b2b/overview/default.asp>
[XML]: <http://www.w3.org/XML/>
[YAML]: <http://www.yaml.org>
