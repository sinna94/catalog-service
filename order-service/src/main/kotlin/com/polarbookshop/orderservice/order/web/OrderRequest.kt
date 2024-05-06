package com.polarbookshop.orderservice.order.web

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class OrderRequest(
    @NotBlank(message = "The book ISBN is must defined.")
    val isbn: String,

    @Min(value = 1, message = "You must order at least one book.")
    @Max(value = 5, message = "you cannot order more than 5 books.")
    val quantity: Int
)
