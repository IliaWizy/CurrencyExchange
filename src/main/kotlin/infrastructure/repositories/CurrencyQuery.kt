package infrastructure.repositories

interface CurrencyQuery<T, R> {
    fun findByCode(code: T): R?
    fun findAll(): List<R>
}
