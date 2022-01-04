package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.spartancookie.hermeslogger.GhostFragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.callbacks.FragmentStateCallback
import com.spartancookie.hermeslogger.callbacks.EventSelectedCallback
import com.spartancookie.hermeslogger.core.HermesHandler
import com.spartancookie.hermeslogger.core.OverviewStateHolder
import com.spartancookie.hermeslogger.core.models.EventDataHolder
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.utils.EMPTY_STRING
import com.spartancookie.hermeslogger.utils.default
import com.spartancookie.hermeslogger.utils.removeFromStack
import kotlinx.android.synthetic.main.screen_info_overview.*
import kotlinx.android.synthetic.main.search_bar.*

@GhostFragment
internal class InfoOverviewFragment : Fragment(R.layout.screen_info_overview), EventSelectedCallback {

    private val fragmentStateCallback = arguments?.run { getSerializable(FRAGMENT_STATE_CALLBACK) as? FragmentStateCallback }
    private val searchContent get() = search_edit_text.text.toString()

    override fun onDetach() {
        fragmentStateCallback?.onFragmentDismissed()
        removeFromStack(parentFragmentManager, InfoDetailedViewFragment.TAG)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        savedInstanceState?.run {
            // if we're returning from a saved instance, remove this fragment and the details
            removeFromStack(parentFragmentManager, InfoDetailedViewFragment.TAG)
            removeFromStack(parentFragmentManager, TAG)
        }

        setSearchLogic()


        val fragment = InfoListFragment.newInstance(this)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.runtimeOverviewFrameLayout, fragment, InfoDetailedViewFragment.TAG)
            commit()
        }

        search_edit_text.setText(OverviewStateHolder.currentContent)
        updateUI()

        fabButton.setOnClickListener {
            Toast.makeText(context, HermesHandler.buildStats(), Toast.LENGTH_LONG).show()
        }
    }

    private fun setSearchLogic() {
        search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateFilters(s?.toString())
                clear_image_view.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFilters(s?.toString())
            }
        })

        // Handle the EditText's hint visibility
        search_edit_text.setOnFocusChangeListener { _, hasFocus ->
            search_edit_text.hint = if (searchContent.isEmpty() || hasFocus) SEARCH_STRING else EMPTY_STRING
        }

        // Handle the clear button action
        clear_image_view.setOnClickListener {
            updateFilters(EMPTY_STRING)
            search_edit_text.setText(EMPTY_STRING)
        }

        // Handle the click on the enable/disable match case
        caps_search_image_view.setOnClickListener {
            OverviewStateHolder.customSearch.matchCase = !OverviewStateHolder.customSearch.matchCase
            updateUI()
            updateFilters(OverviewStateHolder.customSearch.filterContent)
        }
    }

    //-------------------- LogSelectedCallback Implementation --------------------

    override fun onLogSelected(event: EventDataHolder) {
        val fragment = InfoDetailedViewFragment.newInstance(event)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.runtimeInfoContentContainer, fragment, InfoDetailedViewFragment.TAG)
            commit()
        }
    }

    //------------------------- Helper methods -------------------------

    /**
     * Triggered the logs recycler view update, and inform the [CustomSearch] of the new search [content]
     */
    private fun updateFilters(content: String?) {
        OverviewStateHolder.updateSearchContent(OverviewStateHolder.customSearch)
        OverviewStateHolder.customSearch.filterContent = content ?: EMPTY_STRING
        customSearchLiveData.postValue(OverviewStateHolder.customSearch)
    }

    private fun updateUI() {
        val capsDrawableRes = if (OverviewStateHolder.customSearch.matchCase) {
            R.drawable.ic_hermes_logger_match_case_enabled
        } else {
            R.drawable.ic_hermes_logger_match_case_disabled
        }
        caps_search_image_view.setImageDrawable(ContextCompat.getDrawable(requireContext(), capsDrawableRes))
    }

    companion object {
        const val TAG = "InfoOverviewFragment"

        private const val FRAGMENT_STATE_CALLBACK = "fragment_state_callback_arg"
        private const val SEARCH_STRING = "Search"

        val customSearchLiveData = MutableLiveData<CustomSearch>().default(CustomSearch())

        fun newInstance(callback: FragmentStateCallback) = InfoOverviewFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FRAGMENT_STATE_CALLBACK, callback)
            }
        }
    }
}