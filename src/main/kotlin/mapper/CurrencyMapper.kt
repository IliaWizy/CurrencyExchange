package mapper

import dto.CurrencyDto
import model.Currency

object CurrencyMapper {

    fun toDto(currency: Currency): CurrencyDto {
        return CurrencyDto(
            id = currency.id, name = currency.name, code = currency.code, sign = currency.sign
        )
    }

    fun toEntity(dto: CurrencyDto): Currency {
        return Currency(
            id = null, name = dto.name, code = dto.code, sign = dto.sign
        )
    }
}
