package com.exless.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.exless.adapter.adapter_bahan
import com.exless.`object`.datarv_bahan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var dbref : DatabaseReference

    override fun onReceive(context: Context, intent: Intent) {
        AndroidThreeTen.init(context)


        var currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref = FirebaseDatabase.getInstance().getReference("/Users/"+currentuser+"/Inventory")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var jumlahkadaluarsa = 0
                    var jumlahbahaya = 0
                    var cekbahan :String
                    var notification :String = "Semua terlihat baik"
                    for (bahanSnapshot in snapshot.children){

                        cekbahan = bahanSnapshot.child("tglkadaluarsa").getValue().toString()
                        try {
                            val startDateStr = getCurrentDate()
                            val endDateStr = cekbahan
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val startDate = LocalDate.parse(startDateStr, formatter)
                            val endDate = LocalDate.parse(endDateStr, formatter)
                            val days = ChronoUnit.DAYS.between(startDate, endDate)
                            if (endDate.isBefore(startDate)) {
                                jumlahkadaluarsa++
                            }
                            if (days >= 0 && days <= 7) {
                                jumlahbahaya++
                            }

                        } catch (e: Exception) {
                            // Handle any exceptions that might occur
//                            tglkadal = "EXPIRED"
                            println("An error occurred: ${e.message}")
                        }

                        }
                    if (jumlahbahaya == 0 && jumlahkadaluarsa ==0){
                        notification = "bahan aman kokkk"
                    }
                    if (jumlahbahaya > 0 && jumlahkadaluarsa ==0){
                        notification = "duh ada $jumlahbahaya yang bahaya"
                    }
                    if (jumlahbahaya == 0 && jumlahkadaluarsa >=0){
                        notification = "duh ada $jumlahkadaluarsa yang kadaluarsa"
                    }
                    bikinnotifikasi(notification,context)
                    }

                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })





    }
    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val year = c.get(Calendar.YEAR)
        return String.format("%02d/%02d/%04d", day, month, year)
    }
    private fun bikinnotifikasi(pesan:String, ctx: Context){
//        val desiredCalendar = Calendar.getInstance()
//        desiredCalendar.set(Calendar.HOUR_OF_DAY, 23)
//        desiredCalendar.set(Calendar.MINUTE, 5)
//        desiredCalendar.set(Calendar.SECOND, 0)
//        val currentTimeMillis = System.currentTimeMillis()
//        // Start the WorkManager periodic task here
//        val workRequest = PeriodicWorkRequest.Builder(
//            TodoWorker::class.java,
//            15, // Repeat interval (in days)
//            TimeUnit.MINUTES
//        )
//            .setInitialDelay(5,TimeUnit.SECONDS)
//            .setInputData(workDataOf("TITLE" to "Ini keadaan simpananmu", "MESSAGE" to pesan))
//            .build()
//
//        // Enqueue the work request
//        WorkManager.getInstance(ctx).enqueue(workRequest)
    }
}