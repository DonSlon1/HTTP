package com.example.http.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.http.data.WeatherResponse
import com.example.http.repository.WeatherRepository
import kotlinx.coroutines.launch

/**
 * ViewModel pro práci s daty o počasí
 * 
 * Tato třída propojuje UI s daty z repository.
 * Uchovává stav aplikace a zpracovává akce od uživatele.
 */
class WeatherViewModel : ViewModel() {
    
    // Repository pro získávání dat
    private val repository = WeatherRepository()
    
    // Stav UI - data o počasí
    var weatherData by mutableStateOf<WeatherResponse?>(null)
        private set
    
    // Stav UI - indikátor načítání
    var isLoading by mutableStateOf(false)
        private set
    
    // Stav UI - chybová zpráva
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    /**
     * Získá počasí pro zadané město
     * 
     * @param city Název města
     */
    fun getWeather(city: String) {
        // Resetujeme chybovou zprávu
        errorMessage = null
        // Nastavíme příznak načítání
        isLoading = true
        
        // Spustíme korutinu pro asynchronní operaci
        viewModelScope.launch {
            // Získáme data z repository
            val result = repository.getWeatherForCity(city)
            
            // Zpracujeme výsledek
            result.fold(
                onSuccess = { response ->
                    // Uložíme data
                    weatherData = response
                    // Zrušíme příznak načítání
                    isLoading = false
                },
                onFailure = { exception ->
                    // Nastavíme chybovou zprávu
                    errorMessage = exception.message ?: "Neznámá chyba"
                    // Zrušíme příznak načítání
                    isLoading = false
                }
            )
        }
    }
}