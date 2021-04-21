package com.jjswigut.data.local

sealed class CardModel {

    class TaskModel(val body: String) : CardModel()
    class ListModel(val name: String) : CardModel()
    object AddModel : CardModel()
}