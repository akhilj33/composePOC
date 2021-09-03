package com.example.composepoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.composepoc.ui.theme.ComposePOCTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetMainScreen("@akj33 \n cgjdhcbdhbcdjshbc")
        }
    }
}

@Composable
fun SetMainScreen(userHandle: String) {
    val offsetX = remember { mutableStateOf(200f) }
    val offsetY = remember { mutableStateOf(100f) }
    val size = remember { mutableStateOf(Size.Zero) }
    val height = remember { mutableStateOf(0) }
    val scale = remember { mutableStateOf(1f) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .onGloballyPositioned {
            size.value = it.size.toSize()
        }) {
        CommentBox(userHandle, offsetX, offsetY, size, height, scale)
    }

}

@Composable
fun CommentBox(
    userHandle: String,
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    size: MutableState<Size>,
    height: MutableState<Int>,
    scale: MutableState<Float>
) {
    Surface(color = Color.Black, shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .width(200.dp)
            .onSizeChanged {
                height.value = it.height
            }
//            .onGloballyPositioned {
//                height.value = it.size.height
//            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, dragAmount, zoom, rot ->
                    val original = Offset(offsetX.value, offsetY.value)
                    val summed = original + dragAmount
                    val newValue = Offset(
                        x = summed.x.coerceIn(0f, size.value.width - 200.dp.toPx()),
                        y = summed.y.coerceIn(0f, size.value.height - height.value.toFloat())
                    )
                    offsetX.value = newValue.x
                    offsetY.value = newValue.y
                    scale.value *= zoom

                }
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
    ) {
        TextView(text = userHandle)
    }
}

@Composable
fun TextView(text: String) {
    Text(
        text = text, style = TextStyle(color = Color.White), maxLines = 2, overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(10.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePOCTheme {
        SetMainScreen("@akj33 \n cgjdhcbdhbcdjshbc")
    }
}