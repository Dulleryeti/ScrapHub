package com.example.scraphub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ScrapDetailViewModel(scrapId: UUID): ViewModel() {
    private val scrapRepository = ScrapRepository.get()

    private val _scrap: MutableStateFlow<Scrap?> = MutableStateFlow(null)
    val scrap: StateFlow<Scrap?> = _scrap.asStateFlow()


    init {
        viewModelScope.launch {
            _scrap.value = scrapRepository.getScrap(scrapId)
        }
    }

    fun updateScrap(onUpdate: (Scrap) -> Scrap) {
        _scrap.update { oldScrap ->
            oldScrap?.let {onUpdate(it)}
        }
    }

    override fun onCleared() {
        super.onCleared()

        // call updated function from outside a corountine scope
        scrap.value?.let { scrapRepository.updateScrap(it) }

    }
}

// create scrap factory class to create detailviewmodel instances
class ScrapDetailViewModelFactory(
    private val scrapId: UUID // pass in crimeId as constructor parameter
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ScrapDetailViewModel(scrapId) as T
    }
}