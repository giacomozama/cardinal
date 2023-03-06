package me.zama.cardinal.repository.song

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindCardinalRepository(cardinalRepositoryImpl: CardinalRepositoryImpl): CardinalRepository
}