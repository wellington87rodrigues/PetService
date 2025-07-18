package com.example.petservice.ui.theme

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import com.google.android.gms.location.LocationRequest
import android.location.Location
//import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDialog(onDismiss: () -> Unit, onConfirm: (city: String) -> Unit) {

    //val cityName = remember { mutableStateOf("") }

    val services = listOf("Resgate", "Adoção", "Vende-se", "Cruzamento", "Outro(a)")
    var expandedSer by remember { mutableStateOf(false) }
    var selectedTextSer by remember { mutableStateOf("") }

    val animals = listOf("Cachorro", "Gato", "Outro(a)")
    var expandedAni by remember { mutableStateOf(false) }
    var selectedTextAni by remember { mutableStateOf("") }

    val descriptor = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Adicionar Serviço pro Pet:")
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(20.dp))
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth(),
//                    label = { Text(text = "Nome da cidade") },
//                    value = cityName.value,
//                    onValueChange = { cityName.value = it })
//                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { },
                    //modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Obter a Localização") }

                Spacer(modifier = Modifier.height(20.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedSer,
                    onExpandedChange = {
                        expandedSer = !expandedSer
                    }
                ) {
                    OutlinedTextField(
                        value = selectedTextSer,
                        onValueChange = {// No action needed here as selection is handled by the dropdown
                        },
                        readOnly = true,
                        label = { Text("Selecione um Serviço") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Abrir menu"
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSer,
                        onDismissRequest = {
                            expandedSer = false
                        }
                    ) {
                        services.forEach { servico ->
                            DropdownMenuItem(
                                text = { Text(text = servico) },
                                onClick = {
                                    selectedTextSer = servico
                                    expandedSer = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Tirar Foto") }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedAni,
                    onExpandedChange = {
                        expandedAni = !expandedAni
                    }
                ) {
                    OutlinedTextField(
                        value = selectedTextAni,
                        onValueChange = {// No action needed here as selection is handled by the dropdown
                        },
                        readOnly = true,
                        label = { Text("Selecione um Animal") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Abrir menu"
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAni,
                        onDismissRequest = {
                            expandedAni = false
                        }
                    ) {
                        animals.forEach { animal ->
                            DropdownMenuItem(
                                text = { Text(text = animal) },
                                onClick = {
                                    selectedTextAni = animal
                                    expandedAni = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Descrição do Serviço") },
                    value = descriptor.value,
                    onValueChange = { descriptor.value = it },
                    minLines = 5,
                    maxLines = 10,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /*onConfirm(cityName.value)*/ },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text(text = "OK") }
            }

        }
    }
}
