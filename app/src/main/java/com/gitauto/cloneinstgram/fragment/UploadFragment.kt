package com.gitauto.cloneinstgram.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gitauto.cloneinstgram.R
import com.gitauto.cloneinstgram.base.BaseFragment
import com.gitauto.cloneinstgram.data.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_upload.*
import java.text.SimpleDateFormat
import java.util.*

class UploadFragment : BaseFragment() {
    private var storage : FirebaseStorage? = null
    private var photoUri : Uri? = null
    private var auth : FirebaseAuth? = null
    private var fireStore : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        startActivityForResult(photoPickerIntent, 0)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                Log.d("TAG", "onActivityResult: ")
                photoUri = data?.data
                Log.e("TAG", "onActivityCreated: $photoUri")
                addphoto_image.setImageURI(photoUri)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        uploadBtn.setOnClickListener {
            contentUpload()
        }
        addphoto_image.setImageURI(photoUri)
    }

    @SuppressLint("SimpleDateFormat")
    private fun contentUpload(){
        context?.let { showLoadingDialog(it) }
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "IMAGE_" + timestamp + "_.png"
        val storageRef = storage?.reference?.child("images")?.child(imageFileName)

        photoUri?.let {
            storageRef?.putFile(it)?.continueWithTask {
                return@continueWithTask storageRef.downloadUrl
            }?.addOnSuccessListener { uri ->
                removeLoadingDialog()
                val contentDTO = ContentDTO()
                contentDTO.imageUrl = uri.toString()
                contentDTO.uid = auth?.currentUser?.uid
                contentDTO.userId = auth?.currentUser?.email
                contentDTO.explain = addphoto_edit_explain.text?.toString() ?: " "
                contentDTO.timestamp = System.currentTimeMillis()
                fireStore?.collection("images")?.document()?.set(contentDTO)
                activity?.setResult(Activity.RESULT_OK)
                Toast.makeText(context, getString(R.string.upload_success),Toast.LENGTH_LONG).show()
                parentFragmentManager.beginTransaction().replace(R.id.main_content, DetailViewFragment()).commit()
            }
        }
    }
}