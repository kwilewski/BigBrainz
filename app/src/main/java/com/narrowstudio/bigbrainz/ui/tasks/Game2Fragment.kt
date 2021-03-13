package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.narrowstudio.bigbrainz.R
import com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel
import com.narrowstudio.bigbrainz.viewmodel.TimerViewModel
import kotlinx.android.synthetic.main.fragment_game_2.*


class Game2Fragment : Fragment(R.layout.fragment_game_2), LifecycleOwner {

    private lateinit var g2ViewModel : Game2ViewModel
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var timeInMillisLD: MutableLiveData<Long>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        g2ViewModel = ViewModelProvider(this).get(com.narrowstudio.bigbrainz.viewmodel.Game2ViewModel::class.java)
        g2ViewModel.init()
        g2ViewModel.getTimeInMillisLD().observe(viewLifecycleOwner, Observer {
            g2_textview.text = g2ViewModel.getTimeAsString()
        })

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        timerViewModel.init()



        g2_button.setOnClickListener {
            g2ViewModel.buttonPressed()
        }

    }





}