package com.application.militarychatproject.di

import android.content.Context
import com.application.militarychatproject.common.UserData
import com.application.militarychatproject.data.remote.AuthorizationRequests
import com.application.militarychatproject.data.remote.EventsRequests
import com.application.militarychatproject.data.remote.FriendsRequests
import com.application.militarychatproject.data.remote.MessageRequests
import com.application.militarychatproject.data.remote.RefreshTokenRequest
import com.application.militarychatproject.data.remote.TimerRequests
import com.application.militarychatproject.data.remote.UserRequests
import com.application.militarychatproject.data.remote.WebSocketRequests
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
import com.application.militarychatproject.data.remote.network.WebSocketClient
import com.application.militarychatproject.data.repository.AuthorizationRepoImpl
import com.application.militarychatproject.data.repository.RefreshTokenRepoImpl
import com.application.militarychatproject.data.repository.UserRepoImpl
import com.application.militarychatproject.domain.repository.AuthorizationRepository
import com.application.militarychatproject.domain.repository.RefreshTokenRepository
import com.application.militarychatproject.domain.repository.UserRepository
import com.application.militarychatproject.domain.usecases.authorization.AddSoldierUseCase
import com.application.militarychatproject.domain.usecases.authorization.DeleteTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.GetOtpCodeUseCase
import com.application.militarychatproject.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.militarychatproject.domain.usecases.authorization.IsAddedSoldierUseCase
import com.application.militarychatproject.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.militarychatproject.domain.usecases.authorization.LogoutUseCase
import com.application.militarychatproject.domain.usecases.authorization.RegistrationUseCase
import com.application.militarychatproject.domain.usecases.authorization.SaveTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.SendOtpUseCase
import com.application.militarychatproject.domain.usecases.authorization.SignInUseCase
import com.application.militarychatproject.domain.usecases.user.GetPhotoUseCase
import com.application.militarychatproject.domain.usecases.user.GetSelfUserDataUseCase
import com.application.militarychatproject.domain.usecases.user.SavePhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("BaseClient")
    fun providesBaseClient(json: Json) = HttpClient(OkHttp){
        install(Logging){
            level = LogLevel.ALL
        }
        install(ContentNegotiation){
            json(json)
        }
        install(HttpTimeout){
            connectTimeoutMillis = 10_000L
            requestTimeoutMillis = 10_000L
            socketTimeoutMillis = 10_000L
        }

    }

    @Singleton
    @Provides
    @Named("WebSocketClient")
    fun providesWebSocketClient(json: Json) = HttpClient(OkHttp){
        install(Logging){
            level = LogLevel.ALL
        }

        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(json)
        }
    }

    @Singleton
    @Provides
    @Named("AuthClient")
    fun providesAuthClient(json: Json, data: UserData, refreshTokenRequest: RefreshTokenRequest) = HttpClient(OkHttp){
        install(Logging){
            level = LogLevel.ALL
        }
        install(ContentNegotiation){
            json(json)
        }
        install(HttpTimeout){
            connectTimeoutMillis = 10_000L
            requestTimeoutMillis = 10_000L
            socketTimeoutMillis = 10_000L
        }

        install(Auth){
            bearer {
                loadTokens {
                    data.loadToken()
                    val accessToken = data.token.value.accessToken
                    val refreshToken = data.token.value.refreshToken
                    BearerTokens(accessToken, refreshToken)
                }

                refreshTokens {
                    val refreshToken = data.token.value.refreshToken
                    val newToken = refreshTokenRequest.refreshTokenRequest(refreshToken)
                    newToken.data?.let {
                        data.setToken(it.accessToken, it.refreshToken, it.accessTokenExpiresAt, it.refreshTokenExpiresAt)
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
            }
        }

    }


    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun providesJson(): Json = Json{
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesUserData(context: Context) = UserData(context)

    @Provides
    @Singleton
    fun providesWebSocketListener(@Named("WebSocketClient") client: HttpClient) = WebSocketClient(client)

    @Provides
    @Singleton
    fun providesBaseRequest(@Named("BaseClient") client: HttpClient, json: Json) = BaseRequest(client, json)

    @Provides
    @Singleton
    fun providesAuthRequest(@Named("AuthClient") client: HttpClient, json: Json) = AuthRequest(client, json)

    @Provides
    @Singleton
    fun provideRefreshRequest(baseRequest: BaseRequest) = RefreshTokenRequest(baseRequest)

    //Requests

    @Provides
    @Singleton
    fun provideAuthorizationRequests(baseRequest: BaseRequest, authRequest: AuthRequest) = AuthorizationRequests(baseRequest, authRequest)

    @Provides
    @Singleton
    fun provideEventsRequests(authRequest: AuthRequest) = EventsRequests(authRequest)

    @Provides
    @Singleton
    fun provideFriendsRequests(authRequest: AuthRequest) = FriendsRequests(authRequest)

    @Provides
    @Singleton
    fun provideMessageRequests(authRequest: AuthRequest) = MessageRequests(authRequest)

    @Provides
    @Singleton
    fun provideTimerRequests(authRequest: AuthRequest) = TimerRequests(authRequest)

    @Provides
    @Singleton
    fun provideUserRequests(baseRequest: BaseRequest, authRequest: AuthRequest) = UserRequests(baseRequest, authRequest)

    @Provides
    @Singleton
    fun provideWebSocketRequests(client: WebSocketClient) = WebSocketRequests(client)

    //Repos

    @Provides
    @Singleton
    fun provideAuthRepoImpl(requests: AuthorizationRequests) : AuthorizationRepository = AuthorizationRepoImpl(requests)

    @Provides
    @Singleton
    fun provideUserRepoImpl(requests: UserRequests) : UserRepository = UserRepoImpl(requests)

    @Provides
    @Singleton
    fun provideRefreshTokenRepoImpl(request: RefreshTokenRequest) : RefreshTokenRepository = RefreshTokenRepoImpl(request)

    //UseCases

    @Provides
    @Singleton
    fun provideIsAuthorizedUseCase(userData: UserData) = IsAuthorizedUseCase(userData)

    @Provides
    @Singleton
    fun provideSignInUseCase(authorizationRepository: AuthorizationRepository) = SignInUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideIsAddedSoldierUseCase(context: Context) = IsAddedSoldierUseCase(context)

    @Provides
    @Singleton
    fun provideAddSoldierUseCase(context: Context) = AddSoldierUseCase(context)

    @Provides
    @Singleton
    fun provideSaveTokenUseCase(userData: UserData) = SaveTokenUseCase(userData)

    @Provides
    @Singleton
    fun provideGetSoldierDataUseCase(context: Context) = GetSoldierDataUseCase(context)

    @Provides
    @Singleton
    fun provideGetOtpCodeUseCase(authorizationRepository: AuthorizationRepository) = GetOtpCodeUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideRegistrationUseCase(authorizationRepository: AuthorizationRepository) = RegistrationUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideSendOtpUseCase(authorizationRepository: AuthorizationRepository) = SendOtpUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideGetSelfUserDataUseCase(userRepository: UserRepository) = GetSelfUserDataUseCase(userRepository)

    @Provides
    @Singleton
    fun provideLogoutUseCase(authorizationRepository: AuthorizationRepository) = LogoutUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideDeleteTokenUseCase(userData: UserData) = DeleteTokenUseCase(userData)

    @Provides
    @Singleton
    fun provideSavePhotoUseCase(userRepository: UserRepository) = SavePhotoUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetPhotoUseCase(userRepository: UserRepository) = GetPhotoUseCase(userRepository)
}