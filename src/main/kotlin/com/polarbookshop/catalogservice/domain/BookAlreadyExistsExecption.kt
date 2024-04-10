package com.polarbookshop.catalogservice.domain

class BookAlreadyExistsExecption(isbn: String) : RuntimeException("Book with ISBN $isbn already exists")
