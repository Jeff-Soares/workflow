package com.jx.workflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jx.workflow.ui.theme.WorkflowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { WorkflowTheme { WorkflowScreen() } }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorkflowScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        var name by remember { mutableStateOf("") }
        var greeting by remember { mutableStateOf("") }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it }
        )

        Button(
            onClick = { greeting = if (name.isNotEmpty()) "Hello, $name!" else "" },
            modifier = Modifier.semantics { testTag = "btn_greeting" }
        ) {
            Text("Greeting")
        }

        Text(
            text = greeting,
            modifier = modifier.semantics { testTag = "text_greeting" }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkflowTheme {
        WorkflowScreen()
    }
}
