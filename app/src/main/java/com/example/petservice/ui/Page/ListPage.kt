package com.example.petservice.ui.Page

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petservice.model.MainViewModel
import com.example.petservice.model.Service


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import com.example.petservice.model.ServiceStatus
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.petservice.model.RequestsViewModel



@Composable
fun ServiceCard(
    item: Service,
    currentUid: String?,
    onAccept: (String) -> Unit = {},
    onComplete: (String) -> Unit = {},
    onCancel: (String) -> Unit = {}
) {
    val isMine = (item.solicitanteId == currentUid)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.descricao, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Tipos: ${item.serviceTypes.joinToString()}")
            item.location?.let { ll ->
                Spacer(Modifier.height(4.dp))
                Text("Local: ${"%.5f".format(ll.latitude)}, ${"%.5f".format(ll.longitude)}")
            }
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (item.status) {
                    ServiceStatus.OPEN -> {
                        if (!isMine) {
                            Button(onClick = { onAccept(item.id) }) { Text("Aceitar") }
                        } else {
                            // Se for seu pedido e ainda OPEN, dá para cancelar
                            AssistChip(onClick = {}, label = { Text("Aguardando resgate") })
                            IconButton(onClick = { onCancel(item.id) }) {
                                Icon(Icons.Filled.Close, contentDescription = "Cancelar")
                            }
                        }
                    }
                    ServiceStatus.ACCEPTED -> {
                        if (item.atendenteId == currentUid) {
                            Button(onClick = { onComplete(item.id) }) { Text("Concluir") }
                        } else {
                            AssistChip(onClick = {}, label = { Text("Em atendimento") })
                        }
                    }
                    ServiceStatus.COMPLETED -> {
                        AssistChip(onClick = {}, label = { Text("Concluído") })
                    }
                    ServiceStatus.CANCELED -> {
                        AssistChip(onClick = {}, label = { Text("Cancelado") })
                    }
                }
            }
        }
    }
}




@SuppressLint("ContextCastToActivity")
@Composable
fun ListPage() {
    val vm: RequestsViewModel = viewModel()
    val open = vm.openRequests.collectAsState(initial = emptyList()).value
    val mine = vm.myRequests.collectAsState(initial = emptyList()).value
    val accepted = vm.acceptedByMe.collectAsState(initial = emptyList()).value
    val uid = Firebase.auth.currentUser?.uid

    LazyColumn {
        // Abertos
        item { Text("Abertos", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(12.dp)) }
        items(open, key = { it.id }) { s: Service ->
            ServiceCard(
                item = s,
                currentUid = uid,
                onAccept = vm::acceptRequest,   // <-- aceitar
                onComplete = {},                // não conclui aqui
                onCancel = {}                   // quem abre não aparece aqui normalmente
            )
        }

        // Aceitos por mim
        item { Text("Aceitos por mim", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(12.dp)) }
        items(accepted, key = { it.id }) { s: Service ->
            ServiceCard(
                item = s,
                currentUid = uid,
                onAccept = {},
                onComplete = vm::completeRequest, // <-- concluir
                onCancel = {}                      // não cancela aqui
            )
        }

        // Meus pedidos (posso cancelar se ainda OPEN)
        item { Text("Meus pedidos", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(12.dp)) }
        items(mine, key = { it.id }) { s: Service ->
            ServiceCard(
                item = s,
                currentUid = uid,
                onAccept = {},
                onComplete = {},
                onCancel = vm::cancelRequest         // <-- cancelar (se for seu e estiver OPEN)
            )
        }
    }
}

