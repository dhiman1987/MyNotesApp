package com.dotndash.mynotes.ui.note.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _searchText  = MutableStateFlow("")
    var searchText: StateFlow<String> = _searchText

    fun setSearchText(newSearchText: String) { viewModelScope.launch { _searchText.value = newSearchText } }
}