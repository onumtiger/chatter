package com.example.quiplash

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*

class InviteFriendsToGameActivity : AppCompatActivity() {
    lateinit var current_User: UserQP
    lateinit var friend : UserQP
    lateinit var otherUsers: ArrayList<UserQP>

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_invite_friends_to_game)

        val btnBack = findViewById<AppCompatImageButton>(R.id.friends_go_back_arrow)
        val friendsListView = findViewById<ListView>(R.id.friends)
        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.friends_swiperefresh)

        val gameID = intent.getStringExtra("gameID")
        getCurrentGame(gameID, friendsListView)

        btnBack.setOnClickListener() {
            super.onBackPressed();
        }

        refreshLayout.setOnRefreshListener {
            getCurrentGame(gameID, friendsListView)
            refreshLayout.isRefreshing = false
        }

    }

    fun getCurrentGame(gameID: String, friendsListView: ListView) {
        var currentGame: Game
        val callback = object : Callback<Game> {
            override fun onTaskComplete(result: Game) {
                currentGame = result
                // fetch friends from db
                getFriendsList(friendsListView, currentGame)
            }
        }
        DBMethods.DBCalls.getCurrentGame(callback, gameID)
    }

    fun getFriendsList (friendsListView: ListView, currentGame: Game) {
        var friendsListCurrentUser = emptyList<String>()
        val friendsUserList = mutableListOf<UserQP>()

        val callbackGetUsers = object: Callback<ArrayList<UserQP>> {
            override fun onTaskComplete(result: ArrayList<UserQP>) {
                otherUsers = result

                val callbackGetUser = object: Callback<UserQP> {
                    override fun onTaskComplete(result :UserQP) {
                        current_User = result
                        friendsListCurrentUser = current_User.friends

                        for(i in 0 .. friendsListCurrentUser.size-1) {
                            for(j in 0 .. otherUsers.size-1) {
                                // get friend information
                                if(friendsListCurrentUser[i] == otherUsers[j].userName.toString()) {
                                    friend = otherUsers[j]
                                    if(!currentGame.users.contains(friend.userID)) {
                                        friendsUserList.add(friend)
                                    }
                                    break
                                }
                            }
                        }

                        val adapter = InviteFriendsToGameListAdapter(
                            applicationContext,
                            R.layout.invite_friends_to_game_list_item,
                            friendsUserList
                        )
                        friendsListView.adapter = adapter
                    }
                }
                DBMethods.DBCalls.getUser(callbackGetUser)
            }
        }
        DBMethods.DBCalls.getUsers(callbackGetUsers)
    }
}