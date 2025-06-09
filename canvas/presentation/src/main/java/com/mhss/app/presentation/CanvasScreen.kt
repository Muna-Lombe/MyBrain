package com.mhss.app.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mhss.app.presentation.components.AiResultSheet
import com.mhss.app.ui.R
import com.mhss.app.ui.components.common.MyBrainAppBar
import com.mhss.app.ui.toUserMessage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel = koinViewModel(),
) {
    val aiState = viewModel.aiState
    var text by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.text) { text = viewModel.text }
    LaunchedEffect(viewModel.prompt) { prompt = viewModel.prompt }

    if (aiState.showAiSheet) {
        AiResultSheet(
            loading = aiState.loading,
            result = aiState.result,
            error = aiState.error?.toUserMessage(),
            onReplaceClick = {
                aiState.result?.let { viewModel.onEvent(CanvasEvent.UpdateText(it)) }
                viewModel.onEvent(CanvasEvent.AiResultHandled)
            },
            onAddToNoteClick = {},
            onCopyClick = {}
        )
    }

    Scaffold(
        topBar = { MyBrainAppBar(stringResource(id = R.string.canvas)) }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                value = text,
                onValueChange = {
                    text = it
                    viewModel.onEvent(CanvasEvent.UpdateText(it))
                }
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = prompt,
                onValueChange = {
                    prompt = it
                    viewModel.onEvent(CanvasEvent.UpdatePrompt(it))
                },
                label = { Text(stringResource(id = R.string.prompt)) }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.onEvent(CanvasEvent.Summarize) }) { Text(stringResource(id = R.string.summarize)) }
                Button(onClick = { viewModel.onEvent(CanvasEvent.Extend) }) { Text(stringResource(id = R.string.extend)) }
                Button(onClick = { viewModel.onEvent(CanvasEvent.MakeConcise) }) { Text(stringResource(id = R.string.make_concise)) }
            }
            Button(onClick = { viewModel.onEvent(CanvasEvent.GenerateImage) }) {
                Text(stringResource(id = R.string.generate_image))
            }
            aiState.imageUrl?.let { url ->
                AsyncImage(
                    modifier = Modifier.size(200.dp),
                    model = url,
                    contentDescription = null
                )
            }
        }
    }
}
