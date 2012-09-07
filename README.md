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
