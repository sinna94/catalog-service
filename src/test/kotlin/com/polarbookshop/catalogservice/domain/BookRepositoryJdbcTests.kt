package com.polarbookshop.catalogservice.domain

import com.polarbookshop.catalogservice.config.DataConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@DataJdbcTest
@Import(DataConfig::class)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BookRepositoryJdbcTests(
    private val bookRepository: BookRepository,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
) {

    @Test
    fun findBookByIsbnWhenExisting(){
        val bookIsbn = "1234561237"
        val book = Book(isbn = bookIsbn, title = "Title", author = "Author", price = 9.90, publisher = "Publisher")
        jdbcAggregateTemplate.insert(book)
        val actualBook = bookRepository.findByIsbn(isbn = bookIsbn)

        assertThat(actualBook).isNotNull()
        assertThat(actualBook?.isbn).isEqualTo(book.isbn)
    }
}