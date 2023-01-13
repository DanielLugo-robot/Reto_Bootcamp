package com.example.retobootcampmobilesophos2022.model

import com.example.retobootcampmobilesophos2022.viewModel.network.dataItemsResponse

data class Documents(

    val Items:List<dataItemsResponse>,
    val Count:Int,
    val ScannedCount:Int

)
