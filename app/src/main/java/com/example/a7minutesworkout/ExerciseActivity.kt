package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackNotificationBinding
import java.util.Locale
import kotlin.random.Random

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 1

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 1

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        //Keep the screen awake during the workout.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        // This is used to enable back press btn in the action bar.
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        pauseTimers() // Pause timers function when back is pressed.
        customDialogForBackButton()
        //super.onBackPressed()
    }

    // Function to pause timers when back button is pressed.
    private val handler = Handler(Looper.getMainLooper())

    private fun pauseTimers() {
        handler.post {
            //Execute on the main thread.
            restTimer?.cancel()
            exerciseTimer?.cancel()
        }
    }

    // Function to resume timers whether it is a Rest view or Exercise view.
    private fun resumeTimers() {
        handler.post {
            //Execute on the main thread.

            if (restTimer != null && binding?.flRestView?.visibility == View.VISIBLE) {

                // If the rest Timer is active and rest view is visible.
                val remainingTime = (restTimerDuration * 10000) - (restProgress * 1000)

                // Calculate remaining time for the rest timer
                if (remainingTime > 1000) {
                    setRestProgressBar() // resume the rest timer if it is more than 1 second.
                } else {
                    //if remaining time <= 1, then skip rest and proceed to exercise.
                    currentExercisePosition++
                    exerciseList!![currentExercisePosition].setIsSelected(true)

                    exerciseAdapter!!.notifyDataSetChanged()
                    setupExerciseView()
                }


            } else if (exerciseTimer != null && binding?.flExerciseView?.visibility == View.VISIBLE) {
                // If exercise timer is active and exercise view is visible
                val remainingTime = (exerciseTimerDuration * 30000) - (exerciseProgress * 1000)

                // Calculate remaining time for exercise timer
                if (remainingTime > 1000) {
                    setExerciseProgressBar() // Resume exercise timer
                } else {
                    if (currentExercisePosition < exerciseList?.size!! - 1) {

                        exerciseList!![currentExercisePosition].setIsSelected(false) // Mark current exercise as not selected
                        exerciseList!![currentExercisePosition].setIsCompleted(true) // Mark current exercise as completed
                        exerciseAdapter!!.notifyDataSetChanged() // Notify adapter of data change
                        setupRestView() // Setup rest view
                    } else {
                        // If all exercises are completed, finish the activity
                        finish()
                        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        }
    }


    // this is the fun for custom dialog message when pressed during the workout.
    private fun customDialogForBackButton() {
        pauseTimers() // pause timer when dialog is shown
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackNotificationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
            resumeTimers() // Resume timers when 'No' is clicked.
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView() {

        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout/" + R.raw.btn_sound
            )

            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE


        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()

        //Announce rest period and upcoming Exercise name
        val restTimeInSeconds = 10
        val upcomingExerciseName = exerciseList!![currentExercisePosition + 1].getName()

        // TTS message variations
        val ttsVariations = listOf(
            "Rest for $restTimeInSeconds seconds. Get ready for $upcomingExerciseName.",
            "Get ready for $upcomingExerciseName.",
            "Time to recover. Coming up: $upcomingExerciseName. ",
            "Stay strong! Next: $upcomingExerciseName.",
            "Time for a break. Here comes: $upcomingExerciseName."
        )

        //Generate a random index
        val randomIndex = Random.nextInt(ttsVariations.size)

        val textToSpeak = if (currentExercisePosition == -1) {
            "Get ready for ${exerciseList!![currentExercisePosition + 1].getName()}" // for the first exercise.
        } else {
            ttsVariations[randomIndex] //random messages for other exercise.
        }

        /* this is previously used.
        var textToSpeak = "Rest for $restTimeInSeconds seconds. Get ready for $upcomingExerciseName."
        */

        // Introduce a slight delay Before speaking
        Handler(Looper.getMainLooper()).postDelayed(
            { speakOut(textToSpeak) }, 1000
        )

        setRestProgressBar()
    }

    private fun setupExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE



        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    // Function which set the rest progress bar
    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        //Shutting down the Text to speech feature when activity is destroyed.
        //START
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null

        //Clear the flag when the activity is destroyed.
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set US Eng as lang for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}