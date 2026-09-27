package app.velora.track.privacy

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class HealthConnectRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = TextView(this)
        text.setPadding(48, 48, 48, 48)
        text.text = "Velora reads steps, distance, and active calories from Health Connect so it can show the activity you already recorded. It does not sell that data. Writing weight or workouts back to Health Connect is off unless you enable it later."
        setContentView(text)
    }
}
