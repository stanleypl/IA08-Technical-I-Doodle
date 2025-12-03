package com.stanley.doodle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DoodleScreen()
            }
        }
    }
}
data class DrawingStroke(
    val color: Color,
    val strokeWidth: Float,
    val points: List<Offset>
)
@Composable
fun DoodleScreen() {
    var brushSize by remember { mutableStateOf(20f) }
    var brushColor by remember { mutableStateOf(Color.Black) }
    val strokes = remember { mutableStateListOf<DrawingStroke>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ToolPanel(
            brushSize = brushSize,
            onBrushSizeChange = { brushSize = it },
            brushColor = brushColor,
            onColorChange = { brushColor = it },
            onClear = { strokes.clear() },
            onUndo = {
                if (strokes.isNotEmpty()) {
                    strokes.removeAt(strokes.lastIndex)
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        DrawingCanvas(
            strokes = strokes,
            brushColor = brushColor,
            brushSize = brushSize,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}
@Composable
fun ToolPanel(
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    brushColor: Color,
    onColorChange: (Color) -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Brush size: ${brushSize.toInt()}")
        Slider(
            value = brushSize,
            onValueChange = onBrushSizeChange,
            valueRange = 5f..60f
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Colors:")

        val colors = listOf(
            Color.Black,
            Color.DarkGray,
            Color.Gray,
            Color.LightGray,
            Color.White,
            Color.Red,
            Color(0xFFFF5722),
            Color(0xFFFF9800),
            Color.Yellow,
            Color(0xFFCDDC39),
            Color.Green,
            Color(0xFF4CAF50),
            Color.Cyan,
            Color.Blue,
            Color(0xFF3F51B5),
            Color(0xFF2196F3),
            Color(0xFF9C27B0),
            Color.Magenta,
            Color(0xFFE91E63),
            Color(0xFF795548)
        )

        Column {
            colors.chunked(10).forEach { rowColors ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(color, shape = CircleShape)
                                .border(
                                    width = if (brushColor == color) 3.dp else 1.dp,
                                    color = if (brushColor == color) Color.White else Color.Black,
                                    shape = CircleShape
                                )
                                .clickable {
                                    onColorChange(color)
                                }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onClear) {
                Text("Clear")
            }
            Button(onClick = onUndo) {
                Text("Undo")
            }
        }
    }
}
@Composable
fun DrawingCanvas(
    strokes: MutableList<DrawingStroke>,
    brushColor: Color,
    brushSize: Float,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .background(Color.White)
            .pointerInput(brushColor, brushSize) {
                detectDragGestures(
                    onDragStart = { offset ->
                        strokes.add(
                            DrawingStroke(
                                color = brushColor,
                                strokeWidth = brushSize,
                                points = listOf(offset)
                            )
                        )
                    },
                    onDrag = { change, _ ->
                        val lastIndex = strokes.lastIndex
                        if (lastIndex >= 0) {
                            val lastStroke = strokes[lastIndex]
                            val newPoints = lastStroke.points + change.position
                            strokes[lastIndex] = lastStroke.copy(points = newPoints)
                        }
                        change.consume()
                    }
                )
            }
    ) {
        strokes.forEach { stroke ->
            if (stroke.points.size > 1) {
                val path = Path().apply {
                    moveTo(stroke.points.first().x, stroke.points.first().y)
                    for (point in stroke.points.drop(1)) {
                        lineTo(point.x, point.y)
                    }
                }

                drawPath(
                    path = path,
                    color = stroke.color,
                    style = Stroke(width = stroke.strokeWidth)
                )
            } else if (stroke.points.size == 1) {
                val p = stroke.points.first()
                drawCircle(
                    color = stroke.color,
                    radius = stroke.strokeWidth / 2,
                    center = p
                )
            }
        }
    }
}

