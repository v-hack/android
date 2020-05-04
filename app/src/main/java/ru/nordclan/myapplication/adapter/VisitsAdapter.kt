package ru.nordclan.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.api.VisitResponse
import kotlin.random.Random

class VisitsAdapter(private val items: Array<VisitResponse>) :
    RecyclerView.Adapter<VisitsAdapter.VisitsViewHolder>() {

    private val dateFormatter: DateTimeFormatter = DateTimeFormat.shortDate()

    class VisitsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val avatarView: ImageView = itemView.findViewById(R.id.img_avatar)
        val doctorName: TextView = itemView.findViewById(R.id.txt_doctor_name)
        val doctorSpec: TextView = itemView.findViewById(R.id.txt_doctor_spec)
        val date: TextView = itemView.findViewById(R.id.txt_date)
        val imageDate: ImageView = itemView.findViewById(R.id.img_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitsViewHolder =
        VisitsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.visit_card_layout, parent, false)
        )

    override fun onBindViewHolder(holder: VisitsViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            if (item.date.isBefore(LocalDateTime.now())) {
                val res =
                    if (Random.nextBoolean()) R.drawable.ic_down_red_17 else R.drawable.ic_up_green_17
                Glide.with(imageDate)
                    .load(res)
                    .into(imageDate)
            } else {
                Glide.with(imageDate)
                    .load(R.drawable.ic_clock_12)
                    .into(imageDate)
            }

            Glide.with(avatarView)
                .load(R.mipmap.ic_doctor)
                .circleCrop()
                .into(avatarView)
            date.text = item.date.toString(dateFormatter)
            doctorName.text = item.doctorFullName
            doctorSpec.text = item.specialization
        }
    }

    override fun getItemCount() =
        items.size
}
