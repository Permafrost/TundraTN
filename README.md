# TundraTN ❄

A package of cool services for [webMethods Trading Networks] 7.1 and
higher.

## Related

See also [Tundra](https://github.com/Permafrost/Tundra), a package of cool
services for [webMethods Integration Server] 7.1 and higher.

## Dependencies

[TundraTN] is compiled for Java 1.6, and is dependent on the following
[webMethods Integration Server] packages:

* [Tundra]
* WmTN

[TundraTN] is also dependent on the following open source Java libraries,
which provide the underlying implementation for the Java services, and are
included in the [TundraTN] package as [JAR] files in the following locations:

* `./code/jars/Tundra.jar` - http://github.com/Permafrost/Tundra.java
* `./code/jars/TundraTN.jar` - http://github.com/Permafrost/TundraTN.java

## Installation

You have two choices for installing [TundraTN]:

* git
* zip

If you are comfortable using git, I recommend this method as you can then
easily switch between package versions using git checkout and download new
versions using git fetch.

### Using Git

To install with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your Integration
  Server.
* Your Integration Server has internet access to https://github.com (for
  cloning the repository).
* The dependent packages listed above are installed and enabled on your
  Integration Server.
* You have identified what version of [TundraTN] you'd like to install by
  referring to the available [releases].

From your Integration Server installation:

```sh
$ cd ./packages/
$ git clone https://github.com/Permafrost/TundraTN.git
$ cd ./TundraTN/
$ git checkout v<n.n.n> # where <n.n.n> is the required version
```

Then activate and enable the [TundraTN] package from the package management
web page on your Integration Server web administration site.

### Using Zip

1. Download a pre-built zip of the desired version of the package from the
   available [releases].
2. Copy the `TundraTN-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the [TundraTN] package release `TundraTN-vn.n.n.zip`
   from the package management web page on your Integration Server's web
   administration site.

## Upgrading

When upgrading you have to choose the same method used to originally install
the package. Unfortunately, if git wasn't used to install the package then
you can't use git to upgrade it either. However, if you want to switch to
using git to manage the package, delete the installed package and start over
using the git method for installation.

### Using Git

To upgrade with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your Integration
  Server.
* Your Integration Server has internet access to https://github.com (for
  fetching updates from the repository).
* The dependent packages listed above are installed and enabled on your
  Integration Server.
* You have identified what version of [TundraTN] you'd like to upgrade to by
  referring to the available [releases].
* You originally installed [TundraTN] using the git method described above.

From your Integration Server installation:

```sh
$ cd ./packages/TundraTN/
$ git fetch
$ git checkout v<n.n.n> # where <n.n.n> is the desired updated version
```

Then reload the [TundraTN] package from the package management web page on
your Integration Server web administration site.

### Using Zip

1. Download a pre-built zip of the desired updated version of the package
   from the available [releases].
2. Copy the `TundraTN-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the updated [TundraTN] package release
   `TundraTN-vn.n.n.zip` from the package management web page on your
   Integration Server's web administration site.

## Conventions

1. Most input and output pipeline arguments are prefixed with '$' as a poor-
   man's scoping mechanism, since typically user-space variables are
   unprefixed. However some Trading Networks-specific arguments, such as
   `TN_parms`, remain unprefixed.
2. All boolean arguments are suffixed with a '?'.
3. Single-word argument names are preferred. Where multiple words are
   necessary, words are separated with a '.'.
4. Service namespace is kept flat. Namespace folders are usually nouns.
   Service names are usually verbs, indicating the action performed on the noun (parent folder).
5. Services are written in Java where necessary, such as when dealing
   directly with the Trading Networks Java API. Java services are *almost*
   always overloaded by a backing method in the shared source, which
   provides the actual implementation. This way, backing methods can be used
   by other backing methods directly, without needing to deal with the
   IData/IDataCursor/IDataUtil nastiness that a fronting Java service is
   usually required to deal with.
6. Services declare all inputs and outputs, always explicitly marked as
   optional or required, use constrained types where possible, and enable
   input and output pipeline validation, which minimises developer surprise.
7. Services often declare their inputs as optional, and either apply an
   appropriate default value, or take no action and return no output
   (whatever is more appropriate), to minimise the need for existence
   checking in flow map steps.
8. All private elements are kept in the `tundra.tn.support` folder. All other
   elements comprise the public API of the package. As the private elements
   do not contribute to the public API, they are liable to change at any
   time. **Enter at your own risk.**

## Services

### tundra.tn:amend

Edits the given XML or flat file bizdoc content part with the list of {key,
value} pairs specified in `$amendments`.

The keys in `$amendments` can be fully-qualified, for example `a/b/c[0]`, and
the values can include percent-delimited variable substitution strings which
will be substituted prior to being inserted in `$document`.

The bizdoc user status is first updated to 'AMENDING', then the content part
identified by `$part.input` (or the default content part if not specified) is
parsed to an IData document using the named $schema (or the schema
configured on the TN document type if not specified), the amendments are
applied via the `$amendments` {key, value} pairs, the amended IData document
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
as an IData document called `$document`, and the parsing schema/document
blueprint used by the parser as a string called `$schema`.

A null condition will always evaluate to true, and can therefore be used as
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
* `$pipeline` is an optional IData document containing additional arbitrary
  input arguments for `$service` (or `WmPublic/pub.flatFile:convertToValues`,
  `WmPublic/pub.xml:xmlStringToXMLNode`, or `WmPublic/pub.xml:xmlNodeToDocument`
  via `Tundra/tundra.tn.document:parse`). Fully-qualified names will be
  handled correctly, for example an argument named `example/item[0]` will
  be converted to an IData document named `example` containing a String
  list named `item` with it's first value set accordingly.
* `$service.input` is an optional name used when adding either the bizdoc
  content to the input pipeline of the call to `$service`. Defaults to
  `$document` when `$parse?` is true, and `$content` when `$parse` is false.
* `$status.done` is an optional user status to use for the bizdoc when
  processing has completed successfully. Defaults to DONE.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$parse?` is an optional boolean flag which when true will parse the
  bizdoc content part to an IData document which is added to the input
  pipeline of the call to `$service`, and when false will instead add the
  bizdoc content part as an input stream to the input pipeline. Defaults
  to true.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$part` is an optional name identifying the bizdoc content part to be
  parsed and added to the input pipeline of the call to `$service`. Defaults
  to the default content part (xmldata for XML documents, ffdata for Flat
  File documents). Not used if `$parse?` is false.
* `$encoding` optional character encoding to be used when reading the bizdoc
  content part bytes. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$strict` is an optional set of boolean flags which when true abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition`
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:deliver

Delivers Trading Networks document (bizdoc) content to the given destination
URI.

Variable substitution is performed on all variables specified in the
`$pipeline` document, and the `$destination` URI, allowing for dynamic
generation of any of these values. Also, if `$service` is specified, it will
be called prior to variable substitution and thus can be used to populate
the pipeline with variables to be used by the substitution.

When using variable substitution in a URI, because it uses `%` characters
to delineate substitution statements but it is special character in URIs
(used for the escape sequence of [URL-encoded] characters), it needs to be
[URL-encoded] as `%25`. For example, to include the variable substitution
statement `%$bizdoc/DocType/TypeName%` in a mailto URI, the `%` characters
need to be encoded as the `%25` escape sequence as follows:

    mailto:john.doe@example.com?subject=%25$bizdoc/DocType/TypeName%25

