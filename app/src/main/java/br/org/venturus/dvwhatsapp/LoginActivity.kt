package br.org.venturus.dvwhatsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.org.venturus.dvwhatsapp.model.User
import br.org.venturus.dvwhatsapp.repository.UserRepository
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            activityResult(result.resultCode, result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            resultLauncher.launch(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()
            )
        }
    }

    private fun activityResult(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val current = FirebaseAuth.getInstance().currentUser?.apply {
                val user: User = User(
                    name = this.displayName ?: "NO NAME",
                    email = this.email ?: "NO EMAIL"
                )
                UserRepository.addUser(user, {
                    goToMain()
                }, {
                    failToLogin(it)
                })
            }
        } else {
            failToLogin(response?.error?.message)
        }

    }

    fun failToLogin(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun goToMain() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}