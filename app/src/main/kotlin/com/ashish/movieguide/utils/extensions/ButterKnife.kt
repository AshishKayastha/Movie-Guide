@file:Suppress("unused")

package com.ashish.movieguide.utils.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Required Views
fun <V : View> View.bindView(id: Int): ReadOnlyProperty<View, V> = required(id, viewFinder)

fun <V : View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> = required(id, viewFinder)

fun <V : View> Fragment.bindView(id: Int): ReadOnlyProperty<Fragment, V> = required(id, viewFinder)

fun <V : View> ViewHolder.bindView(id: Int): ReadOnlyProperty<ViewHolder, V> = required(id, viewFinder)

// Optional Views
fun <V : View> Activity.bindOptionalView(id: Int): ReadOnlyProperty<Activity, V?> = optional(id, viewFinder)

fun <V : View> Fragment.bindOptionalView(id: Int): ReadOnlyProperty<Fragment, V?> = optional(id, viewFinder)

fun <V : View> ViewHolder.bindOptionalView(id: Int)
        : ReadOnlyProperty<ViewHolder, V?> = optional(id, viewFinder)

// Getters
private val View.viewFinder: View.(Int) -> View?
    get() = { findViewById(it) }

private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }

private val Fragment.viewFinder: Fragment.(Int) -> View?
    get() = { view?.findViewById(it) }

private val ViewHolder.viewFinder: ViewHolder.(Int) -> View?
    get() = { itemView.findViewById(it) }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?)
        = Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?)
        = Lazy { t: T, _ -> t.finder(id) as V? }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<in T, out V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}