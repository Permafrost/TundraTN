# TundraTN ❄

A package of useful services for webMethods Trading Networks 7.1 or higher.

## Dependencies

TundraTN is dependent on the following packages:

* Tundra
* WmTN

## Installation

From your Integration Server installation:

```sh
$ cd ./packages
$ git clone https://github.com/Permafrost/TundraTN.git
$ git checkout v<n.n.n> # where <n.n.n> is the desired version
```

Then activate and enable the TundraTN package from the package management web page on the
Integration Server web administration site.

## Upgrading

From your Integration Server installation:

```sh
$ cd ./packages/TundraTN
$ git fetch
$ git checkout v<n.n.n> # where <n.n.n> is the desired version
```

Then reload the TundraTN package from the package management web page on the
Integration Server web administration site.

## Conventions

1. All input and output pipeline arguments are prefixed with '$' as a poor-man's
   scoping mechanism (typical user-space variables will be unprefixed), except
   Trading Networks-specific arguments, such as bizdoc, sender and receiver
2. All boolean arguments are suffixed with a '?'
3. Single-word argument names are preferred. Where multiple words are necessary, 
   words are separated with a '.'
4. Service namespace is kept flat. Namespace folders are usually nouns. Service 
   names are usually verbs, indicating the action performed on the noun (parent 
   folder)
5. All private elements are kept in the tundra.tn.support folder. All other 
   elements comprise the public API of the package. As the private 
   elements do not contribute to the public API, they are liable to change at 
   any time. Enter at your own risk

## Tests

*Almost* every service in TundraTN has unit tests, located in the 
tundra.tn.support.test folder.

To run the test suite, either:
* run tundra:test($package = "TundraTN") service directly
* visit <http://localhost:5555/invoke/tundra/test?$package=TundraTN>  
  (substitute your own Integration Server host and port for localhost:5555)

## Services

### Tundra

Top-level services for the most common tasks:

```java
// Logs a message to the Trading Networks activity log.
tundra.tn:log($bizdoc, $type, $class, $summary, $message);

// Processes a Trading Networks document by parsing the given document content part, and calling 
// the given service with the following input arguments: $bizdoc, $sender and $receiver are the 
// normal bizdoc processing service inputs (except with the '$' prefix), $document is the parsed 
// content part as an IData document, and $schema is the name of the document reference or flat 
// file schema used by the parser.
// 
// As it provides logging, content parsing, error handling, and document status updates, the
// $service processing service does not need to include any of this common boilerplate code.
// 
// If a custom $catch service is specified, it will be called if an error occurs while processing
// the bizdoc.  The $catch service will be passed the current pipeline, along with the following 
// additional arguments: 
//   $exception - the actual exception object thrown by the $service
//   $exception.message - the error message
//   $exception.class - the exception object's Java class name 
//   $exception.stack - the Java call stack at the time the exception occurred
// 
// This service is designed to be called directly from a Trading Networks bizdoc processing rule,
// hence the non-dollar-prefixed bizdoc argument.
// 
// Additional arbitrary input arguments for $service (or pub.flatFile:convertToValues/
// pub.xml:xmlStringToXMLNode/pub.xml:xmlNodeToDocument via tundra.tn.document:parse) can be 
// specified in the $pipeline document. Fully qualified names will be handled correctly, for 
// example an argument named 'example/item[0]' will be converted to an IData document named 
// 'example' containing a String list named 'item' with it's first value set accordingly.
tundra.tn:process(bizdoc, $service, $catch, $finally, $pipeline, $part, $encoding);
```

#### Document

Bizdoc-related services:

```java
// Returns the document's content associated with the given part name as a stream. If the part
// name is not provided, the default content part is returned (xmldata for XML; ffdata for Flat 
// Files).
tundra.tn.document.content:get($bizdoc, $part, $encoding);

// Returns the document associated with the given internal ID, optionally 
// including the document's content parts.
tundra.tn.document:get($id, $content?);

// Parses the Trading Networks document content part associated with the given part
// name, or the default part if not provided, using the parsing schema configured on 
// the document type.
tundra.tn.document:parse($bizdoc, $part, $encoding);

// Routes arbitrary content specified as a string, byte array, input stream, or IData document 
// to Trading Networks. Correctly supports large documents, so any document considered large will 
// be routed as a large document to Trading Networks, unlike the WmTN/wm.tn.doc.xml:routeXML service.
tundra.tn.document:route($content, $encoding, $schema, TN_parms);

// Returns the parsing schema associated with the given Trading Networks document.
tundra.tn.document.schema:get($bizdoc);

// Sets user status on the given Trading Networks document.
tundra.tn.document.status:set($bizdoc, $status);

// Returns the Trading Networks document type associated with the given ID as an 
// IData document.
// 
// Use this service in preference to WmTN/wm.tn.doctype:view, as the WmTN service 
// returns an object of type com.wm.app.tn.doc.BizDocType which, despite looking
// like one, is not a normal IData document and therefore causes problems in
// Flow services. For example, you cannot branch on fields in the faux document.
tundra.tn.document.type:get($id);

// Returns the parsing schema associated with the given Trading Networks document type.
tundra.tn.document.type.schema:get($type);
```

##### Exception

Exception-related services:

```java
// Handles a Trading Networks document processing error by logging the error against
// the document in the activity log, and setting the user status to 'ERROR'.
tundra.tn.exception:handle($bizdoc);
```

##### Profile

Partner profile-related services:

```java
// Returns the Trading Networks profile associated with the given ID.
tundra.tn.profile:get($id);

// Returns the Trading Networks Enterprise partner profile.
tundra.tn.profile:self();
```

## Contributions

1. Check out the latest master to make sure the feature hasn't been implemented 
   or the bug hasn't been fixed yet
2. Check out the issue tracker to make sure someone already hasn't requested it 
   and/or contributed it
3. Fork the project
4. Start a feature/bugfix branch
5. Commit and push until you are happy with your contribution
6. Make sure to add tests for it. This is important so it won't in a future 
   version unintentionally

Please try not to mess with the package version, or history. If you want your 
own version please isolate it to its own commit, so it can be cherry-picked 
around.

## Copyright

Copyright © 2012 Lachlan Dowding. See license.txt for further details.