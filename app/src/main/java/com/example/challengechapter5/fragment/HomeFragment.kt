package com.example.challengechapter5.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challengechapter5.R
import com.example.challengechapter5.adapter.FilmAdapter
import com.example.challengechapter5.databinding.FragmentHomeBinding
import com.example.challengechapter5.view.FilmViewModel
import com.example.challengechapter5.view.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var filmAdapter: FilmAdapter
    private lateinit var filmViewModel : FilmViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initial adapter
        filmAdapter = FilmAdapter(ArrayList())
        filmViewModel = ViewModelProvider(this).get(FilmViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //firebase
        firebaseAuth = FirebaseAuth.getInstance()

        binding.rvFilm.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvFilm.adapter = filmAdapter

        filmViewModel.callApiFilmPopular()

        filmViewModel.liveDataFilm.observe(viewLifecycleOwner, Observer {
            filmAdapter.setDataFilm(it)
        })
        showPopUpMenu()

        setUsername()


        //invoke onclickitem
        filmAdapter.onClickItem = {
            val bundle = Bundle().apply {
                putInt(MOVIE_ID, it.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }

        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
        object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        })

    }

    private fun setUsername() {
        val currentEmail = firebaseAuth.currentUser!!.email.toString()
        userViewModel.getDataUser(currentEmail)

        userViewModel.getDataUserProfile().observe(viewLifecycleOwner, Observer {
            binding.username = it.username
        })

    }

    private fun showPopUpMenu(){
        binding.btnCategory.setOnClickListener {
            val popUp = PopupMenu(requireContext(), binding.btnCategory)
            popUp.menuInflater.inflate(R.menu.category_menu, popUp.menu)

            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.popular -> {
                        filmViewModel.callApiFilmPopular()
                    }
                    R.id.topRate -> {
                        filmViewModel.callApiFilmTopRate()
                    }
                }
                true
            }
            popUp.show()
        }
    }
    companion object{
        const val MOVIE_ID = "movieid"
    }



}