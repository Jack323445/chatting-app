package com.example.friendsonly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {


    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserActivity>
    private lateinit var adapter: user_adapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()


        userList = ArrayList()
        adapter = user_adapter(this, userList)

        userRecyclerView = findViewById(R.id.user_recyclerview)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val  currentUser = postSnapshot.getValue(UserActivity::class.java)

                     if(mAuth.currentUser?.uid != currentUser?.uid){
                         userList.add(currentUser!!)
                     }
                    userList.add(currentUser!!)

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.google.android.material.R.id.layout){
        //write
            mAuth.signOut()
            val intent = Intent(this,LoginActivity::class.java)
            finish()
            startActivity(intent)
        return true
    }
    return true
    }
}