# News App

## UI
![](app.gif)

## API

https://newsapi.org/

## Build

### Versioning
App version is defined in version.properties file. Then Gradle scripts are building `applicationVersionCode` and `applicationVersionNameFull`

Current version is visible in Build Output during project configuration:
```
> Configure project :
Version build: 0
Resolving versions: majorInt = 0, minorInt = 100000, patchInt = 0, versionBuildInt = 0
```

### Artifact naming:
- `tech.michalik.news-v0.1.0.0-debug.aab`
- `tech.michalik.news-v0.1.0.0-release.aab`

### Additional build properties:
- GIT_SHA
- BUILD_TIME

### Build variants:

#### Debug;
- logging enabled
- network logging enabled
- no minification

#### Release;
- logging disabled
- network logging disabled
- minified

## Proguard rules
- for kotlinx.serialization

## Tech stack
- Jetpack Compose
- Kotlin Coroutines + Flow
- OkHttp/Retrofit
- kotlinx.serialization
- kotlinx.datetime
- Accompanist for nav animations
- Koin for DI
- Coil for image loading
- Timber for logging

## Tests:
- Kotest
- MockK
- Turbine
- OkHttp3 MockWebServer

## UI tests
Install Maestro:

https://maestro.mobile.dev/getting-started/installing-maestro

Run:
```
maestro test ui_tests/app_test.yaml
```

This test only verifies if app is starting with no exceptions.


