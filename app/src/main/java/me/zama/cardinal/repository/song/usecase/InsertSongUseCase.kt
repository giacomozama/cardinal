package me.zama.cardinal.repository.song.usecase

import me.zama.cardinal.domain.entities.SongInfo
import me.zama.cardinal.repository.song.CardinalRepository
import javax.inject.Inject

class InsertSongUseCase @Inject constructor(
    private val songsRepository: CardinalRepository
) {

    suspend fun invoke(songInfo: SongInfo) = songsRepository.insert(songInfo)
}
