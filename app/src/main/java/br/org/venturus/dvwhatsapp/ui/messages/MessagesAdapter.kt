package br.org.venturus.dvwhatsapp.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.org.venturus.dvwhatsapp.R
import br.org.venturus.dvwhatsapp.model.Message
import java.util.*
import kotlin.collections.ArrayList

class MessagesAdapter(private val userEmail: String) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    var messages: ArrayList<Message> = ArrayList<Message>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtMessage: TextView = view.findViewById(R.id.txtMsg)
        private val txtTime: TextView = view.findViewById(R.id.txtTime)

        fun setMessage(message: Message) {
            txtMessage.text = message.message
            txtTime.text = timeToString(message.time)
        }

        private fun timeToString(time: Long): String {
            val date = Date(time)
            return date.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].from == userEmail) 1 else 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = if(viewType == 1) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.messages_item_from, viewGroup, false)
        } else {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.messages_item_to, viewGroup, false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setMessage(messages[position])

    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
