package com.example.academyday3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.example.academyday3.task5.AppRoot
import com.example.academyday3.ui.theme.AcademyDay3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcademyDay3Theme {
                AppRoot(modifier = Modifier)
            }
        }
    }
}
