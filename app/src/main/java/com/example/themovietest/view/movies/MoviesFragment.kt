package com.example.themovietest.view.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.themovietest.R
import com.example.themovietest.databinding.FragmentMoviesBinding
import com.example.themovietest.model.adapters.MoviesAdapter
import com.example.themovietest.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    private val viewModelMovies: MoviesViewModel by viewModel()
    private val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        viewModelMovies.retrievePopularMovies()
        rv_popularMovies.adapter = moviesAdapter
    }

    private fun topRated() {
        viewModelMovies.getTopRatedMovies()
        rv_popularMovies.adapter = moviesAdapter
    }

    private fun incoming() {
        viewModelMovies.getUpcomingMovies()
        rv_popularMovies.adapter = moviesAdapter
    }

    private fun initObservers() {
        viewModelMovies.mMoviesLiveData.observe(
            viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    moviesAdapter.setMoviesList(it)
                }
            }

        viewModelMovies.upcomingMoviesLiveData.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()) {
                moviesAdapter.setMoviesList(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        return (when(item.itemId) {
            R.id.action_popular_movies -> {
                initViews()
                true
            }
            R.id.action_top_rated -> {
                topRated()
                true
            }
            R.id.action_now_playing -> {
                incoming()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}