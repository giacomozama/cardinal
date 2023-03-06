package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class GetAlbumsWithArtist @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    fun invoke() = songsRepository.getAlbumsWithArtist()
}
