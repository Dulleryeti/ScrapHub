package com.example.scraphub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scraphub.databinding.ListItemScrapBinding
import java.util.UUID

class ScrapHolder(
    private val binding: ListItemScrapBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(scrap: Scrap, onScrapClicked: (scrapId: UUID) -> Unit){
        binding.scrapTitle.text = scrap.title
        binding.scrapName.text = "@${scrap.name}"
        binding.scrapPrice.text = "$${scrap.price}"


        binding.root.setOnClickListener() {

            onScrapClicked(scrap.id)
        }

        // add instance variable and toggle its visibility based on solved status
        when {
            scrap.isFavorite && scrap.isBought -> {
                binding.scrapPurchase.visibility = View.VISIBLE
                binding.scrapFavorite.visibility = View.GONE
            }
            scrap.isFavorite -> {
                binding.scrapFavorite.visibility = View.VISIBLE
                binding.scrapPurchase.visibility = View.GONE
            }
            scrap.isBought -> {
                binding.scrapPurchase.visibility = View.VISIBLE
                binding.scrapFavorite.visibility = View.GONE
            }
            else -> {
                binding.scrapFavorite.visibility = View.GONE
                binding.scrapPurchase.visibility = View.GONE
            }
        }

    }
}
// create adapter class to populate recycler views
class ScrapListAdapter(
    private val scraps: List<Scrap>,

    // pass in same lambda as a constructor parameter, and crime ID for UUID
    private val onScrapClicked: (UUID, Boolean) -> Unit
) : RecyclerView.Adapter<ScrapHolder>() {

    // responsible for creating a binding to display, wrapping view in a viewholder, and return result
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapHolder {
        val inflater = LayoutInflater.from(parent.context) // inflate listitembiniding
        val binding = ListItemScrapBinding.inflate(inflater, parent, false) // bind listitem
        return ScrapHolder(binding) // pass resulting binding to instance of CrimeHolder
    }


    // responsible for populating a given holder with the crime for a given position
    override fun onBindViewHolder(holder: ScrapHolder, position: Int) {
        val scrap = scraps[position]
        //move holder apply because adapter should know little about the viewholder
        holder.bind(scrap) { scrapId ->
            onScrapClicked(scrapId, scrap.isBought)
        } // recycler requests viewholder bound to a crime and lambda

    }

    // responsible for letting recycler view know how many items are in the data set
    override fun getItemCount(): Int {
        return scraps.size

    }

}