package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel


class Game2Fragment : Fragment(R.layout.fragment_game_2), LifecycleOwner {

    val ViewModel = ViewModelProvider(this).get(Game2ViewModel::class.java)



}