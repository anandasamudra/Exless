package com.exless.`object`

import com.exless.R

object object_berita {
    data class ListBerita(val gambar: Int)

    val DaftarBerita: List<ListBerita> = listOf(
        ListBerita(R.drawable.berita1),
        ListBerita(R.drawable.berita2),
        ListBerita(R.drawable.berita3),
        ListBerita(R.drawable.berita4),
        ListBerita(R.drawable.berita5)
    )
}