This service leverages the service `Tundra/tundra.content:deliver`. Therefore,
additional delivery protocols can be implemented by creating a service named
for the URI scheme in the folder `tundra.content.deliver`.  Services in this
folder should implement the `tundra.schema.content.deliver:handler`
specification.

This service is designed to be called directly from a Trading Networks
bizdoc processing rule.

#### Inputs:

* `bizdoc` is the Trading Networks document whose content is to be delivered.
* `$destination` is either a URI, or a named destination (such as Receiver's
  Preferred Protocol), to which the bizdoc content will be delivered. If not
  specified, no delivery will be attempted. Supports the following delivery
  protocols (URI schemes):
  * `file`: writes the given content to the file specified by the
    destination URI. The following additional options can be provided via
    the `$pipeline` document:
    * `$mode`: append / write
  * `ftp`: uploads the given content to the FTP server, directory and file
    specified by the destination URI. An example FTP URI is as follows:

        ftp://aladdin:opensesame@example.com:21/path/file?append=true&active=true&ascii=true

    The following additional options can be provided via the `$pipeline`
    document:
    * `$user` is the username used to log in to the FTP server. Defaults to
      the username specified in the authority section of the URI, if not
      specified.
    * `$password` is the password used to log in to the FTP server. Defaults
      to the password specified in the authority section of the URI, if
      not specified.
    * `$active` is a boolean which when true indicates that the connection
      to the FTP server should be in active mode. Defaults to false
      (passive mode), if not specified.
    * `$append` is a boolean which when true will append the given content
      to the file, rather than overwrite it, if the file already exists.
      Defaults to false (overwriting), if not specified.
    * `$ascii` is a boolean which when true indicates that the file transfer
      should be made in ascii mode. Defaults to false (binary mode), if
      not specified.
    * `$timeout` is an optional XML duration string which specifies how long
      the client waits for a response from the server before timing out
      and terminating the request with an error. Defaults to PT60S, if not
      specified.
  * `http`: transmits the given content to the destination URI. The
    following additional options can be provided via the `$pipeline` document:
    * `$method`: get / put / post / delete / head / trace / options
    * `$headers/*`: additional HTTP headers as required
    * `$authority/user`: the username to log on to the remote web server
    * `$authority/password`: the password to log on to the remote web server
    * `$timeout` is an optional XML duration string which specifies how long
      the client waits for a response from the server before timing out
      and terminating the request with an error. Defaults to PT60S, if not
      specified.
  * `https`: refer to http
  * `jms`: sends the given content as a [JMS] [javax.jms.BytesMessage] to
    the specified [JMS] alias and queue or topic. The following additional
    settings can be specified:
    * `$headers/*`: additional properties to be added to the [JMS] message
      header, which can be used for filtering by [JMS] subscribers. Note
      that all extracted bizdoc attributes are automatically included in
      the JMS message header.

    The following example will deliver the given content as a [JMS] bytes
    message to the JMS alias `DEFAULT_IS_JMS_CONNECTION`, [JMS] topic
    `JMS::Temporary::Topic`, with a time to live of 1 day, and with the
    default priority of 4:

        jms://DEFAULT_IS_JMS_CONNECTION?topic=JMS::Temporary::Topic&lifetime=P1D

    The following example will deliver the given content as a [JMS] bytes
    message to the [JMS] alias `DEFAULT_IS_JMS_CONNECTION`, [JMS] queue
    `JMS::Temporary::Queue`, with no expiry, and with the specified priority
    of 1:

        jms://DEFAULT_IS_JMS_CONNECTION?queue=JMS::Temporary::Queue&priority=1

  * `mailto`: sends an email with the given content attached. An example
    mailto URI is as follows:

        mailto:bob@example.com?cc=jane@example.com&subject=Example&body=Example&attachment=message.xml

    The following additional override options can be provided via the
    `$pipeline` document:
    * `$attachment`: the attached file's name
    * `$from`: email address to send the email from
    * `$subject`: the subject line text
    * `$body`: the main text of the email
    * `$smtp`: an SMTP URI specifying the SMTP server to use (for example,
      `smtp://user:password@host:port`), defaults to the SMTP server
      configured in the Integration Server setting `watt.server.smtpServer`
  * `sap+idoc`: sends an IDoc XML message to an SAP system. Both opaque
    and non-opaque URIs are allowed: opaque URIs are useful if the SAP
    Adapter alias contains characters not permitted in a normal domain
    name, such as underscores.

    An example opaque `sap+idoc` URI is as follows, where `sap_r3` is the
    SAP Adapter alias name, and the user and password are provided as
    query string parameters:

        sap+idoc:sap_r3?user=aladdin&password=opensesame&client=200&language=en&queue=xyz

    An example non-opaque `sap+idoc` URI is as follows, where `sappr3` is the
    SAP Adapter alias name, and the user and password are provided in the
    authority section of the URI:

        sap+idoc://aladdin:opensesame@sappr3?client=200&languange=en&queue=xyz

    The following additional override options can be provided via the
    `$pipeline` document, and if specified will overrided the relevant
    parts of the destination URI:
    * `$user` is the username used for the SAP session. Defaults to the
      SAP Adapter alias username, if not specified.
    * `$password` is the password used for the SAP session. Defaults to
      the SAP Adapter alias password, if not specified.
    * `$client` is the SAP client used for the SAP session. Defaults to
      the SAP Adapter alias client, if not specified.
    * `$language` is the language used for the SAP session. Defaults to
      the SAP Adapter alias language, if not specified.
    * `$queue` is the optional name of the SAP system inbound queue,
      required when using queued remote function calls (qRFC).
* `$service` is an optional fully-qualified service name which, when
  specified, will be invoked prior to delivery, thus allowing a service to
  perform processing to influence the delivery (such as populating the
  pipeline with configuration variables at runtime).
* `$catch` is an optional fully-qualified service name which, when
  specified, will be invoked if an exception is thrown while attempting
  delivery. The input pipeline will include the variables described in
  the specification `Tundra/tundra.schema.exception:handler`, as per a
  normal catch service invoked by `Tundra/tundra.service:ensure`. Defaults
  to `TundraTN/tundra.tn.exception:handle`, the standard TundraTN exception
  handler, when not specified.
* `$finally` is an optional fully-qualified service name which, when
  specified, will be invoked after delivery, and whether or not an
  exception is encountered during delivery.
* `$pipeline` is an optional IData document containing arbitrary variables
  which can be used to influence the delivery. See the `$destination`
  description above for transport-specific options which can be provided
  via this IData document.
* `$status.done` is an optional user status to use for the bizdoc when
  delivery has completed successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  delivery destination is provided. Defaults to IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$substitute?` is an optional boolean flag which when true will perform
  variable substitution on all variables in the pipeline (after invoking
  `$service`, if applicable), which allows variables to be set dynamically
  using other values in the pipeline (or values returned by `$service`, if
  applicable). Defaults to true.
* `$part` is an optional name of the bizdoc content part to be delivered.
  Defaults to the default content part when not specified (xmldata for XML
  document types, ffdata for Flat File document types).
* `$parse?` is an optional boolean flag which when true parses the bizdoc
  content part identified by `$part` using the parsing schema configured on
  the Trading Networks document type, prior to both invoking `$service`, if
  specified, and content delivery. The parsed document content can then be
  used in conjunction with variable substitution for influencing the
  delivery URI based on the content of the document. Defaults to false.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (bizdoc, sender, and receiver)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification; when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$encoding` is an optional character set to use when to encode text
  content for delivery. Defaults to [UTF-8].
