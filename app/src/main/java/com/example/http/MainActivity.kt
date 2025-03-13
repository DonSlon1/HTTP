package com.example.http

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.http.ui.WeatherScreen
import com.example.http.ui.theme.HTTPTheme
import com.example.http.viewmodel.WeatherViewModel

/**
 * Hlavní aktivita aplikace
 * 
 * Tato třída je vstupním bodem aplikace a nastavuje UI pomocí Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            // Aplikujeme téma aplikace
            HTTPTheme {
                // Nastavíme základní Surface pro celou aplikaci
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Získáme instanci ViewModelu pomocí viewModel() funkce
                    val weatherViewModel: WeatherViewModel = viewModel()
                    
                    // Zobrazíme hlavní obrazovku aplikace
                    WeatherScreen(weatherViewModel)
                    
                    // Při prvním spuštění načteme počasí pro výchozí město
                    // LaunchedEffect spustí kód pouze jednou při první kompozici
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        weatherViewModel.getWeather("Praha")
                    }
                }
            }
        }
    }
}