package org.fmaghi.badgesimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import org.fmaghi.badgesimulator.ui.theme.BadgeSimulatorTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class SharedViewModel : ViewModel() {
    var simulationParameters: SimulationParameters? by mutableStateOf(null)
    var simulationResults: SimulationResults? by mutableStateOf(null)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedViewModel: SharedViewModel by viewModels()


        enableEdgeToEdge()
        setContent {
            BadgeSimulatorTheme {
                val scrollState = rememberScrollState()
                Column (modifier = Modifier.verticalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(top = 60.dp)) {
                    InputField()
                    ResultField()
                }
            }
        }
    }
}

@Composable
fun InputField() {
    val sharedViewModel: SharedViewModel = viewModel()

    val fieldModifier = Modifier.fillMaxWidth()

    var nBadges by remember { mutableIntStateOf(0) }
    var nStickers by remember { mutableIntStateOf(0) }
    var nUnpacks by remember { mutableIntStateOf(0) }
    var nSimulations by remember { mutableIntStateOf(0) }

    var costPerBadge by remember { mutableDoubleStateOf(0.0) }
    var orderPrice by remember { mutableDoubleStateOf(0.0) }
    var nOrdersPerDay by remember { mutableIntStateOf(0) }

    Column (fieldModifier) {
        val rowModifier = fieldModifier.padding(start = 30.dp, end = 30.dp, top = 5.dp)
        val textFieldModifier = Modifier.width(100.dp)
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("徽章总数", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(nBadges),
                onValueChange = { nBadges = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
          Text("贴纸数", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(nStickers),
                onValueChange = { nStickers = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
          Text("拍一拆", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(nUnpacks),
                onValueChange = { nUnpacks = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
          Text("模拟次数", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(nSimulations),
                onValueChange = { nSimulations = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
          Text("单个徽章成本价", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(costPerBadge),
                onValueChange = { costPerBadge = it.toDoubleOrNull() ?: 0.0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("每单售价", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(orderPrice),
                onValueChange = { orderPrice = it.toDoubleOrNull() ?: 0.0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(rowModifier, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("每天订单数", modifier = Modifier.align(Alignment.CenterVertically))
            TextField(
                modifier = textFieldModifier,
                value = textFieldValue(nOrdersPerDay),
                onValueChange = { nOrdersPerDay = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        // run simulation
        Button (
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp),
            onClick = {
                val simulationParameters = SimulationParameters(
                    nTotalBadges = nBadges,
                    nStickers = nStickers,
                    nUnpacksPerOrder = nUnpacks,
                    costPerBadge = costPerBadge,
                    pricePerOrder = orderPrice,
                    nOrdersPerDay = nOrdersPerDay,
                    nSimulations = nSimulations
                )
                sharedViewModel.simulationParameters = simulationParameters

                sharedViewModel.simulationResults = runSimulation(simulationParameters) }) {
            Text("开始")
        }
    }

}

@Composable
fun ResultField() {
    val sharedViewModel: SharedViewModel = viewModel()
    Text(sharedViewModel.simulationResults.toString())
}

fun <T : Number> textFieldValue(numeric: T): String =
    if (numeric.toDouble() == 0.0) ""
    else numeric.toString()


