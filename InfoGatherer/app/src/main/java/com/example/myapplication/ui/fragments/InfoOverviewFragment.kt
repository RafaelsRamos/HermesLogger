package com.example.myapplication.ui.fragments

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
import com.example.myapplication.R
import com.example.myapplication.callbacks.FragmentCommunicator
import com.example.myapplication.callbacks.SpecificItemCallback
import com.example.myapplication.debugToaster.LogType
import com.example.myapplication.debugToaster.Toaster
import com.example.myapplication.managers.OverviewStateHolderUpdater
import com.example.myapplication.managers.TabNotificationsHandler
import com.example.myapplication.models.LogDataHolder
import com.example.myapplication.ui.adapters.InfoListTabAdapter
import com.example.myapplication.ui.search.CustomSearch
import com.example.myapplication.utils.default
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


const val EMPTY_STRING = ""
private const val SEARCH_STRING = "Search"

class InfoOverviewFragment : Fragment(R.layout.screen_info_overview), SpecificItemCallback {

    companion object {
        val customSearchLiveData = MutableLiveData<CustomSearch>().default(CustomSearch())
    }

    lateinit var stateHolder: OverviewStateHolderUpdater
    lateinit var communicator: FragmentCommunicator

    private lateinit var tabNotificationsHandler: TabNotificationsHandler
    private lateinit var adapter: InfoListTabAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var searchEditText: EditText
    private lateinit var clearView: View
    private lateinit var capsSearchImageView: ImageView

    private var selectedTabPosition = 0

    private val searchContent get() = searchEditText.text.toString()
    private val infoHolder get() = Toaster.instance.infoHolder

    private val customSearch by lazy { stateHolder.customSearch }

    override fun onDetach() {
        communicator.onFragmentDetached()
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initViews(view)
        setSearchLogic()

        adapter = InfoListTabAdapter(this, this)
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

            tabNotificationsHandler = TabNotificationsHandler(this)

        }

        searchEditText.setText(stateHolder.currentContent)
        updateUI()

        setObservers()
    }

    private fun initViews(view: View) {
        searchEditText = view.findViewById(R.id.search_edit_text)
        clearView = view.findViewById(R.id.clear_image_view)
        viewPager = view.findViewById(R.id.pager)
        capsSearchImageView = view.findViewById(R.id.caps_search_image_view)
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
            customSearch.ignoreCase = !customSearch.ignoreCase
            updateUI()
            updateFilters(customSearch.filterContent)
        }
    }

    //-------------------- SpecificItemCallback Implementation --------------------

    override fun onSpecificItemClicked(item: LogDataHolder) {
        val fragment = InfoDetailedViewFragment(item)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.runtimeInfoContentContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }

    //----------------------------- Observers -------------------------

    private fun setObservers() {
        infoHolder.infoLiveData.observe(this, Observer {
            tabNotificationsHandler.updateBadges()
        })
    }

    //------------------------- Helper methods -------------------------

    /**
     * @param content Search content
     */
    private fun updateFilters(content: String?) {
        stateHolder.onCustomSearchChanged(customSearch)
        customSearch.filterContent = content ?: EMPTY_STRING
        customSearchLiveData.postValue(customSearch)
    }

    private fun updateUI() {
        val capsDrawableRes = if (customSearch.ignoreCase) R.drawable.ic_match_case_enabled else R.drawable.ic_match_case_disabled
        capsSearchImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), capsDrawableRes))
    }
}