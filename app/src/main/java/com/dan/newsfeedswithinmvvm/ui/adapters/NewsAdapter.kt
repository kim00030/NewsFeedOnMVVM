package com.dan.newsfeedswithinmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dan.newsfeedswithinmvvm.R
import com.dan.newsfeedswithinmvvm.ui.models.Article

/**
 * Created by Dan Kim
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_article_preview,
            parent,
            false
        )

        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article = differ.currentList[position]

        holder.itemView.apply {
            val imageView = findViewById<ImageView>(R.id.ivArticleImage)
            val tvSource = findViewById<TextView>(R.id.tvSource)
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val tvDescription = findViewById<TextView>(R.id.tvDescription)
            val tvPublicationAt = findViewById<TextView>(R.id.tvPublishedAt)

            Glide.with(this).load(article.urlToImage).into(imageView)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublicationAt.text = article.publishedAt

            setOnItemClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}