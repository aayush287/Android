/*
 * Copyright (c) 2020 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dummy.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import javax.inject.Inject

class VpnPreferences @Inject constructor(
    private val applicationContext: Context,
    private val appBuildConfig: AppBuildConfig
) {

    fun updateDebugLoggingPreference(enabled: Boolean) {
        preferences.edit { putBoolean(PREFS_KEY_DEBUG_LOGGING, enabled) }
    }

    fun getDebugLoggingPreference(): Boolean = preferences.getBoolean(PREFS_KEY_DEBUG_LOGGING, appBuildConfig.isDebug)

    fun useCustomDnsServer(dnsServer: Boolean) {
        preferences.edit { putBoolean(PREFS_KEY_DNS_SERVER, dnsServer) }
    }

    fun isCustomDnsServerSet(): Boolean = preferences.getBoolean(PREFS_KEY_DNS_SERVER, false)

    var privateDns: String?
        get() = preferences.getString("private_dns", null)
        set(value) = preferences.edit { putString("private_dns", value) }

    var underlyingNetworks: String?
        get() = preferences.getString("underlying_networks", null)
        set(value) = preferences.edit(commit = true) { putString("underlying_networks", value) }

    fun isPrivateDnsKey(key: String) = key == "private_dns"

    fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private val preferences: SharedPreferences
        get() = applicationContext.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    companion object {

        @VisibleForTesting
        const val PREFS_FILENAME = "VpnDummySettings"

        private const val PREFS_KEY_DEBUG_LOGGING = "PREFS_KEY_DEBUG_LOGGING"
        private const val PREFS_KEY_DNS_SERVER = "PREFS_KEY_DNS_SERVER"
        const val PREFS_KEY_REMINDER_NOTIFICATION_SHOWN = "PREFS_KEY_REMINDER_NOTIFICATION_SHOWN"
    }
}
