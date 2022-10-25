package com.gitauto.cloneinstgram.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitauto.cloneinstgram.LoginActivity
import com.gitauto.cloneinstgram.MainActivity
import com.gitauto.cloneinstgram.R
import com.gitauto.cloneinstgram.adapter.UserViewAdapter
import com.gitauto.cloneinstgram.data.Status
import com.gitauto.cloneinstgram.databinding.FragmentUserBinding
import com.gitauto.cloneinstgram.viewmodel.MainViewModel
import com.gitauto.cloneinstgram.viewmodel.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment() {
    companion object{
        const val TAG = "UserFragment"
    }
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
    }
    private lateinit var userViewAdapter : UserViewAdapter
    private lateinit var binding: FragmentUserBinding

    private var uid : String? = null
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    private var currentUser : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater,container,false)
        uid = arguments?.getString("uid")

        if (uid == currentUserUid || uid == null){
            //My Profile
            Log.d(TAG, "onCreateView: true")
            currentUser = currentUserUid
            binding.userFollowBtn.text = getString(R.string.signout)
            binding.userFollowBtn.setOnClickListener {
                requestLogOut()
                activity?.finish()
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            activity?.toolbar?.backArrow?.visibility = View.GONE
            binding.userImage.setOnClickListener {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                activity?.setResult(10, photoPickerIntent)
            }
        }else{
            //Select Profile
            currentUser = uid
            binding.userFollowBtn.text = getString(R.string.follow)
            activity?.toolbar?.backArrow?.visibility = View.VISIBLE
            activity?.toolbar?.backArrow?.setOnClickListener{
                activity?.bottom_navigation?.selectedItemId = R.id.action_home
            }
            binding.userFollowBtn.setOnClickListener {
            }
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        initView()
        initData()
    }
    private fun initData(){
        requestData()
    }
    private fun initView(){
        userViewAdapter = UserViewAdapter()
        val width = resources.displayMetrics.widthPixels / 3
        userViewAdapter.setWidth(width)
        contentRecycler.apply {
            adapter = userViewAdapter
            layoutManager = GridLayoutManager(activity,3)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun requestData(){
        Log.d(TAG, "requestData: $currentUser")
        currentUser?.let { viewModel.requestUserContentData(it) }
        viewModel.userContentLiveData.observe(viewLifecycleOwner,{
            if (it.status == Status.SUCCESS){
                binding.postCount.text = it.data?.size?.toString()
                it.data?.let { itData -> userViewAdapter.setList(itData) }
                userViewAdapter.notifyDataSetChanged()
            }
        })
    }
    private fun requestLogOut(){
        viewModel.requestLogOut()
    }
}