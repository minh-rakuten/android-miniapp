package com.rakuten.tech.mobile.miniapp.js

import androidx.annotation.Keep

@Keep
internal data class CallbackObj(
    var action: String,
    var param: Any?,
    var id: String
)

@Keep
internal data class Permission(val permission: String)

// custom permission region
@Keep
internal data class CustomPermissionCallbackObj(
    var action: String,
    val param: CustomPermission?,
    var id: String
)

@Keep
internal data class CustomPermission(
    val permissions: List<CustomPermissionObj>
)

@Keep
internal data class CustomPermissionObj(
    val name: String,
    val description: String
)
// end region

// shared info region
@Keep
internal data class ShareInfo(val content: String)

@Keep
internal data class ShareInfoCallbackObj(val param: ShareInfoParam) {

    @Keep
    internal data class ShareInfoParam(val shareInfo: ShareInfo)
}
// end region

// action response region
internal const val SUCCESS = "success"

@Keep
internal data class Error(
    val message: String,
    val name: String
)
// end region