package com.example.petservice.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng

@Composable
fun ServiceDialog(
    onDismiss: () -> Unit,
    onConfirm: (descricao: String, serviceType: String, location: LatLng?) -> Unit,
    onGetLocationClick: () -> Unit,
    currentLocation: LatLng?
) {
    val serviceDescription = remember { mutableStateOf("") }
    val selectedServiceType = remember { mutableStateOf("Adoção") }
    var location by remember { mutableStateOf(currentLocation) }
    LaunchedEffect(currentLocation) {
        location = currentLocation
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Adicionar novo serviço de pet:")
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Fechar diálogo",
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Descrição do serviço") },
                    value = serviceDescription.value,
                    onValueChange = { serviceDescription.value = it }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            selectedServiceType.value = "Adoção" }) {
                        RadioButton(
                            selected = selectedServiceType.value == "Adoção",
                            onClick = { selectedServiceType.value = "Adoção" }
                        )
                        Text(text = "Adoção")
                        RadioButton(
                            selected = selectedServiceType.value == "Resgate",
                            onClick = { selectedServiceType.value = "Resgate" }
                        )
                        Text(text = "Resgate")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = onGetLocationClick) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Pegar Localização")
                    Spacer(Modifier.width(8.dp))
                    Text("Pegar Localização")
                }
                if (location != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Localização \nLatitude: ${location!!.latitude}\nLongitude: ${location!!.longitude}",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        onConfirm(
                            serviceDescription.value,
                            selectedServiceType.value,
                            location
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = serviceDescription.value.isNotBlank()
                ) {
                    Text(text = "Adicionar Serviço")
                }
            }
        }
    }
}