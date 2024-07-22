package dev.gbenga.fancontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
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
                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.White) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                   // Clock(modifier = Modifier.padding(innerPadding).size(320.dp))
                }
            }
        }
    }
}



data class ClockStyle(
    val secondsDialStyle: DialStyle = DialStyle(),
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun Clock(
    modifier: Modifier = Modifier.size(320.dp),
    clockStyle: ClockStyle = ClockStyle()
) {
    val textMeasurer = rememberTextMeasurer()

    var minuteRotation by remember { mutableStateOf(0f) }

    var secondRotation by remember { mutableStateOf(0f) }

    //secondRotation is updated by 6 degree clockwise every one second
    //here rotation is in negative, in order to get clockwise rotation
    LaunchedEffect(key1 = true) {
        while (true) {
            //in-order to get smooth transition we are updating rotation angle every 16ms
            //1000ms -> 6 degree
            //16ms -> 0.096
            delay(16)
            secondRotation -= 0.096f
        }
    }

    //minuteRotation is updated by 0.1 degree clockwise every one second
    //here rotation is in negative, in order to get clockwise rotation
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000)
            minuteRotation -= 0.1f
        }
    }

    Canvas(
        modifier = modifier
    ) {
        val outerRadius = minOf(this.size.width, this.size.height) / 2f
        val innerRadius = outerRadius - 60.dp.toPx()

        //Seconds Dial
//        dial(
//            radius = outerRadius,
//            rotation = secondRotation,
//            textMeasurer = textMeasurer,
//            dialStyle = clockStyle.secondsDialStyle
//        )

        //Minute Dial
        dial(
            radius = innerRadius,
            rotation = minuteRotation,
            textMeasurer = textMeasurer,
            dialStyle = clockStyle.secondsDialStyle
        )
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val turnerState = rememberTurnerState()

    Turner(modifier = Modifier.fillMaxSize(),
        turnerState= turnerState, icons = listOf(
            ImageVector.vectorResource(id = R.drawable.ic_android_black_24dp),
            Icons.Default.Call, Icons.Default.Build,
            Icons.Default.DateRange, Icons.Default.Email, Icons.Default.Face
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FanControllerTheme {
        Greeting("Android")
    }
}

data class DialStyle(
    val stepsWidth: Dp = 1.2.dp,
    val stepsColor: Color = Color.Black,
    val normalStepsLineHeight: Dp = 8.dp,
    val fiveStepsLineHeight: Dp = 16.dp,
    val stepsTextStyle: TextStyle = TextStyle(),
    val stepsLabelTopPadding: Dp = 12.dp,
)


@OptIn(ExperimentalTextApi::class)
fun DrawScope.dial(
    radius: Float,
    rotation: Float,
    textMeasurer: TextMeasurer,
    dialStyle: DialStyle = DialStyle()
) {
    var stepsAngle = 0

    //this will draw 60 steps
    repeat(60) { steps ->

        //fiveStep lineHeight > normalStep lineHeight
        val stepsHeight = if (steps % 5 == 0) {
            dialStyle.fiveStepsLineHeight.toPx()
        } else {
            dialStyle.normalStepsLineHeight.toPx()
        }

        //calculate steps, start and end offset
        val stepsStartOffset = Offset(
            x = center.x + (radius * cos((stepsAngle + rotation) * (Math.PI / 180f))).toFloat(),
            y = center.y - (radius * sin((stepsAngle + rotation) * (Math.PI / 180))).toFloat()
        )
        val stepsEndOffset = Offset(
            x = center.x + (radius - stepsHeight) * cos(
                (stepsAngle + rotation) * (Math.PI / 180)
            ).toFloat(),
            y = center.y - (radius - stepsHeight) * sin(
                (stepsAngle + rotation) * (Math.PI / 180)
            ).toFloat()
        )

        //draw step
        drawLine(
            color = dialStyle.stepsColor,
            start = stepsStartOffset,
            end = stepsEndOffset,
            strokeWidth = dialStyle.stepsWidth.toPx(),
            cap = StrokeCap.Round
        )

        //draw steps labels
        if (steps % 5 == 0) {
            //measure the given label width and height
            val stepsLabel = String.format("%02d", steps)
            val stepsLabelTextLayout = textMeasurer.measure(
                text = buildAnnotatedString { append(stepsLabel) },
                style = dialStyle.stepsTextStyle
            )

            //calculate the offset
            val stepsLabelOffset = Offset(
                x = center.x + (radius - stepsHeight - dialStyle.stepsLabelTopPadding.toPx()) * cos(
                    (stepsAngle + rotation) * (Math.PI / 180)
                ).toFloat(),
                y = center.y - (radius - stepsHeight - dialStyle.stepsLabelTopPadding.toPx()) * sin(
                    (stepsAngle + rotation) * (Math.PI / 180)
                ).toFloat()
            )

            //subtract the label width and height to position label at the center of the step
            val stepsLabelTopLeft = Offset(
                stepsLabelOffset.x - ((stepsLabelTextLayout.size.width) / 2f),
                stepsLabelOffset.y - (stepsLabelTextLayout.size.height / 2f)
            )

            drawText(
                textMeasurer = textMeasurer,
                text = stepsLabel,
                topLeft = stepsLabelTopLeft,
                style = dialStyle.stepsTextStyle
            )
        }
        stepsAngle += 6
    }
}