package com.example.sambandh_20.model

class ChatMessage(val id: String, val text: String,val fromId: String, val toId: String, val mediaLink: String,
                  val timestamp: Long){
    constructor() : this("","", "", "", "", -1)
}