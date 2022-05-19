package com.tikonsil.tikonsil509.domain.model

import java.util.ArrayList

/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
data class FCMResponse(
    var multicast_id: Int,
    var success: Int,
    var failure: Int,
    var canonical_ids: Int,
    var results: ArrayList<Any?>?
)
