package com.example.petservice.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.petservice.model.Service

@Composable
fun ServiceDialog(onDismiss: () -> Unit, onConfirm: (service: Service) -> Unit) {
    val servEndereco = remember { mutableStateOf("") }
    val servLocation = remember { mutableStateOf("") }
    val servServicos = remember { mutableStateOf("") }
    val servFoto = remember { mutableStateOf("") }
    val servAnimals = remember { mutableStateOf("") }
    val servDescricao = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() } ) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Adicionar o Serviço:")
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Digite o Endereço, caso não consiga a localização") },
                    value = servEndereco.value,
                    onValueChange = { servEndereco.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Sua Localização") },
                    value = servLocation.value,
                    onValueChange = { servLocation.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Escolha o Serviço") },
                    value = servServicos.value,
                    onValueChange = { servServicos.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Escolha sua foto") },
                    value = servFoto.value,
                    onValueChange = { servFoto.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Escolha o Animal") },
                    value = servAnimals.value,
                    onValueChange = { servAnimals.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Digite a sua Descrição") },
                    value = servDescricao.value,
                    onValueChange = { servDescricao.value = it })
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text(text = "OK") }
            }
        }
    }
}