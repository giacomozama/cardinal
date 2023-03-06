package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    fun invoke() = songsRepository.getAllSongs()
}

