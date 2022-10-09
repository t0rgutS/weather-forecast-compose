package ru.mirea.weather_jetpack.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import ru.mirea.weather_jetpack.presentation.view.theme.BlueSky

@Composable
fun CityChoiceCard(
    cities: List<String>,
    currentCity: String,
    applySelected: (city: String) -> Unit,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(currentCity) }
    var labelSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        backgroundColor = BlueSky,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { selectedCity = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp
                        )
                        .onGloballyPositioned { coordinates ->
                            labelSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { labelSize.width.toDp() })
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(onClick = {
                            selectedCity = city
                            expanded = false
                        }) {
                            Text(text = city)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 55.dp,
                        start = 20.dp,
                        end = 20.dp,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text(text = "Back")
                }
                Button(
                    onClick = {
                        applySelected(selectedCity)
                        navController.navigate("forecast")
                    }
                ) {
                    Text(text = "Apply")
                }
            }
        }
    }
}