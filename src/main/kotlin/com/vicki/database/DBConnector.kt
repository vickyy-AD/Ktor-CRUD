package com.vicki.database

import com.vicki.utility.AppConstants
import org.ktorm.database.Database

object DBConnector {

    fun configureDatabase(): Database {
        return Database.connect (
            url = AppConstants.MYSQL_DB_URL,
            driver = "com.mysql.cj.jdbc.Driver",
            user =  AppConstants.MYSQL_DB_USERNAME,
            password =  AppConstants.MYSQL_DB_PASSWORD
        )
    }
}