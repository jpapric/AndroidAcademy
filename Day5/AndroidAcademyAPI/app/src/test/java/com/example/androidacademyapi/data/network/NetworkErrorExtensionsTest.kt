package com.example.androidacademyapi.data.network

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException

class NetworkErrorExtensionsTest {

    @Test
    fun `returns true when throwable is no internet error`() {
        assertTrue(UnknownHostException().isNoInternetError())
        assertTrue(RuntimeException(ConnectException()).isNoInternetError())
    }

    @Test
    fun `returns false for non network exceptions`() {
        assertFalse(IllegalArgumentException("Invalid id").isNoInternetError())
    }
}
