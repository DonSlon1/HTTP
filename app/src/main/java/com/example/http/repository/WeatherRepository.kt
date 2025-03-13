package com.example.http.repository

import com.example.http.data.WeatherResponse
import com.example.http.network.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository třída pro práci s daty o počasí
 * 
 * Tato třída zapouzdřuje logiku pro získávání dat z API.
 * Slouží jako prostředník mezi ViewModel a API službou.
 */
class WeatherRepository {
    
    // Instance API služby
    private val weatherService = WeatherService.create()
    
    /**
     * Získá aktuální počasí pro zadané město
     * 
     * @param city Název města
     * @return Výsledek operace jako Result objekt, který obsahuje buď data nebo chybu
     */
    suspend fun getWeatherForCity(city: String): Result<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Volání API
                val response = weatherService.getCurrentWeather(city = city)
                
                // Kontrola, zda bylo volání úspěšné
                if (response.isSuccessful) {
                    // Vrátíme data jako úspěch
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Prázdná odpověď od API"))
                } else {
                    // Vrátíme chybu
                    Result.failure(Exception("Chyba při získávání dat: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                // Zachytíme výjimky (např. problémy s připojením)
                Result.failure(Exception("Síťová chyba: ${e.message}", e))
            }
        }
    }
}