package com.utils

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.androidsensorengine.utils.LogUtils.TAG
import com.christianfoulcard.android.androidsensorengine.databinding.DialogFragmentBinding
import com.utils.LayoutSizingUtil.configureDefaultDialogLayoutSize
import timber.log.Timber

/** Allows the creation of a custom alert dialog fragment */
class CustomDialog : DialogFragment() {

    private var _binding: DialogFragmentBinding? = null
    private val binding get() = _binding!!

    private var titleName: String? = null
    private var descriptionName: String? = null
    private var image: Int? = null
    private var dismissDialog: Boolean? = false
    private var leaveActivity: Boolean? = false

    private val defaultFadeIn = AlphaAnimation(0.3f, 1.0f).apply{
        duration= 500
        fillAfter= true
    }

    private fun defaultAlertCustomDialogFragment(
        title: String?,
        description: String?,
        int: Int?,
        dismiss: Boolean?
    ) {
        titleName = title
        descriptionName = description
        image = int
        dismissDialog = dismiss
    }

    private fun showAlertCustomDialogFragment(fragmentManager: FragmentManager, tag: String?) {
        dialog
        show(fragmentManager, tag)
    }

    fun shouldLeaveCurrentActivity(boolean: Boolean?) {
        leaveActivity = boolean
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureDefaultDialogLayoutSize(activity, dialog)
        configureViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissFragmentOnDestroy()
    }

    private fun dismissFragmentOnDestroy() {
        try {
            if (dialog?.isShowing== true) {
                dialog?.dismiss()
            }
        } catch (e: IllegalStateException) {
            Timber.tag(TAG).e("onDestroy: " + e.message)
        }
    }

    private fun configureViews() {
        setDismissDialogProperties()
        binding.titleName.text= titleName
        binding.descriptionText.text = descriptionName
        setImageProperties()
        setOkButtonProperties()
        setBackButtonClickProperties()
    }

    /** To help direct when the user presses the back button. */
    private fun setBackButtonClickProperties() {
        dialog?.setOnKeyListener{dialog, keyCode, event->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                binding.okBtn.performClick()
            }
            true
        }
    }

    private fun setOkButtonProperties() {
        try {
            if (leaveActivity == false) {
                binding.okBtn.setOnClickListener{
                    dismiss()
                }
            } else {
                binding.okBtn.setOnClickListener{
                    dismiss()
                    activity?.finish()
                }
            }
        } catch (e: IllegalStateException) {
            Timber.tag(TAG).e("configureViews: %s", e.message)
        }
    }

    private fun setDismissDialogProperties() {
        if (dismissDialog == false) {
            dialog?.setCanceledOnTouchOutside(false)
        } else {
            dialog?.setCanceledOnTouchOutside(true)
        }
    }

    private fun setImageProperties() {
        when (image) {
            0 -> {
          //      val res = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_error_ic)
         //       image_view.setImageDrawable(res)
                binding.imageView.startAnimation(defaultFadeIn)
            }
            1 -> {
           //     val res = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_triangle_exclamation)
           //     image_view.setImageDrawable(res)
                binding.imageView.startAnimation(defaultFadeIn)
            }
            2 -> {
            //    val res = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_exclamation)
           //     image_view.setImageDrawable(res)
                binding.imageView.startAnimation(defaultFadeIn)
            }
            3 -> {
        //        val res = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_checkmark)
        //        image_view.setImageDrawable(res)
                binding.imageView.startAnimation(defaultFadeIn)
            }
            else -> {
                binding.imageView.visibility= View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private var customDialog : CustomDialog? = null

        private fun newInstance(): CustomDialog {
            return CustomDialog()
        }

        /** Constructs our alert dialog with custom properties
         *
         * Image Int Helper:
         *
         * 0 -> Red Exclamation Triangle
         *
         * 1 -> Solid White Exclamation Triangle
         *
         * 2 -> Circular Exclamation Triangle
         *
         * 3 -> Green Checkmark
         *
         *@paramtitle: Title of the alert to display
         *@paramdescription: Description related to the alert title
         *@paramint: Sets an image to be displayed depending on the integer chosen
         *@paramdismiss: Controls whether the dialog can be canceled by tapping outside the view
         *@paramfragmentManager: Returns the fragment manager the activity is associated with
         *@paramtag: Useful for fragment transactions. Acts as an identifier for the fragment
         *@paramactivity: The activity the dialog is associated with
         * */
        fun displayCustomDialog(
            title: String?,
            description: String?,
            int: Int?,
            dismiss: Boolean?,
            fragmentManager: FragmentManager,
            tag: String?,
            activity: Activity?
        ) {
            if (activity?.isDestroyed== false || activity?.isFinishing== false) {
                customDialog = newInstance()
                customDialog?.defaultAlertCustomDialogFragment(title, description, int, dismiss)
                customDialog?.showAlertCustomDialogFragment(fragmentManager, tag)
            }
        }

        /** Constructs our alert dialog with custom properties and leave current activity
         *
         * Image Int Helper:
         *
         * 0 -> Red Exclamation Triangle
         *
         * 1 -> Solid White Exclamation Triangle
         *
         * 2 -> Circular Exclamation Triangle
         *
         * 3 -> Green Checkmark
         *
         *@paramtitle: Title of the alert to display
         *@paramdescription: Description related to the alert title
         *@paramint: Sets an image to be displayed depending on the integer chosen
         *@paramdismiss: Controls whether the dialog can be canceled by tapping outside the view
         *@paramfragmentManager: Returns the fragment manager the activity is associated with
         *@paramtag: Useful for fragment transactions. Acts as an identifier for the fragment
         *@paramactivity: The activity the dialog is associated with
         * */
        fun displayAlertDialogFragmentAndDismissActivity(
            title: String?,
            description: String?,
            int: Int?,
            dismiss: Boolean?,
            fragmentManager: FragmentManager,
            tag: String?,
            activity: Activity?
        ) {
            if (activity?.isDestroyed== false || activity?.isFinishing== false) {
                customDialog = newInstance()
                customDialog?.defaultAlertCustomDialogFragment(title, description, int, dismiss)
                customDialog?.shouldLeaveCurrentActivity(true)
                customDialog?.showAlertCustomDialogFragment(fragmentManager, tag)
            }
        }
    }
}