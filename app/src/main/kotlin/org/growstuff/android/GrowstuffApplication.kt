// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android

import android.app.Application

class GrowstuffApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
