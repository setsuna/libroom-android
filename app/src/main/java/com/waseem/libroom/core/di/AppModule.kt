package com.waseem.libroom.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.waseem.libroom.feature.auth.data.MeetingDataRepositoryImpl
import com.waseem.libroom.feature.auth.domain.GetMeetingInfo
import com.waseem.libroom.feature.auth.domain.GetMeetingInfoImpl
import com.waseem.libroom.feature.auth.domain.MeetingDataRepository
import com.waseem.libroom.feature.auth.domain.UpdateMeetingInfo
import com.waseem.libroom.feature.auth.domain.UpdateMeetingInfoImpl
import com.waseem.libroom.feature.author.data.AuthorRepositoryImpl
import com.waseem.libroom.feature.author.domain.AuthorRepository
import com.waseem.libroom.feature.book.data.BookRepositoryImpl
import com.waseem.libroom.feature.book.domain.BookRepository
import com.waseem.libroom.feature.category.data.CategoryRepositoryImpl
import com.waseem.libroom.feature.category.domain.CategoryRepository
import com.waseem.libroom.feature.root.data.DeviceInfoRepositoryImpl
import com.waseem.libroom.feature.root.data.UserPreferenceRepositoryImpl
import com.waseem.libroom.feature.root.device.DeviceDataRepository
import com.waseem.libroom.feature.root.device.GetDeviceInfo
import com.waseem.libroom.feature.root.device.GetDeviceInfoImpl
import com.waseem.libroom.feature.root.device.UniqueIdManager
import com.waseem.libroom.feature.root.device.UpdateDeviceInfo
import com.waseem.libroom.feature.root.device.UpdateDeviceInfoImpl
import com.waseem.libroom.feature.root.domain.GetAuthState
import com.waseem.libroom.feature.root.domain.GetAuthStateImpl
import com.waseem.libroom.feature.root.domain.UpdateAuthState
import com.waseem.libroom.feature.root.domain.UpdateAuthStateImpl
import com.waseem.libroom.feature.root.domain.UserPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    fun provideBookRepository(): BookRepository {
        return BookRepositoryImpl()
    }

    @Provides
    fun provideAuthRepository(): AuthorRepository {
        return AuthorRepositoryImpl()
    }

    @Provides
    fun provideCategoryRepository(): CategoryRepository {
        return CategoryRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_prefs")
            //context.preferencesDataStoreFile("device_info")
        }
    }

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(dataStore: DataStore<Preferences>): UserPreferenceRepository {
        return UserPreferenceRepositoryImpl(dataStore)
    }

    @Provides
    fun provideGetAuthState(userPreferenceRepository: UserPreferenceRepository): GetAuthState {
        return GetAuthStateImpl(userPreferenceRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateAuthState(userPreferenceRepository: UserPreferenceRepository): UpdateAuthState {
        return UpdateAuthStateImpl(userPreferenceRepository)
    }
/**
    @Provides
    @Singleton
    fun provideDeviceInfoDataStore(@ApplicationContext context: Context):DataStore<Preferences>{
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("device_info")
        }
    }
**/
    @Provides
    fun provideDeviceInfoRepository(dataStore: DataStore<Preferences>): DeviceDataRepository {
        return DeviceInfoRepositoryImpl(dataStore)
    }

    @Provides
    fun provideGetDeviceInfo(deviceInfoRepository: DeviceDataRepository): GetDeviceInfo {
        return GetDeviceInfoImpl(deviceInfoRepository)
    }

    @Provides
    fun provideUpdateDeviceInfo(deviceInfoRepository: DeviceDataRepository): UpdateDeviceInfo {
        return UpdateDeviceInfoImpl(deviceInfoRepository)
    }

    @Provides
    @Singleton
    fun provideUniqueIdManager(@ApplicationContext context: Context): UniqueIdManager {
        return UniqueIdManager(context)
    }
    // UpdateMeetingINfo
    @Provides
    fun provideUpdateMeetingInfo(dataStore: DataStore<Preferences>):MeetingDataRepository{
        return MeetingDataRepositoryImpl(dataStore)
    }

    @Provides
    fun provideGetMeetingInfo(meetingDataRepository: MeetingDataRepository):GetMeetingInfo{
        return GetMeetingInfoImpl(meetingDataRepository)
    }

    @Provides
    fun provideUpdateMeetingInfo(meetingDataRepository: MeetingDataRepository):UpdateMeetingInfo{
        return UpdateMeetingInfoImpl(meetingDataRepository)
    }
}