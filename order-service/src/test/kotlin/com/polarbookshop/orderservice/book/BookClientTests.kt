package com.polarbookshop.orderservice.book

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class BookClientTests {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var bookClient: BookClient

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toUri().toString())
            .build()
        bookClient = BookClient(webClient)
    }

    @AfterEach
    fun clean() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenBookExistsThenReturnBook() {
        val bookIsbn = "1234567890"

        val mockResponse = MockResponse()
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""
                {
                    "isbn": "$bookIsbn",
                    "title": "Book Title",
                    "author": "Book Author",
                    "price": 9.90,
                    "publisher": "Polarsophia"
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        val book = bookClient.getBookByIsbn(bookIsbn)

        StepVerifier.create(book)
            .expectNextMatches { b -> b.isbn == bookIsbn }
            .verifyComplete()
    }

}