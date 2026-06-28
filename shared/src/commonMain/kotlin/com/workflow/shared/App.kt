package com.workflow.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun App(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    Box(
        modifier = modifier.padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        val brush = Brush.linearGradient(
            colors = List(4) {
                Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat(),
                    alpha = 1.0f
                )
            }
        )

        Text(
            "Hello from Shared Module!",
            style = TextStyle(fontWeight = FontWeight.Bold, brush = brush)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}
