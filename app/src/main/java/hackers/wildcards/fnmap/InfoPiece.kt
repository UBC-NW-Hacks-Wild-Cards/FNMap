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
    file.printWriter().use{ out ->
        out.println("Reconciliation Pole~https://students.ubc.ca/sites/students.ubc.ca/files/styles/large_image_desktop_1_5x/public/UBC_20170327_1697.jpg~In April 2017, UBC Vancouver installed a symbolic art piece: Reconciliation Pole. It represents the history of Indigenous people in Canada before, during, and after the Indian residential school era. The Reconciliation Pole, installed on the southern end of campus near the Forestry building, encourages everyone who comes across it to learn more about the history of Indian residential schools and to understand their role in reconciliation between Indigenous and non-Indigenous Canadians.~49.260116~-123.248995~500")
        out.println("Residential School History and Dialogue Centre~https://cdn.visit.ubc.ca/wp-content/uploads/2019/07/irshdc_1-1940x1216_2x.jpg~Between 1883 and 1996, the Government of Canada and church organizations operated the Indian Residential School System. An estimated 150,000 First Nations, Métis, and Inuit children were removed from their families, homes, languages, and lands. These schools were part of Canada’s official policy, which aimed to eliminate Indigenous cultures and, through assimilation, cause Indigenous peoples to cease to exist.~49.267017~-123.253645~500")
        out.println("Bill Reid Gallery of Northwest Coast Art~https://thegreattrail.ca/wp-content/uploads/2017/10/Gallery_Exterior_wcredit-e1508958549916.jpg~A hive of Indigenous creativity in the heart of downtown Vancouver, the Bill Reid Gallery of Northwest Coast Art boasts a rich collection of art from the acclaimed Haida artist, Bill Reid. A master goldsmith, carver, sculptor and writer of Haida descent, Reid’s extraordinary creations helped to bring Aboriginal art and culture to an international audience.~49.284584~-123.119431~500")
        out.println("Stanley Park~https://www.aboriginalecotours.com/images/products/aboriginal-eco-tours/vancouver/nature-walks-hikes-walking-tours/Spoken-Treasures---A-History-of-Vancouver-and-Stanley-Park-through-Indigenous-eyes-1518488423.jpg~Prior to its use as a public park, Stanley Park was the traditional territory of Coast Salish First Nations, including the Musqueam, Squamish and Tsleil Waututh. Indigenous habitation of the Stanley Park peninsula is ancient. Archaeologists have found artifacts in the park that are more than 3,200 years old.~49.295557~-123.149486~500
    }
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

        val d_radians = calcGeogDistance(lat1, lon1, lat2, lon2)
        val radius_earth_km = 6371
        if (d_radians*radius_earth_km <= radius){
            results.add(InfoPiece(parts[0], parts[1], parts[2], lat2, lon2, parts[5].toInt()))
        }
    }
    return results
}