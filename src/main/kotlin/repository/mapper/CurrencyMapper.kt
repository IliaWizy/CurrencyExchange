package repository.mapper

import model.Currency
import java.sql.ResultSet


fun ResultSet.toCurrency() = Currency(
    id = getInt("id"),
    name = getString("name"),
    code = getString("code"),
    sign = getString("sign")
)
