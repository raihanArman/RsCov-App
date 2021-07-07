package id.co.data.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hospital (
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("nama")
    @Expose
    val name: String,

    @SerializedName("lokasi")
    @Expose
    val location: String,

    @SerializedName("telp")
    @Expose
    val number_telp: String,

    @SerializedName("gambar")
    @Expose
    val image: String,


    @SerializedName("jarak")
    @Expose
    val jarak: String?= "0",
)