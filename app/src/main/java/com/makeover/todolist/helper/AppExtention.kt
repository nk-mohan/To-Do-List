package com.makeover.todolist.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

fun MenuItem.show(){
    isVisible= true
}

fun MenuItem.hide(){
    isVisible = false
}


fun hideMenu(vararg menuItems: MenuItem) {
    menuItems.map { it.hide() }
}

fun showMenu(vararg menuItems: MenuItem) {
    menuItems.map { it.show() }
}

fun Menu.get(menuId: Int): MenuItem = findItem(menuId)

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

inline fun <reified T : Any> Context.launchActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)


fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
