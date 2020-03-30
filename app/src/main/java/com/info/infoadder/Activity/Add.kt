package com.info.infoadder.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.info.infoadder.R
import com.info.infoadder.module.Info
import kotlinx.android.synthetic.main.activity_add.*
import java.text.DateFormat
import java.util.*

const val PATH="info"
const val ID="id"
const val NAME ="name"
const val IMAGE="image "
const val TimeZone="timeZone"
const val DISCRPTION="discriptin"
const val PHONE ="phone"
const val EMAIL="email"
const val FACEBOOK="facebook"
const val INSTAGRAM="instagram"
const val ADDRESS="address"
class Add : AppCompatActivity() {
    val Ref : DatabaseReference = FirebaseDatabase.getInstance().getReference(PATH)
    companion object {
        val TAG = "AddActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        imageButton.setOnClickListener {
            Log.d(TAG, "demo woeks ")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)}
        button_save.setOnClickListener {
            uploadImageToFirebaseStorage(selectPhotoUri)
        }}
    var selectPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")
            val uri=data.data
            selectPhotoUri=uri
            val bitmap= MediaStore.Images.Media.getBitmap(contentResolver,uri)
            imageButton.setImageBitmap(bitmap)
           }}
    private fun uploadImageToFirebaseStorage(selectPhotoUri: Uri?) {
        if (this.selectPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(this.selectPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")
                    saveInfoToFirebaseDatabase(it.toString())}}
            .addOnFailureListener {
                Log.d(TAG, "Failed to upload image to storage: ${it.message}")}}
    private fun saveInfoToFirebaseDatabase(profileImageUrl: String) {
        //val uid = FirebaseAuth.getInstance().uid ?: ""
        // val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val info = Info(
            id = Ref.push().key!!,
            name = univer_name.text.toString(),
            image = profileImageUrl!!.toString(),
            timestamp = getTimestamp(),
            discriptin = editText_discriptin.text.toString(),
            phone = editText_phone.text.toString(),
            email = editText_email.text.toString(),
            facebook = editText_facebook.text.toString(),
            instagram = editText_instagram.text.toString(),
            address = editText_address.text.toString())
        info.id=Ref.push().key!!
        Ref.child(info.name).setValue(info)
//info.timestamp= getTimestamp()
//info.image=profileImageUrl
//Ref.setValue(info)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)}
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")}}

    private fun getTimestamp(): String {
        val date = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateTimeInstance()
        return dateFormat.format(date)
    }
    fun deleteInfo(info: Info){
        // Ref.child(info.id).setValue(null)
    }

    fun updateInfo(info: Info){
        Ref.child(info.id).child(NAME).setValue(info.name)
        Ref.child(info.id).child(DISCRPTION).setValue(info.discriptin)
        Ref.child(info.id).child(IMAGE).setValue(info.image)
        Ref.child(info.id).child(TimeZone).setValue(getTimestamp())
        Ref.child(info.id).child(PHONE).setValue(info.phone)
        Ref.child(info.id).child(EMAIL).setValue(info.email)
        Ref.child(info.id).child(FACEBOOK).setValue(info.facebook)
        Ref.child(info.id).child(INSTAGRAM).setValue(info.instagram)
        Ref.child(info.id).child(ADDRESS).setValue(info.address)
        Ref.child(info.id).child(INSTAGRAM).setValue(info.instagram)}


    fun createInfo (info: Info){
        info.id=Ref.push().key!!
        info.timestamp= getTimestamp()
        Ref.child(info.id).setValue(info)
    }

    fun saveInfo(info: Info){
        if (info.id==null )createInfo(info)
        else updateInfo(info)
    }

}
