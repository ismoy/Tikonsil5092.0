package com.tikonsil.tikonsil509.data.remote.provider

import android.content.Context
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tikonsil.tikonsil509.utils.CompressorBitmapImage
import java.io.File

class ImagesProvider(ref: String?) {
    private var mStorage: StorageReference

    init {
        mStorage = FirebaseStorage.getInstance().reference.child(ref!!)
    }

    fun saveImage(context: Context? , image: File , idUser: String): UploadTask {
        val imageByte: ByteArray =
            CompressorBitmapImage.getImage(context , image.path , 500 , 500)
        val storage: StorageReference = mStorage.child("$idUser.jpg")
        mStorage = storage
        return storage.putBytes(imageByte)
    }

    val storage: StorageReference
        get() = mStorage
}