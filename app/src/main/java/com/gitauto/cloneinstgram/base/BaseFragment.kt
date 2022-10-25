package com.gitauto.cloneinstgram.base
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gitauto.cloneinstgram.R
import kotlin.math.roundToInt


abstract class BaseFragment : Fragment() {
    private var mContext: Context? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showLoadingDialog(context: Context?){
        mContext = context
        loadingDialog = LoadingDialog(context)
        loadingDialog?.setCancelable(false)
        loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        loadingDialog?.show()
    }

    fun removeLoadingDialog(){
        if (loadingDialog == null)
            return
        loadingDialog?.dismiss()
    }

    inner class LoadingDialog(mContext : Context?) : Dialog(mContext!!) {
        var textView : TextView
        init {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_loading)
            textView = findViewById(R.id.connect)
        }
    }
}
