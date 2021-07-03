package br.org.venturus.dvwhatsapp.ui.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.org.venturus.dvwhatsapp.databinding.ActivityChatBinding
import br.org.venturus.dvwhatsapp.repository.ChatRepository
import br.org.venturus.dvwhatsapp.repository.UserRepository
import java.util.*

class MessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatId = intent.getStringExtra("chatId")
        if(chatId == null) {
            finish()
            return
        }

        val me = UserRepository.myEmail()
        val messageList = binding.messageList
        val adapter = MessagesAdapter(me)
        messageList.adapter = adapter

        ChatRepository.getMessages(chatId){
            adapter.messages = it
        }

        binding.btnSend.setOnClickListener{
            val msg = binding.txtMessage.text.toString()

            ChatRepository.addMessageToChat(chatId, me, msg)
        }

    }
}