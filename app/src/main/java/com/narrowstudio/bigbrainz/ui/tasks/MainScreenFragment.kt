package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R

class MainScreenFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    var navController : NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.game_1_main_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.game_2_main_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.game_3_main_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.game_4_main_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.game_1_main_button -> navController!!.navigate(R.id.action_mainScreenFragment_to_game1Selection)
            R.id.game_2_main_button -> navController!!.navigate(R.id.action_mainScreenFragment_to_game2SelectionFragment)
        }
    }


}