* `$strict` is an optional set of boolean flags that control 'strict' mode
  processing of bizdocs: if any error classes are set to 'true' and the
  bizdoc contains errors for those classes, the bizdoc will not be
  processed; instead an exception will be thrown and handled by the
  $catch service.

  For example, if you have enabled duplicate document checking on the
  Trading Networks document type and do not wish to deliver duplicates,
  set the `$strict/Saving` error class to 'true' and duplicate documents
  will not be delivered, and will instead have their user status set to
  'ABORTED' (when using the standard `$catch` service).

  The following flags are supported, and all default to true if not
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
* `$pipeline` is an optional IData document containing arbitrary variables
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
    * `condition` is an optional `Tundra/tundra.condition:evaluate`
      conditional statement can also be specified, which is evaluated
      against the pipeline containing `$bizdoc`, `$sender`, `$receiver`, and
      `$document` (the parsed bizdoc content), and only if the condition
      evaluates to true will the associated amended value be applied. If
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
      evaluates to true will the attribute be added to the derived bizdoc.
      If not specified, the attribute will always be added to the derived
      bizdoc.
  * `TN_parms` is an optional IData document containing routing hints used
    when routing the derivative bizdoc.
  * `force?` is an optional boolean flag: when true a new derivative will
    always be created even if an existing derivative for the same sender
    and receiver already exists; when false a new derivative will only
    be created if there is no existing derivative with the same sender
    and receiver. Defaults to false, if not specified.
  * `enabled?` is an optional boolean flag, when true this derivative rule
    is active, when false, this derivative rule is inactive and ignored.
    Defaults to true when not specified.
* `$status.done` is an optional user status to use for the bizdoc when
  derivatives have been created successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  derivatives are created due to filtering. Defaults to IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (bizdoc, sender, and receiver)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$part` is the optional name of the bizdoc content part to be copied to
  the resulting derivatives. Defaults to the default content part when not
  specified (xmldata for XML document types, ffdata for Flat File document
  types).
* `$strict` is an optional set of boolean flags which when true abort the
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
    only if the condition evaluates to true will the document be enqueued
    to this queue. If not specified, the document will always be enqueued
    to this queue.
  * `force?` is an optional boolean flag: when true the document will always
    be enqueued even if an existing task for the same queue already exists;
    when false the document will only be enqueued if there is no existing
    task for the same queue. Defaults to false, if not specified.
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
* `$strict` is an optional set of boolean flags which when true abort the
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
* `$context` is an optional IData document containing arbitrary
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
* `$pipeline` is an optional IData document containing additional arbitrary
  input arguments for `$service` (or `WmPublic/pub.flatFile:convertToValues`,
  `WmPublic/pub.xml:xmlStringToXMLNode`, or `WmPublic/pub.xml:xmlNodeToDocument` via
  `Tundra/tundra.tn.document:parse`). Fully-qualified names will be handled
  correctly, for example an argument named `example/item[0]` will be
  converted to an IData document named `example` containing a String list
  named `item` with it's first value set accordingly
* `$service.input` is an optional name used when adding either the bizdoc
  content to the input pipeline of the call to `$service`. Defaults to
  `$document` when `$parse?` is true, and `$content` when `$parse` is false.
* `$status.done` is an optional user status to use for the bizdoc when
  processing has completed successfully. If `$service` returns an output
  pipeline argument named `$status.done` or it changes the status itself,
  this takes precedence over any value specified here. Defaults to `DONE`.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$parse?` is an optional boolean flag which when true will parse the
  bizdoc content part to an IData document which is added to the input
  pipeline of the call to `$service`, and when false will instead add the
  bizdoc content part as an input stream to the input pipeline. Defaults
  to true.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:processor` specification, when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$part` is an optional name identifying the bizdoc content part to be
  parsed and added to the input pipeline of the call to `$service`. Defaults
  to the default content part (xmldata for XML documents, ffdata for Flat
  File documents). Not used if `$parse?` is false.
* `$encoding` optional character encoding to be used when reading the bizdoc
  content part bytes. If not specified, defaults to the character set
  specified in the MIME content type of the content part being parsed, or
  failing that [UTF-8].
* `$strict` is an optional set of boolean flags which when true abort the
  processing of the bizdoc when it contains any errors with the associated
  class.
  * `Recognition `
  * `Verification`
  * `Validation`
  * `Persistence`
  * `Saving`
  * `Routing`

---

### tundra.tn:receive

Receives arbitrary (XML or flat file) content and routes it
to Trading Networks. The content can be specified as a string,
byte array, java.io.InputStream, or org.w3c.dom.Node object.

This service is either intended to be invoked directly by
clients via HTTP or FTP, or it can be wrapped by another
service which specifies appropriate TN_parms to control the
routing of the content (ie. a one-line flat file gateway
service).

When invoked via HTTP, if the content is received successfully
an HTTP 200 OK response is returned, with a 'text/plain'
response body containing the resulting Trading Networks bizdoc
internal ID. If a security exception is encountered, an HTTP
403 Forbidden response is returned with a 'text/plain' response
body containing the exception message. If any other type of
exception is encountered, an HTTP 500 Internal Server Error
response is returned, with a 'text/plain' response body
containing the exception message.

When invoked via transports other than HTTP, for example FTP,
if the content is received successfully the service invocation
will succeed and a response body containing the resulting
Trading Networks bizdoc internal ID is returned. If a security
or any other exception is encountered, the service invocation
will fail by rethrowing the exception.

When invoked by a wrapping service, an exceptions encountered
will be thrown to the calling service. It is then the calling
service's responsibility to set an appropriate response for
the transport in question.

#### Inputs:

* `strict` is an optional boolean flag indicating whether 'strict'
  mode routing should be used for the received content. Defaults
  to true. To disable 'strict' mode when using HTTP, include
  strict=false as part of the query string of the receive URL:
  http://localhost:5555/invoke/tundra.tn/receive?strict=false.
* `TN_parms` is an optional set of routing hints for Trading
  Networks to use when routing the received content. If not
  specified by the caller, the following TN_parms are set
  automatically as follows:
  * `$contentType` is an optional mime media type of the received
    content. Defaults to the transport content type returned by
    `WmPublic/pub.flow:getTransportInfo`, or failing that 'text/xml'
    if a node object is present in the pipeline, or failing that
    'application/octet-stream'.
  * `$contentEncoding` is an optional character encoding used by
    the received content, for example UTF-16. Defaults to the
    encoding specified as the charset property in the content
    type, or failing that defaults to [UTF-8].
  * `$contentName` is an optional logical label or name for the
    received content, typically the filename for flat files.
    For HTTP transports, defaults to the the filename specified
    in the Content-Disposition header, or failing that the
    filename part of the request URI. For non-HTTP transports,
    defaults to the filename returned by
    `WmPublic/pub.flow:getTransportInfo`.
  * `$contentSchema` is an optional Integration Server document
    reference or flat file schema the received content conforms
    to. Defaults to the value of the `$schema` variable, if
    specified in the pipeline.
  * `$user` is the user that sent the received content. Defaults
    to the currently logged on user.
  * `SenderID` is an optional Trading Networks profile external ID
    which identifies the sender of the content. For flat files
    only, defaults to the currently logged on user.
  * `ReceiverID` is an optional Trading Networks profile external ID
    which identifies the receiver of the content. For flat files
    only, defaults to the value of the HTTP header 'X-Recipient',
    or failing that the required External ID value of the My
    Enterprise profile.
  * `DocumentID` is an optional ID used to identify the content in
    Trading Networks. For flat files only, defaults to the value
    of the HTTP header 'Message-ID', or failing that an [UUID] is
    automatically generated.
  * `GroupID` is an optional ID used to identify the group this
    content belongs to in Trading Networks. For flat files only,
    defaults to the value of `TN_parms/DocumentID`.

