package com.narrowstudio.bigbrainz.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.narrowstudio.bigbrainz.R

class Game1SelectionFragment : Fragment(R.layout.fragment_game_1_selection), View.OnClickListener {

    var navController : NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.g1s_m1_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.g1s_m2_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.g1s_m3_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.g1s_m1_button -> navController!!.navigate(R.id.action_game1SelectionFragment_to_game1l1Fragment)
            R.id.g1s_m2_button -> navController!!.navigate(R.id.action_game1SelectionFragment_to_game1l2Fragment)
            R.id.g1s_m3_button -> navController!!.navigate(R.id.action_game2SelectionFragment_to_game2l3Fragment)
        }
    }

}