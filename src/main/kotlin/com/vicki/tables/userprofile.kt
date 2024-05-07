package com.vicki.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object userProfile : Table<Nothing>("userProfile") {

    var userId = int("userId").primaryKey()
    var userName = varchar("userName")
    var phoneNumber = varchar("phoneNumber")
    var password = varchar("password")
}