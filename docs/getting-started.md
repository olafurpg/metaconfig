---
id: getting-started
title: Getting started
---

Metaconfig is a library to manage configuration options for Scala applications
with the following goals:

- **Model configuration as Scala data structures**: Metaconfig allows you to
  manage all user configuration options as immutable case classes and sealed
  traits.
- **Limit boilerplate where possible**: Metaconfig provides automatic
  configuration decoders and encoders to help you avoid copy-pasting the same
  setting name in multiple places like the application implementation,
  configuration parser and configuration documentation. Copy-pasting strings is
  not only cumbersome but also increases the risk of making mistakes resulting
  in a bad user experience.
- **Evolve configuration without breaking changes**: it's normal that
  configuration options change as your application evolves (naming is hard,
  after all). Metaconfig supports several ways to evolve user configuration
  options in a backwards compatible way so that your existing users have an
  easier time to upgrade to upgrade to the latest versions of your application.
- **Report helpful error messages**: Metaconfig reports errors using source
  positions in the user-written configuration files, similar to how a compiler
  reports errors.
- **Treat command-line arguments as configuration**: Metaconfig provides a
  command-line parser with automatic generation of `--help` messages, tab
  completions for bash/zsh and more. Command-line arguments map into Scala case
  classes, just like HOCON and JSON configuration.
