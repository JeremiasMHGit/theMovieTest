package com.example.themovietest

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.themovietest.databinding.ActivityMainBinding
import com.example.themovietest.di.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var __datos: HashMap<String, Any>? = null
    var __idNotificacion: Int? = null
    private var _dbFirebase = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        setupWithNavController(binding.navView, navController)

        initKoin()
        initPermissions()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainActivity)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    apiServiceModule,
                    retrofitModule,
                    databaseModule
                )
            )
        }
    }

    private fun initPermissions () {
        __datos = HashMap()
        __idNotificacion = 0

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if (allAreGranted) {
                subirUbicacion()
            } else mostrarDialogo()
        }

        solicitarPermisos()
    }

    private fun solicitarPermisos() {
        val appPerms = arrayOf(
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION,
            permission.ACCESS_BACKGROUND_LOCATION
        )
        permissionLauncher.launch(appPerms)
    }

    private fun mostrarDialogo(
        esPermisos: Boolean = true,
        titulo: String? = "",
        contenido: String? = ""
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(if (esPermisos) "Necesita activar la ubicación del dispositivo para continuar" else contenido)
            .setTitle(if (esPermisos) "Permisos Requeridos" else titulo)
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
            if (esPermisos)
                solicitarPermisos()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun subirUbicacion() {
        GlobalScope.launch {
            while (true) {
                subirInformacion()
                delay(60000 * 5)
            }
        }
    }

    private fun subirInformacion() {
        val locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val criteria = Criteria()

        val location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false)!!)

        __datos!!["latitud"] = location!!.latitude
        __datos!!["longitud"] = location.longitude
        __datos!!["fecha"] = Date()
        Log.e("", "latitud " + (location.latitude) + "Longitud " + location.longitude)
        _dbFirebase.collection("users")
            .add(__datos!!)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.e("", "UBIcacion ENVIADA" )
                }
            }
            .addOnSuccessListener {
                __idNotificacion = __idNotificacion!! + 1
                notificationUser()
            }
            .addOnFailureListener{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Por favor verifique su conexión a internet wifi/datos moviles")
                    .setTitle("Fallo envio de ubicación")
                builder.setPositiveButton(
                    "Aceptar"
                ) { _, _ ->

                }

                val dialog = builder.create()
                dialog.show()
            }
    }

    private fun notificationUser() {
        createNotificationChannel()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel()
        val builder = NotificationCompat.Builder(applicationContext, "examenAndroid")
            .setSmallIcon(R.mipmap.ic_icon_j)
            .setContentTitle("Ubicación")
            .setContentText("Se ah actualizado su ubicacion")
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(__idNotificacion!!, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "examenAndroid1"
            val description = "pruebas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("examenAndroid", name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                applicationContext.getSystemService<NotificationManager>(
                    NotificationManager::class.java
                )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        val __LOCATION_REQUEST = 10
        val __COARSE_LOCATION_REQUEST = 20
        val __BACKGROUND_LOCATION_REQUEST = 30
        val __CAMERA_REQUEST = 40
        val __READ_REQUEST = 60
    }
}