#### Outputs:

* `id` is the internal/native ID assigned by Trading Networks to
  the resulting bizdoc.

---

### tundra.tn:reject

Receives arbitrary (XML or flat file) content and then rejects it
by always returning an error to the client.

This service is intended to be invoked by clients via HTTP or FTP.

---

### tundra.tn:reroute

Reprocesses the given document in Trading Networks by rematching it against
the processing rule base and executing the first processing rule that
matches.

#### Inputs:

* `bizdoc` is the Trading Networks document to be rerouted.

#### Outputs:

* `bizdoc`
* `sender`
* `receiver`
* `TN_parms`

---

### tundra.tn:retrieve

Retrieves arbitrary content from the given `$source` URI, and routes it to
Trading Networks.

Additional retrieval protocols can be implemented by creating a service named
for the URI scheme in the folder `Tundra/tundra.content.retrieve`.  Services in
this folder must implement the `Tundra/tundra.schema.content.retrieve:handler`
specification.

#### Inputs:

* `$source` is a URI identifying the location from which content is to be
  retrieved. Supports the following retrieval protocols / URI schemes:
  * `file:` routes each file matching the given `$source` URI to Trading
    Networks. The file component of the URI can include wildcards
    or globs (such as `*.txt` or `*.j?r`) for matching multiple files at once.

    The following example would process all `*.txt` files in the specified
    directory:

        file:////server:port/directory/*.txt

    To ensure each file processed is not locked or being written to by
    another process, the file is first moved to a working directory. The
    name of this directory can be configured by adding a query string
    parameter called `working` to the URI, for example:

        file:////server:port/directory/*.txt?working=temp

    In this example, files are first moved to a subdirectory named `temp`.
    If not specified, the working directory defaults to a subdirectory
    named `working`.

    After successful processing, the file is then moved to an archive
    directory. The name of this directory can be configured by adding a
    query string parameter called `archive` to the URI, for example:

        file:////server:port/directory/*.txt?archive=backup

    In this example, files are moved to a subdirectory named `backup` after
    being successfully processed. If not specified, the archive directory
    defaults to a subdirectory named `archive`.

    Optionally, archived files older than a given age can be cleaned up
    automatically by the retrieve process by specifying a query string
    parameter called `purge` with an XML duration value representing the
    age an archived file must be before being purged, for example:

        file:////server:port/directory/*.txt?purge=P14D

    In this example, any files in the archive directory older than 14 days
    will be automatically deleted by the retrieve process. If the query
    string parameter `purge` is not specified, archived files will not be
    automatically cleaned up.
* `$limit` is an optional maximum number of content matches to be processed in
  a single execution. Defaults to 1000.
* `$strict?` is an optional boolean, which if true will abort routing/
  processing rule execution of the document if any any errors (such as
  validation errors) are encountered prior to processing. Defaults to false.
* `TN_parms` is an optional set of routing hints for Trading Networks to use
  when routing the retrieved content. The following TN_parms are set
  automatically, if no other value is provided:
  * `DocumentID` is set to a generated [UUID] unique for each retrieved content.
  * `GroupID` is set to a generated [UUID] and groups all content retrieved per
    invocation of this service.
  * `$contentType` is the MIME media type of the content.
  * `$contentEncoding` is the character set of the content.
  * `Content Name` is the name associated with the retrieved content, for example
    the file name for content retrieved from a file URI.
  * `Content Source` is the source location from which the content was retrieved.
  * `Content Archive` is the archive location to which the content was archived.
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
  must accept a single IData document and return an IData[] document list,
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
* `$pipeline` is an optional IData document containing arbitrary variables
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
  then `$service` should return a IData document list called `$namespaces`
  of the same length as the split document list, where `$namespaces[n]`
  will be used to serialize `$documents[n]`.
* `$schema.input` is the optional name of the Integration Server document
  reference or flat file schema to use to parse the bizdoc content into an
  IData structure. Defaults to the parsing schema specified on the
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
* `$validate.input?` is an optional boolean flag which when true will
  validate the input content against the given `$schema.input`, or the
  parsing schema specified on the associated Trading Networks document
  type if `$schema.input` is not specified, and throw an exception to abort
  processing if the content is invalid. Defaults to false.
* `$validate.output?` is an optional boolean flag which when true will
  validate each output content against the appropriate schema (`$schema.output`
  if specified, otherwise `$schemas[n]` returned by `$service`), and throw
  an exception to abort processing if the content is invalid. Defaults to
  false.
