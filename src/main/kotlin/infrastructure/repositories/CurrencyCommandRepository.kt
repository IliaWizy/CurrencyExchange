package infrastructure.repositories

import domain.Currency
import interfaces.rest.dto.CurrencyDto
import util.DBLoader
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import javax.sql.DataSource

class CurrencyCommandRepository : CurrencyCommand<CurrencyDto, Currency> {

    private val ds: DataSource = DBLoader.getDataSource()

    override fun add(dto: CurrencyDto): Currency {
        lateinit var connect: Connection
        lateinit var ps: PreparedStatement

        try {
            connect = ds.connection
            ps = connect.prepareStatement(
                "INSERT INTO currency (code, full_name, sign) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            )

            ps.setString(1, dto.code)
            ps.setString(2, dto.name)
            ps.setString(3, dto.sign)

            ps.executeUpdate()

            val generatedKeys = ps.generatedKeys

            if (generatedKeys.next()) {
                val id = generatedKeys.getInt(1)
                return Currency(id, dto.code, dto.name, dto.sign)
            } else {
                throw SQLException("Не удалось создать валюту, идентификатор не получен.")
            }
        } finally {
            ps.close()
            connect.close()
        }
    }


}
