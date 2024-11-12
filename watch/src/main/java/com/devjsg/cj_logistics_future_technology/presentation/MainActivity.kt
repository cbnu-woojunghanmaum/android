package com.devjsg.cj_logistics_future_technology.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devjsg.cj_logistics_future_technology.presentation.home.HomeScreen
import com.devjsg.cj_logistics_future_technology.presentation.theme.CJLogisticsFutureTechnologyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        requestHealthPermissions()  // 헬스 관련 권한 요청 추가
        disableBatteryOptimizations()

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            CJLogisticsFutureTechnologyTheme {
                HomeScreen()
            }
        }

        // Android 13 이상에서 알림 권한을 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        // Health Connect 설정 화면 열기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openHealthConnectSettings()
        }
    }

    // Health Connect 설정 화면 열기 (설치 여부 확인)
    private fun openHealthConnectSettings() {
        val healthConnectIntent = Intent("androidx.health.ACTION_HEALTH_CONNECT_SETTINGS")
        if (isHealthConnectAppInstalled()) {
            startActivity(healthConnectIntent)
        } else {
            Toast.makeText(this, "Health Connect 앱이 설치되어 있지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    // Health Connect 앱 설치 여부 확인
    private fun isHealthConnectAppInstalled(): Boolean {
        val pm = packageManager
        return try {
            pm.getPackageInfo("com.google.android.apps.healthdata", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun requestPermissions() {
        val requiredPermissions = arrayOf(
            android.Manifest.permission.BODY_SENSORS,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.FOREGROUND_SERVICE,
            android.Manifest.permission.WAKE_LOCK
        )

        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), 0)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "알림 권한이 거부되었습니다. \n설정 화면에서 권한을 허용해 주세요.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        when {
            checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 이미 허용된 경우
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                // 권한 설명이 필요한 경우
                Toast.makeText(this, "이 앱은 알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }

            else -> {
                // 권한 요청
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 헬스 관련 권한 요청
    private fun requestHealthPermissions() {
        val permissions = arrayOf(
            "android.permission.health.READ_HEART_RATE",
            "android.permission.health.WRITE_HEART_RATE"
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missingPermissions.toTypedArray(), 1)
        }
    }

    private fun disableBatteryOptimizations() {
        val intent = Intent()
        val packageName = packageName
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager

        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            // 이미 배터리 최적화에서 제외되어 있습니다.
            return
        }

        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }
}