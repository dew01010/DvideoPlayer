package com.dew.dewvideoplayer.model

import android.os.Parcel
import android.os.Parcelable

class Video(
    var description: String? = null,
    var id: String? = null,
    var thumb: String? = null,
    var title: String? = null,
    var url: String? = null):Parcelable{


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(thumb)
        parcel.writeString(title)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Video(description=$description, id=$id, thumb=$thumb, title=$title, url=$url)"
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }


}