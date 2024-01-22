package com.ets.preduzmi.ui.businesses

import android.content.Context
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ets.preduzmi.R
import com.ets.preduzmi.models.BusinessModel

class BusinessesRecyclerViewAdapter (
    private val context: Context,
    private val dataSet: ArrayList<BusinessModel>,
    private val cardClickAction: (BusinessModel) -> Unit,
    private val likeButtonAction: (BusinessModel) -> Unit,
    private val saveButtonAction: (BusinessModel) -> Unit,
) :
    RecyclerView.Adapter<BusinessesRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root: ConstraintLayout
        val photo: ImageView
        val authorPhoto: ImageView
        val name: TextView
        val description: TextView
        val type: TextView
        val likeButton: ImageButton
        val saveButton: ImageButton

        init {
            root = view.findViewById(R.id.root)
            photo = view.findViewById(R.id.image_business)
            authorPhoto = view.findViewById(R.id.image_author)
            name = view.findViewById(R.id.text_name)
            description = view.findViewById(R.id.text_description)
            type = view.findViewById(R.id.text_type)
            likeButton = view.findViewById(R.id.button_like)
            saveButton = view.findViewById(R.id.button_save)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_business_like_save, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet[position].name
        viewHolder.description.text = dataSet[position].description
        viewHolder.type.text = dataSet[position].type

        viewHolder.root.setOnClickListener { cardClickAction(dataSet[position]) }
        viewHolder.likeButton.setOnClickListener { likeButtonAction(dataSet[position]) }
        viewHolder.saveButton.setOnClickListener { saveButtonAction(dataSet[position]) }

        val photoByteArray: ByteArray = Base64.decode(dataSet[position].photo, Base64.DEFAULT)
        val authorPhotoByteArray: ByteArray = Base64.decode(dataSet[position].postedBy.photo, Base64.DEFAULT)

        Glide.with(context)
            .asBitmap()
            .load(photoByteArray)
            .into(viewHolder.photo);

        Glide.with(context)
            .asBitmap()
            .load(authorPhotoByteArray)
            .placeholder(R.drawable.default_user)
            .into(viewHolder.authorPhoto);
    }

    override fun getItemCount() = dataSet.size
}
