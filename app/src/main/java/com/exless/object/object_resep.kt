package com.exless.`object`

import com.exless.R

object object_resep {
    data class ResepMakanan (val gambar: Int, val durasi_masak: String)

    val DaftarResep: List<ResepMakanan> = listOf(
        ResepMakanan(R.drawable.nasigoreng, "10 min"),
        ResepMakanan(R.drawable.rendang, "15 min")
    )
}