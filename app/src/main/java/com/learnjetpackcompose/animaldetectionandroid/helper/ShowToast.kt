package com.learnjetpackcompose.animaldetectionandroid.helper

import android.content.Context
import android.widget.Toast

fun ShowToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}