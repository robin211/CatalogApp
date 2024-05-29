package com.dharmaputera.catalogapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

/**
 * @author Robin D. Putera
 * @date 28/05/2024
 */
class CatalogItemViewModel : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set

    var listItem = listOf(
        CatalogItemModel(
            id = 1,
            name = "Item One",
            image_url = "https://picsum.photos/id/201/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 2,
            name = "Item Two",
            image_url = "https://picsum.photos/id/202/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 3,
            name = "Item Three",
            image_url = "https://picsum.photos/id/203/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 4,
            name = "Item Four",
            image_url = "https://picsum.photos/id/204/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 5,
            name = "Item Five",
            image_url = "https://picsum.photos/id/205/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 6,
            name = "Item Six",
            image_url = "https://picsum.photos/id/206/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 7,
            name = "Item Seven",
            image_url = "https://picsum.photos/id/207/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 8,
            name = "Item Eight",
            image_url = "https://picsum.photos/id/208/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 9,
            name = "Item Nine",
            image_url = "https://picsum.photos/id/209/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 10,
            name = "Item Ten",
            image_url = "https://picsum.photos/id/210/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 11,
            name = "Item Eleven",
            image_url = "https://picsum.photos/id/211/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 12,
            name = "Item Twelve",
            image_url = "https://picsum.photos/id/212/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 13,
            name = "Item Thirteen",
            image_url = "https://picsum.photos/id/213/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 14,
            name = "Item Fourteen",
            image_url = "https://picsum.photos/id/214/100/100",
            is_favourite = false
        ),
        CatalogItemModel(
            id = 15,
            name = "Item Fifteen",
            image_url = "https://picsum.photos/id/215/100/100",
            is_favourite = false
        ),
    )

    private var itemsFlow = flowOf(
        listItem
    )

    var searchResults: StateFlow<List<CatalogItemModel>> =
        snapshotFlow { searchQuery }
            .combine(itemsFlow) { searchQuery, items ->
                items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true)
            }

            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

}