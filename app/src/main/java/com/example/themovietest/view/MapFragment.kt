package com.example.themovietest.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.themovietest.R
import com.example.themovietest.databinding.FragmentMapBinding
import com.example.themovietest.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

@Suppress("UNREACHABLE_CODE")
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _dbFirebase = Firebase.firestore
    private var _binding: FragmentMapBinding? = null
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private lateinit var __LatLng:LatLng

    private val binding get() = _binding!!

    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val MapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        _dbFirebase.collection("users")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    var fecha: Timestamp
                    val sfd = SimpleDateFormat("dd/MM/yyyy HH:mm")
                    for (document in it.result) {
                        latitud = document.data["latitud"] as Double
                        longitud = document.data["longitud"] as Double
                        fecha = document.data["fecha"] as Timestamp
                        val date = sfd.format(fecha.toDate())
                        p0.addMarker(
                            MarkerOptions()
                                .position(LatLng(latitud, longitud))
                                .title("Prueba $date")
                        )
                        Log.e("TAG", document.id + " => " + document.data["latitud"])
                    }

                    p0.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                latitud,
                                longitud
                            ), 10f
                        )
                    )
                } else {
                    Log.e("TAG", "Error getting documents.", it.getException())
                }
            }
    }
}