package infrastructure.repositories

interface CurrencyCommand<T, R> {
    fun add(dto: T): R
}
