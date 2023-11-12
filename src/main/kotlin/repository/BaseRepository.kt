package repository

import java.sql.SQLException
import java.util.*


interface BaseRepository<T> {
    @Throws(SQLException::class)
    fun create(entity: T): T

    @Throws(SQLException::class)
    fun findById(id: Int?): T?

    @Throws(SQLException::class)
    fun findAll(): List<T>?

    @Throws(SQLException::class)
    fun update(entity: T)

    @Throws(SQLException::class)
    fun delete(id: Int)
}
