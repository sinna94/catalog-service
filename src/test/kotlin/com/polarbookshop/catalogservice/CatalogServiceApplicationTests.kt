package com.polarbookshop.catalogservice

import com.polarbookshop.catalogservice.domain.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests(
    private val webTestClient: WebTestClient
) {

    @Test
    fun whenPostRequestThenBookCreated() {
        val expectedBook = Book(isbn = "1231231231", title = "Title", author = "Author", price = 9.90, publisher = "Publisher")

        webTestClient
            .post()
            .uri("/books")
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Book::class.java).value { actualBook ->
                run {
                    assertThat(actualBook).isNotNull()
                    assertThat(actualBook.isbn).isEqualTo(expectedBook.isbn)
                }
            }
    }
}
