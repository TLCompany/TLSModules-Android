package com.tlsolution.tlsmodules.Models

import android.os.Parcel
import android.os.Parcelable

data class Policy(val title: String,
                  val content: String,
                  val isMandatory: Boolean = true): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readString() ?: "null",
        parcel.readString() ?: "null",
        if (parcel.readInt() == 1) true else false
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(content)
        dest?.writeInt(if (isMandatory) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Policy> {
        override fun createFromParcel(source: Parcel?): Policy? {
            check(source != null) {
                return null
            }

            return Policy(source)
        }

        override fun newArray(size: Int): Array<Policy?> {
            return arrayOfNulls(size)
        }
    }
}