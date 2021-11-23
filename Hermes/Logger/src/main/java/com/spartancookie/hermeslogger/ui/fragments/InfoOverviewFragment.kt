package com.spartancookie.hermeslogger.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.debugToaster.LogType
import com.spartancookie.hermeslogger.debugToaster.Toaster
import com.spartancookie.hermeslogger.managers.OverviewStateHolder
import com.spartancookie.hermeslogger.managers.TabNotificationsHandler
import com.spartancookie.hermeslogger.models.LogDataHolder
import com.spartancookie.hermeslogger.ui.adapters.InfoListTabAdapter
import com.spartancookie.hermeslogger.ui.search.CustomSearch
import com.spartancookie.hermeslogger.utils.default
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.spartancookie.hermeslogger.callbacks.FragmentStateCallback
import com.spartancookie.hermeslogger.callbacks.LogSelectedCallback
import com.spartancookie.hermeslogger.utils.removeFromStack


private const val FRAGMENT_STATE_CALLBACK = "fragment_state_callback_arg"
private const val SEARCH_STRING = "Search"
const val EMPTY_STRING = ""

internal class InfoOverviewFragment : Fragment(R.layout.screen_info_overview), LogSelectedCallback {

    companion object {
        const val TAG = "InfoOverviewFragment"
        val customSearchLiveData = MutableLiveData<CustomSearch>().default(CustomSearch())

        fun newInstance(callback: FragmentStateCallback) = InfoOverviewFragment().apply {
            arguments = Bundle().apply {
                putSerializable(FRAGMENT_STATE_CALLBACK, callback)
            }
        }
    }

    private val fragmentStateCallback = arguments?.run { getSerializable(FRAGMENT_STATE_CALLBACK) as? FragmentStateCallback }

    private val searchEditText: EditText get() = requireView().findViewById(R.id.search_edit_text)
    private val clearView: View get() = requireView().findViewById(R.id.clear_image_view)
    private val capsSearchImageView: ImageView get() = requireView().findViewById(R.id.caps_search_image_view)

    private var selectedTabPosition = 0

    private val searchContent get() = searchEditText.text.toString()
    private val infoHolder get() = Toaster.instance.infoHolder

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

        val adapter = InfoListTabAdapter(this, this)
        val viewPager: ViewPager2 = view.findViewById(R.id.pager)

        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedTabPosition = position
            }
        })

        with(view.findViewById<TabLayout>(R.id.tab_layout)) {

            TabLayoutMediator(this, viewPager) { tab, position ->
                tab.setCustomView(R.layout.tab_custom_view)
                tab.customView?.let {
                    val tabName = it.findViewById(R.id.name) as TextView
                    tabName.text = if (position > 0) LogType.values()[position - 1].name else "ALL"
                }
            }.attach()

        }

        searchEditText.setText(OverviewStateHolder.currentContent)
        updateUI()

        setObservers()
    }

    private fun setSearchLogic() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateFilters(s?.toString())
                clearView.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFilters(s?.toString())
            }
        })

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            searchEditText.hint = if (searchContent.isEmpty() || hasFocus) SEARCH_STRING else EMPTY_STRING
        }

        clearView.setOnClickListener {
            updateFilters(EMPTY_STRING)
            searchEditText.setText(EMPTY_STRING)
        }

        capsSearchImageView.setOnClickListener {
            OverviewStateHolder.customSearch.matchCase = !OverviewStateHolder.customSearch.matchCase
            updateUI()
            updateFilters(OverviewStateHolder.customSearch.filterContent)
        }
    }

    //-------------------- LogSelectedCallback Implementation --------------------

    /**
     * On item from list clicked
     * @param log Information from the item clicked
     */
    override fun onLogSelected(log: LogDataHolder) {
        val fragment = InfoDetailedViewFragment.newInstance(log)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.runtimeInfoContentContainer, fragment, InfoDetailedViewFragment.TAG)
            commit()
        }
    }

    //----------------------------- Observers -------------------------

    private fun setObservers() {
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tab_layout)
        val tabNotificationsHandler = TabNotificationsHandler(tabLayout)

        infoHolder.infoLiveData.observe(viewLifecycleOwner, Observer {
            tabNotificationsHandler.updateBadges()
        })
    }

    //------------------------- Helper methods -------------------------

    /**
     * @param content Search content
     */
    private fun updateFilters(content: String?) {
        OverviewStateHolder.updateSearchContent(OverviewStateHolder.customSearch)
        OverviewStateHolder.customSearch.filterContent = content ?: EMPTY_STRING
        customSearchLiveData.postValue(OverviewStateHolder.customSearch)
    }

    private fun updateUI() {
        val capsDrawableRes = if (OverviewStateHolder.customSearch.matchCase) R.drawable.ic_hermes_logger_match_case_enabled else R.drawable.ic_hermes_logger_match_case_disabled
        capsSearchImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), capsDrawableRes))
    }
}