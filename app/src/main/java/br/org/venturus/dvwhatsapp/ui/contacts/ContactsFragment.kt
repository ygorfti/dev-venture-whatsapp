package br.org.venturus.dvwhatsapp.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import br.org.venturus.dvwhatsapp.ChatActivity
import br.org.venturus.dvwhatsapp.databinding.FragmentContactsBinding
import br.org.venturus.dvwhatsapp.model.Contact
import br.org.venturus.dvwhatsapp.repository.ChatRepository
import br.org.venturus.dvwhatsapp.repository.UserRepository

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)

        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val contactsList: RecyclerView = binding.contactsList
        val adapter: ContactsAdapter = ContactsAdapter { contact ->
            onContactSelected(contact)
        }
        contactsViewModel.contactsList.observe(viewLifecycleOwner, Observer {
            adapter.setContactsList(it)
        })

        contactsList.adapter = adapter


        return root
    }

    private fun onContactSelected(contact: Contact) {
        val email = UserRepository.myEmail()
        ChatRepository.getChatWith(email) { chatId, e ->
            if (e != null) {
                Toast.makeText(context, e, Toast.LENGTH_LONG).show()
            } else {
                goToChat(chatId)
            }
        }
    }

    private fun goToChat(chatId: String) {
        val intent: Intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(
            "chatId",
            chatId
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}