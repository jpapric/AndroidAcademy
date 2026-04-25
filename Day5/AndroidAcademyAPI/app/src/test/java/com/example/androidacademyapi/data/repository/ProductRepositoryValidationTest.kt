package com.example.androidacademyapi.data.repository

import org.junit.Assert.assertThrows
import org.junit.Test

class ProductRepositoryValidationTest {

    @Test
    fun `accepts positive product id`() {
        requireValidProductId(1)
    }

    @Test
    fun `rejects non positive product id`() {
        assertThrows(IllegalArgumentException::class.java) {
            requireValidProductId(-1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            requireValidProductId(0)
        }
    }
}
