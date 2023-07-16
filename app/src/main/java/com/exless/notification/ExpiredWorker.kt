package com.exless.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class ExpiredWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {
    private lateinit var dbref: DatabaseReference

    override fun doWork(): Result {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")

        var count = 0 // hampir
        var count2 = 0 // expired

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentDate = LocalDate.now()

                    for (bahanSnapshot in snapshot.children) {
                        try {
                            val bahanDateStr = bahanSnapshot.child("tglkadaluarsa").getValue(String::class.java)
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val bahanDate = LocalDate.parse(bahanDateStr, formatter)
                            val daysBetween = ChronoUnit.DAYS.between(currentDate, bahanDate)

                            if (daysBetween <= 7 && daysBetween >= 0) {
                                count++
                            }
                            if (daysBetween < 0) {
                                count2++
                            }
                        } catch (e: Exception) {
                            continue
                        }
                    }

                    var notificationTitle = "Check kulkas yuk!" // default text 1 atas
                    var notificationMessage = "Makan apa ya hari ini?" // default text 2 bawah
                    if (count == 0 && count2 == 0) {
                        notificationTitle = "Bahan segar, badan pun bugar!" // semuanya segar
                        notificationMessage = "Yuk masak bahan-bahan mu selagi segar!"
                    }
                    if (count > 0 && count2 == 0) {
                        notificationTitle = "Walaupun keadaan belum mendesak," // ada yang hampir kadaluarsa
                        notificationMessage = "$count bahan mau kedaluarsa, yuk di masak!"
                    }
                    if (count == 0 && count2 > 0) {
                        notificationTitle = "Sayangnya nasi telah menjadi bubur," // ada yang kadaluarsa
                        notificationMessage = "$count2 bahan yang kadaluarsa jangan di kubur!"
                    }
                    if (count > 0 && count2 > 0) {
                        notificationTitle = "Gawat! $count2 bahan telah kadaluarsa," // ada yang kadaluarsa dan hampir
                        notificationMessage = "Dan $count sudah dekat kadaluarsa!"
                    }

                    NotificationHelper(context).createNotification(notificationTitle, notificationMessage)

                    // Remove the ValueEventListener to stop listening for further changes
                    dbref.removeEventListener(this)
                }

                if (!snapshot.exists()) {
                    val notificationTitle = "Bahan segar, badan pun bugar!" // default text 1 atas
                    val notificationMessage = "Makan apa ya hari ini?" // default text 2 bawah
                    NotificationHelper(context).createNotification(notificationTitle, notificationMessage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        }

        // Add the ValueEventListener to start listening for data changes
        dbref.addListenerForSingleValueEvent(valueEventListener)

        return Result.success()
    }
}