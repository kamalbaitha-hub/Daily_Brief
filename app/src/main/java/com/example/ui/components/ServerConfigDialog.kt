package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServerConfigDialog(
    currentUrl: String,
    githubOwner: String,
    githubRepo: String,
    isCheckingUpdate: Boolean,
    onSaveSettings: (url: String, owner: String, repo: String) -> Unit,
    onCheckUpdateNow: () -> Unit,
    onTriggerPipeline: () -> Unit,
    onTriggerNotification: () -> Unit,
    onDismiss: () -> Unit
) {
    var urlText by remember { mutableStateOf(currentUrl) }
    var ownerText by remember { mutableStateOf(githubOwner) }
    var repoText by remember { mutableStateOf(githubRepo) }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Backend & Updates Setup",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // Section 1: GitHub Over-The-Air Updater
                Text(
                    text = "GITHUB OVER-THE-AIR UPDATES",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Configure your GitHub repository to enable automatic release checking, in-app update notifications, and 1-tap APK installation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = ownerText,
                        onValueChange = { ownerText = it },
                        label = { Text("GitHub Owner") },
                        placeholder = { Text("e.g. kamalbaitha") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("github_owner_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = repoText,
                        onValueChange = { repoText = it },
                        label = { Text("Repository") },
                        placeholder = { Text("e.g. daily-brief") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("github_repo_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onCheckUpdateNow,
                    enabled = !isCheckingUpdate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_check_update_now"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isCheckingUpdate) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Checking GitHub Releases...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Check for GitHub Update Now")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Backend Server URL
                Text(
                    text = "NODE.JS BACKEND SERVER",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Connects to Express server for AI summarization & RSS ingestion.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    label = { Text("Backend Server URL") },
                    placeholder = { Text("http://10.0.2.2:5000/") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("backend_url_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Emulator: http://10.0.2.2:5000/\n• Phone on Wi-Fi: http://<PC_LOCAL_IP>:5000/",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Diagnostic quick actions
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "DIAGNOSTICS & ALARMS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { onTriggerPipeline() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_trigger_pipeline"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Trigger Real-Time Backend Pipeline Now")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = { onTriggerNotification() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_trigger_notification"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Real-Time Push Notification")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveSettings(urlText.trim(), ownerText.trim(), repoText.trim())
                    onDismiss()
                },
                modifier = Modifier.testTag("save_server_url_button")
            ) {
                Text("Save Configuration")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
