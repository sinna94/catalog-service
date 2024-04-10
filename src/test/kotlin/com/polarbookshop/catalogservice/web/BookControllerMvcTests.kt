package com.polarbookshop.catalogservice.web

import com.polarbookshop.catalogservice.domain.BookNotFoundException
import com.polarbookshop.catalogservice.domain.BookService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BookControllerMvcTests(
    private val mockMvc: MockMvc,
    @MockBean private val bookService: BookService,
) {

    @Test
    fun whenGetBookNotExistsThenShouldReturn404() {
        val isbn = "73737313940"
        BDDMockito.given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException(isbn))
        mockMvc.perform(get("/books/$isbn"))
            .andExpect(status().isNotFound())
    }

}