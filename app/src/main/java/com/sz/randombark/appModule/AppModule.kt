package com.sz.randombark.appModule

import com.sz.randombark.feature.data.repository.RandomDogRepository
import com.sz.randombark.feature.data.repository.RandomDogRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module that provides application-level dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides an implementation of [RandomDogRepository].
     *
     * @param service The [NetworkService] used to perform network operations.
     * @return An instance of [RandomDogRepository] implemented by [RandomDogRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideRandomDogRepository(service: NetworkService): RandomDogRepository {
        return RandomDogRepositoryImpl(service)
    }
}