package com.spartancookie.hermeslogger.commands.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spartancookie.hermeslogger.R
import com.spartancookie.hermeslogger.commands.FileManager
import com.spartancookie.hermeslogger.commands.IArrangeable
import com.spartancookie.hermeslogger.commands.models.HermesCommand
import com.spartancookie.hermeslogger.commands.models.HermesFolder
import com.spartancookie.hermeslogger.commands.sortFoldersFirst
import com.spartancookie.hermeslogger.commands.ui.viewholders.FolderViewHolder
import com.spartancookie.hermeslogger.commands.ui.viewholders.CommandViewHolder
import com.spartancookie.hermeslogger.filters.ui.viewholders.BaseViewHolder

internal class FilesAdapter(private val commands: MutableList<IArrangeable>, private val folderInteraction: Callback): RecyclerView.Adapter<BaseViewHolder<*>>(){

    private var path: MutableList<HermesFolder> = mutableListOf(FileManager.rootFolder)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val type = Type.from(viewType)
        val view = layoutInflater.inflate(type.layoutRes, parent, false)
        return type.fetchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = commands[position]

        when {
            // In case the item is a filter
            item is HermesCommand && holder is CommandViewHolder -> holder.bind(item)

            // In case the item is the section string
            item is HermesFolder && holder is FolderViewHolder -> {
                holder.bind(item)
                holder.root.setOnClickListener {
                    updateItems(item.files)
                    path.add(item)
                    folderInteraction.updatePath(path)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return commands.size
    }

    override fun getItemViewType(position: Int): Int {
        // Fetch the ViewType from the Type inner enum class
        return Type.getIndex(commands[position])
    }

    // --------------- Controls ---------------

    fun updateItems(commands: MutableList<IArrangeable>) {
        this.commands.clear()
        this.commands.addAll(commands.sortFoldersFirst())
        notifyDataSetChanged()
    }

    fun exitCurrentDirectory() {
        // We need to keep the rootFolder
        if (path.size > 1) {
            path.remove(path.last())
            updatePath()
        }
    }

    fun goToRootDirectory() {
        if (path.size > 1) {
            path.removeAll(path.subList(1, path.size))
            updatePath()
        }
    }

    // ------------- Helper methods -------------

    private fun updatePath() {
        updateItems(path.last().files)
        folderInteraction.updatePath(path)
    }

    // ---------------- Type enum ----------------

    /**
     * Types of Item that can be displayed on [FilesAdapter] along with the respective
     * layout - [layoutRes] and ViewHolder creation function - [fetchViewHolder]
     */
    private enum class Type(val layoutRes: Int, val fetchViewHolder: (View) -> BaseViewHolder<*>) {
        COMMAND(R.layout.hermes_command, { v -> CommandViewHolder(v) }),
        FOLDER(R.layout.hermes_command_folder, { v -> FolderViewHolder(v) });

        companion object {

            fun from(index: Int) = values().firstOrNull { index == it.ordinal } ?: FOLDER

            fun getIndex(obj: Any?): Int {
                val type = if (obj is HermesCommand) COMMAND else FOLDER
                return type.ordinal
            }
        }
    }

    //-------------------------------------------

    interface Callback {
        /**
         * Method triggered to inform that the path changed.
         */
        fun updatePath(path: MutableList<HermesFolder>)
    }
}