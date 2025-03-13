package com.example.http.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.http.data.WeatherResponse
import com.example.http.viewmodel.WeatherViewModel

/**
 * Hlavní obrazovka aplikace pro zobrazení počasí
 * 
 * @param viewModel ViewModel obsahující logiku a data
 * @param modifier Volitelný Modifier pro úpravu layoutu
 */
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    // Stav pro vstupní pole města
    var cityInput by remember { mutableStateOf("Praha") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nadpis aplikace
        Text(
            text = "Předpověď počasí",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Vyhledávací pole
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Zadejte město") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Tlačítko pro vyhledání
        Button(
            onClick = { viewModel.getWeather(cityInput) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text("Vyhledat počasí")
        }
        
        // Obsah - závisí na stavu
        when {
            // Zobrazujeme indikátor načítání
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            // Zobrazujeme chybovou zprávu
            viewModel.errorMessage != null -> {
                ErrorMessage(viewModel.errorMessage!!)
            }
            
            // Zobrazujeme data o počasí
            viewModel.weatherData != null -> {
                WeatherContent(viewModel.weatherData!!)
            }
            
            // Výchozí stav - výzva k vyhledání
            else -> {
                EmptyState()
            }
        }
    }
}

/**
 * Komponenta pro zobrazení dat o počasí
 * 
 * @param weather Data o počasí k zobrazení
 */
@Composable
fun WeatherContent(weather: WeatherResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Lokalita
                    Text(
                        text = "${weather.location.name}, ${weather.location.country}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Ikona počasí
                    AsyncImage(
                        model = "https:${weather.current.condition.icon}",
                        contentDescription = weather.current.condition.text,
                        modifier = Modifier.size(100.dp)
                    )
                    
                    // Aktuální teplota
                    Text(
                        text = "${weather.current.temp_c}°C",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Popis počasí
                    Text(
                        text = weather.current.condition.text,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Další detaily
                    WeatherDetailsSection(weather)
                }
            }
            
            // Výukový komentář
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Jak aplikace funguje?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = """
                            • Aplikace používá architekturu MVVM (Model-View-ViewModel).
                            • Data jsou získávána z WeatherAPI.com pomocí knihovny Retrofit.
                            • JSON odpověď je deserializována pomocí knihovny Gson.
                            • Asynchronní operace jsou zpracovány pomocí Kotlin Coroutines.
                            • UI je vytvořeno pomocí Jetpack Compose.
                            • Obrázky jsou načítány pomocí knihovny Coil.
                            • Celá aplikace demonstruje moderní přístup k vývoji Android aplikací.
                        """.trimIndent(),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/**
 * Sekce s detaily počasí
 */
@Composable
fun WeatherDetailsSection(weather: WeatherResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Pocitová teplota
        WeatherDetailItem(
            label = "Pocitová teplota",
            value = "${weather.current.feelslike_c}°C"
        )
        
        // Vlhkost
        WeatherDetailItem(
            label = "Vlhkost",
            value = "${weather.current.humidity}%"
        )
        
        // Vítr
        WeatherDetailItem(
            label = "Vítr",
            value = "${weather.current.wind_kph} km/h"
        )
    }
}

/**
 * Položka s detailem počasí
 */
@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Komponenta pro zobrazení chybové zprávy
 */
@Composable
fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Chyba!",
            color = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Výchozí stav, když nejsou načtena žádná data
 */
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Zadejte město a klikněte na 'Vyhledat počasí'",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}