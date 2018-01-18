IntelliJ Node Security plugin
=============

[![Gitter][badge-gitter-img]][badge-gitter] [![Build Status][badge-travis-img]][badge-travis]

[![Donate][badge-paypal-img]][badge-paypal]

[![Version](http://phpstorm.espend.de/badge/7495/version)][plugin-website]
[![Downloads](http://phpstorm.espend.de/badge/7495/downloads)][plugin-website]
[![Downloads last month](http://phpstorm.espend.de/badge/7495/last-month)][plugin-website]

Introduction
------------

IntelliJ Node Security plugin that indicates which Node packages in your project are vulnerable.
Plugin checks your dependencies against [nodesecurity.io](nodesecurity-io) service.

It supports following JetBrains IDEs:

- Android Studio
- AppCode
- CLion
- IntelliJ IDEA
- PhpStorm
- PyCharm
- RubyMine
- WebStorm
- DataGrip



Features
--------

- Highlight vulnerable dependencies in `package.json` file.


Installation
------------

- Using IDE built-in plugin system:
  - <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "Node security"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - Download the [latest release][latest-release] and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>
  
Restart IDE.


Changelog
---------

### [v0.1.0](https://github.com/hsz/idea-nodesecurity/tree/v0.1.0) (2018-01-18)

[Full Changelog](https://github.com/hsz/idea-nodesecurity/compare/v0.1.0)

**Implemented enhancements:**

- Initial commit

[Full Changelog History](./CHANGELOG.md)


Contribution
------------

Check [`CONTRIBUTING.md`](./CONTRIBUTING.md) file.


Developed By
------------

[**@hsz** Jakub Chrzanowski][hsz]


**Contributors**

- none


License
-------

Copyright (c) 2018 hsz Jakub Chrzanowski. See the [LICENSE](./LICENSE) file for license rights and limitations (MIT).

    
[hsz]:                    http://hsz.mobi
[plugin-website]:         https://plugins.jetbrains.com/plugin/7495
[latest-release]:         https://github.com/hsz/idea-nodesecurity/releases/latest
[nodesecurity-io]:        https://nodesecurity.io

[badge-gitter-img]:       https://badges.gitter.im/hsz/idea-nodesecurity.svg
[badge-gitter]:           https://gitter.im/hsz/idea-nodesecurity
[badge-travis-img]:       https://travis-ci.org/hsz/idea-nodesecurity.svg?branch=master
[badge-travis]:           https://travis-ci.org/hsz/idea-nodesecurity
[badge-coveralls-img]:    https://coveralls.io/repos/github/hsz/idea-nodesecurity/badge.svg?branch=master
[badge-coveralls]:        https://coveralls.io/github/hsz/idea-nodesecurity?branch=master
[badge-paypal-img]:       https://img.shields.io/badge/donate-paypal-yellow.svg
[badge-paypal]:           https://www.paypal.me/hsz
