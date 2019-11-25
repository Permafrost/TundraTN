# TundraTN ❄

A package of cool services for [webMethods Trading Networks] 7.1 and
higher.

## Services

Refer to the [SERVICES.md] file for detailed descriptions of all
services provided by the package.

## Installation

You have two choices for installing [TundraTN]:

* zip
* git

If you are comfortable using git, I recommend this method as you can
then easily switch between package versions using git checkout and
download new versions using git fetch.

### Using Zip

1. Download a pre-built zip of the desired version of the package
   from the available [releases].
2. Copy the `TundraTN-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the package release `TundraTN-vn.n.n.zip`
   from the package management web page on your Integration Server's
   web administration site.

### Using Git

To install with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your
  Integration Server.
* Your Integration Server has internet access to https://github.com
  (for cloning the repository).
* The dependent packages listed above are installed and enabled on
  your Integration Server.
* You have identified what version of [TundraTN] you'd like to
  install by referring to the available [releases].

From the root directory of your Integration Server installation or
instance in which you want to install:

```sh
$ cd ./packages/
$ git clone https://github.com/Permafrost/TundraTN.git
$ cd ./TundraTN/
$ git checkout v<n.n.n> # where <n.n.n> is the required version
```

Then activate and enable the [TundraTN] package from the package
management web page on your Integration Server web administration
site.

## Upgrading

When upgrading you have to choose the same method used to originally
install the package. Unfortunately, if git wasn't used to install
the package then you can't use git to upgrade it either. However, if
you want to switch to using git to manage the package, delete the
installed package and start over using the git method for
installation.

### Using Zip

1. Download a pre-built zip of the desired updated version of the
   package from the available [releases].
2. Copy the `TundraTN-vn.n.n.zip` file to your Integration Server's
   `./replicate/inbound/` directory.
3. Install and activate the package release `TundraTN-vn.n.n.zip`
   from the package management web page on your Integration Server's
   web administration site.

### Using Git

To upgrade with this method, first make sure that:

* Git is [installed](http://git-scm.com/downloads) on your
  Integration Server.
* Your Integration Server has internet access to https://github.com
  (for fetching updates from the repository).
* The dependent packages listed above are installed and enabled on
  your Integration Server.
* You have identified what version of [TundraTN] you'd like to
  upgrade to by referring to the available [releases].
* You originally installed [TundraTN] using the git method described
  above.

From the root directory of your Integration Server installation or
instance in which you want to install:

```sh
$ cd ./packages/TundraTN/
$ git fetch
$ git checkout v<n.n.n> # where <n.n.n> is the desired updated version
```

Then reload the [TundraTN] package from the package management web
page on your Integration Server web administration site.

## Dependencies

[TundraTN] is compiled for Java 1.6, and is dependent on the
following [webMethods Integration Server] packages:

* [Tundra]
* WmTN

[TundraTN] is also dependent on the following open source Java
libraries, which provide the underlying implementation for the Java
services, and are included in the [TundraTN] package as [JAR] files
in the following locations:

* `./code/jars/Tundra.jar` - http://github.com/Permafrost/Tundra.java
* `./code/jars/TundraTN.jar` - http://github.com/Permafrost/TundraTN.java

## Conventions

1. Most input and output pipeline arguments are prefixed with `$` as
   a poor-man's scoping mechanism, since typically user-space
   variables are unprefixed. However some Trading Networks-specific
   arguments, such as `TN_parms`, remain unprefixed to be compatible
   with Trading Networks.
2. All boolean arguments are suffixed with a `?`.
3. Single-word argument names are preferred. Where multiple words are
   necessary, words are separated with a `.`.
4. Service namespace is kept flat. Namespace folders are usually
   nouns. Service names are usually verbs, indicating the action
   performed on the noun (parent folder).
5. Services declare all inputs and outputs (except where deprecated),
   always explicitly marked as optional or required, use constrained
   types where possible, and enable input and output pipeline
   validation where possible, to minimise surprise.
6. Services often declare their inputs as optional, and either apply
   an appropriate default value, or take no action and return no
   output (whatever is more appropriate), to minimise the need for
   existence checking in flow map steps.
7. All private elements are kept in the `tundra.tn.support` folder.
   All other elements comprise the public API of the package. As the
   private elements do not contribute to the public API, they are
   liable to change at any time. **Enter at your own risk.**

## Contributions

1. Check out the latest master to make sure the feature hasn't been
   implemented or the bug hasn't been fixed yet
2. Check out the issue tracker to make sure someone already hasn't
   requested it and/or contributed it
3. Fork the project
4. Start a feature/bugfix branch
5. Commit and push until you are happy with your contribution

Please try not to mess with the package version, or history. If you
want your own version please isolate it to its own commit, so it can
be cherry-picked around.

## Related

See also [Tundra](https://github.com/Permafrost/Tundra), a package
of cool services for [webMethods Integration Server] 7.1 and higher.

## Copyright

Copyright &copy; 2012 Lachlan Dowding. See the [LICENSE.txt] file for
further details.

[JAR]: <http://en.wikipedia.org/wiki/JAR_(file_format)>
[LICENSE.txt]: <https://raw.githubusercontent.com/Permafrost/TundraTN/master/LICENSE.txt>
[releases]: <https://github.com/Permafrost/TundraTN/releases>
[SERVICES.md]: <https://github.com/Permafrost/TundraTN/blob/master/SERVICES.md>
[Tundra]: <https://github.com/Permafrost/Tundra>
[Tundra.java]: <https://github.com/Permafrost/Tundra.java>
[TundraTN]: <https://github.com/Permafrost/TundraTN>
[TundraTN.java]: <https://github.com/Permafrost/TundraTN.java>
[webMethods Integration Server]: <http://www.softwareag.com/corporate/products/wm/integration/products/ai/overview/default.asp>
[webMethods Trading Networks]: <http://www.softwareag.com/corporate/products/wm/integration/products/b2b/overview/default.asp>
