package hackers.wildcards.fnmap

import android.Manifest
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var ll: Location
    private lateinit var locationCallback: LocationCallback
    private var requestingLocationUpdates = false
    var REQUESTING_LOCATION_UPDATES_KEY = "RLUK"

    var madeMarkers: ArrayList<InfoPiece> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.println(Log.INFO, "", "OnCreate")
        setContentView(R.layout.activity_maps)

        checkPerms()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        Log.println(Log.INFO, "", "Async!")

        ll = Location("LocationUpdates")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.println(Log.INFO, "", "Location updated")
                if (location != null) {
                    ll = location
                }
                // Got last known location. In some rare situations this can be null.
            }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                ll = locationResult.lastLocation
                updateLocation()
            }
        }

        updateValuesFromBundle(savedInstanceState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        // Update the value of requestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                REQUESTING_LOCATION_UPDATES_KEY)
        }
    }

    private fun checkPerms(){
        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            val backgroundLocationPermissionApproved = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

            if (backgroundLocationPermissionApproved) {
                // App can access location both in the foreground and in the background.
                // Start your service that doesn't have a foreground service type
                // defined.
            } else {
                // App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to
                // location in order to function properly. Then, request background
                // location.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    0
                )
            }
        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                1
            )
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create(),
            locationCallback,
            Looper.getMainLooper())
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener {marker: Marker ->
            onMarkerClick(marker)
            true
        }


            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            var success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json))

        Log.println(Log.INFO, "", "Map Ready!")

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(ll.latitude, ll.longitude)))
    }

    var nextNotification = 0

    private fun updateLocation(){
        var pos = LatLng(ll.latitude, ll.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.addMarker(MarkerOptions().position(pos).title("Created at " + pos.latitude))

        //Load server stuff
        var info: ArrayList<InfoPiece> = getInfoPiecesWithinRadius(applicationContext, ll.latitude, ll.longitude, 2.0)
        Log.println(Log.INFO, "", "Creating markers")
        for(ip: InfoPiece in info){
            Log.println(Log.INFO, "", "start mark")
            if(ip != null){
                Log.println(Log.INFO, "", "Not null")
                if(!madeMarkers.contains(ip)){
                    Log.println(Log.INFO, "", "Making mark")
                    mMap.addMarker(MarkerOptions().position(LatLng(ip.lat, ip.long)).title(ip.header))
                    madeMarkers.add(ip)
                }
            }
        }
        var nearby = getInfoPiecesWithinRadius(applicationContext, ll.latitude, ll.longitude, 0.25)
        if(!nearby.isNullOrEmpty()){
            sendNotification(nearby.get(0).header)
        }
    }

    fun sendNotification(name: String){
        var builder = NotificationCompat.Builder(this, "fnmapp")
            .setSmallIcon(R.drawable.musqueam)
            .setContentTitle("Landmark Nearby")
            .setContentText(name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //Log.println(Log.INFO, "", "Pre notify.")
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(nextNotification, builder.build())
            nextNotification++
        }
    }

    private fun onMarkerClick(marker: Marker) {
        Log.println(Log.INFO, "", "Recieved marker click: " + marker.title)

        val intent = Intent(this, InfoActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, marker.title)
        }
        startActivity(intent)
    }
}
