package com.polarbookshop.catalogservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalogServerApplication

fun main(args: Array<String>) {
    runApplication<CatalogServerApplication>(*args)
}
