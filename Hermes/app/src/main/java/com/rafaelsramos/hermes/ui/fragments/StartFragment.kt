package com.rafaelsramos.hermes.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rafaelsramos.hermes.R
import com.rafaelsramos.hermes.ui.activities.MainActivity
import com.rafaelsramos.hermes.utils.saveHermesPreference
import com.spartancookie.hermeslogger.core.HermesConfigurations
import com.spartancookie.tree.HermesTree
import kotlinx.android.synthetic.main.screen_start_fragment_controls.*
import kotlinx.android.synthetic.main.screen_start_fragment_redirections.*
import timber.log.Timber

class StartFragment: BaseFragment(R.layout.screen_start_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setControls()
        setRedirections()
    }

    private fun setControls() {
        initialize_hermes.setOnClickListener {
            saveHermesPreference(requireContext(), !HermesConfigurations.isEnabled)
            (activity as? MainActivity)?.triggerRebirth()
        }

        plant_timber_tree.setOnClickListener {
            Timber.plant(HermesTree())
            showToast("HermesTree planted...")
        }
    }

    private fun setRedirections() {
        sample_timber_recycler_button.setOnClickListener { loadFragment(SampleTimberRecyclerFragment()) }
        single_trigger_button.setOnClickListener { loadFragment(SingleTriggerFragment()) }
        automatic_trigger_button.setOnClickListener { loadFragment(AutomaticTriggerFragment()) }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}