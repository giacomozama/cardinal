package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    fun invoke() = songsRepository.getAllGenres()
}