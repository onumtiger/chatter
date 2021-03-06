package com.example.quiplash.user.addQuestions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.quiplash.database.DBMethods
import com.example.quiplash.R
import com.example.quiplash.Sounds
import com.example.quiplash.user.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_choose_question_type.*

class ChooseQuestionTypeActivity : AppCompatActivity() {

    var newType = ""
    lateinit var textfield :TextView
    lateinit var addQuestion :Button
    lateinit var new_question_text : EditText

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_choose_question_type)

        val btnStandard = findViewById<Button>(R.id.btnStandard)
        val btnFunny = findViewById<Button>(R.id.btnFunny)
        val btnPoetic = findViewById<Button>(R.id.btnPoetic)
        val btnHarsh = findViewById<Button>(R.id.btnHarsh)
        val btnBck = findViewById<ImageButton>(R.id.profile_go_back_arrow3)

        textfield = findViewById(R.id.choose)
        addQuestion = findViewById(R.id.BtnAddNewQuestion)
        new_question_text = findViewById(R.id.new_question_text)

        addQuestion.visibility = View.INVISIBLE
        new_question_text.visibility = View.INVISIBLE


        addQuestion.setOnClickListener {
            if (new_question_text.text.toString() != ""){
                DBMethods.saveQuestion(new_question_text.text.toString(), newType)
                Toast.makeText(this, "New Question Added To Database!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@ChooseQuestionTypeActivity, ProfileActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Tip In A New Question", Toast.LENGTH_LONG).show()
            }
        }

        btnBck.setOnClickListener {
            Sounds.playClickSound(this)

            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
        }

        btnStandard.setOnClickListener {
            Sounds.playClickSound(this)
            newType = "Standard"
            ButtonsInvisible()
        }

        btnFunny.setOnClickListener {
            Sounds.playClickSound(this)

            newType = "Funny"
            ButtonsInvisible()
        }

        btnPoetic.setOnClickListener {
            Sounds.playClickSound(this)

            newType = "Poetic"
            ButtonsInvisible()
        }

        btnHarsh.setOnClickListener {
            Sounds.playClickSound(this)

            newType = "Harsh"
            ButtonsInvisible()
        }



    }

    fun ButtonsInvisible(){
        btnStandard.visibility= View.INVISIBLE
        btnFunny.visibility= View.INVISIBLE
        btnPoetic.visibility= View.INVISIBLE
        btnHarsh.visibility= View.INVISIBLE

        textfield.text = "Please Tip in the new Question"
        addQuestion.visibility = View.VISIBLE
        new_question_text.visibility = View.VISIBLE

    }
}