package com.polarbookshop.catalogservice.domain

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class BookValidationTests {
    companion object {
        private lateinit var validator: Validator

        @BeforeAll
        @JvmStatic
        fun setup() {
            validator = Validation.buildDefaultValidatorFactory().validator
        }
    }


    @Test
    fun whenAllFieldsCorrectThenValidationSucceeds() {
        val book = Book(isbn = "1234567890", title = "Title", author = "Author", price = 9.90, publisher = "Publisher")
        val violations = validator.validate(book)
        assertThat(violations).isEmpty()
    }

    @Test
    fun whenIsbnDefinedButIncorrectThenValidationFails() {
        val book = Book(isbn = "a234567890", title = "Title", author = "Author", price = 9.90, publisher = "Publisher")
        val violations = validator.validate(book)
        assertThat(violations).hasSize(1)
        assertThat(violations.iterator().next().message)
            .isEqualTo("The ISBN format must be valid.")
    }
}