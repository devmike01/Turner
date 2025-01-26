package dev.gbenga.fancontroller

//import dev.gbenga.turner.Turner
//import dev.gbenga.turner.rememberTurnerState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.gbenga.fancontroller.ui.theme.FanControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FanControllerTheme {
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize(),
                    containerColor = Color.DarkGray) { innerPadding ->

                   // val turnerState = rememberTurnerState()

                   // val selectedPosition = turnerState.rememberSelectedPosition()

//                    LaunchedEffect(selectedPosition.value) {
//                        Toast.makeText(context,"clicked: ${selectedPosition.value}",
//                            Toast.LENGTH_SHORT).show()
//                    }
//
//                    Log.d("Greeting", "turnerState _ ${selectedPosition.value}")

//                    Turner(modifier = Modifier.fillMaxSize().padding(innerPadding),
//                        turnerState= turnerState, icons = listOf( //baseline_phone_24
//                            Icons.Default.Build,
//                            Icons.Default.Face,
//                            Icons.Default.ShoppingCart,
//                            Icons.Default.Notifications,
//                            Icons.Default.Delete,
//                            Icons.Default.AccountCircle,
//                        )
//                    )
                }
            }
        }

    }
}

