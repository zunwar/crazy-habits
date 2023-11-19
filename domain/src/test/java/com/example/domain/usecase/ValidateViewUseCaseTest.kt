package com.example.domain.usecase

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * Unit tests for the implementation of [ValidateViewUseCase].
 */
class ValidateViewUseCaseTest {

    val validateViewUseCase = ValidateViewUseCase()

    @Test
    fun `null input`(){
        val text: String? = null
        val result = validateViewUseCase(text)
        assertThat(result, equalTo(false))
    }

    @Test
    fun `empty input`(){
        val text = ""
        val result = validateViewUseCase(text)
        assertThat(result, equalTo(false))
    }

    @Test
    fun `ok input`(){
        val text = "lfj*a###slfj2344;;6634534123das;d;3"
        val result = validateViewUseCase(text)
        assertThat(result, equalTo(true))
    }
}