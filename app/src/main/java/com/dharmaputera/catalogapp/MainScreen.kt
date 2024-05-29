package com.dharmaputera.catalogapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * @author Robin D. Putera
 * @date 28/05/2024
 */

@Composable
fun MainScreen(viewModel: CatalogItemViewModel) {
    val isFavOn = remember {
        mutableStateOf(false)
    }

    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val listFav : ArrayList<CatalogItemModel> = ArrayList()

    SearchScreen(
        searchQuery = viewModel.searchQuery,
        searchResults = searchResults,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        viewModel = viewModel,
        list = listFav,
        isFavOn
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<CatalogItemModel>,
    onSearchQueryChange: (String) -> Unit,
    viewModel: CatalogItemViewModel,
    list : ArrayList<CatalogItemModel>,
    isFavOn: MutableState<Boolean>
) {
    SearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        placeholder = {
            Text(text = "Search item")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "",
                modifier = Modifier.clickable {
                    isFavOn.value = !isFavOn.value
                },
                tint = if (isFavOn.value) Color(0xffff0000) else Color(0xffd9d9d9)
                )
        },
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (isFavOn.value){
                    items(
                        count = list.size,
                        key = { index -> list[index].id },
                        itemContent = { index ->
                            val item = list[index]
                            CatalogListFavItem(item = item, viewModel = viewModel, position = index, list = list)
                        }
                    )
                }else{
                    items(
                        count = searchResults.size,
                        key = { index -> searchResults[index].id },
                        itemContent = { index ->
                            val item = searchResults[index]
                            CatalogListItem(item = item, viewModel = viewModel, position = index, list = list)
                        }
                    )
                }
            }
        },
        active = true,
        onActiveChange = {},
        tonalElevation = 0.dp
    )
}

@Composable
fun CatalogListFavItem(
    item: CatalogItemModel,
    modifier: Modifier = Modifier,
    viewModel: CatalogItemViewModel,
    position: Int,
    list : ArrayList<CatalogItemModel>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.image_url)
                .error(R.drawable.noimage)
                .crossfade(true)
                .build(),
            contentDescription = "Item image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = item.name)
        Spacer(modifier = Modifier.weight(1f))
        FavoriteButton(viewModel = viewModel, pos = position, list = list, isFavOn = true)
    }
}

@Composable
fun CatalogListItem(
    item: CatalogItemModel,
    modifier: Modifier = Modifier,
    viewModel: CatalogItemViewModel,
    position: Int,
    list : ArrayList<CatalogItemModel>
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.image_url)
                .error(R.drawable.noimage)
                .crossfade(true)
                .build(),
            contentDescription = "Item image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = item.name)
        Spacer(modifier = Modifier.weight(1f))
        FavoriteButton(viewModel = viewModel, pos = position, list = list, isFavOn = false)
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffff0000),
    viewModel: CatalogItemViewModel,
    pos: Int,
    list : ArrayList<CatalogItemModel>,
    isFavOn: Boolean
) {
    val isFav : Boolean =
        if (isFavOn) true else list.contains(viewModel.listItem[pos])
    var isFavorite by remember { mutableStateOf(isFav) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            viewModel.listItem[pos].is_favourite = !isFavorite
            val forFavListItem = viewModel.listItem[pos]
            forFavListItem.is_favourite = isFavorite
            if (isFavorite)list.add(forFavListItem) else list.remove(forFavListItem)
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}
