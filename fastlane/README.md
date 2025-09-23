fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android test

```sh
[bundle exec] fastlane android test
```



### android build_release

```sh
[bundle exec] fastlane android build_release
```

Build aab and apk of Production Release variant

### android publish_google_play

```sh
[bundle exec] fastlane android publish_google_play
```

Publish a new version to the Google Play

### android publish_firebase

```sh
[bundle exec] fastlane android publish_firebase
```

Publish test build to Firebase App Distribution

### android publish_internal_app_sharing

```sh
[bundle exec] fastlane android publish_internal_app_sharing
```



### android send_release_notification

```sh
[bundle exec] fastlane android send_release_notification
```



----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
