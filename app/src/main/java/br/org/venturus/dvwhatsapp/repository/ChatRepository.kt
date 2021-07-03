package br.org.venturus.dvwhatsapp.repository

import android.util.Log
import br.org.venturus.dvwhatsapp.model.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

object ChatRepository {
    private const val TAG: String = "ChatRepository"
    private val db by lazy { Firebase.firestore }
    private val currentUserEmail by lazy { UserRepository.myEmail() }

    fun getMessages(chatId: String, onComplete: (ArrayList<Message>) -> Unit) {
        val docRef = db.collection("chats")
            .document(chatId)
            .collection("messages").addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onComplete(ArrayList<Message>())
                    Log.e(TAG, e.localizedMessage)
                    return@addSnapshotListener
                }

                val messages = ArrayList<Message>()
                if (snapshot != null) {
                    for (msg in snapshot) {
                        val m = msg.toObject<Message>()
                        messages.add(m)
                    }
                    messages.sortBy { it.time }
                    onComplete(messages)
                } else {
                    Log.d(TAG, "snapshot is null")
                }
            }
    }

    private fun createChatId(email1: String, email2: String): String {
        return if (email1.compareTo(email2) > 0) "$email1-$email2" else "$email2-$email1"
    }

    fun getChatWith(contactEmail: String, onComplete: (chatId: String, e: String?) -> Unit) {
        val chat = createChatId(currentUserEmail, contactEmail)
        db.collection("chats")
            .document(chat)
            .get()
            .addOnSuccessListener {
                onComplete(it.id, null)
            }
            .addOnFailureListener {
                onComplete("", it.localizedMessage)
            }
    }

    fun addMessageToChat(chatId: String, from: String, message: String) {
        val data = hashMapOf(
            "from" to from,
            "message" to message,
            "time" to Date().time
        )
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .document()
            .set(data)
    }
}