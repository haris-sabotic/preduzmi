package com.ets.preduzmi.ui.search

import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.api.responses.SearchResponse
import com.ets.preduzmi.models.BusinessModel
import com.ets.preduzmi.models.UserModel

class SearchRecyclerViewAdapter (
    private val context: Context,
    private val dataSet: SearchResponse,
    private val businessClickAction: (BusinessModel) -> Unit,
    private val userClickAction: (UserModel) -> Unit,
) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: ConstraintLayout
        val imageBusinessParent: CardView
        val imageBusiness: ImageView
        val imageUser: ImageView
        val textName: TextView
        val textType: TextView

        init {
            root = view.findViewById(R.id.root)
            imageBusinessParent = view.findViewById(R.id.image_business_parent)
            imageBusiness = view.findViewById(R.id.image_business)
            imageUser = view.findViewById(R.id.image_user)
            textName = view.findViewById(R.id.text_name)
            textType = view.findViewById(R.id.text_type)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_search, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Businesses go first, users second
        if (position < dataSet.businesses.size) {
            // BUSINESS
            val business = dataSet.businesses[position]

            viewHolder.textName.text = business.name
            viewHolder.textType.text = business.type

            viewHolder.imageUser.visibility = View.GONE
            viewHolder.imageBusinessParent.visibility = View.VISIBLE

            val photoByteArray: ByteArray = Base64.decode(business.photo, Base64.DEFAULT)

            Glide.with(context)
                .asBitmap()
                .load(photoByteArray)
                .into(viewHolder.imageBusiness);

            viewHolder.root.setOnClickListener { businessClickAction(business) }
        } else {
            // USER
            val user = dataSet.users[position - dataSet.businesses.size]

            viewHolder.textName.text = user.name
            viewHolder.textType.text = ""

            viewHolder.imageUser.visibility = View.VISIBLE
            viewHolder.imageBusinessParent.visibility = View.GONE

            val photoByteArray: ByteArray = Base64.decode(user.photo, Base64.DEFAULT)

            Glide.with(context)
                .asBitmap()
                .load(photoByteArray)
                .into(viewHolder.imageUser);

            viewHolder.root.setOnClickListener { userClickAction(user) }
        }
    }

    override fun getItemCount() = dataSet.businesses.size + dataSet.users.size
}
