package com.example.signinapp.ui.home

sealed class HomeEvent {

    data class NavigateNext(val value: String): HomeEvent()
    object NavigateBack: HomeEvent()
    object Logout: HomeEvent()
}
