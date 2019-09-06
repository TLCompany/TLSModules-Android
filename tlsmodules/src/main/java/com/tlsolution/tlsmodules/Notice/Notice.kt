package com.tlsolution.tlsmodules.Notice

import android.os.Parcel
import android.os.Parcelable
import com.tlsolution.tlsmodules.Extensions.formattedString
import java.util.*

data class Notice(val id: Int,
                  val title: String,
                  val content: String,
                  val date: Date
): Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readInt(),
        parcel.readString() ?: "null",
        parcel.readString() ?: "null",
        Date(parcel.readLong())
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(title)
        dest?.writeString(content)
        dest?.writeLong(date.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Notice> {
        override fun createFromParcel(source: Parcel?): Notice? {
            check(source != null) {
                return null
            }

            return Notice(source)
        }

        override fun newArray(size: Int): Array<Notice?> {
            return arrayOfNulls(size)
        }
    }

}