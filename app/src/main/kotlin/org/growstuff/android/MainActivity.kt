// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.growstuff.android.ui.GrowstuffApp
import org.growstuff.android.ui.theme.GrowstuffTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Light icons on the brown top bar, dark icons over the white bottom of each screen.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)
        val container = (application as GrowstuffApplication).container
        setContent {
            GrowstuffTheme {
                GrowstuffApp(container)
            }
        }
    }
}
