package com.dew.dewvideoplayer.adpater

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dew.dewvideoplayer.R
import com.dew.dewvideoplayer.adpater.VideoAdapter.MyViewHolder
import com.dew.dewvideoplayer.model.Video

class VideoAdapter(
    var videos: ArrayList<Video>,
    val context: Context,
    val listener: VideoAdapterListener
) :
    RecyclerView.Adapter<MyViewHolder?>() {

    private var selectedRow: Int = 0

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var disc: TextView
        var thumb: ImageView
        var root: View

        init {
            root = view
            thumb = view.findViewById(R.id.thumb) as ImageView
            title = view.findViewById(R.id.title) as TextView
            disc = view.findViewById(R.id.disc) as TextView
        }
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.list_row_small, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(@NonNull myViewHolder: MyViewHolder, i: Int) {
        val video: Video = videos[i]
        myViewHolder.title.text = video.title
        myViewHolder.disc.text = video.description
        myViewHolder.root.isSelected = selectedRow == i
        val requestOption: RequestOptions = RequestOptions().centerCrop()
        Glide.with(context).setDefaultRequestOptions(requestOption).load(Uri.parse(video.thumb))
            .into(myViewHolder.thumb)
        myViewHolder.root.setOnClickListener {
            selectedRow = i
            listener.onClickRow(video)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    interface VideoAdapterListener {
        fun onClickRow(video: Video)
    }
}