* `$status.done` is an optional user status to use for the bizdoc when
  it has been split successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  split documents are returned by `$service` and `$required` is false.
  Defaults to IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$required?` is an optional boolean indicating whether $service is
  required to return a one or more split documents. If true, and no
  documents are returned by `$service`, an exception will be thrown and
  handled by the `$catch` service. Defaults to false.
* `$relate?` is an optional boolean indicating whether the original document
  should be related to each of the individual split documents. Defaults to
  true.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:splitter` specification, when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$part` is the optional name of the bizdoc content part to be split.
  Defaults to the default content part when not specified (xmldata for XML
  document types, ffdata for Flat File document types).
* `$strict` is an optional set of boolean flags which when true abort the
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
* `$strict` is an optional set of boolean flags which when true abort the
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
  must accept a single IData document and return a single IData document,
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
* `$pipeline` is an optional IData document containing arbitrary variables
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
  IData structure. Defaults to the parsing schema specified on the
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
* `$validate.input?` is an optional boolean flag which when true will
  validate the input content against the given `$schema.input`, or the
  parsing schema specified on the associated Trading Networks document
  type if `$schema.input` is not specified, and throw an exception to abort
  processing if the content is invalid. Defaults to false.
* `$validate.output?` is an optional boolean flag which when true will
  validate the output content against the given `$schema.output`, or the
  value in `TN_parms/$schema` returned by `$service` if `$schema.output` is
  not specified, and throw an exception to abort processing if the content
  is invalid. Defaults to false.
* `$status.done` is an optional user status to use for the bizdoc when
  it has been translated successfully. Defaults to DONE.
* `$status.ignored` is an optional user status to use for the bizdoc when no
  translation is returned by `$service` and `$required` is false. Defaults to
  IGNORED.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.
* `$required?` is an optional boolean indicating whether `$service` is
  required to return a translated document. If true, and no translation is
  returned by `$service`, an exception will be thrown and handled by the
  `$catch` service. Defaults to false.
* `$prefix?` is an optional boolean flag indicating whether to use the '$'
  prefix on the standard input arguments (`bizdoc`, `sender`, and `receiver`)
  when calling `$service`. When true `$service` should implement the
  `TundraTN/tundra.tn.schema:translator` specification, when false `$service`
  should implement the `WmTN/wm.tn.rec:ProcessingService` specification.
  Defaults to true.
* `$part` is the optional name of the bizdoc content part to be translated.
  Defaults to the default content part when not specified (xmldata for XML
  document types, ffdata for Flat File document types).
* `$strict` is an optional set of boolean flags which when true abort the
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

* `$content` is string, byte array, input stream, or IData document
  content to be recognized by Trading Networks.
* `$namespace` is an optional list of namespace prefixes and the URIs
  they map to, used when `$content` is provided as an IData document
  to be serialized to [XML] with elements in one or more namespaces.
* `TN_parms` is an optional set of routing hints for Trading Networks
  to use when recognizing `$content`.

#### Outputs:

* `$content` is the recognized content returned as a byte array.
* `$bizdoc` is a new Trading Networks document (BizDocEnvelope)
  representing the recognized content, but not yet routed to Trading
  Networks for processing.
* `TN_parms` is the set of routing hints Trading Networks used when
  recognizing the given content.

---

### tundra.tn.content:route

Routes arbitrary content specified as a string, byte array, input stream, or
IData document to Trading Networks.

Correctly supports large documents, so any document considered large will be
routed as a large document in TN, unlike the `WmTN/wm.tn.doc.xml:routeXML`
service which does not correctly use tspace when routing large documents.

Also supports overriding the normally recognised document attributes, unlike
`WmTN/wm.tn.doc.xml:routeXML`, such as sender, receiver, document ID, group
ID, conversation ID, and document type with the value specified in TN_parms
for both XML and flat files documents.

#### Inputs:

* `$content` is string, byte array, input stream, or IData document content
  to be routed to Trading Networks.

  If `$content` is provided as an IData document, it will be serialized
  using an emitter determined in order of precedence by `$schema` and
  `$content.type`. If `$schema` is specified, the type of reference determines
  the emitter to use: a document reference will use the XML emitter, a
  flat file schema reference will use the Flat File emitter. If `$schema` is
  not specified, `$content.type` is used to determine the most appropriate
  emitter for the MIME media type in question. If neither `$schema`, nor
  `$content.type` are specified, `$content` is serialized as XML by default.

  Emitter implementions are as follows:
  * CSV: `Tundra/tundra.csv:emit`
  * Flat File: `WmFlatFile/pub.flatFile:convertToString`
  * JSON: `Tundra/tundra.json:emit`
  * Pipe separated values: `Tundra/tundra.csv:emit`
  * TSV: `Tundra/tundra.csv:emit`
  * XML: `WmPublic/pub.xml:documentToXMLString`
  * YAML: `Tundra/tundra.yaml:emit`
* `$content.type` is the MIME media type that describes the format of the
  given content:
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
* `$attributes` is a list of attributes to be set on the resulting
  Trading Networks document, and can be used to override the values
  of any extracted attributes, or to set the values of additional
  attributes not extracted by Trading Networks when it recognizes
  the type of document being routed. Attribute values that include
  percent-delimited variable substitutions will have their value
  resolved against the pipeline. If any variable substitution includes
  references to `$document`, the content will first be parsed and added
  to the pipeline as `$document` to support substituting attribute
  values based on the content being routed.
* `$namespace` is an optional list of namespace prefixes and the URIs
  they map to, used when `$content` is provided as an IData document
  to be serialized to [XML] with elements in one or more namespaces.
* `$encoding` is an optional character set to use when encoding the
  resulting text data to a byte array or input stream. Defaults to [UTF-8].
* `$schema` is the fully-qualified name of the parsing schema to use to
  serialize `$content` when provided as an IData document to [XML] or
  Flat File content, and can have the following values:
  * For [XML] content, specify the fully-qualified name of the document
    reference that defines the [XML] format.
  * For Flat File content specify the fully-qualified name of the flat
    file schema that defines the Flat File format.

  Defaults to serializing `$content` as [XML], if neither `$content.type` nor
  `$schema` are specified.
* `TN_parms` is an optional set of routing hints for Trading Networks to use
  when routing `$content`. If specified, the following values will overwrite
  the normal bizdoc recognised values, allowing for sender, receiver,
  document ID, group ID, conversation ID, and document type to be forced
  to have the specified value (even for XML document types):
  * `TN_parms/SenderID`
  * `TN_parms/ReceiverID`
  * `TN_parms/DocumentID`
  * `TN_parms/DoctypeID`
  * `TN_parms/DoctypeName`
  * `TN_parms/GroupID`
  * `TN_parms/ConversationID`
* `$strict?` is an optional boolean, which if true will abort routing/
  processing rule execution of the document if any errors (such as
  validation errors) are encountered prior to processing, and result in an
  exception being thrown. Defaults to true.

#### Outputs:

* `$bizdoc` is the resulting Trading Networks document that was routed.
* `$sender` is the Trading Networks profile of the sender of the document.
* `$receiver` is the Trading Networks profile of the receiver of the
  document.
* `TN_parms` is the routing hints used to route the document in Trading
  Networks.

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
datetime      | ISO8601/XML datetime
datetime.jdbc | yyyy-MM-dd HH:mm:ss.SSS
date          | ISO8601/XML date
date.jdbc     | yyyy-mm-dd
time          | ISO8601/XML time
time.jdbc     | HH:mm:ss
milliseconds  | Number of milliseconds since the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)

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

* `newValues` is a list the same length as the input `values` list,
  but where every item is either the localhost's domain name, host
  name, or IP address, depending on the value of arg provided.

---

### tundra.tn.document.attribute.string.transformer:find

Trading Networks string transformer which returns whether the given Trading
Networks document attribute value or list of values includes the given [regular
expression pattern].

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the [regular
  expression pattern].
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list boolean values indicating if the given input
  string `values` were found to include the given [regular expression pattern]
  `arg`.

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

### tundra.tn.document.attribute.string.transformer.id:generate

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

* `newValues` is a list the same length as the input `values` list,
  but where every item is a newly generated [UUID].

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

Trading Networks string transformer which returns whether the given Trading
Networks document attribute value or list of values match the given [regular
expression pattern].

This service is intended to be invoked by Trading Networks as a custom
document type attribute transformer.

#### Inputs:

* `values` is the list of strings to be matched against the [regular
  expression pattern].
* `isArray` is a boolean indicating if the `values` argument contains multiple
  items.
* `arg` is a [regular expression pattern].

#### Outputs:

* `newValues` is a list boolean values indicating if the given input
  string `values` match the given [regular expression pattern] `arg`.

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

* `newValues` is a list the same length as the input values list,
  but where every item is the internal ID of the desired Trading
  Networks partner profile.

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

* `newValues` is a list the same length as the input `values` list, but where
  every item is the internal ID for the Trading Networks My Enterprise
  profile.

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

* `newValues` is a list the same length as the input values list,
  but where every item is the currently loggged on username.

---

### tundra.tn.document.content:add

Adds a content part with the given name and content, specified as a string,
bytes or stream, to the given Trading Networks document (bizdoc).

#### Inputs:

* `$bizdoc` is the Trading Networks document to add the content part to.
  Only the internal ID of the bizdoc must be specified, with the remainder
  of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$part` is the name of the content part to be added to the Trading
  Networks document, and uniquely identifies the part being added.
* `$content` is the content part data to be added, specified as a string,
  byte array, or input stream.
* `$content.type` is an optional MIME media type describing the type of content
  being added. Defaults to application/octet-stream (the default MIME media
  type for arbitrary binary data) if not specified.
* `$encoding` is the optional character set used to encode `$content` when
  specified as a byte array or input stream and representing text data.
  Defaults to [UTF-8].
