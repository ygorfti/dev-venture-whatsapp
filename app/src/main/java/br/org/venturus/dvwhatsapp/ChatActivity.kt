package br.org.venturus.dvwhatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.org.venturus.dvwhatsapp.databinding.ActivityChatBinding
import br.org.venturus.dvwhatsapp.repository.ChatRepository
import br.org.venturus.dvwhatsapp.repository.UserRepository
import java.util.*

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

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
        val messages = binding.messages

        ChatRepository.getMessages(chatId){
            messages.text.clear()
            for(msg in it){
                messages.text.append("${msg.message}\n")
            }
        }

        binding.btnSend.setOnClickListener{
            val msg = binding.txtMessage.text.toString()

            ChatRepository.addMessageToChat(chatId, me, msg)
        }

    }
}