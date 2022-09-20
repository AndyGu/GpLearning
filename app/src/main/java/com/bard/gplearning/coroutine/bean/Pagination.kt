package com.bard.gplearning.coroutine.bean

class Pagination<T> {
    var curPage = 0
    var datas: List<T>? = null
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0
}