/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.coffeelogs.preferences

import android.content.Context
import com.raywenderlich.android.coffeelogs.key.Constants.Companion.PREFS_NAME
import com.raywenderlich.android.coffeelogs.key.Constants.Companion.PREF_LIMIT_KEY
import com.raywenderlich.android.coffeelogs.key.Constants.Companion.PREF_TODAY_TOTAL_COFFEE_KEY
import java.text.SimpleDateFormat
import java.util.*

class CoffeeLogPreferences(private val context: Context) {
  private val strFormatter = SimpleDateFormat("yyyyMMdd")

  // Write the prefix to the SharedPreferences object for this widget
  internal fun saveTodayCoffeePref(value: Int) {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putInt(PREF_TODAY_TOTAL_COFFEE_KEY + strFormatter.format(date), value)
    prefs.apply()
  }

  // Read the prefix from the SharedPreferences object for this widget.
  // If there is no preference saved, get the default from a resource
  internal fun getTodayCoffeePref(): Int {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getInt(PREF_TODAY_TOTAL_COFFEE_KEY + strFormatter.format(date), /*default*/0)
  }

  // Write the prefix to the SharedPreferences object for this widget
  internal fun saveLimitPref(value: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putInt(PREF_LIMIT_KEY, value)
    prefs.apply()
  }

  // Write the prefix to the SharedPreferences object for this widget
  internal fun getLimitPref(): Int {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getInt(PREF_LIMIT_KEY, 0)
  }

  internal fun deleteTodayCoffeePref() {
    val date = Date()
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_TODAY_TOTAL_COFFEE_KEY + strFormatter.format(date))
    prefs.apply()
  }
}
