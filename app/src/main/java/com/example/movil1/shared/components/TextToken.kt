package com.example.movil1.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextToken(textToken:String){
    Text(
        text = "Token: ${textToken?.take(20)}...",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            )
            .padding(8.dp)
    )
}