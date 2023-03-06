package me.zama.cardinal.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.zama.cardinal.domain.entities.CacheInfo

@Dao
interface CacheInfoDao {

    @Query("SELECT * FROM cacheinfo WHERE info_key = :key LIMIT 1")
    suspend fun getByKey(key: String): CacheInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cacheInfo: CacheInfo)
}

suspend inline fun <reified T: Any> CacheInfoDao.get(key: String) = getByKey(key)
    ?.rawValue
    ?.let { CacheInfo.convert(T::class, it) }

suspend fun CacheInfoDao.put(key: String, value: Any) {
    insert(CacheInfo(key, value.toString()))
}