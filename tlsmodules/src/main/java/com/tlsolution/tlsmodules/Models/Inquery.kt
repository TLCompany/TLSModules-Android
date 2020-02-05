package com.tlsolution.tlsmodules.Models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Inquery(val id: Int,
                   val content: String,
                   val answer: String?,
                   val date: Date,
                   val isAnswered: Boolean): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readInt(),
        parcel.readString() ?: "null",
        parcel.readString(),
        Date(parcel.readLong()),
        if (parcel.readInt() == 1) true else false
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(content)
        dest?.writeString(answer)
        dest?.writeLong(date.time)
        dest?.writeInt(if (isAnswered) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Inquery> {
        override fun createFromParcel(source: Parcel?): Inquery? {
            check(source != null) {
                return null
            }
            return Inquery(source)
        }

        override fun newArray(size: Int): Array<Inquery?> {
            return arrayOfNulls(size)
        }
    }

}