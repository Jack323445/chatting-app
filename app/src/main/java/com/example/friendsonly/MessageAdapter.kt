package com.example.friendsonly

import android.app.Notification
import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, private val messageList: ArrayList<com.example.friendsonly.Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ITEM_RECIEVE = 1
    var ITEM_SENT = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            //inflate recieve
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieved,parent,false)
            return RecieveViewHolder(view)
        }else {
            //inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }



    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){


            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else{
            //do stuff recieved
            val viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT

        }else {
            return ITEM_RECIEVE
        }
    }


    override fun getItemCount(): Int {
        return messageList.size

    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent)

    }


    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.txt_recieve)

    }

}