package ru.nordclan.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.api.DrugResponse

class PreparationsAdapter(private val items: MutableList<DrugResponse>) :
    RecyclerView.Adapter<PreparationsAdapter.DrugViewHolder>(), ItemTouchHelperAdapter {

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position);
    }

    class DrugViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameView: TextView = itemView.findViewById(R.id.txt_name)
        val typeView: TextView = itemView.findViewById(R.id.txt_type)
        val tmpView: TextView = itemView.findViewById(R.id.txt_tmp)
        val periodView: TextView = itemView.findViewById(R.id.txt_period)
        val frequencyView: TextView = itemView.findViewById(R.id.txt_frequency)
//        val statusView: TextView = itemView.findViewById(R.id.txt_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder =
        DrugViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.preparation_card_ex_layout, parent, false)
        )

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val item = items[position]

//        item.times

        with(holder) {
            nameView.text = item.name
            typeView.text = item.drugType
            tmpView.text = item.featuresReception

            if (item.startReception != null && item.finishReception != null) {
                periodView.text = HtmlCompat.fromHtml(
                    periodView.resources.getString(
                        R.string.tmp_1_1,
                        item.startReception.toString("dd.MM"),
                        item.finishReception.toString("dd.MM")
                    ), HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }

            frequencyView.text = HtmlCompat.fromHtml(
                frequencyView.resources.getString(
                    R.string.tmp_2_1,
                    item.frequencyAdmission
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    override fun getItemCount() =
        items.size
}
