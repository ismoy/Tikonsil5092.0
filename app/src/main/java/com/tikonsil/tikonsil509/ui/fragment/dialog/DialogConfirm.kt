package com.tikonsil.tikonsil509.ui.fragment.dialog

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentDialogConfirmBinding
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.fragment.mercadoPago.TakeCredentialsCardFormFragment
import com.tikonsil.tikonsil509.utils.constants.UtilsView.screenDisplay

class DialogConfirm : DialogFragment() {
    var title:String?=null
    var subtitle:String?=null
    var btnConfirm:String?=null
    var btnCancel:Boolean?=false
    var isSalesMaster:Boolean?=false
    var isSalesMasterError:Boolean?=false
    private lateinit var binding:FragmentDialogConfirmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCancelable(false)
        // Inflate the layout for this fragment
        binding = FragmentDialogConfirmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }

    private fun initComponent(){
        setSubTitles()
        setButton()
    }

    private fun setButton() {
        with(binding) {
            if (!btnConfirm.isNullOrEmpty() || btnCancel == false){
                confirm.text = btnConfirm
                confirm.isGone = false
                cancel.isGone = true
                binding.imageView.background =  ResourcesCompat.getDrawable(requireContext().resources,R.drawable.bg_iv,null)
                binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                binding.textView3.text = requireActivity().getText(R.string.aproved)
                confirm.setOnClickListener {
                    startActivity(Intent(requireActivity(),HomeActivity::class.java))
                    requireActivity().finish()
                }
            }
            if (btnCancel == true){
                cancel.isGone = false
                confirm.isGone = true
                cancel.setOnClickListener {
                 startActivity(Intent(requireActivity(),HomeActivity::class.java))
                    requireActivity().finish()
                }
            }
            if (btnCancel == true && isSalesMasterError == true){
                binding.imageView.background =  ResourcesCompat.getDrawable(requireContext().resources,R.drawable.bg_iv_er,null)
                binding.textView3.text = requireActivity().getText(R.string.error)
                cancel.setOnClickListener {
                   dialog?.dismiss()
                }
            }

            if (isSalesMaster ==true){
                binding.imageView.background =  ResourcesCompat.getDrawable(requireContext().resources,R.drawable.bg_iv,null)
                binding.textView3.text = requireActivity().getText(R.string.aproved)
                binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))

            }
        }

    }


    private fun setSubTitles() {
        if (!subtitle.isNullOrEmpty()) {
            binding.messagenomoney.text = subtitle
        }
    }
    override fun onResume() {
        super.onResume()
        val window = dialog!!.window
        val x = screenDisplay(window = window!!.windowManager, tolerance = 1.0)
        window.setLayout(x, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }
}