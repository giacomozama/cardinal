package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class GetArtistsWithAlbumsUseCase @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    fun invoke() = songsRepository.getArtistsWithAlbums()
}