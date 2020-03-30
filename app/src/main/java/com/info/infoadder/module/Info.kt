package com.info.infoadder.module

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import java.security.Timestamp

@Parcelize
class Info(
    var id: String = "",
    var name: String="",
    var image :String="",
    var timestamp: String="",
    val discriptin:String="",
    val phone:String="",
    val email:String="",
    val facebook:String="",
    val instagram:String="",
    val address:String=""
) : Parcelable {}
