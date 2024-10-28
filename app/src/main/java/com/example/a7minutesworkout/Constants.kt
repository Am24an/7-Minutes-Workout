package com.example.a7minutesworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()
        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )
        exerciseList.add(jumpingJacks)

        val wallSitLowerBody = ExerciseModel(
            2,
            "Wall Sit Lower Body",
            R.drawable.ic_wall_sit_lower_body,
            false,
            false
        )
        exerciseList.add(wallSitLowerBody)

        val pushUpUpperBody = ExerciseModel(
            3,
            "Push Up Upper Body",
            R.drawable.ic_pushup_upper_body,
            false,
            false
        )
        exerciseList.add(pushUpUpperBody)

        val abdominalCrunchCore = ExerciseModel(
            4,
            "Abdominal Crunch Core",
            R.drawable.ic_abdominal_crunch_core,
            false,
            false
        )
        exerciseList.add(abdominalCrunchCore)

        val setupOntoChair = ExerciseModel(
            5,
            "Setup Onto Chair",
            R.drawable.ic_stepup_onto_chair,
            false,
            false
        )
        exerciseList.add(setupOntoChair)

        val squatLowerBody = ExerciseModel(
            6,
            "Squat Lower Body",
            R.drawable.ic_squat_lower_body,
            false,
            false
        )
        exerciseList.add(squatLowerBody)

        val tricepsDip = ExerciseModel(
            7,
            "Triceps Dip",
            R.drawable.ic_triceps_dip,
            false,
            false
        )
        exerciseList.add(tricepsDip)

        val plankCore = ExerciseModel(
            8,
            "Plank Core",
            R.drawable.ic_plank_core,
            false,
            false
        )
        exerciseList.add(plankCore)

        val highKneesLowerBody = ExerciseModel(
            9,
            "High Knees Lower Body",
            R.drawable.ic_high_knees_lower_body,
            false,
            false
        )
        exerciseList.add(highKneesLowerBody)

        val lungeLowerBody = ExerciseModel(
            10,
            "Lunge Lower Body",
            R.drawable.ic_lunge_lower_body,
            false,
            false
        )
        exerciseList.add(lungeLowerBody)

        val pushUpAndRotation = ExerciseModel(
            11,
            "Push Up And Rotation",
            R.drawable.ic_pushup_and_rotation,
            false,
            false
        )
        exerciseList.add(pushUpAndRotation)

        val sidePlankCore = ExerciseModel(
            12,
            "Side Plank Core",
            R.drawable.ic_side_plank_core,
            false,
            false
        )
        exerciseList.add(sidePlankCore)

        return exerciseList
    }

}