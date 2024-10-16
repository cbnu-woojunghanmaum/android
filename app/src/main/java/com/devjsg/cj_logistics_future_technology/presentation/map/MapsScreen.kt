package com.devjsg.cj_logistics_future_technology.presentation.map

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devjsg.cj_logistics_future_technology.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    latitude: Double,
    longitude: Double,
    age: Int,
    phone: String,
    employeeName: String,
    createdAt: String,
) {
    var savedLatitude by remember { mutableDoubleStateOf(latitude) }
    var savedLongitude by remember { mutableDoubleStateOf(longitude) }
    var savedAge by remember { mutableIntStateOf(age) }
    var savedPhone by remember { mutableStateOf(phone) }
    var savedEmployeeName by remember { mutableStateOf(employeeName) }
    var savedCreatedAt by remember { mutableStateOf(createdAt) }

    val initialPosition = LatLng(savedLatitude, savedLongitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 17f)
    }

    val uiSettings = remember {
        MapUiSettings(myLocationButtonEnabled = true)
    }

    val properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }

    val context = LocalContext.current
    val activity = (context as? Activity)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("신고자 조회") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 49.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleMap(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .shadow(
                        elevation = 12.dp,
                        spotColor = Color(0x1A000000),
                        ambientColor = Color(0x1A000000)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFDFDFDF),
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .width(328.dp)
                    .height(328.dp),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                if (savedLatitude != 0.0 && savedLongitude != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(savedLatitude, savedLongitude)),
                        title = "Saved Location",
                        snippet = "Latitude: $savedLatitude, Longitude: $savedLongitude"
                    )
                }
            }

            Text(
                text = "신고자 조회",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .align(Alignment.Start)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFF7F7F7), shape = RoundedCornerShape(size = 16.dp))
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_man_face),
                    contentDescription = "Member Icon",
                    tint = Color.Unspecified
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = savedEmployeeName,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF242424),
                        )
                    )
                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "만 $savedAge 세",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF6F6F6F),
                            )
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(1.dp)
                                .height(8.dp)
                                .background(Color(0xFFDFDFDF))
                        )

                        Text(
                            text = savedPhone,
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF6F6F6F),
                            )
                        )
                    }
                }
                Text(
                    text = savedCreatedAt,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFEF151E),
                    )
                )
            }
        }
    }
}