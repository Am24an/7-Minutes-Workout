package com.example.a7minutesworkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //val flStartButton : FrameLayout = findViewById(R.id.flStart)
        binding?.flStart?.setOnClickListener{
            //Toast.makeText(this, "Now we will start the exercise.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ExerciseActivity::class.java)
        startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}