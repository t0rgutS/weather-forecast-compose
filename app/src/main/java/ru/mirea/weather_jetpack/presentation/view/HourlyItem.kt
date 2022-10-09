package ru.mirea.weather_jetpack.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.mirea.weather_jetpack.domain.model.Weather
import ru.mirea.weather_jetpack.presentation.view.theme.Teal

@Composable
fun HourlyItem(weather: Weather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp),
        backgroundColor = Teal,
        elevation = 0.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                weather.dateTime?.let {
                    Text(
                        text = it,
                        color = Color.White
                    )
                }
                weather.weatherDescription?.let {
                    Text(
                        text = it,
                        color = Color.White
                    )
                }
            }
            Text(
                text = "${"%.2f".format(weather.temp)}℃",
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            AsyncImage(
                model = "http://openweathermap.org/img/wn/${weather.iconCode}d@2x.png",
                contentDescription = "im5",
                modifier = Modifier
                    .padding(
                        end = 8.dp
                    )
                    .size(35.dp)
            )
        }
    }
}