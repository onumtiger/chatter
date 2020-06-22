package com.example.quiplash

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quiplash.DBMethods.DBCalls.Companion.newQuestionType

class ChooseQuestionTypeActivity : AppCompatActivity() {

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

        btnStandard.setOnClickListener {
            Sounds.playClickSound(this)
            newQuestionType = 1
            val intent = Intent(this, AddQuestion::class.java);
            startActivity(intent);
        }

        btnFunny.setOnClickListener {
            Sounds.playClickSound(this)

            newQuestionType = 2
            val intent = Intent(this, AddQuestion::class.java);
            startActivity(intent);
        }

        btnPoetic.setOnClickListener {
            Sounds.playClickSound(this)

            newQuestionType = 3
            val intent = Intent(this, AddQuestion::class.java);
            startActivity(intent);
        }

        btnHarsh.setOnClickListener {
            Sounds.playClickSound(this)

            newQuestionType = 4
            val intent = Intent(this, AddQuestion::class.java);
            startActivity(intent);
        }



    }
}