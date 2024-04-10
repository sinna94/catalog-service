package com.polarbookshop.catalogservice.domain

class BookNotFoundException(isbn: String) : RuntimeException("Book with ISBN $isbn not found")
