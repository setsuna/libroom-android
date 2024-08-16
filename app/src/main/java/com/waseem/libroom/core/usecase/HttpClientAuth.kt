package com.waseem.libroom.core.usecase

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey

class HttpClientAuth {
    var token: String = ""
    var u4dvv5qa: String = ""

    class Config {
        var token: String = ""
        var u4dvv5qa: String = ""
    }

    companion object : HttpClientPlugin<Config, HttpClientAuth> {
        override val key: AttributeKey<HttpClientAuth> = AttributeKey("HttpClientAuth")

        override fun prepare(block: Config.() -> Unit): HttpClientAuth {
            val config = Config().apply(block)
            return HttpClientAuth().apply {
                token = config.token
                u4dvv5qa = config.u4dvv5qa
            }
        }

        override fun install(plugin: HttpClientAuth, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                if (plugin.token.isNotEmpty()) {
                    context.headers.append("Authorization", "Bearer ${plugin.token}")
                }
                if (plugin.u4dvv5qa.isNotEmpty()) {
                    context.headers.append("U4dvv5qa", plugin.u4dvv5qa)
                }
            }
        }
    }
}