* `$overwrite?` is an optional boolean flag indicating whether to overwrite
  an existing content part with the same name, if one exists. Defaults to
  `false`.

---

### tundra.tn.document.content:exists

Returns true if the content part identified by the given part name exists
for the given bizdoc.

#### Inputs:

* `$bizdoc` is the Trading Networks document to check for the existence of
  the given content part. Only the internal ID of the bizdoc must be
  specified, with the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope`
  structure purely optional.
* `$part` is the name of the content part check for the existence of.

#### Outputs:

* `$exists?` is a boolean that when true indicates that the given `$bizdoc`
  has a content part with the given `$part` name.

---

### tundra.tn.document.content:get

Returns the given Trading Networks document's content associated with the
given part name as a stream.

#### Inputs:

* `$bizdoc` is the Trading Networks document to retrieve the content part
  from. Only the internal ID of the bizdoc must be specified, with the
  remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$part` is an optional name of the content part to be returned. If not
  specified, the default content part (xmldata for XML; ffdata for Flat
  Files) is returned.

#### Outputs:

* `$content` is the content part data as an input stream associated with
  the given `$part` name.
* `$content.type` is the MIME media type describing the type of data
  returned.

---

### tundra.tn.document.content:remove

Deletes the Trading Networks document content part with the given name from the
Trading Networks database.

#### Inputs:

* `$bizdoc` is the Trading Networks document to delete the content part from.
  Only the internal ID of the bizdoc must be specified, with the remainder
  of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$part` is the name of the content part to be deleted from the Trading
  Networks document, and uniquely identifies the part being deleted.

---

### tundra.tn.document.derivative:exists

Returns true if the given bizdoc is related to a derived bizdoc with the
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

* `$exists?` is a boolean that when true indicates the existence of a
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
      evaluates to true will the associated amended value be applied. If
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
      evaluates to true will the attribute be added to the derived bizdoc.
      If not specified, the attribute will always be added to the derived
      bizdoc.
  * `TN_parms` is an optional IData document containing routing hints used
    when routing the derivative bizdoc.
  * `force?` is an optional boolean flag: when true a new derivative will
    always be created even if an existing derivative for the same sender
    and receiver already exists; when false a new derivative will only
    be created if there is no existing derivative with the same sender
    and receiver. Defaults to false, if not specified.
  * `enabled?` is an optional boolean flag, when true this derivative rule
    is active, when false, this derivative rule is inactive and ignored.
    Defaults to true when not specified.
* `$part` is an optional name of the bizdoc content part to be copied to the
  resulting derivative. If not specified, the default content part is
  copied (xmldata for XML document types, ffdata for Flat File document
  types).

#### Outputs:

* `$derivative` is the resulting bizdoc copy after it has been routed to
  Trading Networks.

---

### tundra.tn.document.error:exists

Returns true if any errors (of the given class, if specified) exist on the
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

* `$exists?` is a boolean that when true indicates the existence of one or
  more errors of the given classes (if specified) have been logged against
  the given bizdoc.
* `$errors` is the list of activity error logs of the given classes (if
  specified) that were found logged against the given bizdoc.

---

### tundra.tn.document:get

Returns the Trading Networks document (bizdoc) associated with the given
internal ID, optionally including the document's content parts.

Unlike `WmTN/wm.tn.doc:viewAs`, this service does not throw an exception if
the given internal ID does not exist.

#### Inputs:

* `$id` is the internal ID of the Trading Networks document (bizdoc) to be
  retrieved.
* `$content?` is an optional boolean indicating whether to also return the
  bizdoc's content parts. Defaults to false.

#### Outputs:

* `$bizdoc` is the Trading Networks document associated with the given
  `$id`, if found. If no bizdoc exists with the given `$id`, an exception will
  be thrown.
* `$sender` is the Trading Networks partner profile of the sender of the
  returned bizdoc.
* `$receiver` is the Trading Networks partner profile of the receiver of the
  returned bizdoc.

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

* `$namespace` is an IData document containing the namespace
  declarations associated with the given `$bizdoc`, where the keys
  in the document are the namespace prefixes and the values are
  the namespace URIs.

---

### tundra.tn.document:normalize

Returns the given Trading Networks document (bizdoc) and its associated
sender and receiver profiles if desired. When the given bizdoc is a subset
(only the `InternalID` is required), the full bizdoc will be returned.

#### Inputs:

* `$bizdoc` is the Trading Networks document (bizdoc) to be normalized, and
  can be specified as a subset containing at least the InternalID field.
* `$content?` is an optional boolean indicating whether to also return the
  bizdoc's content parts. Defaults to false.
* `$sender?` is an optional boolean indicating whether to also return the
  associated sender partner profile. Defaults to false.
* `$receiver?` is an optional boolean indicating whether to also return the
  associated receiver partner profile. Defaults to false.

#### Outputs:

* `$bizdoc` is the normalized full Trading Networks document (bizdoc).
* `$sender` is the Trading Networks partner profile of the sender of the
  returned bizdoc, if requested.
* `$receiver` is the Trading Networks partner profile of the receiver of the
  returned bizdoc, if requested.

---

### tundra.tn.document:parse

Parses the Trading Networks document content part associated with the given
part name, or the default part if not provided, using the parsing schema
configured on the associated document type.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be parsed.
  Only the internal ID of the bizdoc must be specified, with the remainder
  of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional. If the
  specified bizdoc does not exist, an exception will be thrown.
* `$part` is an optional content part name to be parsed. If not specified,
  the default content part (xmldata for XML, ffdata for Flat Files) will
  be parsed. If the specified content part name does not exist, an
  exception will be thrown.
* `$encoding` is an optional character set to use when decoding the content
  part data. If not specified, defaults to the character set specified in
  the MIME content type of the content part being parsed, or failing that
  [UTF-8].
* `$validate?` is an optional boolean flag which when `true` will validate
  the parsed content against the given `$schema`, and throw an exception
  if the content is invalid. Defaults to `false`.

#### Outputs:

* `$document` is the parsed content part in an IData document representation.
* `$content.type` is the MIME media type that describes the format of the
  parsed content.
* `$namespaces` is the list of XML namespace prefixes and URIs declared on
  the associated document type and used when parsing the content.
* `$schema` is an optional output that specifies the fully-qualified name of
  the document reference (for XML) or flat file schema (for Flat Files)
  declared on the associated document type.
* `$schema.type` is an optional output that specifies whether `$schema` is an
  XML document reference or flat file schema, and is a choice of one of the
  following values:
  * Flat File
  * XML

---

### tundra.tn.document:relate

Relates two Trading Networks documents (bizdocs) together.

Use this service in preference to `WmTN/wm.tn.doc:relateDocuments`, as this
service does not throw an exception if the relationship already exists, and
this service also logs the creation of the relationship on the target
bizdoc.

#### Inputs:

* `$bizdoc.source` is the source Trading Networks document in the
  relationship. Only the internal ID of the bizdoc must be specified, with
  the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely
  optional.
* `$bizdoc.target` is the target Trading Networks document in the
  relationship. Only the internal ID of the bizdoc must be specified, with
  the remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely
  optional.
* `$relationship` is an optional string describing the relationship between
  the source and target bizdocs. If not specified, defaults to 'Unknown'.

---

### tundra.tn.document.schema:get

Returns the parsing schema associated with the given Trading Networks
document.

#### Inputs:

* `$bizdoc` is the Trading Networks document to retrieve the parsing schema
  from. Only the internal ID of the bizdoc must be specified, with the
  remainder of the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.

#### Outputs:

* `$schema` is the fully-qualified name of the document reference (for XML)
  or flat file schema (for flat files) declared on the given bizdoc's
  document type.
* `$schema.type` specifies whether `$schema` is an XML document reference or
  flat file schema, and is a choice of one of the following values:
  * `Flat File`
  * `XML`

---

### tundra.tn.document.status:set

Sets user status on the given Trading Networks document.

#### Inputs:

* `$bizdoc` is the Trading Networks document to set the user status on. Only
  the internal ID of the bizdoc must be specified, with the remainder of
  the `WmTN/wm.tn.rec:BizDocEnvelope` structure purely optional.
* `$status` is an optional user status string to be set on the given document.
  If errors exist on the given Trading Networks document, and the specified
  user status is "DONE", then it is automatically suffixed with "W/ ERRORS".
  If not specified, the document's user status will not be changed.
* `$status.silence?` is an optional boolean which when `true` will cause this
  service not to change the status on the document. Defaults to `false`.

---

### tundra.tn.document.type:get

Returns the Trading Networks document type associated with the given ID or
name as an IData document.

Use this service in preference to `WmTN/wm.tn.doctype:view`, as the WmTN
service returns an object of type `com.wm.app.tn.doc.BizDocType` which,
despite looking like one, is not a normal IData document and therefore
causes problems in Flow services. For example, you cannot branch on fields
in the `com.wm.app.tn.doc.BizDocType` document.

Also, unlike `WmTN/wm.tn.doctype:view`, this service does not throw an
exception if the document type does not exist.

#### Inputs:

* `$id` is an optional internal ID that identifies the Trading Networks
  document type to be returned. If both `$id` and `$name` are specified, `$id`
  takes precedence.
* `$name` is an optional Trading Networks document type name that identifies
  the document type to be returned. If both `$id` and `$name` are specified,
  `$id` takes precedence.

#### Outputs:

* `$type` is the Trading Networks document type identified by either `$id` or
  `$name`, returned as an IData document with the `WmTN/wm.tn.rec:BizDocType`
  structure. If no document type exists with the given `$id` or `$name`,
  nothing will be returned by this service, nor will an exception be thrown.

---

### tundra.tn.document.type:normalize

Returns the given Trading Networks document type. When the given type is a
subset (only the `TypeID` is required), the full type will be returned.

#### Inputs:

* `$type` is the Trading Networks document type to be normalized, and
  can be specified as a subset containing at least the `TypeID` field.

#### Outputs:

* `$type` is the normalized full Trading Networks document type.

---

### tundra.tn.document.type.schema:get

Returns the parsing schema associated with the given Trading Networks
document type.

#### Inputs:

* `$type` is the Trading Networks document type, specified in the
  `WmTN/wm.tn.rec:BizDocType` structure, to retrieve the parsing
  schema from.

#### Outputs:

* `$schema` is the fully-qualified name of the document reference (for XML)
  or flat file schema (for flat files) declared on the given document
  type.
* `$schema.type` specifies whether `$schema` is an XML document reference or
  flat file schema, and is a choice of one of the following values:
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

#### Outputs:

* `$profile` is the Trading Networks My Enterprise partner profile. The
  profile structure is a superset of both `wm.tn.rec:ProfileSummary` and
  `wm.tn.rec:Profile`, with additional convenient and usable structures
  added for External IDs, Delivery Methods, and Extended Fields.

---

### tundra.tn.queue:branch

Invokes `TundraTN/tundra.tn:branch` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:branch` for details on the following inputs:
  * `$branches`
  * `$catch`
  * `$finally`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:chain

