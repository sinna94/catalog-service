package com.polarbookshop.orderservice.order.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("orders")
data class Order (
    @Id
    var id: Long = 0L,
    var bookIsbn: String,
    var bookName: String? = null,
    var bookPrice: Double? = null,
    var quantity: Int,
    var status: OrderStatus,

    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @LastModifiedDate
    var lastModifiedDate: Instant = Instant.now(),

    @Version
    var version: Long = 0L
)