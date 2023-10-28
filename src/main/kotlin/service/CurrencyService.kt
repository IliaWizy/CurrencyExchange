package service

import data.CurrencyRepository
import data.Repository
import model.Currency

class CurrencyService {

    private val repository: Repository<Currency> by lazy { CurrencyRepository() }

    fun findAll(): List<Currency> {
        return repository.findAll()
    }

    fun findById(id: Int): Currency? {
        return repository.findById(id)
    }

    fun create(currency: Currency): Currency {
        // логика или валидация
        return repository.create(currency)
    }

    fun update(currency: Currency): Boolean {
        // логика или валидация
        return repository.update(currency)
    }

    fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    fun findByCode(code: String): Currency {
        // TODO
        return Currency(0, "sss", "www", "qw")
    }
}
