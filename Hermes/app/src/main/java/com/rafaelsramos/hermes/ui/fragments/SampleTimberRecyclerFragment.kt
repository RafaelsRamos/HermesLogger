package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.ui.adapters.SampleTimberAdapter
import kotlinx.android.synthetic.main.screen_sample_timber_recycler_fragment.*
import timber.log.Timber
import java.lang.Exception

/**
 * Sample of a Fragment with a recycler view, logged using timber
 */
class SampleTimberRecyclerFragment: BaseFragment(R.layout.screen_sample_timber_recycler_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.setOnClickListener {
            activity?.onBackPressed()
        }

        // Should be fetched from a Presenter or ViewModel
        fetchInformation()

        fillViews()
    }

    private fun fillViews() {
        with(sample_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            val items = (1..100).map { "Item $it" }
            adapter = SampleTimberAdapter(items)
        }
    }

    private fun fetchInformation() {
        Timber.v("Fetching information to fill view")
        makeService1Call()
        sortService1Call()

        try {
            makeService2Call()
        } catch (e: Exception) {
            Timber.e(e.cause)
        }

        retryService2Call()
    }

    private fun sortService1Call() {
        Timber.v("Sorting Service 1...")
    }

    private fun makeService1Call() {
        Timber.i("Making Service 1 call...")
        Timber.i("Service 1 call succeeded with the following result ...")
    }

    private fun makeService2Call() {
        Timber.i("Making Service 2 call...")
        throw Exception("Service call timed out.")
    }

    private fun retryService2Call() {
        Timber.i("Making Service 2 call...")
        Timber.i("Service 2 call succeeded with the following result ...")
    }

}