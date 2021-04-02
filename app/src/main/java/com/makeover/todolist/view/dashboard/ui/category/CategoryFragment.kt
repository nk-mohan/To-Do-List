package com.makeover.todolist.view.dashboard.ui.category

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.makeover.todolist.databinding.FragmentCategoryBinding
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(),
    ViewBindingHolder<FragmentCategoryBinding> by ViewBindingHolderImpl() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    private val categoryAdapter by lazy { CategoryAdapter(categoryViewModel.categoryListAdapter) }

    private var _categoryFragmentBinding: FragmentCategoryBinding? = null

    private val categoryFragmentBinding get() = _categoryFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentCategoryBinding.inflate(layoutInflater), this) {
        _categoryFragmentBinding = requireBinding()
    }

    private fun initViews() {
        categoryFragmentBinding.categoryRecyclerView.apply {
            adapter = categoryAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()

        categoryViewModel.getCategoryList()
    }

    private fun setObservers() {
        categoryViewModel.categoryDiffResult.observe(
            viewLifecycleOwner,
            { diffUtilResult ->
                // Save Current Scroll state to retain scroll position after DiffUtils Applied
                val previousState =
                    categoryFragmentBinding.categoryRecyclerView.layoutManager?.onSaveInstanceState() as Parcelable
                diffUtilResult.dispatchUpdatesTo(categoryAdapter)
                categoryFragmentBinding.categoryRecyclerView.layoutManager?.onRestoreInstanceState(
                    previousState
                )
            })
    }
}