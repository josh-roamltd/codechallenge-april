package joshvdh.com.codechallengeapril.screen.recordings

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import joshvdh.com.codechallengeapril.model.Recording

class RecordingsAdapter(
    private val recordings: List<Recording>,
    private val callback: RecordingsCell.Callback
) : RecyclerView.Adapter<RecordingsCell>() {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = recordings[position].getId()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) =
        RecordingsCell(parent.context)

    override fun getItemCount() = recordings.size

    override fun onBindViewHolder(cell: RecordingsCell, position: Int) {
        cell.bindData(recordings[position], callback)
    }
}