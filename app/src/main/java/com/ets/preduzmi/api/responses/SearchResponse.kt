package com.ets.preduzmi.api.responses

import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.models.UserModel

data class SearchResponse (
    var users: ArrayList<UserModel>,
    var businesses: ArrayList<BusinessModel>
)
