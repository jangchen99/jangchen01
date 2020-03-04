package com.example.hongsapp0229.model

data class NoticeDTO(
    var date : Long = 0,
    var noticeNum : Int = 0,
    var writer : String = "",
    var title : String = "",
    var content : String = ""
)