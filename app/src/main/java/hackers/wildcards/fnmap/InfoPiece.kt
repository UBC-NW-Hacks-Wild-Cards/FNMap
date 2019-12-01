package hackers.wildcards.fnmap

import android.content.Context
import java.io.File
import java.lang.Double.MAX_VALUE
import java.lang.Math.*
import kotlin.math.pow

class InfoPiece(var header: String,
                var imgUrl: String,
                var info: String,
                var lat: Double,
                var long: Double,
                var lastUpdated: Int) {
}

fun populateData(appContext: Context) {
    val file = File(appContext.getFilesDir(), "data.csv")
    file.writeText("Totem Pole,https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/82296943-SLD-001-050.jpg/220px-82296943-SLD-001-050.jpg,Cool totem poles are cool,50,150,500")
    file.writeText("Totem Pole,https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/82296943-SLD-001-050.jpg/220px-82296943-SLD-001-050.jpg,Cool totem poles are cool,51,151,500")
    file.writeText("Totem Pole,https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/82296943-SLD-001-050.jpg/220px-82296943-SLD-001-050.jpg,Cool totem poles are cool,52,152,500")
    file.writeText("Totem Pole,https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/82296943-SLD-001-050.jpg/220px-82296943-SLD-001-050.jpg,Cool totem poles are cool,53,153,500")
    file.writeText("Totem Pole,https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/82296943-SLD-001-050.jpg/220px-82296943-SLD-001-050.jpg,Cool totem poles are cool,54,154,500")
}

fun calcGeogDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    return 2*asin(sqrt((sin((lat1-lat2)/2)).pow(2) + cos(lat1)*cos(lat2)*(sin((lon1-lon2)/2)).pow(2)))
}

fun getNearestInfoPiece(appContext: Context, lat1: Double, lon1: Double): InfoPiece? {
    val file = File(appContext.getFilesDir(), "data.csv")

    var nearestInfoPiece: InfoPiece? = null
    var nearestDistance = MAX_VALUE

    file.forEachLine {
        val parts = it.split(",")
        val lat2 = parts[3].toDouble()
        val lon2 = parts[4].toDouble()

        // https://stackoverflow.com/questions/5031268/algorithm-to-find-all-latitude-longitude-locations-within-a-certain-distance-fro
        val d = calcGeogDistance(lat1, lon1, lat2, lon2)
        if (d < nearestDistance) {
            nearestDistance = calcGeogDistance(lat1, lon1, lat2, lon2)
            nearestInfoPiece = InfoPiece(parts[0], parts[1], parts[2], lat2, lon2, parts[5].toInt())
        }
    }
    return nearestInfoPiece
}


fun getInfoPiecesWithinRadius(appContext: Context, lat1: Double, lon1: Double, radius: Double): ArrayList<InfoPiece?> {
    val results = ArrayList<InfoPiece?>()
    val file = File(appContext.getFilesDir(), "data.csv")
    file.forEachLine {
        val parts = it.split(",")
        val lat2 = parts[3].toDouble()
        val lon2 = parts[4].toDouble()

        val d = calcGeogDistance(lat1, lon1, lat2, lon2)
        if (d <= radius){
            results.add(InfoPiece(parts[0], parts[1], parts[2], lat2, lon2, parts[5].toInt()))
        }
    }
    return results
}