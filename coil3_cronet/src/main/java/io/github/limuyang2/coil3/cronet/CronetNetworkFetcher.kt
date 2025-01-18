@file:JvmName("CronetNetworkFetcher")

package io.github.limuyang2.coil3.cronet

import coil3.PlatformContext
import coil3.network.CacheStrategy
import coil3.network.ConnectivityChecker
import coil3.network.NetworkClient
import coil3.network.NetworkFetcher
import okcronet.Call
import okcronet.CronetClient
import org.chromium.net.CronetEngine

/**
 * @author 李沐阳
 * @date 2025/1/18
 * @description
 */

@JvmName("factory")
fun CronetNetworkFetcherFactory(cronetEngine: CronetEngine) = NetworkFetcher.Factory(
    networkClient = { CronetClient.Builder(cronetEngine).build().asNetworkClient() },
)

@JvmName("factory")
fun CronetNetworkFetcherFactory(cronetClient: CronetClient) = NetworkFetcher.Factory(
    networkClient = { cronetClient.asNetworkClient() },
)

@JvmName("factory")
fun CronetNetworkFetcherFactory(
    callFactory: Call.Factory,
) = NetworkFetcher.Factory(
    networkClient = { callFactory.asNetworkClient() },
)

@JvmName("factory")
fun CronetNetworkFetcherFactory(
    callFactory: () -> Call.Factory,
) = NetworkFetcher.Factory(
    networkClient = { callFactory().asNetworkClient() },
)

@JvmName("factory")
fun CronetNetworkFetcherFactory(
    callFactory: () -> Call.Factory,
    cacheStrategy: () -> CacheStrategy = { CacheStrategy.DEFAULT },
) = NetworkFetcher.Factory(
    networkClient = { callFactory().asNetworkClient() },
    cacheStrategy = cacheStrategy,
)

@JvmName("factory")
fun CronetNetworkFetcherFactory(
    callFactory: () -> Call.Factory,
    cacheStrategy: () -> CacheStrategy = { CacheStrategy.DEFAULT },
    connectivityChecker: (PlatformContext) -> ConnectivityChecker = ::ConnectivityChecker,
) = NetworkFetcher.Factory(
    networkClient = { callFactory().asNetworkClient() },
    cacheStrategy = cacheStrategy,
    connectivityChecker = connectivityChecker,
)

fun Call.Factory.asNetworkClient(): NetworkClient {
    return CallFactoryNetworkClient(this)
}