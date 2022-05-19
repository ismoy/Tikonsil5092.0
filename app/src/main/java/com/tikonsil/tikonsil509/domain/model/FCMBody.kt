package com.tikonsil.tikonsil509.domain.model

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
data class FCMBody(
    var to: String?,
    var priority: String?,
    var ttl: String?,
    var data: Map<String, String>
)
