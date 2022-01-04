package com.spartancookie.hermeslogger.commands.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.spartancookie.hermeslogger.GhostFragment
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.commands.FileManager
import com.spartancookie.hermeslogger.commands.models.HermesFolder
import com.spartancookie.hermeslogger.commands.sortFoldersFirst
import com.spartancookie.hermeslogger.commands.ui.adapters.FilesAdapter
import com.spartancookie.hermeslogger.ui.decorators.MarginItemDecoration
import kotlinx.android.synthetic.main.hermes_path_controller.*
import kotlinx.android.synthetic.main.hermes_screen_filters.*

@GhostFragment
internal class FilesFragment: Fragment(R.layout.hermes_screen_commands), FilesAdapter.Callback {

    private val filesSortedFoldersFirst get() = FileManager.rootFolder.files.sortFoldersFirst().toMutableList()

    private val mAdapter: FilesAdapter by lazy { FilesAdapter(filesSortedFoldersFirst, this@FilesFragment) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (filters_recycler) {
            this.adapter = mAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            addItemDecoration(MarginItemDecoration.withMargins(10, 0, 10, 10))
        }

        back_button.setOnClickListener { mAdapter.exitCurrentDirectory() }
        back_button.setOnLongClickListener { mAdapter.goToRootDirectory()
            true
        }

        FileManager.rootFolderLiveData.observe(viewLifecycleOwner, { folder ->
            mAdapter.updateItems(folder.files)
            mAdapter.goToRootDirectory()
        })

    }

    override fun updatePath(path: MutableList<HermesFolder>) {
        path_tv.text = path.map { it.name }.joinToString(separator = " > ") { it }
    }

    companion object {
        fun newInstance() = FilesFragment()
    }
}