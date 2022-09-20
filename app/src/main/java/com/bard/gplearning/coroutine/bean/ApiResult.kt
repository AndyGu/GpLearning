package com.bard.gplearning.coroutine.bean

class ApiResult<T> {
    var errorCode = 0
    var errorMsg: String? = null
    var data: T? = null
}