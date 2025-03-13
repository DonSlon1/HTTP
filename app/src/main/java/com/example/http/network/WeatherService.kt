package com.example.http.network

import com.example.http.data.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API rozhraní pro komunikaci s WeatherAPI.com
 * 
 * Tento interface definuje koncové body API, ke kterým se budeme připojovat.
 * Je implementován pomocí Retrofit knihovny, která generuje konkrétní implementaci.
 */
interface WeatherService {
    
    /**
     * Získá aktuální počasí pro dané město
     * 
     * @param apiKey API klíč pro přístup k WeatherAPI.com
     * @param city Název města, pro které chceme získat počasí
     * @return Response objekt obsahující data o počasí nebo chybovou odpověď
     */
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "no"
    ): Response<WeatherResponse>
    
    companion object {
        // Základní URL pro API
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
        
        // API klíč pro WeatherAPI.com - pro výukové účely
        // V reálné aplikaci by byl klíč v BuildConfig nebo proměnných prostředí
        private const val API_KEY = "d5237c1750b7400d92e125158243103"
        
        /**
         * Vytvoří instanci WeatherService pro komunikaci s API
         */
        fun create(): WeatherService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            
            return retrofit.create(WeatherService::class.java)
        }
    }
}