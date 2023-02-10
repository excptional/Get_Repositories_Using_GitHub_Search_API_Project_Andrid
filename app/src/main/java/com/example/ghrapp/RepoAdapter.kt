package com.example.ghrapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.*
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
class RepoAdapter(private val context: Context, private val RepoItems: ArrayList<RepoItems>) :
RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_items, parent,false)
        return RepoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val currentItem = RepoItems[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        if(currentItem.language.isNullOrEmpty()) holder.language.visibility = View.GONE
        else holder.language.text = "language : " + currentItem.language
        holder.issuesCount.text = "issues : " + currentItem.issuesCount
        holder.viewersCount.text = currentItem.watchers
        if(currentItem.visibility == "public") {
            holder.visibility.text = "Public"
            holder.visibilityIcon.setImageResource(R.drawable.visible_icon)
        } else {
            holder.visibility.text = "Private"
            holder.visibilityIcon.setImageResource(R.drawable.invisible_icon)
        }

        holder.cardView.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            )
            builder.addDefaultShareMenuItem()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(currentItem.contentUrl))
        }

    }

    override fun getItemCount(): Int {
        return RepoItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRepo(updateRepoItems: ArrayList<RepoItems>) {
        RepoItems.clear()
        RepoItems.addAll(updateRepoItems)
        notifyDataSetChanged()
    }

    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val visibilityIcon: ImageView = itemView.findViewById(R.id.visibility_icon)
        val visibility: TextView = itemView.findViewById(R.id.visibility)
        val viewersCount: TextView = itemView.findViewById(R.id.viewers_count)
        val language: TextView = itemView.findViewById(R.id.language)
        val issuesCount: TextView = itemView.findViewById(R.id.issues_count)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }
}