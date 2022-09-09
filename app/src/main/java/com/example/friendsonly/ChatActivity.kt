package com.example.friendsonly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<com.example.friendsonly.Message>
    private lateinit var mDbRef: DatabaseReference

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        recieverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageRecyclerView = findViewById(R.id.chatrecyclerview)
        messageBox = findViewById(R.id.messagebox)
        sendButton = findViewById(R.id.chat_btn)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        // logic to adding data to recyclerview
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message =
                            postSnapshot.getValue(com.example.friendsonly.Message::class.java)
                         messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding message to the database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                    messageBox.setText("")
                }

        }

    }
}