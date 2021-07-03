package br.org.venturus.dvwhatsapp.ui.contacts

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.org.venturus.dvwhatsapp.model.Contact
import br.org.venturus.dvwhatsapp.repository.UserRepository

class ContactsViewModel : ViewModel() {

    private val _contactsList = MutableLiveData<ArrayList<Contact>>().apply {
        UserRepository.getMyContacts {
            value = it
        }
    }
    val contactsList: LiveData<ArrayList<Contact>> = _contactsList
}