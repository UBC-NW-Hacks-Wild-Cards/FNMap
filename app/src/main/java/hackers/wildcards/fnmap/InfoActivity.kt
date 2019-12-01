package hackers.wildcards.fnmap

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.InputStream
import java.net.URL

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // Get the Intent that started this activity and extract the string
        val title = intent.getStringExtra(EXTRA_MESSAGE)

        // Capture the layout's TextView and set the string as its text

        var info_desired: InfoPiece? = null
        //Load server stuff
        var info: ArrayList<InfoPiece> = getInfoPieces(applicationContext)
        Log.println(Log.INFO, "", "Creating markers")
        for(ip: InfoPiece in info){
            Log.println(Log.INFO, "", "start mark")
            if(ip != null){
                Log.println(Log.INFO, "", "Not null")

                if (ip.header == title) {
                    info_desired = ip
                    break
                }

            }
        }

        val textView = findViewById<TextView>(R.id.textView1).apply {
            text = if (info_desired == null) "" else info_desired.info
        }

    }
}
