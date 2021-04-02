package com.makeover.todolist.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.makeover.todolist.R
import com.makeover.todolist.helper.views.CustomToast
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun View.show() {
    let { visibility = View.VISIBLE }
}

fun View.hide() {
    let { visibility = View.INVISIBLE }
}

fun View.gone() {
    let { visibility = View.GONE }
}

fun <ViewT : View> AppCompatActivity.bindView(@IdRes idRes: Int): Lazy<ViewT> {
    return lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ViewT>(idRes)
    }
}

fun <ViewT : View> Fragment.bindView(@IdRes idRes: Int): ReadOnlyProperty<Fragment, ViewT> {
    return FragmentBinder(this) {
        it.view!!.findViewById<ViewT>(idRes)
    }
}

private class FragmentBinder<out ViewT : View>(
    val fragment: Fragment,
    val initializer: (Fragment) -> ViewT
) : ReadOnlyProperty<Fragment, ViewT>, LifecycleObserver {
    private object EMPTY

    private var viewValue: Any? = EMPTY

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment, androidx.lifecycle.Observer {
            it.lifecycle.addObserver(this)
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): ViewT {
        if (viewValue === EMPTY) {
            viewValue = initializer(fragment)
        }
        @Suppress("UNCHECKED_CAST")
        return viewValue as ViewT
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        viewValue = EMPTY
    }
}

inline fun Context.checkInternetAndExecute(showToast: Boolean = true, function: () -> Unit) {
    if (Utility.isNetworkAvailable(this))
        function()
    else if (showToast)
        showToast(getString(R.string.msg_no_internet))
}

inline fun Fragment.checkInternetAndExecute(function: () -> Unit) {
    if (Utility.isNetworkAvailable(activity)) {
        function()
    } else {
        requireActivity().showToast(getString(R.string.msg_no_internet))
    }
}

fun Context.showToast(text: String?) {
    text?.let {
        CustomToast.show(this, text)
    }
}

fun Activity.hideKeyBoard() {
    val imm: InputMethodManager =
        this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view: View? = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyBoard(editText: EditText) {
    val imm =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyBoard(editText: EditText) {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Fragment.showKeyBoard(editText: EditText) {
    val imm =
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}