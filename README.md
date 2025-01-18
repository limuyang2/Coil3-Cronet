# Coil3-Cronet
Coil3 uses Cronet as the network loading component.

[中文](https://github.com/limuyang2/Coil3-Cronet/blob/main/README_CN.md)

## Why
Cronet supports the `HTTP1.1`, `HTTP2`, and `QUIC/HTTP3` protocols, offering better performance. This library is encapsulated based on[okcronet](https://github.com/limuyang2/okcronet).

## Usage
### Import this library
```
implementation("io.github.limuyang2:coil3-cronet:1.0.0")

// coil
implementation("io.coil-kt.coil3:coil:3.0.4")
// coil network functionality
implementation("io.coil-kt.coil3:coil-network-core:3.0.4")


// Add your Cronet dependencies, for example:
implementation("org.chromium.net:cronet-api:119.6045.31")
implementation("org.chromium.net:cronet-common:119.6045.31")
implementation("org.chromium.net:cronet-embedded:119.6045.31")
```
### Configuration
## Method 1
Add the following code in the `onCreate()` method of `Application`:
```kotlin
SingletonImageLoader.setSafe { context ->
    ImageLoader.Builder(context)
    .crossfade(true)
    components {
        add(CronetNetworkFetcherFactory(cronetEngine)) // Add network request component
    }
    .build()
}
```
## Method 2
Let `Application` implement the `SingletonImageLoader.Factory` interface:
```kotlin
class App : Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(CronetNetworkFetcherFactory(cronetEngine)) // Add network request component
            }
            .build()
    }
```
## More methods
Please refer to the[coil](https://coil-kt.github.io/coil/getting_started)documentation


# Thanks
[coil](https://github.com/coil-kt/coil)