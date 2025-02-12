package com.application.timer_dmb.di

import android.content.Context
import androidx.room.Room
import com.application.timer_dmb.common.UserData
import com.application.timer_dmb.data.calendar_database.CalendarDatabase
import com.application.timer_dmb.data.remote.AuthorizationRequests
import com.application.timer_dmb.data.remote.EventsRequests
import com.application.timer_dmb.data.remote.FriendsRequests
import com.application.timer_dmb.data.remote.MessageRequests
import com.application.timer_dmb.data.remote.RefreshTokenRequest
import com.application.timer_dmb.data.remote.TimerRequests
import com.application.timer_dmb.data.remote.UserRequests
import com.application.timer_dmb.data.remote.WebSocketRequests
import com.application.timer_dmb.data.remote.network.AuthRequest
import com.application.timer_dmb.data.remote.network.BaseRequest
import com.application.timer_dmb.data.remote.network.WebSocketClient
import com.application.timer_dmb.data.repository.AuthorizationRepoImpl
import com.application.timer_dmb.data.repository.EventsRepoImpl
import com.application.timer_dmb.data.repository.FriendsRepoImpl
import com.application.timer_dmb.data.repository.MessageRepoImpl
import com.application.timer_dmb.data.repository.RefreshTokenRepoImpl
import com.application.timer_dmb.data.repository.TimerRepoImpl
import com.application.timer_dmb.data.repository.UserRepoImpl
import com.application.timer_dmb.data.repository.WebSocketRepoImpl
import com.application.timer_dmb.domain.repository.AuthorizationRepository
import com.application.timer_dmb.domain.repository.EventDaoRepository
import com.application.timer_dmb.domain.repository.EventsRepository
import com.application.timer_dmb.domain.repository.FriendsRepository
import com.application.timer_dmb.domain.repository.MessageRepository
import com.application.timer_dmb.domain.repository.RefreshTokenRepository
import com.application.timer_dmb.domain.repository.TimerRepository
import com.application.timer_dmb.domain.repository.UserRepository
import com.application.timer_dmb.domain.repository.WebSocketRepository
import com.application.timer_dmb.domain.usecases.authorization.AddSoldierUseCase
import com.application.timer_dmb.domain.usecases.authorization.DeleteAccUseCase
import com.application.timer_dmb.domain.usecases.authorization.DeleteTokenUseCase
import com.application.timer_dmb.domain.usecases.authorization.GetOtpCodeUseCase
import com.application.timer_dmb.domain.usecases.authorization.GetOtpForResetPasswordUseCase
import com.application.timer_dmb.domain.usecases.authorization.GetSoldierDataUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAddedSoldierUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.authorization.LogoutUseCase
import com.application.timer_dmb.domain.usecases.authorization.LogoutWhenNoConnectionUseCase
import com.application.timer_dmb.domain.usecases.authorization.RegistrationUseCase
import com.application.timer_dmb.domain.usecases.authorization.ResetPasswordUseCase
import com.application.timer_dmb.domain.usecases.authorization.SaveTokenUseCase
import com.application.timer_dmb.domain.usecases.authorization.SendOtpForResetPasswordUseCase
import com.application.timer_dmb.domain.usecases.authorization.SendOtpUseCase
import com.application.timer_dmb.domain.usecases.authorization.SignInUseCase
import com.application.timer_dmb.domain.usecases.authorization.UpdateSoldierUseCase
import com.application.timer_dmb.domain.usecases.events.CreateBaseEventsUseCase
import com.application.timer_dmb.domain.usecases.events.CreateEventUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteEventFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.DeleteEventUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.GetAllEventsUseCase
import com.application.timer_dmb.domain.usecases.events.InsertEventIntoDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateBaseEventsUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateEventFromDatabaseUseCase
import com.application.timer_dmb.domain.usecases.events.UpdateEventUseCase
import com.application.timer_dmb.domain.usecases.messages.DeleteMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.GetGlobalChatUseCase
import com.application.timer_dmb.domain.usecases.messages.GetListOfMessagesUnregisteredUseCase
import com.application.timer_dmb.domain.usecases.messages.GetListOfMessagesUseCase
import com.application.timer_dmb.domain.usecases.messages.ReadMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.SendMessageUseCase
import com.application.timer_dmb.domain.usecases.messages.UpdateMessageUseCase
import com.application.timer_dmb.domain.usecases.timer.GetTimerDataUseCase
import com.application.timer_dmb.domain.usecases.timer.UpdateTimerUseCase
import com.application.timer_dmb.domain.usecases.user.BanUserUseCase
import com.application.timer_dmb.domain.usecases.user.DeleteBackgroundUseCase
import com.application.timer_dmb.domain.usecases.user.GetPhotoUseCase
import com.application.timer_dmb.domain.usecases.user.GetSelfUserDataUseCase
import com.application.timer_dmb.domain.usecases.user.SavePhotoUseCase
import com.application.timer_dmb.domain.usecases.user.SetUserTypeUseCase
import com.application.timer_dmb.domain.usecases.web_socket.CloseSessionUseCase
import com.application.timer_dmb.domain.usecases.web_socket.ListenToSocketUseCase
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

        install(ContentNegotiation){
            json(json)
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
                        data.setToken(it.accessToken.drop(7), it.refreshToken, it.accessTokenExpiresAt, it.refreshTokenExpiresAt)
                        BearerTokens(it.accessToken.drop(7), it.refreshToken)
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
    fun providesWebSocketListener(@Named("WebSocketClient") client: HttpClient, userData: UserData) = WebSocketClient(client, userData)

    @Provides
    @Singleton
    fun providesBaseRequest(@Named("BaseClient") client: HttpClient, json: Json) = BaseRequest(client, json)

    @Provides
    @Singleton
    fun providesAuthRequest(@Named("AuthClient") client: HttpClient, json: Json) = AuthRequest(client, json)

    //Requests

    @Provides
    @Singleton
    fun provideRefreshRequest(baseRequest: BaseRequest) = RefreshTokenRequest(baseRequest)

    @Provides
    @Singleton
    fun provideAuthorizationRequests(baseRequest: BaseRequest, authRequest: AuthRequest, @Named("AuthClient") client: HttpClient) = AuthorizationRequests(baseRequest, authRequest)

    @Provides
    @Singleton
    fun provideEventsRequests(authRequest: AuthRequest) = EventsRequests(authRequest)

    @Provides
    @Singleton
    fun provideFriendsRequests(authRequest: AuthRequest) = FriendsRequests(authRequest)

    @Provides
    @Singleton
    fun provideMessageRequests(authRequest: AuthRequest, baseRequest: BaseRequest) = MessageRequests(authRequest, baseRequest)

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

    @Provides
    @Singleton
    fun provideMessageRepoImpl(request: MessageRequests) : MessageRepository = MessageRepoImpl(request)

    @Provides
    @Singleton
    fun provideEventsRepoImpl(request: EventsRequests) : EventsRepository = EventsRepoImpl(request)

    @Provides
    @Singleton
    fun provideFriendsRepoImpl(request: FriendsRequests) : FriendsRepository = FriendsRepoImpl(request)

    @Provides
    @Singleton
    fun provideTimerRepoImpl(request: TimerRequests) : TimerRepository = TimerRepoImpl(request)

    @Provides
    @Singleton
    fun provideWebsocketRepoImpl(request: WebSocketRequests) : WebSocketRepository = WebSocketRepoImpl(request)

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

    @Provides
    @Singleton
    fun provideDeleteAccUseCase(authorizationRepository: AuthorizationRepository) = DeleteAccUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideGetGlobalChatUseCase(messageRepository: MessageRepository) = GetGlobalChatUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetListOfMessagesUseCase(messageRepository: MessageRepository) = GetListOfMessagesUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideSendMessageUseCase(messageRepository: MessageRepository) = SendMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideListenToSocketUseCase(webSocketRepository: WebSocketRepository) = ListenToSocketUseCase(webSocketRepository)

    @Provides
    @Singleton
    fun provideGetAllEventsUseCase(eventsRepository: EventsRepository) = GetAllEventsUseCase(eventsRepository)

    @Provides
    @Singleton
    fun provideReadMessagesUseCase(messageRepository: MessageRepository) = ReadMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideUpdateMessageUseCase(messageRepository: MessageRepository) = UpdateMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideDeleteMessageUseCase(messageRepository: MessageRepository) = DeleteMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideCreateBaseEventsUseCase(dao: EventDaoRepository) = CreateBaseEventsUseCase(dao)

    @Provides
    @Singleton
    fun provideUpdateBaseEventsUseCase(dao: EventDaoRepository) = UpdateBaseEventsUseCase(dao)

    @Provides
    @Singleton
    fun provideUpdateSoldierUseCase(context: Context) = UpdateSoldierUseCase(context)

    @Provides
    @Singleton
    fun provideGetAllEventsFromDatabaseUseCase(dao: EventDaoRepository) = GetAllEventsFromDatabaseUseCase(dao)

    @Provides
    @Singleton
    fun provideDeleteBackgroundUseCase(@ApplicationContext context: Context) = DeleteBackgroundUseCase(context)

    @Provides
    @Singleton
    fun provideDeleteEventFromDatabaseUseCase(dao: EventDaoRepository) = DeleteEventFromDatabaseUseCase(dao)

    @Provides
    @Singleton
    fun provideInsertEventIntoDatabaseUseCase(dao: EventDaoRepository) = InsertEventIntoDatabaseUseCase(dao)

    @Provides
    @Singleton
    fun provideUpdateEventFromDatabaseUseCase(dao: EventDaoRepository) = UpdateEventFromDatabaseUseCase(dao)

    @Provides
    @Singleton
    fun provideSetUserTypeUseCase(userRepository: UserRepository) = SetUserTypeUseCase(userRepository)

    @Provides
    @Singleton
    fun provideCreateEventUseCase(eventsRepository: EventsRepository) = CreateEventUseCase(eventsRepository)

    @Provides
    @Singleton
    fun provideDeleteAllEventsFromDatabaseUseCase(dao: EventDaoRepository) = DeleteAllEventsFromDatabaseUseCase(dao)

    @Provides
    @Singleton
    fun provideDeleteEventUseCase(eventsRepository: EventsRepository) = DeleteEventUseCase(eventsRepository)

    @Provides
    @Singleton
    fun provideUpdateEventUseCase(eventsRepository: EventsRepository) = UpdateEventUseCase(eventsRepository)

    @Provides
    @Singleton
    fun provideUpdateTimerUseCase(timerRepository: TimerRepository) = UpdateTimerUseCase(timerRepository)

    @Provides
    @Singleton
    fun provideGetTimerDataUseCase(timerRepository: TimerRepository) = GetTimerDataUseCase(timerRepository)

    @Provides
    @Singleton
    fun provideGetOtpForResetPasswordUseCase(authorizationRepository: AuthorizationRepository) = GetOtpForResetPasswordUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideSendOtpForResetPasswordUseCase(authorizationRepository: AuthorizationRepository) = SendOtpForResetPasswordUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(authorizationRepository: AuthorizationRepository) = ResetPasswordUseCase(authorizationRepository)

    @Provides
    @Singleton
    fun provideGetListOfMessagesUnregisteredUseCase(messageRepository: MessageRepository) = GetListOfMessagesUnregisteredUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideBanUserUseCase(userRepository: UserRepository) = BanUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideLogoutWhenNoConnectionUseCase(userData: UserData, authRequest: AuthRequest) = LogoutWhenNoConnectionUseCase(userData, authRequest)

    //Database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CalendarDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = CalendarDatabase::class.java,
            name = "calendar.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDaoRepository(db: CalendarDatabase) : EventDaoRepository = db.dao


    @Provides
    @Singleton
    fun provideCloseSessionUseCase(webSocketRepository: WebSocketRepository) = CloseSessionUseCase(webSocketRepository)
}