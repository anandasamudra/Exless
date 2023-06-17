package com.exless.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.exless.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var toRegisterTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.Email)
        passwordEditText = findViewById(R.id.pass)
        loginButton = findViewById(R.id.button)
        toRegisterTextView = findViewById(R.id.tv_toregister)
        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        fun signInWithGoogle() {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val buttonGoogle = findViewById<Button>(R.id.logingoogle)
        buttonGoogle.setOnClickListener {
            signInWithGoogle()
            fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login berhasil
                            val user = firebaseAuth.currentUser
                            // Lakukan tindakan selanjutnya dengan pengguna yang berhasil login
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Handle login failure
                            Log.e(TAG, "Firebase authentication failed", task.exception)
                        }
                    }

            }

            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)

                if (requestCode == RC_SIGN_IN) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            firebaseAuthWithGoogle(account)
                        }
                    } catch (e: ApiException) {
                        // Handle login failure
                        Log.e(TAG, "Google sign-in failed", e)
                    }
                }
            }


            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Login berhasil, lakukan tindakan yang diinginkan
                                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                                // Contoh: Pindah ke halaman utama aplikasi
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // Login gagal, tampilkan pesan error
                                Toast.makeText(
                                    this,
                                    "Login gagal: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
                }
            }

            toRegisterTextView.setOnClickListener {
                // Pindah ke halaman registrasi
                startActivity(Intent(this, Register_Activity::class.java))
                finish()
            }
        }
    }
}