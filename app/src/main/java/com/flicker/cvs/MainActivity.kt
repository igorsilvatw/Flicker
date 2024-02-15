package com.flicker.cvs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flicker.cvs.feature.flimage.presentation.SearchScreen
import com.flicker.cvs.ui.theme.FlickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickerTheme {
                Surface(
                    modifier = Modifier
                        .padding(top = 48.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SearchScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
