package com.bassem.myapplication.ui.main

import android.icu.util.MeasureUnit.WEEK
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val dividerItemDecoration = DividerItemDecoration(binding.asteroidRecycler.context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.divider)!!)
        binding.asteroidRecycler.addItemDecoration(dividerItemDecoration)

        binding.asteroidRecycler.adapter = MainAdapter(MainAdapter.OnClickListener {
            viewModel.onAsteroidItemClicked(it)

        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            if (null != it && it.isNotEmpty()) {
                viewModel.status.value = AsteroidApiStatus.DONE
            }
        })

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                AsteroidApiStatus.ERROR -> {
                    Toast.makeText(this.context, R.string.connection_Error, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {

            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayAsteroidDetailsComplete()
            }

        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onChangeFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> {
                    com.bassem.myapplication.ui.main.FilterAsteroid.TODAY
                }
                R.id.show_all_menu -> {
                    FilterAsteroid.WEEK
                }
                else -> {
                    FilterAsteroid.ALL
                }
            }
        )
        return true
    }


}
