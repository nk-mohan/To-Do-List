package com.makeover.todolist.view.dashboard.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.makeover.todolist.databinding.FragmentHomeBinding
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(),
    ViewBindingHolder<FragmentHomeBinding> by ViewBindingHolderImpl() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    private val categoryAdapter by lazy { CategoryAdapter(categoryViewModel.categoryListAdapter) }

    private var _homeFragmentBinding: FragmentHomeBinding? = null

    private val homeFragmentBinding get() = _homeFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentHomeBinding.inflate(layoutInflater), this) {
        _homeFragmentBinding = requireBinding()

        initViews()
        setObservers()
    }

    private fun initViews() {
        homeFragmentBinding.categoryRecyclerView.apply {
            adapter = categoryAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.getCategoryList()
    }

    private fun setObservers() {
        categoryViewModel.categoryDiffResult.observe(
            viewLifecycleOwner,
            Observer { diffUtilResult ->
                // Save Current Scroll state to retain scroll position after DiffUtils Applied
                val previousState =
                    homeFragmentBinding.categoryRecyclerView.layoutManager?.onSaveInstanceState() as Parcelable
                diffUtilResult.dispatchUpdatesTo(categoryAdapter)
                homeFragmentBinding.categoryRecyclerView.layoutManager?.onRestoreInstanceState(
                    previousState
                )
            })
    }
}