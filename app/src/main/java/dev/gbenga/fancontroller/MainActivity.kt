package dev.gbenga.fancontroller

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.gbenga.fancontroller.ui.theme.FanControllerTheme
import dev.gbenga.turner.Turner
import dev.gbenga.turner.rememberTurnerState
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FanControllerTheme {
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize(),
                    containerColor = Color.DarkGray) { innerPadding ->

                    val turnerState = rememberTurnerState()

                    val selectedPosition = turnerState.rememberSelectedPosition()

                    LaunchedEffect(selectedPosition.value) {
                        Toast.makeText(context,"clicked: ${selectedPosition.value}",
                            Toast.LENGTH_SHORT).show()
                    }

                    Log.d("Greeting", "turnerState _ ${selectedPosition.value}")

                    Turner(modifier = Modifier.fillMaxSize().padding(innerPadding),
                        turnerState= turnerState, icons = listOf( //baseline_phone_24
                            Icons.Default.Build,
                            Icons.Default.Face,
                            Icons.Default.ShoppingCart,
                            Icons.Default.Notifications,
                            Icons.Default.Delete,
                            Icons.Default.AccountCircle,
                        )
                    )
                }
            }
        }

    }
}

