package com.tikonsil.tikonsil509.domain.model

import com.google.gson.annotations.SerializedName

/** * Created by ISMOY BELIZAIRE on 21/05/2022. */
data class BonusUser(
 @SerializedName("bonuslapoulahaiti") val bounuslapoulahaiti:Float?=null,
 @SerializedName("bonustopuphaiti") val bounustopuphaiti:Float?=null,
 @SerializedName("bonusmoncashhaiti") val bounusmoncashhaiti:Float?=null,
 @SerializedName("bonusnatcashhaiti") val bounusnatcashhaiti:Float?=null,
 @SerializedName("bonuslapoulachile")val bounuslapoulachile:Float?=null,
 @SerializedName("bonustopupchile")val bounustopupchile:Float?=null,
 @SerializedName("bonusmoncashchile")val bounusmoncashchile:Float?=null,
 @SerializedName("bonusnatcashchile")val bounusnatcashchile:Float?=null,
 @SerializedName("bonuslapoulacuba")val bounuslapoulacuba:Float?=null,
 @SerializedName("bonustopupcuba")val bounustopupcuba:Float?=null,
 @SerializedName("bonusmoncashcuba")val bounusmoncashcuba:Float?=null,
 @SerializedName("bonusnatcashcuba")val bounusnatcashcuba:Float?=null,
 @SerializedName("bonuslapoulamexico")val bounuslapoulamexico:Float?=null,
 @SerializedName("bonustopupmexico")val bounustopupmexico:Float?=null,
 @SerializedName("bonusmoncashmexico")val bounusmoncashmexico:Float?=null,
 @SerializedName("bonusnatcashmexico")val bounusnatcashmexico:Float?=null,
 @SerializedName("bonuslapoulapanama")val bounuslapoulapanama:Float?=null,
 @SerializedName("bonustopuppanama")val bounustopuppanama:Float?=null,
 @SerializedName("bonusmoncashpanama")val bounusmoncashpanama:Float?=null,
 @SerializedName("bonusnatcashpanama")val bounusnatcashpanama:Float?=null,
 @SerializedName("bonuslapoulard")val bounuslapoulard:Float?=null,
 @SerializedName("bonustopuprd")val bounustopuprd:Float?=null,
 @SerializedName("bonosmoncashrd")val bounusmoncashrd:Float?=null,
 @SerializedName("bonusnatcashrd")val bounusnatcashrd:Float?=null,
 @SerializedName("bonuslapoulabrazil")val bounuslapoulabrazil:Float?=null,
 @SerializedName("bonustopupbrazil")val bounustopupbrazil:Float?=null,
 @SerializedName("bonusmoncashbrazil")val bounusmoncashbrazil:Float?=null,
 @SerializedName("bonusnatcashbrazil")val bounusnatcashbrazil:Float?=null,
 @SerializedName("bonuslapoulaothercountry")val bounuslapoulaothercountry:Float?=null,
 @SerializedName("bonustopupothercountry") val bounustopupothercountry:Float?=null,
 @SerializedName("bonusmoncashothercountry")val bounusmoncashothercountry:Float?=null,
 @SerializedName("bonusnatcashothercountry")val bounusnatcashothercountry:Float?=null
)