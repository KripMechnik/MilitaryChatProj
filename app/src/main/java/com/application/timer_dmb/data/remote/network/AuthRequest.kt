package com.application.timer_dmb.data.remote.network

import android.util.Log
import com.application.timer_dmb.common.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.headers
import io.ktor.client.request.host
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.util.StringValues
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class AuthRequest @Inject constructor(
    @Named("AuthClient") val baseClient: HttpClient,
    val json: Json
){

    suspend inline operator fun <reified T> invoke(
        formData: Boolean = false,
        path: String,
        query: String = "",
        params: StringValues = StringValues.Empty,
        method: HttpMethod,
        body: Any? = null
    ) = try {
        val response = baseClient.request {
            this.method = method
            this.host = Constants.BASE_HOST
            url {
                protocol = URLProtocol.HTTP
                path(path + query)
                parameters.appendAll(params)
                headers {
                    append("Connection", "close")
                }
            }
            headers{
                append("Connection", "close")
            }
            if (formData){
                contentType(ContentType.MultiPart.FormData)
            } else {
                contentType(ContentType.Application.Json)
            }
            Log.i("menu", this.headers.entries().toString())
            body?.let { setBody(it) }
        }



        if (response.status.isSuccess()){
            val stringBody = response.body<String>()
            if (stringBody.startsWith("{") || stringBody.startsWith("[")) ApiResponse.Success(data = json.decodeFromString<T>(stringBody))
            else ApiResponse.Success()
        } else {
            val stringBody = response.body<String>()
            ApiResponse.Error(
                errorCode = response.status.value,
                errorMessage = response.status.description + "\n" + stringBody
            )
        }

    } catch (e: SerializationException){
        e.printStackTrace()
        ApiResponse.Error(
            errorCode = -1,
            errorMessage = "Serialization exception"
        )
    } catch (e: ServerResponseException){
        e.printStackTrace()
        ApiResponse.Error(
            errorCode = 500,
            errorMessage = "Server error"
        )
    } catch (e: UnknownHostException){
        e.printStackTrace()
        ApiResponse.Error(
            errorCode = -2,
            errorMessage = "No internet connection"
        )
    } catch (e: Exception){
        e.printStackTrace()
        ApiResponse.Error(
            errorCode = -3,
            errorMessage = "Unknown error"
        )
    }

}