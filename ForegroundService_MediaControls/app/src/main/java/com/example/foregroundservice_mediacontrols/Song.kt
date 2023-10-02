package com.example.foregroundservice_mediacontrols

import java.io.Serializable

class Song(
    val title: String,
    val singer: String,
    val image: Int,
    val resource: Int, // file mp3 chạy local
) : Serializable {
    //Khi sử dụng Bundle để gửi một đối tượng (object),
// lớp của đối tượng đó cần phải kế thừa lớp Serializable
// để có thể được đóng gói và gửi đi qua Bundle

}