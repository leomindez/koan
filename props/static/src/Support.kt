/*
 * Copyright 2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlinx.android.koan

import android.app.Activity
import android.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import kotlinx.android.koan.internals.__internalStartActivity
import android.content.Intent

/* SECTION HELPERS */
private fun <T : View> android.support.v4.app.Fragment.addFragmentTopLevelView(v: T, init: T.() -> Unit): T {
    UI { addView(v, init, this) }
    return v
}

public fun <T : View> __dslAddView(
    view: (ctx: Context) -> T,
    init: T.() -> Unit,
    fragment: android.support.v4.app.Fragment): T {
    val ctx = fragment.getActivity()
    return fragment.addFragmentTopLevelView(view(ctx), init)
}

public fun android.support.v4.app.Fragment.UI(init: UiHelper.() -> Unit): UiHelper = getActivity().UI(false, init)
/* END SECTION */


/* SECTION CONTEXT UTILS */
public val android.support.v4.app.Fragment.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(getActivity())

public val android.support.v4.app.Fragment.act: Activity
    get() = getActivity()

public val android.support.v4.app.Fragment.ctx: Context
    get() = getActivity()

public fun android.support.v4.app.Fragment.browse(url: String): Boolean = ctx.browse(url)

public fun android.support.v4.app.Fragment.share(text: String, subject: String = ""): Boolean = ctx.share(text, subject)

public fun android.support.v4.app.Fragment.email(email: String, subject: String = "", text: String = ""): Boolean =
    ctx.email(email, subject, text)

public fun android.support.v4.app.Fragment.makeCall(number: String): Boolean = ctx.makeCall(number)

public fun android.support.v4.app.Fragment.startActivity(
    activity: Class<out Activity>,
    vararg params: Pair<String, Any>
): Unit = ctx.__internalStartActivity(activity, params)

public inline fun <reified T: Class<*>> android.support.v4.app.Fragment.Intent(): Intent = Intent(getActivity(), javaClass<T>())
/* END SECTION */


/* SECTION DIALOGS */
public fun android.support.v4.app.Fragment.toast(textResource: Int): Unit = ctx.toast(textResource)

public fun android.support.v4.app.Fragment.toast(text: String): Unit = ctx.toast(text)

public fun android.support.v4.app.Fragment.longToast(textResource: Int): Unit = ctx.longToast(textResource)

public fun android.support.v4.app.Fragment.longToast(text: String): Unit = ctx.longToast(text)

public fun android.support.v4.app.Fragment.selector(
    title: CharSequence = "",
    items: List<CharSequence>,
    onCancel: () -> Unit = {},
    onClick: (Int) -> Unit
): Unit = ctx.selector(title, items, onCancel, onClick)
/* END SECTION */


/* SECTION ASYNC */
public fun android.support.v4.app.Fragment.async(task: KoanAsyncContext.() -> Unit): Future<Unit> = getActivity().async(task)

public fun android.support.v4.app.Fragment.async(executorService: ExecutorService, task: KoanAsyncContext.() -> Unit): Future<Unit> =
    getActivity().async(executorService, task)

public fun <T> android.support.v4.app.Fragment.asyncResult(task: () -> T): Future<T> = getActivity().asyncResult(task)

public fun <T> android.support.v4.app.Fragment.asyncResult(executorService: ExecutorService, task: () -> T): Future<T> =
    getActivity().asyncResult(executorService, task)

public fun android.support.v4.app.Fragment.verticalLayout(init: _LinearLayout.() -> Unit): LinearLayout =
    __dslAddView(verticalLayoutFactory, init, this)

public fun <T: View> android.support.v4.app.Fragment.include(layoutId: Int, init: T.() -> Unit): T =
    __dslAddView(inflaterFactory(layoutId), init, this)

public fun android.support.v4.app.Fragment.alert(
    title: String,
    message: String,
    init: AlertDialogBuilder.() -> Unit): AlertDialogBuilder =
    getActivity().alert(title, message, init)

public fun android.support.v4.app.Fragment.alert(
    title: Int,
    message: Int,
    init: AlertDialogBuilder.() -> Unit): AlertDialogBuilder =
    getActivity().alert(title, message, init)

public fun android.support.v4.app.Fragment.alert(init: AlertDialogBuilder.() -> Unit): AlertDialogBuilder =
    getActivity().alert(init)
/* END SECTION */