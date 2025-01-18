package io.github.limuyang2.coil3.cronet

import coil3.network.NetworkClient
import coil3.network.NetworkHeaders
import coil3.network.NetworkRequest
import coil3.network.NetworkRequestBody
import coil3.network.NetworkResponse
import coil3.network.NetworkResponseBody
import okcronet.Call
import okcronet.http.Headers
import okcronet.http.Request
import okcronet.http.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.ByteString
import org.chromium.net.UrlResponseInfo
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator


/**
 * @author 李沐阳
 * @date 2025/1/18
 * @description
 */
internal class CallFactoryNetworkClient(private val callFactory: Call.Factory) : NetworkClient {
    override suspend fun <T> executeRequest(
        request: NetworkRequest,
        block: suspend (NetworkResponse) -> T,
    ): T {
        val response = callFactory.newCall(request.toRequest()).await()

        return response.body.use {
            val networkResponse = NetworkResponse(
                code = response.code,
                headers = response.urlResponseInfo.toNetworkHeaders(),
                body = it?.source()?.let(::NetworkResponseBody),
                delegate = this,
            )
            block(networkResponse)
        }
    }
}

private suspend fun NetworkRequest.toRequest(): Request {
    val request = Request.Builder()
    request.url(url)
    request.method(method, body?.readByteString()?.toRequestBody())
    request.headers(headers.toHeaders())
    return request.build()
}

private suspend fun NetworkRequestBody.readByteString(): ByteString {
    val buffer = Buffer()
    writeTo(buffer)
    return buffer.readByteString()
}

private fun NetworkHeaders.toHeaders(): Headers {
    val headers = Headers.Builder()
    for ((key, values) in asMap()) {
        for (value in values) {
            headers.addUnsafeNonAscii(key, value)
        }
    }
    return headers.build()
}

private fun UrlResponseInfo.toNetworkHeaders(): NetworkHeaders {
    val headers = NetworkHeaders.Builder()
    for ((key, values) in allHeaders) {
        headers[key] = values
    }
    return headers.build()
}
