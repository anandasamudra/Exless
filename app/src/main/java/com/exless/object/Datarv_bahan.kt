package com.exless.`object`

data class datarv_bahan(var nama: String ?= null,
                        var jenismakanan: String ?=null,
                        var tglbeli: String ?=null,
                        var tglkadaluarsa: String ?=null,
                        var jumlah: String ?=null,
                        var jenissimpan: String ?=null, ){
    fun getId(): String {
        return nama+jenissimpan
    }
}



