package com.example.scraphub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scraphub.databinding.FragmentScrapListBinding
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "ScrapListFragment"


// create crimeListFragment class and associate it to CrimeListViewModel
class ScrapListFragment: Fragment() {

    private var _binding: FragmentScrapListBinding? = null
    private val binding
        get () = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    private val scrapListViewModel: ScrapListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    // hook up view to inflate and bind layout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrapListBinding.inflate(inflater, container, false)

        // set up layout manager to display items for recyclerview
        binding.scrapRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root

    }

    // instead of using onStart use repeatonLifecycle that executes coroutine code in a specified state
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // add collect function to observe the state flow of crimes
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                scrapListViewModel.scraps.collect { scraps ->
                    binding.scrapRecyclerView.adapter = ScrapListAdapter(scraps) {scrapId, isBought ->
                        // perform navigation

                        if (!isBought) {
                            // Only navigate if the item has not been bought
                            findNavController().navigate(
                                ScrapListFragmentDirections.showScrapDetail(scrapId)
                            )
                        } else {
                            // Optionally, show a message indicating the item is already bought
                            Toast.makeText(context, "This item is already bought.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_scrap_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_scrap -> {
                showNewScrap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showNewScrap() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newScrap = Scrap(
                id = UUID.randomUUID(),
                title = "",
                name = "",
                desc = "",
                price = 0,
                isBought = false,
                isFavorite = false
            )

            scrapListViewModel.addScrap(newScrap)
            findNavController().navigate(
                ScrapListFragmentDirections.showScrapDetail(newScrap.id)
            )
        }
    }



}