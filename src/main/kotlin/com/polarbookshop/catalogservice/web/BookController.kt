package com.polarbookshop.catalogservice.web

import com.polarbookshop.catalogservice.domain.Book
import com.polarbookshop.catalogservice.domain.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("books")
class BookController(
    private val bookService: BookService,
) {

    @GetMapping
    fun get(): Iterable<Book> {
        return bookService.viewBookList()
    }

    @GetMapping("{isbn}")
    fun getByIsbn(@PathVariable("isbn") isbn: String): Book {
        return bookService.viewBookDetails(isbn)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@Valid @RequestBody book: Book): Book {
        return bookService.addBookToCatalog(book)
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("isbn") isbn: String) {
        bookService.removeBookFromCatalog(isbn)
    }

    @PostMapping("{isbn}")
    fun put(@PathVariable("isbn") isbn: String, @Valid @RequestBody book: Book): Book {
        return bookService.editBookDetails(isbn, book)
    }
}