package com.example.composepoc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
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
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
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
    val height = remember { mutableStateOf(0f) }
    val width = remember { mutableStateOf(0f) }
    val scale = remember { mutableStateOf(1f) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .onGloballyPositioned {
            size.value = it.size.toSize()
        }) {
        CommentBox(userHandle, offsetX, offsetY, size, height, width, scale)
    }

}

val maxScale = 4f
val minScale = 0.7f

@Composable
fun CommentBox(
    userHandle: String,
    offsetX: MutableState<Float>,
    offsetY: MutableState<Float>,
    size: MutableState<Size>,
    height: MutableState<Float>,
    width: MutableState<Float>,
    scale: MutableState<Float>
) {
    fun calculateNewScale(k: Float): Float =
        if ((scale.value <= maxScale && k > 1f) || (scale.value >= minScale && k < 1f)) scale.value * k else scale.value

//    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
//        scale *= zoomChange
//        rotation += rotationChange
//        offset += offsetChange
//    }

    Surface(color = Color.Black, shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .width(200.dp)
            .onSizeChanged {
                width.value = it.width.toFloat()
                height.value = it.height.toFloat()
            }

            .pointerInput(Unit) {
                detectTransformGestures { centroid, dragAmount, zoom, rot ->
                    var original = Offset(offsetX.value, offsetY.value)

                    val newScale = calculateNewScale(zoom)


                    if (newScale != scale.value && (size.value.width - width.value * newScale) >= 0f &&
                        (size.value.height - height.value * newScale) >= 0f
                    ) {
//                        Log.d("Akhil", "newscale ${original.x - ((width.value * newScale - width.value) / 2)}")
//                        original -= Offset(((width.value * newScale - width.value) / 2), ((height.value * newScale - height.value) / 2))
//                         original = Offset(
//                            original.x - ((width.value * newScale - width.value) / 2),
//                            original.y - ((height.value * newScale - height.value) / 2)
//                        )
                        Log.d("Akhil", "original x ${original.x - ((width.value * newScale - width.value) / 2)}")
                        Log.d("Akhil", "drag ${original.x - ((width.value * newScale - width.value) / 2) + dragAmount.x}")

                        width.value = width.value * newScale
                        height.value = height.value * newScale
                        scale.value = newScale
//                        original = Offset((centroid.x - (width.value / 2)), (centroid.y - (height.value / 2)))
                    }

//                    Log.d("Akhil", "total width ${size.value.width} width ${width.value}, height ${height.value}")

//
//
//                    if ((size.value.height - height.value * newScale) >= 0f)
//                        height.value = height.value * newScale
//
//                    val x = size.value.width - width.value
//                    val y = size.value.height - height.value
//                    if (x >= 0f && y >= 0f) scale.value = newScale
//                    Log.d("Akhil", "dragAmount x $dragAmount")
                    val summed = original + dragAmount
//                    Log.d("Akhil", "summed x ${summed.x}")
                    val newValueAfterDrag = Offset(
                        x = summed.x.coerceIn(0f, size.value.width - width.value),
                        y = summed.y.coerceIn(0f, size.value.height - height.value)
                    )

//                    Log.d("Akhil", "x ${newValueAfterDrag.x}, y ${newValueAfterDrag.y}")

                    offsetX.value = newValueAfterDrag.x
                    offsetY.value = newValueAfterDrag.y
//                    scale.value *= zoom


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


//@Composable
//fun ZoomableImage(imageUrl: String?, maxScale: Float = 4f, minScale: Float = 0.7f) {
//    val scale by remember { mutableStateOf(1f) }
//    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
//    val translation by remember { mutableStateOf(Offset(0f, 0f)) }
//    fun calculateNewScale(k: Float): Float =
//        if ((scale <= maxScale && k > 1f) || (scale >= minScale && k < 1f)) scale * k else scale
//    Glide.with(AmbientContext.current).asBitmap()
//        .load(imageUrl)
//        .into(object : CustomTarget<Bitmap>() {
//            override fun onLoadCleared(placeholder: Drawable?) {}
//            override fun onResourceReady(
//
//                resource: Bitmap,
//                transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
//            ) {
//                bitmap.value = resource
//            }
//        })
//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .background(Color.Black)
//
//    ) {
//        bitmap?.let { loadedBitmap ->
//            Image(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .zoomable(onZoomDelta = {
//                        scale = calculateNewScale(it)
//                    })
//                    .rawDragGestureFilter(
//                        object : DragObserver {
//                            override fun onDrag(dragDistance: Offset): Offset {
//                                translation = translation.plus(dragDistance)
//                                return super.onDrag(dragDistance)
//                            }
//                        })
//                    .graphicsLayer(
//                        scaleX = scale,
//                        scaleY = scale,
//                        translationX = translation.x,
//                        translationY = translation.y
//                    ),
//                bitmap = loadedBitmap.asImageBitmap()
//            )
//        } ?: kotlin.run {
//            Text("Loading Image...", color = Color.White)
//        }
//    }
//}