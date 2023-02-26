package com.tikonsil.tikonsil509.data.remote.retrofitInstance

import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi

class Headers {
    companion object{

        suspend fun getHeaderTikonsil509(): Map<String, String> {
            val headerTikonsil509 = mutableMapOf<String, String>()
            headerTikonsil509["Content-Type"] = "application/json"
            headerTikonsil509["Authorization"] = FirebaseApi.getFSApis().key
            return headerTikonsil509
        }

        suspend fun getHeaderFcm():Map<String,String>{
            val headerfcm = mutableMapOf<String,String>()
            headerfcm["Content-Type"] = "application/json"
            headerfcm["Authorization"]="key=${FirebaseApi.getFSApis().server_key}"
            return headerfcm
        }
    }
}