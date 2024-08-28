package com.example.arduinin

import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.wifi.WifiNetworkSuggestion
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arduinin.ui.theme.ArduininTheme
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setupWifi()
        super.onCreate(savedInstanceState)
        setContent {
            ArduininTheme {
                MainScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (wifiManager.isWifiEnabled) {
            connectWithArduino()
        } else {
            Toast.makeText(this, "Wi-Fi is still off", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupWifi() {
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(this, "Turn on Wi-Fi...", Toast.LENGTH_LONG).show()
            openWifiSettings()
        }
    }

    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun connectWithArduino() {
        val wifiPassword = "Maklowicz"
        val wifiSSID = "Opel Corso"
        val wifiNetworkSuggestion = WifiNetworkSuggestion.Builder()
            .setSsid(wifiSSID)
            .setWpa2Passphrase(wifiPassword)
            .setIsAppInteractionRequired(true)
            .build()

        val suggestionsList = listOf(wifiNetworkSuggestion)

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val status = wifiManager.addNetworkSuggestions(suggestionsList)
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            Toast.makeText(this, "Failed to add network suggestions", Toast.LENGTH_LONG).show()
        }

        val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!intent.action.equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                    return
                }
            }
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current


    var enteredCode by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enter Code",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = enteredCode,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        if (enteredCode.isNotEmpty()) {
                            enteredCode = enteredCode.dropLast(1)
                        }
                    }
            )

            Spacer(modifier = Modifier.height(70.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 1..3) {
                    Row {
                        for (j in 1..3) {
                            val number = (i - 1) * 3 + j
                            Button(
                                onClick = {
                                    if (enteredCode.length < 4) {
                                        enteredCode += number.toString()
                                    }
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(100.dp)
                            ) {
                                Text(
                                    text = number.toString(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (enteredCode.length == 4) {
                        sendCode(enteredCode.toInt())
                        enteredCode = ""
                    } else {
                        Toast.makeText(context, "Enter 4 digits", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Submit",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }


    }
}


private fun sendCode(code: Int) {
    sendCommand("sendcode?value=$code")
}


private fun sendCommand(command: String) {
    val serverIP = "192.168.4.1"
    val fullURL = "http://$serverIP/$command"
    println("Sending command: $fullURL")

    Thread {
        try {
            val url = URL(fullURL)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                println("Response Code : $responseCode")
                println("Response Message : $responseMessage")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}