Invokes `TundraTN/tundra.tn:chain` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:chain` for details on the following inputs:
  * `$services`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$service.input`
  * `$status.done`
  * `$parse?`
  * `$prefix?`
  * `$part`
  * `$encoding`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:clear

Clears all items from the given Trading Networks queue.

#### Inputs:

* `$status` is the user status that will be set on each dequeued bizdoc. If not
  specified, defaults to `DONE`.
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:deliver

Invokes `TundraTN/tundra.tn:deliver` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:deliver` for details on the following inputs:
  * `$destination`
  * `$service`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$status.done`
  * `$status.ignored`
  * `$substitute?`
  * `$parse?`
  * `$prefix?`
  * `$part`
  * `$encoding`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:derive

Invokes `TundraTN/tundra.tn:derive` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:derive` for details on the following inputs:
  * `$service`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$derivatives`
  * `$status.done`
  * `$status.ignored`
  * `$prefix?`
  * `$part`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

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
* `$ordered?` is an optional boolean flag which when `true` processes
  tasks in ascending task creation order. Defaults to `false` if
  not specified.
* `$suspend?` is an optional boolean flag which when `true` will
  automatically suspend the queue when all retries of a task are
  exhausted. At this point, the exhausted task's retry count is
  reset to `0`, the task is requeued to the front of the queue, and
  the queue is suspended, requiring manual intervention to resolve
  the queue processing error. Defaults to `false` if not specifed.
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

  Defaults to the retry factor on the receiver's profile, or `1` if
  the receiver's profile has no retry factor configured.
* `$thread.priority` is an optional priority used by the threads
  processing queued tasks. Defaults to [Thread.NORM_PRIORITY],
  if not specified.

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

* `$queue.properties` is an IData document containing the properties
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
  * length is the number of tasks with a status of `QUEUED` or
    `DELIVERING` currently in the queue.

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

---

### tundra.tn.queue:process

Invokes `TundraTN/tundra.tn:process` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:process` for details on the following inputs:
  * `$service`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$service.input`
  * `$status.done`
  * `$parse?`
  * `$prefix?`
  * `$part`
  * `$encoding`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:reroute

Invokes `TundraTN/tundra.tn:reroute` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:split

Invokes `TundraTN/tundra.tn:split` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:split` for details on the following inputs:
  * `$service`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$content.type.input`
  * `$content.type.output`
  * `$namespace.input`
  * `$namespace.output`
  * `$schema.input`
  * `$schema.output`
  * `$service.input`
  * `$service.output`
  * `$encoding.input`
  * `$encoding.output`
  * `$status.done`
  * `$status.ignored`
  * `$required?`
  * `$prefix?`
  * `$part`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.queue:status

Invokes `TundraTN/tundra.tn:status` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:status` for details on the following inputs:
  * `$status.user`
  * `$catch`
  * `$finally`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.factor`
  * `$retry.wait`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

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

Invokes `TundraTN/tundra.tn:translate` for each item in the given Trading
Networks queue.

#### Inputs:

* Refer to `TundraTN/tundra.tn:translate` for details on the following
  inputs:
  * `$service`
  * `$catch`
  * `$finally`
  * `$pipeline`
  * `$content.type.input`
  * `$content.type.output`
  * `$namespace.input`
  * `$namespace.output`
  * `$schema.input`
  * `$schema.output`
  * `$service.input`
  * `$service.output`
  * `$encoding.input`
  * `$encoding.output`
  * `$status.done`
  * `$status.ignored`
  * `$required?`
  * `$prefix?`
  * `$part`
  * `$strict`
* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  inputs:
  * `queue`
  * `$concurrency`
  * `$ordered?`
  * `$suspend?`
  * `$retry.limit`
  * `$retry.wait`
  * `$retry.factor`
  * `$thread.priority`

