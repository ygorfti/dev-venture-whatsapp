package br.org.venturus.dvwhatsapp.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.org.venturus.dvwhatsapp.R
import br.org.venturus.dvwhatsapp.model.Contact

class ContactsAdapter(private val onContactSelected: (contact: Contact) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    var contacts: ArrayList<Contact> = ArrayList<Contact>()

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView = view.findViewById(R.id.contact_name)
        private val contactDetails: TextView = view.findViewById(R.id.contact_details)
        val send: ImageView = view.findViewById(R.id.send)

        fun setUser(contact: Contact){
            contactName.text = contact.name
            contactDetails.text = contact.email
        }
    }

    fun setContactsList(contacts: ArrayList<Contact>){
        this.contacts = contacts
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.contacts_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUser(contacts[position])
        holder.send.setOnClickListener{
            onContactSelected(contacts[position])
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}