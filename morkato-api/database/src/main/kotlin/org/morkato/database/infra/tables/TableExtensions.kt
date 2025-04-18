package org.morkato.database.infra.tables

import org.jetbrains.exposed.sql.Table
import org.morkato.api.entity.art.ArtType

fun Table.nameType(name: String) = this.varchar(name, length = 32)
fun Table.keyType(name: String) = this.varchar(name, length = 32)
fun Table.titleType(name: String) = this.varchar(name, 96)
fun Table.artType(name: String) = this.enumerationByName<ArtType>(name, 16, klass = ArtType::class)
//fun Table.userType(name: String) = this.enumerationByName<UserType>(name, 16, klass = UserType::class)
fun Table.descriptionType(name: String) = this.varchar(name, length = 2048)
fun Table.bannerType(name: String) = this.text(name)
fun Table.namePrefixArt(name: String) = this.varchar(name, length = 32)
fun Table.attrType(name: String) = this.decimal(name, 12, 0)
fun Table.energyType(name: String) = this.decimal(name, 3, 0)
fun Table.rollType(name: String) = this.decimal(name, 3, 0)
fun Table.discordSnowflakeIdType(name: String) = this.varchar(name, length = 30)
fun Table.percentType(name: String) = this.decimal(name, 3, 0)
fun Table.idType(name: String) = this.long(name)