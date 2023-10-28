package data

interface Repository<T> {
    fun findById(id: Int): T?
    fun findAll(): List<T>
    fun create(entity: T): T
    fun update(entity: T): Boolean
    fun delete(id: Int): Boolean
}
