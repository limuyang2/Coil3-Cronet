# Coil3-Cronet
Coil3 使用 Cronet 作为网络加载组件

## Why
Cronet 支持 `HTTP1.1`、`HTTP2`及`QUIC/HTTP3` 协议，拥有更好的性能。本库基于[okcronet](https://github.com/limuyang2/okcronet)封装。

## 使用
### 引入本库
```
implementation("io.github.limuyang2:coil3-cronet:1.0.0")

// coil
implementation("io.coil-kt.coil3:coil:3.0.4")
// coil 网络功能
implementation("io.coil-kt.coil3:coil-network-core:3.0.4")


// 添加你的cronet依赖，例如：
implementation("org.chromium.net:cronet-api:119.6045.31")
implementation("org.chromium.net:cronet-common:119.6045.31")
implementation("org.chromium.net:cronet-embedded:119.6045.31")
```
### 配置
## 方式一
在`Application`的`onCreate()`中添加如下代码：
```kotlin
SingletonImageLoader.setSafe { context ->
    ImageLoader.Builder(context)
    .crossfade(true)
    components {
        add(CronetNetworkFetcherFactory(cronetEngine)) // 添加网络请求组件
    }
    .build()
}
```
## 方式二
`Application` 实现 `SingletonImageLoader.Factory`接口：
```kotlin
class App : Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(CronetNetworkFetcherFactory(cronetEngine)) // 添加网络请求组件
            }
            .build()
    }
```
## 更多方式
请参考[coil](https://coil-kt.github.io/coil/getting_started/)文档


# Thanks
[coil](https://github.com/coil-kt/coil)