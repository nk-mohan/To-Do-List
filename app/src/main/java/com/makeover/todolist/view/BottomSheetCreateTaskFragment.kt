package com.makeover.todolist.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeover.todolist.R
import com.makeover.todolist.databinding.BottomSheetCreateTaskBinding
import com.makeover.todolist.helper.showKeyBoard
import com.makeover.todolist.helper.views.CustomToast
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetCreateTaskFragment : BottomSheetDialogFragment(), View.OnClickListener,
    ViewBindingHolder<BottomSheetCreateTaskBinding> by ViewBindingHolderImpl() {

    private val viewModel: DashboardViewModel by viewModels()

    private var _createTaskBinding: BottomSheetCreateTaskBinding? = null
    private val createTaskBinding get() = _createTaskBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = initBinding(BottomSheetCreateTaskBinding.inflate(layoutInflater), this) {
        _createTaskBinding = requireBinding()
        setUpViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        //Setup Key board when bottom sheet open
        createTaskBinding.taskNameEditText.requestFocus()
        createTaskBinding.taskNameEditText.postDelayed({
            showKeyBoard(createTaskBinding.taskNameEditText)
        }, 50)

        createTaskBinding.taskNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createTaskBinding.btnCreateTaskCategory.isActivated = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /* No Implementation needed */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /* No Implementation needed */
            }

        })

        createTaskBinding.btnCreateTaskCategory.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): BottomSheetCreateTaskFragment {
            val fragment = BottomSheetCreateTaskFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            createTaskBinding.btnCreateTaskCategory -> {
                createTaskCategory()
            }
        }
    }

    private fun createTaskCategory() {
        if (isValidTextToCreateCategory()) {
            viewModel.createCategory(createTaskBinding.taskNameEditText.text.toString())
            dismiss()
        }
    }

    private fun isValidTextToCreateCategory(): Boolean {
        if (createTaskBinding.taskNameEditText.text.isNullOrBlank()) {
            CustomToast.showShortToast(
                requireContext(),
                getString(R.string.alert_task_category_empty)
            )
            return false
        }
        return true
    }
}