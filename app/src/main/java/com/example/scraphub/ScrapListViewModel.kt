package com.example.scraphub


import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "ScrapListViewModel"

class ScrapListViewModel: ViewModel() {
    private val scrapRepository = ScrapRepository.get()


    // use a private state flow to protect access from the stream and efficiently cache db results
    private val _scraps: MutableStateFlow<List<Scrap>> = MutableStateFlow(emptyList())
    val scraps: StateFlow<List<Scrap>> //
        get() = _scraps.asStateFlow()

    init {

        viewModelScope.launch {
            scrapRepository.getScraps().collect {
                _scraps.value = it
            }
        }
    }

    suspend fun addScrap(scrap: Scrap) {
        scrapRepository.addScrap(scrap)
    }
}