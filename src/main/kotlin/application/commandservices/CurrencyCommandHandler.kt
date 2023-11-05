package application.commandservices

import domain.Currency
import infrastructure.repositories.CurrencyCommandRepository
import interfaces.rest.dto.CurrencyDto

class CurrencyCommandHandler {

    private val commandRepository: CurrencyCommandRepository = CurrencyCommandRepository()

    fun add(dto: CurrencyDto): Currency = commandRepository.add(dto)

}
