package com.example.taximeter

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomnavigation.BottomNavigationView

class LocationActivity : AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocationMarker: Marker? = null

    private lateinit var playPauseButton: Button
    private var isPlaying = false

    // Fare calculation variables
    private val baseFare: Double = 2.5
    private val farePerKm: Double = 1.5
    private val farePerMinute: Double = 0.5

    private var totalFare: Double = 0.0
    private var startLocation: Location? = null
    private var previousLocation: Location? = null
    private var elapsedTimeInMinutes: Double = 0.0
    private var startTime: Long = 0
    private var distanceTraveled: Double = 0.0
    private var handler = Handler()
    private var runnable: Runnable? = null

    private lateinit var distanceTimeInfoTextView: TextView

    private val NOTIFICATION_CHANNEL_ID = "ride_channel"
    private val NOTIFICATION_ID = 1

    private lateinit var totalFareTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.location

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.location -> true
                R.id.travel -> {
                    val intent = Intent(this, TravelActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }


    }
}