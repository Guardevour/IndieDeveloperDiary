package org.guardevour.developerdiary

object DataBaseFormats {
    val DataBaseDataTypes = mapOf(
        DataBase.MySql to listOf(
            DataType("INT", false),
            DataType("VARCHAR", true),
            DataType("TEXT", false),
            DataType("DATE", false),
            DataType("TIME", false),
            DataType("DATETIME", false),
            DataType("FLOAT", true),
            DataType("DOUBLE", true),
            DataType("DECIMAL", true),
            DataType("BOOLEAN", false),
            DataType("TINYINT", false),
            DataType("SMALLINT", false),
            DataType("MEDIUMINT", false),
            DataType("BIGINT", false),
            DataType("CHAR", true),
            DataType("BLOB", false),
            DataType("ENUM", false),
            DataType("SET", false)
        ),
        DataBase.MsSqlServer to listOf(
            DataType("INT", false),
            DataType("VARCHAR", true),
            DataType("TEXT", false),
            DataType("DATE", false),
            DataType("TIME", false),
            DataType("DATETIME", false),
            DataType("FLOAT", true),
            DataType("DECIMAL", true),
            DataType("BIT", false),
            DataType("SMALLINT", false),
            DataType("TINYINT", false),
            DataType("MONEY", false),
            DataType("SMALLMONEY", false),
            DataType("CHAR", true),
            DataType("NCHAR", true),
            DataType("BINARY", true),
            DataType("VARBINARY", true)
        ),
                DataBase.PostgresSql to listOf(
                    DataType("INTEGER", false),
                    DataType("CHARACTER VARYING", true),
                    DataType("TEXT", false),
                    DataType("DATE", false),
                    DataType("TIME", false),
                    DataType("TIMESTAMP", false),
                    DataType("REAL", true),
                    DataType("DOUBLE PRECISION", true),
                    DataType("DECIMAL", true),
                    DataType("BOOLEAN", false),
                    DataType("SMALLINT", false),
                    DataType("BIGINT", false),
                    DataType("MONEY", false),
                    DataType("BYTEA", false)
                ),
        DataBase.None to listOf<DataType>()
    )
}

data class DataType(val name: String, val isEnabledLength: Boolean){
    override fun toString(): String = name
}

enum class DataBase{
    MySql, MsSqlServer, PostgresSql, None
}