#### Outputs:

* Refer to `TundraTN/tundra.tn.queue:each` for details on the following
  outputs:
  * `queue`
  * `logMsg`

---

### tundra.tn.reliable:amend

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:amend`.

Refer to `tundra.tn:amend` for futher details.

---

### tundra.tn.reliable:branch

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:branch`.

Refer to `tundra.tn:branch` for futher details.

---

### tundra.tn.reliable:chain

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:chain`.

Refer to `tundra.tn:chain` for futher details.

---

### tundra.tn.reliable:deliver

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:deliver`.

Refer to `tundra.tn:deliver` for futher details.

---

### tundra.tn.reliable:derive

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:derive`.

Refer to `tundra.tn:derive` for futher details.

---

### tundra.tn.reliable:process

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:process`.

Refer to `tundra.tn:process` for futher details.

---

### tundra.tn.reliable:reroute

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:reroute`.

Refer to `tundra.tn:reroute` for futher details.

---

### tundra.tn.reliable:split

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:split`.

Refer to `tundra.tn:split` for futher details.

---

### tundra.tn.reliable:translate

Reliably processes (as a service execution task) a Trading Networks document
via `tundra.tn:translate`.

Refer to `tundra.tn:translate` for futher details.

---

### tundra.tn.schema.attribute.datetime:transformer

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

### tundra.tn.schema.attribute.string:transformer

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
  file schema used to parse the content into an IData structure.
* `$derivative` is the derivative rule to be filtered. The filter service is
  allowed to edit the `$derivative` rule, so that it may, for example,
  disable the rule by setting `$derivative/enabled?` to `false`, or specify
  a different sender and/or receiver.

#### Outputs:

* `$derivative` is the derivative rule after filtering. The filter service
  is required to return the rule whether it makes changes to it or not.

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
  `$parse?` parameter is true.
* `$content` is the raw bizdoc content as an input stream for processing.
  This is the default name for this input parameter. The actual name of
  the parameter can be changed using the `TundraTN/tundra.tn:process`
  `$service.input` parameter. This input is only provided when the
  `TundraTN/tundra.tn:process` `$parse?` parameter is false.
* `$content.type` is the MIME media type that describes the format of the
  bizdoc content.
* `$namespaces` is the list of XML namespace prefixes and URIs declared on
  the associated document type and used when parsing the content.
* `$schema` is the name of the Integration Server document reference or flat
  file schema used to parse the content into an IData structure.
* `$schema.type` describes whether the schema used to parse the content was
  a Flat File or XML schema.

#### Outputs:

* `$summary` is an optional diagnostic summary message to be automatically
  logged in the Trading Networks Activity Log.
* `$message` is an optional diagnostic detailed message to be automatically
  logged in the Trading Networks Activity Log.

---

### tundra.tn.schema:profile

A compatible superset of wm.tn.rec:ProfileSummary and wm.tn.rec:Profile,
with some developer-friendly formats for all the external IDs, extended
fields, and delivery methods.

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
  file schema used to parse the content into an IData structure.

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
  serialize `$document[n]` to an input stream for routing to Trading
  Networks.
* `TN_parms` provides routing hints for Trading Networks. It can be
  specified as either a singleton IData or an IData list. If specified as
  a singleton, it will be used when routing every item in the `$documents`
  list. If specified as a list, the length of `TN_parms` must match the
  length of `$documents`, and `TN_parms[n]` will be used when routing
  `$documents[n]` to Trading Networks.

---

### tundra.tn.schema:translator

Translation services used by `TundraTN/tundra.tn:translate` can implement this
specification.

#### Inputs:

* `$bizdoc` is the Trading Networks document whose content is to be
  translated.
* `$sender` is the Trading Networks partner profile associated with the
  sender of the bizdoc.
* `$receiver` is the Trading Networks partner profile associated with the
  receiver of the bizdoc.
* `$document` is the parsed bizdoc content for translation. This is the
  default name for this input parameter. The actual name of the parameter
  can be changed using the `TundraTN/tundra.tn:translate` `$service.input`
  parameter, which allows the use of `TundraTN/tundra.tn:translate` with
  existing mapping services.
* `$schema` is the name of the Integration Server document reference or flat
  file schema used to parse the content into an IData structure.

#### Outputs:

* `$translation` is the translated content which will be routed back to
  Trading Networks as a new document. This is the default name for this
  output parameter. The actual name of the parameter can be changed using
  the `TundraTN/tundra.tn:translate` `$service.output` parameter, which
  allows the use of `TundraTN/tundra.tn:translate` with existing mapping
  services.
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

## Contributions

1. Check out the latest master to make sure the feature hasn't been implemented
   or the bug hasn't been fixed yet
2. Check out the issue tracker to make sure someone already hasn't requested it
   and/or contributed it
3. Fork the project
4. Start a feature/bugfix branch
5. Commit and push until you are happy with your contribution
6. Make sure to add tests for it. This is important so it won't break in a future
   version unintentionally

Please try not to mess with the package version, or history. If you want your
own version please isolate it to its own commit, so it can be cherry-picked
around.

## Copyright

Copyright © 2012 Lachlan Dowding. See the [LICENSE] file for further details.

[/dev/null]: <http://en.wikipedia.org/wiki//dev/null>
[Base64]: <http://en.wikipedia.org/wiki/Base64>
[CSV]: <http://en.wikipedia.org/wiki/Comma-separated_values>
[default charset]: <http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html#defaultCharset()>
[ISO8601]: <http://en.wikipedia.org/wiki/ISO_8601>
[JAR]: <http://en.wikipedia.org/wiki/JAR_(file_format)>
[java.text.SimpleDateFormat]: <http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html>
[java.util.Date]: <http://docs.oracle.com/javase/6/docs/api/java/util/Date.html>
[javax.jms.BytesMessage]: <http://docs.oracle.com/javaee/1.4/api/javax/jms/BytesMessage.html>
[JMS]: <http://en.wikipedia.org/wiki/Java_Message_Service>
[JSON]: <http://www.json.org>
[LICENSE]: <https://github.com/Permafrost/TundraTN/blob/master/LICENSE>
[regular expression pattern]: <http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html>
[releases]: <https://github.com/Permafrost/TundraTN/releases>
[RFC 4122]: <http://www.ietf.org/rfc/rfc4122.txt>
[Thread.NORM_PRIORITY]: <http://docs.oracle.com/javase/6/docs/api/java/lang/Thread.html#NORM_PRIORITY>
[TSV]: <http://en.wikipedia.org/wiki/Tab-separated_values>
[Tundra]: <https://github.com/Permafrost/Tundra>
[Tundra.java]: <https://github.com/Permafrost/Tundra.java>
[TundraTN]: <https://github.com/Permafrost/TundraTN>
[TundraTN.java]: <https://github.com/Permafrost/TundraTN.java>
[URL-encoded]: <http://en.wikipedia.org/wiki/Percent-encoding>
[UTF-8]: <http://en.wikipedia.org/wiki/UTF-8>
[UUID]: <http://docs.oracle.com/javase/6/docs/api/java/util/UUID.html>
[webMethods Integration Server]: <http://www.softwareag.com/corporate/products/wm/integration/products/ai/overview/default.asp>
[webMethods Trading Networks]: <http://www.softwareag.com/corporate/products/wm/integration/products/b2b/overview/default.asp>
[XML]: <http://www.w3.org/XML/>
[YAML]: <http://www.yaml.org>
