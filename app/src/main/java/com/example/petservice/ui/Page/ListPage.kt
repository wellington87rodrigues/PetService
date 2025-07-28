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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petservice.model.MainViewModel
import com.example.petservice.model.Service

@SuppressLint("ContextCastToActivity")
@Composable
fun ListPage(modifier: Modifier = Modifier,
             viewModel: MainViewModel) {
    val serviceList = viewModel.servicies
    val activity = LocalContext.current as? Activity
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(serviceList, key = { it.descriptor}) { service ->
            ServiceItem(service = service, onClose = {
                viewModel.remove(service)
                Toast.makeText(activity, "Serviço Excluido!", Toast.LENGTH_LONG).show()
            }, onClick = {
                Toast.makeText(activity, "Serviço Selecionado", Toast.LENGTH_LONG).show()
            })
        }
    }
}

@Composable
fun ServiceItem(
    service: Service,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.FavoriteBorder,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = modifier.weight(1f)) {
            Text(modifier = Modifier,
                text = service.descriptor,
                fontSize = 24.sp)
            Text(modifier = Modifier,
                text = service.photo?:"Carregando Foto...",
                fontSize = 16.sp)
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}