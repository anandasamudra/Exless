package com.exless.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.exless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class Profile_Activity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
//    val image = findViewById<ImageView>(R.id.img_profiluser)
private lateinit var imageView: ImageView
    private lateinit var selectPhotoButton: Button
    private lateinit var uploadButton: Button
    private lateinit var viewSelect: View

    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        imageView = findViewById(R.id.img_profiluser)
        viewSelect = findViewById(R.id.viewselect)
        uploadButton = findViewById(R.id.btn_saveprofile)

//
        storageRef = FirebaseStorage.getInstance().reference
        databaseRef = FirebaseDatabase.getInstance().reference

        imageView.setOnClickListener {
            openImagePicker()
            println("ini select button")
        }

        uploadButton.setOnClickListener {
            uploadPhoto()
            println("ini uppppppp")
        }
        try {
            getphoto()
        }catch (e:Exception){
            println("Ini profile eror photo ="+e)
        }

    }

    fun tohome(view: View) {
        val imageUrl = 1
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }
    fun addphoto(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    fun addtodatabase(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun getphoto() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {
            val userPhotosRef = FirebaseDatabase.getInstance().getReference("/Users/$currentUserId/photos")

            userPhotosRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this@Profile_Activity)
                            .load(imageUrl)
                            .into(imageView)

                    } else {
                        // Handle the case when the image URL is not available
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }
    }
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Glide.with(this).load(imageUri).into(imageView)
        }
    }

    private fun uploadPhoto() {
        try {
            if (imageUri != null) {
                val imageRef = storageRef.child("images/${UUID.randomUUID()}")
                val uploadTask = imageRef.putFile(imageUri!!)

                uploadTask.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            val photoData = HashMap<String, Any>()
                            photoData["imageUrl"] = uri.toString()

                            databaseRef.child("Users").child(userId).child("photos").setValue(photoData)
                            println(photoData)
                        }
                    }
                }.addOnFailureListener {
                    // Handle the upload failure
                }
            }
        }catch (e:Exception){
            println("Ini uploadPhoto ="+e)
        }

    }
}
