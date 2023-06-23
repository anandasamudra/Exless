package com.exless.view

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.exless.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var toRegisterTextView: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleButton: Button

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.Email)
        passwordEditText = findViewById(R.id.pass)
        loginButton = findViewById(R.id.button)
        googleButton = findViewById(R.id.logingoogle)
        toRegisterTextView = findViewById(R.id.tv_toregister)
        firebaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            //sharedpreferences untuk login
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogin", true).apply()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login berhasil, lakukan tindakan yang diinginkan
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            // sharepreferences untuk login username dan password
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogin", true).apply()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Login gagal, tampilkan pesan error
                            Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
            }
        }

        // Login dengan akun Google
        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        toRegisterTextView.setOnClickListener {
            // Pindah ke halaman registrasi
            startActivity(Intent(this, Register_Activity::class.java))
            finish()
        }
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    firebaseAuthWithGoogle(credential)
                } else {
                    Toast.makeText(this, "Login dengan akun Google gagal", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Login dengan akun Google gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login dengan akun Google berhasil
                    Toast.makeText(this, "Login dengan akun Google berhasil", Toast.LENGTH_SHORT).show()
                    // menympan data sharedpreferences dengan login google
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogin", true).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // Login dengan akun Google gagal
                    Toast.makeText(this, "Login dengan akun Google gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }
}