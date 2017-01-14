package com.ashish.movies.data.models

import com.squareup.moshi.Json

data class ImageItem(@Json(name = "file_path") val filePath: String? = null)