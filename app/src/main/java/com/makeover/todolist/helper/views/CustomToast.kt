package com.makeover.todolist.helper.views

import android.content.Context
import android.widget.Toast

/**
 * Display the Toast on the main UI thread
 *
 * @author MakeOverTeam <developers@makeover.in>
 * @version 1.0
 */
object CustomToast {

    /**
     * Shows the toast message.
     *
     * @param context Instance of the context
     * @param msg     Message for the toast
     */
    fun show(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    /**
     * Shows the toast message with short duration.
     *
     * @param context Instance of the context
     * @param msg     Message for the toast
     */
    fun showShortToast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}