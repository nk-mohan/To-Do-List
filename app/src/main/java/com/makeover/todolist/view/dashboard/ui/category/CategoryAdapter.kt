package com.makeover.todolist.view.dashboard.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeover.todolist.databinding.RowCategoryBinding
import com.makeover.todolist.room.model.Category

class CategoryAdapter(private var categoryList: List<Category>, private val clickListener: CategoryAdapterClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val rowCategoryBinding: RowCategoryBinding) :
        RecyclerView.ViewHolder(rowCategoryBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            RowCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.rowCategoryBinding.category = categoryList[position]
        holder.rowCategoryBinding.categoryItemClick = clickListener
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface CategoryAdapterClickListener {
        fun onCategoryClicked(category: Category)
    }
}