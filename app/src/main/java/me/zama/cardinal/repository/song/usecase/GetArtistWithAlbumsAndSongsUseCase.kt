package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class GetArtistWithAlbumsAndSongsUseCase @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    suspend fun invoke(id: Long) = songsRepository.getArtistWithAlbumsAndSongs(id)
}