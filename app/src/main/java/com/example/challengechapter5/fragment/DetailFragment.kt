package com.example.challengechapter5.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.challengechapter5.R
import com.example.challengechapter5.databinding.FragmentDetailBinding
import com.example.challengechapter5.view.FilmViewModel

class DetailFragment : Fragment() {

    lateinit var binding : FragmentDetailBinding
    lateinit var filmViewModel: FilmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getMovieId = arguments?.getInt("movieid")
        filmViewModel = ViewModelProvider(this).get(FilmViewModel::class.java)

        getDataDetail(getMovieId!!)

    }

    private fun getDataDetail(id : Int){
        filmViewModel.callApiDetailFilm(id)
        filmViewModel.getDetailFilm().observe(viewLifecycleOwner, Observer {

            if(it != null){
                binding.detail = it

                var genre = ""
                for (i in 0 until  it.genres.size){//4

                    genre += if((i+1) < it.genres.size){
                        it.genres[i].name + ", "
                    }else{
                        it.genres[i].name
                    }
                }
                binding.genremovie = genre

                binding.progressRate.progress = (it.voteAverage*10).toInt()

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500${it.posterPath}")
                    .into(binding.imgFilm)

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500${it.backdropPath}")
                    .into(binding.imgBackdrop)

                binding.btnHomePage.setOnClickListener { _->
                    val moveToHomePage = Intent(Intent.ACTION_VIEW)
                    if(it.homepage.isNotEmpty()){
                        moveToHomePage.data = Uri.parse(it.homepage)
                        startActivity(moveToHomePage)
                    }else{
                        Toast.makeText(context, R.string.homepage_information, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}