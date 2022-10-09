package ru.mirea.weather_jetpack.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ru.mirea.weather_jetpack.domain.model.Weather
import ru.mirea.weather_jetpack.presentation.view.theme.BlueSky

//@Preview(showBackground = true)
@Composable
fun MainCard(
    currentCity: String,
    currentWeather: Weather?,
    refreshData: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BlueSky,
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    currentWeather?.dateTime?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(
                                top = 16.dp,
                                start = 8.dp
                            ),
                            style = TextStyle(fontSize = 15.sp),
                            color = Color.White
                        )
                    }
                    IconButton(
                        onClick = { refreshData() },
                        modifier = Modifier
                            .size(50.dp)
                            .padding(
                                top = 8.dp,
                                end = 8.dp
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.stat_notify_sync),
                            tint = Color.White,
                            contentDescription = "reload"
                        )
                    }
                }
                Text(
                    text = currentCity,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White,
                    modifier = Modifier.clickable { navController.navigate("settings") }
                )
                Text(
                    text = "${"%.2f".format(currentWeather?.temp)}℃",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
                Text(
                    text = "Feels like ${"%.2f".format(currentWeather?.feelsLike)}℃",
                    style = TextStyle(fontSize = 25.sp),
                    color = Color.White,
                    modifier = Modifier.padding(
                        end = 8.dp
                    )
                )
                currentWeather?.weatherDescription?.let {
                    Text(
                        text = it,
                        style = TextStyle(fontSize = 25.sp),
                        color = Color.White,
                        modifier = Modifier.padding(
                            top = 8.dp
                        )
                    )
                }
                AsyncImage(
                    model = "http://openweathermap.org/img/wn/${currentWeather?.iconCode}@2x.png",
                    contentDescription = "im2",
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            end = 8.dp
                        )
                        .size(120.dp)
                )
                Text(
                    text = "${"%.2f".format(currentWeather?.tempMin)}℃/" +
                            "${"%.2f".format(currentWeather?.tempMax)}℃",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.White,
                    modifier = Modifier.padding(
                        end = 16.dp
                    )
                )
            }
        }
    }
}