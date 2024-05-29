package com.dharmaputera.catalogapp

/**
 * @author Robin D. Putera
 * @date 28/05/2024
 */
data class CatalogItemModel(
    val id: Int,
    val image_url: String,
    val name: String,
    var is_favourite: Boolean
)
