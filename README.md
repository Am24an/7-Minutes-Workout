# 7-Minute Workout Application  

## Project Description  
The **7-Minute Workout App** is a simple yet effective fitness application designed to guide users through a quick workout routine. It includes 12 exercises, a BMI calculator, motivational support, and a history tracker to help users stay fit and track their progress.  

---

## Features  

### 1. Workout Session  
- **12 Exercises**:  
  - Each exercise is accompanied by an image for guidance.  
  - Targets various muscle groups for a balanced workout.  
- **Timer**:  
  - 30 seconds for each exercise.  
  - 10 seconds rest period between exercises.  
- **Motivational Text-to-Speech**:  
  - Encourages users during rest periods with motivational lines using **Kotlin's built-in Text-to-Speech** feature.  

### 2. BMI Calculator  
- Calculates **Body Mass Index (BMI)** based on weight and height.  
- Supports **two units of measurement** (metric and us).

### 3. Exercise Progress Tracker  
- **RecyclerView** displays:  
  - The current exercise being performed.  
  - The number of remaining exercises in the session.  

### 4. Exercise History  
- Tracks all completed workout sessions.  
- History data is stored and displayed using **RecyclerView** and **Room Database**.  

### 5. Congratulations Page  
- At the end of the workout, users are greeted with a **congratulations screen** to celebrate their achievement.  

---

## Technology Stack  

- **Language**: Kotlin  
- **UI Components**: RecyclerView for dynamic exercise tracking.  
- **Database**: Room Database for storing exercise history.  
- **Text-to-Speech**: Implemented using Kotlin’s built-in Text-to-Speech functionality.  

---

## Conclusion  

The **7-Minute Workout App** is a perfect companion for users looking for quick, efficient workouts. With additional features like the BMI calculator and exercise history, the app keeps users motivated and on track with their fitness goals.  
