package com.gitauto.cloneinstgram.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gitauto.cloneinstgram.data.ContentDTO
import com.gitauto.cloneinstgram.data.Result
import com.gitauto.cloneinstgram.data.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContentRepository {
    private var contentDTO : ArrayList<ContentDTO> = arrayListOf()
    private var userContentDTO : ArrayList<ContentDTO> = arrayListOf()
    private var contentUidList : ArrayList<String> = arrayListOf()
    private val fireStore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var auth : FirebaseAuth? = null
    var contentDTOLiveData = MutableLiveData<Result<ArrayList<ContentDTO>>>()
    var contentUidListLiveData = MutableLiveData<Result<ArrayList<String>>>()
    var contentFavoriteLiveData = MutableLiveData<Result<String>>()
    var userContentDTOLiveData = MutableLiveData<Result<ArrayList<ContentDTO>>>()
    init {
        auth = FirebaseAuth.getInstance()
    }
    fun requestContent(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("tag", Thread.currentThread().toString())
            fireStore.collection("images").orderBy("timestamp",  Query.Direction.DESCENDING).addSnapshotListener { querySnapShot, error ->
                contentDTO.clear()
                contentUidList.clear()
                if (querySnapShot == null) return@addSnapshotListener
                for (snapShot in querySnapShot.documents) {
                    val item = snapShot.toObject(ContentDTO::class.java)
                    contentDTO.add(item!!)
                    contentUidList.add(snapShot.id)
                }
                contentDTOLiveData.postValue(Result(Status.SUCCESS, contentDTO, null))
                contentUidListLiveData.postValue(Result(Status.SUCCESS, contentUidList, null))
            }
        }
    }
    fun requestUserContent(user : String?){
        CoroutineScope(Dispatchers.IO).launch {
            fireStore.collection("images").whereEqualTo("uid", user ?: uid).addSnapshotListener { querySnapShot, error ->
                userContentDTO.clear()
                for (snapShot in querySnapShot?.documents!!) {
                    snapShot.toObject(ContentDTO::class.java)?.let { userContentDTO.add(it) }
                }
                userContentDTOLiveData.postValue(Result(Status.SUCCESS, userContentDTO, null))
            }
        }
    }
    fun uploadFavorite(position : Int){
        Log.d("TAG", "uploadFavorite repository: $position")
        val tsDoc = fireStore.collection("images").document(contentUidList[position])
        fireStore.runTransaction { transaction ->
            val content = transaction.get(tsDoc).toObject(ContentDTO::class.java)
            if (content?.favorites?.containsKey(uid) == true){
                content.favoriteCount = content.favoriteCount.minus(1)
                content.favorites.remove(uid)
            }else{
                content?.favoriteCount= content?.favoriteCount?.plus(1)!!
                content.favorites[uid!!] = true
            }
            transaction.set(tsDoc, content)
        }
        if (contentDTO[position].favorites.containsKey(uid))
            contentFavoriteLiveData.postValue(Result(Status.SUCCESS, "like", null))
        else
            contentFavoriteLiveData.postValue(Result(Status.SUCCESS, "unlike",null))
    }
    fun requestLogOut(){
        auth?.signOut()
    }
}