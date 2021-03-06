package com.example.quiplash.user.friends

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.quiplash.database.Callback
import com.example.quiplash.database.DBMethods
import com.example.quiplash.R
import com.example.quiplash.Sounds
import com.example.quiplash.user.UserQP
import java.util.ArrayList

class Add_Player : DialogFragment() {
    lateinit var current_User: UserQP

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.interaction_add_player, container, false)
    }
    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val btnAdd = view.findViewById<TextView>(R.id.interaction_add_btn)
        val btnCancel = view.findViewById<TextView>(R.id.interaction_cancel_btn)
        val viewUsername: EditText = view.findViewById(R.id.interaction_username_add)

        lateinit var otherUsers: ArrayList<UserQP>
        var friendsListCurrentUser = emptyList<String>()
        var friendsListFriend = emptyList<String>()

        val callbackGetUsers = object:
            Callback<ArrayList<UserQP>> {
            override fun onTaskComplete(result: ArrayList<UserQP>) {
                otherUsers = result
            }
        }
        DBMethods.getUsers(callbackGetUsers)

        val callbackGetUser = object:
            Callback<UserQP> {
            override fun onTaskComplete(result : UserQP) {
                current_User = result
                friendsListCurrentUser = current_User.friends
                Log.d("friends current user", friendsListCurrentUser.toString())
            }
        }
        DBMethods.getUser(callbackGetUser)

        btnAdd.setOnClickListener(){
            context?.let { it1 ->
                Sounds.playClickSound(
                    it1
                )
            }

            var usernameFriend = viewUsername.text.toString()
            var alreadyfriends = false

            // check if empty
            if (usernameFriend.isNotEmpty()) {
                // check if already friends
                for (i in 0..friendsListCurrentUser.size - 1) {
                    if (friendsListCurrentUser[i].equals(usernameFriend, true)) {
                        Log.d("already friends", "Failed!")
                        alreadyfriends = true
                        break
                    }
                }
                if (alreadyfriends) {
                    dismiss()
                }
                else {
                    // check if input = username
                    if (usernameFriend.equals(current_User.userName.toString(), true)) {
                        Log.d("Can't add yourself", "Failed!")
                        dismiss()
                    } else {
                        var friend = UserQP(
                            "",
                            "",
                            false,
                            -1,
                            "",
                            emptyList<String>(),
                            ""
                        )
                        // check if friend user exists and get other user and its friendlist
                        for (i in 0..otherUsers.size - 1) {
                            if (otherUsers[i].userName.equals(usernameFriend, true)) {
                                friend = otherUsers[i]
                                friendsListFriend = friend.friends
                            }
                        }
                        if (friend.userID == ""){
                            Log.d("user not found", "Failed!")
                            dismiss()
                        }
                        else {
                            var newfriendsListFriend = emptyList<String>().toMutableList()
                            for(i in 0..friendsListFriend.size-1) {
                                newfriendsListFriend.add(i, friendsListFriend[i])
                            }
                            newfriendsListFriend.add(0, current_User.userName)
                            friend.friends = newfriendsListFriend

                            var newfriendsListCurrentUser = emptyList<String>().toMutableList()
                            for(i in 0..friendsListCurrentUser.size-1) {
                                newfriendsListCurrentUser.add(i,friendsListCurrentUser[i])
                            }
                            newfriendsListCurrentUser.add(0, friend.userName)
                            current_User.friends = newfriendsListCurrentUser

                            // update users
                            current_User.userID?.let { it1 -> DBMethods.editUser(it1, current_User) }
                            friend.userID?.let { it1 -> DBMethods.editUser(it1, friend) }
                        }
                    }
                }
            }
            val intent = Intent(context, FriendsActivity::class.java);
            startActivity(intent);
        }

        btnCancel.setOnClickListener {
            context?.let { it1 ->
                Sounds.playClickSound(
                    it1
                )
            }
            dismiss()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var  setFullScreen = false
        if (arguments != null) {
            setFullScreen = requireNotNull(arguments?.getBoolean("fullScreen"))
        }
        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }
}