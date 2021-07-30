package com.adevinta.randomusers.allusers.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adevinta.randomusers.R
import com.adevinta.randomusers.allusers.model.User
import com.bumptech.glide.Glide

class AllUsersAdapter(private val listItemClickListener: ListItemClickListener) :
    ListAdapter<User, AllUsersAdapter.UserItemViewHolder>(ListItemCallback()) {

    interface ListItemClickListener {
        fun onItemClick(item: User, position: Int)
        fun onDeleteClick(item: User, position: Int)
    }

    class ListItemCallback : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
        holder.itemView.setOnClickListener {
            listItemClickListener.onItemClick(item, position)
        }
        holder.deleteUser.setOnClickListener {
            listItemClickListener.onDeleteClick(item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_all_users_item, parent, false)
        return UserItemViewHolder(view)
    }

    class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namesurname: TextView = itemView.findViewById(R.id.user_name_text)
        var phone: TextView = itemView.findViewById(R.id.phone_text)
        var userImage: ImageView = itemView.findViewById(R.id.user_image)
        var email: TextView = itemView.findViewById(R.id.email_text)
        var deleteUser: ImageView = itemView.findViewById(R.id.delate_image)

        fun bind(item: User, position: Int) {
            namesurname.text = item.nameSurname
            phone.text = item.phone
            email.text = item.email
            Glide.with(itemView).load(item.picture).circleCrop()
                .placeholder(R.drawable.ic_user_placeholder).into(userImage)

        }
    }

}