package com.example.quiplash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.quiplash.DBMethods.DBCalls.Companion.updateGameUsers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.quiplash.GameManager.Companion.game
import com.example.quiplash.GameManager.Companion.setGameinfo


class Join_GameActivity : AppCompatActivity() {
    lateinit var gameList: MutableList<Game>
    private lateinit var auth: FirebaseAuth

    //Firestore
    lateinit var db: CollectionReference
    private val dbGamesPath = "testGames"

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_game)
        auth = FirebaseAuth.getInstance()

        db = FirebaseFirestore.getInstance().collection(dbGamesPath)

        val btnNewGameActivity = findViewById<AppCompatImageButton>(R.id.join_new_game_btn)
        val btnBack = findViewById<AppCompatImageButton>(R.id.join_game_go_back_arrow)
        val activeGamesList = findViewById<ListView>(R.id.active_games_list)
        gameList = mutableListOf()

        btnNewGameActivity.setOnClickListener() {
            val intent = Intent(this, New_GameActivity::class.java);
            startActivity(intent);
        }

        btnBack.setOnClickListener() {
            super.onBackPressed();
        }

        db.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    val activeGame = document.toObject(Game::class.java)
                    gameList.add(activeGame)
                    val adapter = GameListAdapter(
                        applicationContext,
                        R.layout.active_game_list_item,
                        gameList
                    )
                    activeGamesList.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)

            }

        activeGamesList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // Get the selected item text from ListView
                val selectedItem = parent.getItemAtPosition(position) as Game
                val userSize = (selectedItem.users.size) + 1
                //val userID = "userID$userSize"
                selectedItem.users.toMutableList().add(auth.currentUser?.uid.toString())
                updateGameUsers(selectedItem)


                db.document(selectedItem.gameID).get()
                    .addOnSuccessListener { documentSnapshot ->
                        try {

                            game = documentSnapshot.toObject(Game::class.java)!!

                        } finally {
                            val intent = Intent(this, Host_WaitingActivity::class.java);
                            intent.putExtra("gameID", selectedItem.gameID)
                            startActivity(intent);
                        }

                    }


            }

    }


    fun setGame(gameid: String) {
        db.document(gameid).get()
            .addOnSuccessListener { documentSnapshot ->
                setGameinfo(documentSnapshot.toObject(Game::class.java)!!)
            }
    }


}


