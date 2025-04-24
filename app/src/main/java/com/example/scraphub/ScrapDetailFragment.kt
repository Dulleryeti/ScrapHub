package com.example.scraphub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController

import com.example.scraphub.databinding.FragmentScrapDetailBinding
import kotlinx.coroutines.launch

class ScrapDetailFragment: Fragment() {

    // create nullable backing property
    private var _binding: FragmentScrapDetailBinding? = null

    // change binding property to become a computed property
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }


    private val args: ScrapDetailFragmentArgs by navArgs()

    private val scrapDetailViewModel: ScrapDetailViewModel by viewModels {
        ScrapDetailViewModelFactory(args.scrapId)
    }


    // add implementation of onCreateView that inflates and binds the fragment scrap detail view
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentScrapDetailBinding.inflate(layoutInflater, container, false)
        return binding.root // return root view
    }


    // Temporary storage for name
    private var tempName: String? = null

    // add onViewCreated lifecycle callback to wire up views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val images = listOf(
            R.drawable.airhorn,
            R.drawable.apparatus,
            R.drawable.bee,
            R.drawable.bell,
            R.drawable.bolt,
            R.drawable.bottles,
            R.drawable.candy,
            R.drawable.cash_register,
            R.drawable.chemical_jug,
            R.drawable.clown_horn_png,
            R.drawable.coffee_mug,
            R.drawable.comedy_mask,
            R.drawable.cookie_mold_pan,
            R.drawable.dust_pan_png,
            R.drawable.egg_beater,
            R.drawable.fancy_lamp,
            R.drawable.flask,
            R.drawable.gift,
            R.drawable.goldbar,
            R.drawable.golden_cup,
            R.drawable.hair_brush,
            R.drawable.hair_dryer,
            R.drawable.jar_of_pickles,
            R.drawable.large_axle,
            R.drawable.laser,
            R.drawable.magic,
            R.drawable.magnifying_glass,
            R.drawable.old_phone,
            R.drawable.painting,
            R.drawable.red_soda,
            R.drawable.remote,
            R.drawable.ring,
            R.drawable.robot,
            R.drawable.duck,
            R.drawable.steering_wheel,
            R.drawable.stop,
            R.drawable.metal,
            R.drawable.tea_kettle,
            R.drawable.teeth,
            R.drawable.toothpaste,
            R.drawable.toy_cube,
            R.drawable.tragedy,
            R.drawable.engine,
            R.drawable.whoopie,
            R.drawable.flashbang,
            R.drawable.yieldsign
        )

        // Select a random image and set it as the ImageView's source
        val randomImage = images.random()
        binding.scrapImage.setImageResource(randomImage)



        // add listener to edittext
        binding.apply {

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    scrapDetailViewModel.scrap.collect { scrap ->
                        if (scrap != null) {
                            updateUi(scrap)

                            // Determine if this is a new scrap based on the name being empty
                            val isNewScrap = scrap.name.isEmpty()
                            if (isNewScrap) {
                                // UI adjustments for adding a new scrap
                                binding.scrapPurchase.visibility = View.GONE
                                binding.scrapFavorite.visibility = View.GONE
                                binding.scrapName.visibility = View.VISIBLE
                                binding.scrapUsername.visibility = View.VISIBLE
                                binding.scrapHeading.text = "Add Your Scrap"
                            } else {
                                // UI adjustments for editing an existing scrap
                                binding.scrapPurchase.visibility = View.VISIBLE
                                binding.scrapFavorite.visibility = View.VISIBLE
                                binding.scrapName.visibility = View.GONE
                                binding.scrapUsername.visibility = View.GONE
                            }
                        }

                    }
                }

            }
            // hook up Ui to new function for responding to user input for changing the title
            scrapTitle.doOnTextChanged { text, _, _, _ ->
                scrapDetailViewModel.updateScrap { oldScrap ->
                    oldScrap.copy(title = text.toString())
                }
            }


            scrapName.doOnTextChanged { text, _, _, _ ->
                tempName = text.toString()
            }



            scrapDescription.doOnTextChanged { text, _, _, _ ->
                scrapDetailViewModel.updateScrap { oldScrap ->
                    oldScrap.copy(desc = text.toString())
                }
            }

            scrapPrice.doOnTextChanged { text, _, _, _ ->
                val price = text.toString().toIntOrNull() ?: 0
                if (price > 999) {
                    scrapPrice.setText("999")
                    Toast.makeText(context, "Maximum price is 999.", Toast.LENGTH_SHORT).show()
                } else {
                    scrapDetailViewModel.updateScrap { oldScrap ->
                        oldScrap.copy(price = price)
                    }
                }
            }


            scrapPurchase.setOnClickListener{
                scrapDetailViewModel.updateScrap { oldScrap ->
                    oldScrap.copy(isBought = true)

                }
                findNavController().popBackStack()

            }




            // add listener for checkbox changes
            scrapFavorite.setOnCheckedChangeListener { _, isChecked ->
                scrapDetailViewModel.updateScrap { oldScrap ->
                    oldScrap.copy(isFavorite = isChecked)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveChanges()
    }

    private fun saveChanges() {
        tempName?.let { newName ->
            if (newName.isNotEmpty() && newName != scrapDetailViewModel.scrap.value?.name) {
                scrapDetailViewModel.updateScrap { oldScrap ->
                    oldScrap.copy(name = newName)
                }
            }
        }

        tempName = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // create updateUi function to change ui when changes are made
    private fun updateUi(scrap: Scrap) {
        binding.apply {
            if (scrapTitle.text.toString() != scrap.title) {
                scrapTitle.setText(scrap.title)
            }

            if (tempName == null && scrapName.text.toString() != scrap.name) {
                scrapName.setText(scrap.name)
            }

            if (scrapDescription.text.toString() != scrap.desc) {
                scrapDescription.setText(scrap.desc)
            }

            if (scrapPrice.text.toString() != scrap.price.toString()) {
                scrapPrice.setText(scrap.price.toString())
            }


            scrapHeading.text = "${scrap.name}'s Scrap"
            scrapFavorite.isChecked = scrap.isFavorite

        }
    }

}