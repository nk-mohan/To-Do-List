package com.makeover.todolist.view.dashboard.ui.category

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeover.todolist.databinding.FragmentCategoryBinding
import com.makeover.todolist.room.model.Category
import com.makeover.todolist.view.delegate.ViewBindingHolder
import com.makeover.todolist.view.delegate.ViewBindingHolderImpl
import com.makeover.todolist.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.CategoryAdapterClickListener,
    ViewBindingHolder<FragmentCategoryBinding> by ViewBindingHolderImpl() {

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    private val categoryAdapter by lazy {
        CategoryAdapter(
            dashboardViewModel.categoryListAdapter,
            this
        )
    }

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
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setObservers()

        dashboardViewModel.getCategoryList()
    }

    private fun setObservers() {
        dashboardViewModel.categoryDiffResult.observe(
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

    override fun onCategoryClicked(category: Category) {
        val action =
            CategoryFragmentDirections.actionNavigationCategoryToTaskFragment(category.id!!)
        findNavController().navigate(action)
    }
}