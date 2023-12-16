package com.unamad.aulago.emums

enum class ServerAddressType(val address: String) {
    Work("http://172.17.2.77:5157"),
    Home("http://192.168.1.3:5157"),
    Remote("http://200.37.144.19:6030"),
    Production("https://daa-documentos.unamad.edu.pe:8081")
}