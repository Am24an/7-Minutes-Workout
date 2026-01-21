package com.example.a7minutesworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode


class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // Us unit View
    }

    // A variable to hold a value to make a selected view visible.
    private var currentVisibleView: String = METRIC_UNITS_VIEW
    private var binding: ActivityBmiBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBMI)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBMI?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView() // call this to show the metric units by default

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }

        binding?.btnCalculate?.setOnClickListener {
            calculateUnits()
        }

    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE //Metric Height units
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE // Metric weight Units
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tilUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilUsUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared if it is a Us Unit.
        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared if it is a Us Unit.
        binding?.llDisplayBMiResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW  // current view is updated is here.
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear() // weight value is cleared.
        binding?.etUsUnitHeightFeet?.text!!.clear() // height feet is cleared.
        binding?.etUsUnitHeightInch?.text!!.clear() // height inch is cleared.
        binding?.llDisplayBMiResult?.visibility = View.INVISIBLE
    }

    //fun to display BMI Result
    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription =
                "Oops! You really need to take better care of yourself! Eat more but Nutritious as well!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {

            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {

            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {

            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a Good shape!👍"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {

            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {

            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {

            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {

            bmiLabel = "Obese Class ||| ( Very severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMiResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription
    }

    //function for BMI
    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }


    private fun calculateUnits() {
        if (currentVisibleView == METRIC_UNITS_VIEW) {

            if (validateMetricUnits()) {
                val heightValue: Float =
                    binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResult(bmi)
            } else {
                Toast.makeText(
                    this@BMIActivity,
                    "Please enter valid values.", Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (validateUsUnits()) {
                val usUnitHeightValueFeet: String =
                    binding?.etUsUnitHeightFeet?.text.toString()
                val usUnitHeightValueInch: String =
                    binding?.etUsUnitHeightInch?.text.toString()

                // Weight value entered in EditText Component
                val usUnitWeightValue: Float =
                    binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                //Here is the Height feet and inch values are merged and multiplied by 12 for converter.
                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                displayBMIResult(bmi)

            } else {
                Toast.makeText(
                    this@BMIActivity,
                    "Please enter valid values.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Fun for US Units to validate the data
    private fun validateUsUnits(): Boolean {
        var isValid = true
        if (binding?.etUsMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etUsUnitHeightFeet?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etUsUnitHeightInch?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }
}