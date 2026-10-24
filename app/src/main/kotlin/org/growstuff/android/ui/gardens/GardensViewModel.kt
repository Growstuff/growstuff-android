// SPDX-License-Identifier: AGPL-3.0-or-later
package org.growstuff.android.ui.gardens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.Clock
import java.time.Instant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.growstuff.android.data.Garden
import org.growstuff.android.data.GardensRepository

data class GardensUiState(
    val gardens: List<Garden> = emptyList(),
    val refreshing: Boolean = false,
    val refreshedAt: Instant = Instant.EPOCH,
)

class GardensViewModel(private val repository: GardensRepository, private val clock: Clock) : ViewModel() {
    private val refreshing = MutableStateFlow(false)
    private val refreshedAt = MutableStateFlow(clock.instant())

    val state: StateFlow<GardensUiState> = combine(
        repository.gardens,
        refreshing,
        refreshedAt,
        ::GardensUiState,
    )
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            GardensUiState(refreshedAt = clock.instant()),
        )

    fun refresh() {
        if (refreshing.value) return
        viewModelScope.launch {
            refreshing.value = true
            try {
                repository.refresh()
                refreshedAt.value = clock.instant()
            } finally {
                refreshing.value = false
            }
        }
    }
}
