package com.dew.dewvideoplayer.model

import android.os.Parcel
import android.os.Parcelable

class MetaData(  val id: String? = null,
         val duration: Long = 0,
         val playbackPosition: Long = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong()
    ) {


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(duration)
        parcel.writeLong(playbackPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "MetaData(id=$id, duration=$duration, playbackPosition=$playbackPosition)"
    }

    companion object CREATOR : Parcelable.Creator<MetaData> {
        override fun createFromParcel(parcel: Parcel): MetaData {
            return MetaData(parcel)
        }

        override fun newArray(size: Int): Array<MetaData?> {
            return arrayOfNulls(size)
        }
    }

}