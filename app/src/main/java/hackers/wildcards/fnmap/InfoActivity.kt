package hackers.wildcards.fnmap

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        /*val textView = findViewById<TextView>(R.id.textView1).apply {
            text = if (info_desired == null) "" else info_desired.info
        }*/
        Log.println(Log.INFO, "", "Text view is " + info_desired?.info)
        findViewById<TextView>(R.id.textView1).text = info_desired?.info

        Log.println(Log.INFO, "", "Now displaying " + findViewById<TextView>(R.id.textView1).text)
        //textView.setText(info_desired?.info)

        val myImg = findViewById<ImageView>(R.id.imageView)
        myImg.setImageDrawable(LoadImageFromWebOperations(info_desired?.imgUrl))
    }

    private fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "image")
        } catch (e: Exception) {
            null
        }
    }

    /*
    private fun LoadText(what: String): String {

        if (what.contains("Reconciliation Pole", true)) {
            return reconciliationPole
        } else if (what.contains("Residential school History and Dialogue Centre", true)) {
            return residentialSchoolHistory
        } else if (what.contains("Bill Reid Gallery", true)) {
            return billReidGallery
        } else if (what.contains("Stanley", true)) {
            return stanleyPark
        } else {
            return "Error, please go back and try again"
        }
    }



    val reconciliationPole: String = "In April 2017, UBC Vancouver installed a symbolic art piece: Reconciliation Pole. It represents the history of Indigenous people in Canada before, during, and after the Indian residential school era. The Reconciliation Pole, installed on the southern end of campus near the Forestry building, encourages everyone who comes across it to learn more about the history of Indian residential schools and to understand their role in reconciliation between Indigenous and non-Indigenous Canadians.\n" +
            "\n" +
            "Canada’s Indian residential schools were institutions run by churches and funded by the federal government. They operated for more than 100 years across the country, with the last one closing in 1996 (the last school in B.C. closed in 1984). Over this time, more than 150,000 Indigenous children from numerous cultural groups were taken from their homes, families, and communities in order to “kill the Indian in the child”.\n" +
            "\n" +
            "In 2008, the Government of Canada apologized on behalf of Canadians to survivors of these institutions for the abuses they suffered and the subsequent negative consequences that followed many of them, as well as for the Indian residential school system itself. Also that year, the Truth and Reconciliation Commission (TRC) of Canada was launched, which completed its work in 2015 with the issuance of its Final Report and 94 Calls to Action.\n" +
            "\n" +
            "A compelling feature of the pole depicts a residential schoolhouse embedded with thousands of copper nails that were hammered in by residential school survivors and their families, as well as school children and members of the public. The nails commemorate the many children who died while attending the schools.\n" +
            "The pole, which was commissioned by well-known Canadian philanthropist Michael Audain, was carved by renowned Haida master carver, 7idansuu (Edenshaw) James Hart, and a number of assistant carvers and painters over 2 years.\n" +
            "\n"

    val residentialSchoolHistory: String = "Between 1883 and 1996, the Government of Canada and church organizations operated the Indian Residential School System. An estimated 150,000 First Nations, Métis, and Inuit children were removed from their families, homes, languages, and lands. These schools were part of Canada’s official policy, which aimed to eliminate Indigenous cultures and, through assimilation, cause Indigenous peoples to cease to exist.\n" +
            "\n" +
            "When the Truth and Reconciliation Commission (TRC) report was published in June 2015, it documented a devastating history of abuse and neglect of Indigenous children. Non-Indigenous Canadians were confronted with a stark reality of the nation’s history as experienced by Indigenous peoples. It provided not only a sobering look at Canada’s history, but a meaningful context for the reality many Indigenous people face today, and for the complicated relationships between Indigenous peoples and non-Indigenous Canadians.\n" +
            "\n" +
            "Even after the work of the TRC, too many Canadians remain unaware of this history or its lasting effects. With no widely shared understanding of the circumstances that have shaped Indigenous experience in Canada or the actions taken by Canadian institutions, we are unable to understand each other or begin to talk from a common understanding. Yet the issues we must navigate are critical to our common future. To respond to the need for more informed understandings, UBC established the Indian Residential School History and Dialogue Centre (IRSHDC).\n"

    val billReidGallery: String = "A hive of Indigenous creativity in the heart of downtown Vancouver, the Bill Reid Gallery of Northwest Coast Art boasts a rich collection of art from the acclaimed Haida artist, Bill Reid. A master goldsmith, carver, sculptor and writer of Haida descent, Reid’s extraordinary creations helped to bring Aboriginal art and culture to an international audience.\n" +
            "This gallery continues that tradition not only by collecting Reid’s finest works, but also by showcasing emerging artistic talent in Indigenous communities. Its current exhibition, Intangible, features everything from hand-weaving to hip-hop performance art, reflecting the diversity of contemporary Coast Salish art.\n" +
            "Beth Carter, curator at the gallery, says that the artists featured at Intangible have a wide range of influences, including their respective lineages, or sources within and outside their immediate community. This results in a wealth of new and exciting ideas about art and Indigenous culture, she adds.\n"

    val stanleyPark: String = "Prior to its use as a public park, Stanley Park was the traditional territory of Coast Salish First Nations, including the Musqueam, Squamish and Tsleil Waututh. Indigenous habitation of the Stanley Park peninsula is ancient. Archaeologists have found artifacts in the park that are more than 3,200 years old.\n" +
            "The peninsula was the site of one of the largest Indigenous settlements in the Lower Mainland, known as Whoi Whoi (X̱wáýx̱way), home to hundreds of people near the present-day location of Lumberman’s Arch. For many generations, they drew from the forest and marine resources of the surrounding environment to create homes and sustain families. In 1887, city employees destroyed the remaining structures of Whoi Whoi and evicted the residents to build the first Park Road.\n" +
            "A small number of Indigenous and settler residents continued to live in Stanley Park into the 20th century. The Park Board eventually won a series of legal cases against these park residents in the 1920s and began evictions in the 1930s. The board permitted Tim and Agnes Cummings, two park residents, to remain in their home in Stanley Park until their deaths in the 1950s.\n"
*/
}
