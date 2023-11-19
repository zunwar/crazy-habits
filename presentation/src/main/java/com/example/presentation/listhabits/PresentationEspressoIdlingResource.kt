package com.example.presentation.listhabits

import androidx.test.espresso.idling.CountingIdlingResource

object PresentationEspressoIdlingResource {
    private const val RESOURCE = "ui"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    PresentationEspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        PresentationEspressoIdlingResource.decrement() // Set app as idle.
    }
}