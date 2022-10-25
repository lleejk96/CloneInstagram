package com.gitauto.cloneinstgram.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitauto.cloneinstgram.R
import com.gitauto.cloneinstgram.adapter.DetailViewAdapter
import com.gitauto.cloneinstgram.data.Status
import com.gitauto.cloneinstgram.viewmodel.MainViewModel
import com.gitauto.cloneinstgram.viewmodel.MainViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail_view.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlin.concurrent.thread
import android.app.Activity
import android.opengl.Visibility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class DetailViewFragment : Fragment() {
    companion object{
        const val TAG = "DetailViewFragment"
    }
    private lateinit var detailViewAdapter: DetailViewAdapter
    private val viewModel : MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.toolbar?.backArrow?.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_detail_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")
        initView()
        initData()
        viewModel.requestContentData()
    }

    private fun initData(){
        viewModel.contentLiveData.observe(viewLifecycleOwner, {
            if (it.status == Status.SUCCESS){
                viewModel.contentUidListLiveData.observe(viewLifecycleOwner, { it2 ->
                    if (it2.status == Status.SUCCESS){
                        detailViewAdapter.setList(it.data, it2.data)
                        detailViewAdapter.notifyDataSetChanged()
                    }
                })

            }
        })
        viewModel.contentFavoriteLiveData.observe(viewLifecycleOwner,{
            Log.d(TAG, "initData: ${it.status}")
            if (it.status == Status.SUCCESS)
                detailViewAdapter.uploadFavoriteStatus(it.data)
        })
    }
    private fun initView(){
        detailViewAdapter = DetailViewAdapter()
        item_recyclerView.apply {
            adapter = detailViewAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        item_recyclerView.adapter = detailViewAdapter
        item_recyclerView.layoutManager = LinearLayoutManager(activity)

        detailViewAdapter.setListener(object : DetailViewAdapter.OnItemClickListener{
            override fun onItemClick(pos: Int) {
                Log.d(TAG, "onItemClick: ")
                viewModel.uploadFavorite(pos)
            }
        }, object : DetailViewAdapter.OnUserClickListener{
            override fun onUserClick(uid: String, userId: String) {
                val userFragment = UserFragment()
                val bundle = Bundle()
                bundle.putString("uid",uid)
                bundle.putString("userId",userId)
                userFragment.arguments = bundle
                activity?.bottom_navigation?.selectedItemId = R.id.action_account
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,userFragment)?.commit()
            }
        })
    }

}