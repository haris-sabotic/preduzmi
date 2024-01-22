package com.ets.preduzmi.models

import android.os.Parcel
import android.os.Parcelable

data class BusinessModel (
    var id: Int,
    var name: String,
    var legalType: String,
    var type: String,
    var description: String,
    var photo: String,
    var postedBy: UserModel,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(UserModel::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(legalType)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeString(photo)
        parcel.writeParcelable(postedBy, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BusinessModel> {
        override fun createFromParcel(parcel: Parcel): BusinessModel {
            return BusinessModel(parcel)
        }

        override fun newArray(size: Int): Array<BusinessModel?> {
            return arrayOfNulls(size)
        }
    }
}
