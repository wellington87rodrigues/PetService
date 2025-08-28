package com.example.petservice.ui.Page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petservice.model.RequestsViewModel
import com.example.petservice.model.Service
import com.example.petservice.model.ServiceStatus
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

private enum class RequestsFilter { OPEN, ACCEPTED_BY_ME, MINE }

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
                    ServiceStatus.COMPLETED -> AssistChip(onClick = {}, label = { Text("Concluído") })
                    ServiceStatus.CANCELED  -> AssistChip(onClick = {}, label = { Text("Cancelado") })
                }
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ListPage() {
    val vm: RequestsViewModel = viewModel()

    // Fluxos das listas
    val open by vm.openRequests.collectAsState(initial = emptyList())
    val mine by vm.myRequests.collectAsState(initial = emptyList())
    val accepted by vm.acceptedByMe.collectAsState(initial = emptyList())
    val uid = Firebase.auth.currentUser?.uid

    // Filtro selecionado (sobrevive a rotação/volta da tela)
    var selectedTab by rememberSaveable { mutableStateOf(RequestsFilter.OPEN) }

    val tabs = listOf("Todos Chamados", "Para Resgatar", "Meus Pedidos")
    val selectedIndex = when (selectedTab) {
        RequestsFilter.OPEN -> 0
        RequestsFilter.ACCEPTED_BY_ME -> 1
        RequestsFilter.MINE -> 2
    }

    // Decide a lista corrente e a mensagem de vazio
    val currentList: List<Service>
    val emptyText: String
    when (selectedTab) {
        RequestsFilter.OPEN -> {
            currentList = open
            emptyText = "Não há solicitações abertas."
        }
        RequestsFilter.ACCEPTED_BY_ME -> {
            currentList = accepted
            emptyText = "Você não aceitou nenhuma solicitação."
        }
        RequestsFilter.MINE -> {
            currentList = mine
            emptyText = "Você ainda não abriu solicitações."
        }
    }

    Column(Modifier.fillMaxSize()) {
        // Top tabs (3 botões)
        TabRow(selectedTabIndex = selectedIndex) {
            tabs.forEachIndexed { i, label ->
                Tab(
                    selected = selectedIndex == i,
                    onClick = {
                        selectedTab = when (i) {
                            0 -> RequestsFilter.OPEN
                            1 -> RequestsFilter.ACCEPTED_BY_ME
                            else -> RequestsFilter.MINE
                        }
                    },
                    text = { Text(label) }
                )
            }
        }

        // Conteúdo por filtro
        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(emptyText, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(currentList, key = { it.id }) { s ->
                    ServiceCard(
                        item = s,
                        currentUid = uid,
                        onAccept = vm::acceptRequest,
                        onComplete = vm::completeRequest,
                        onCancel = vm::cancelRequest
                    )
                }
            }
        }
    }
}
