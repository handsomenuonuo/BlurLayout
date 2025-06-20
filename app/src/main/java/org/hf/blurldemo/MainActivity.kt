package org.hf.blurldemo

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.hf.blurlayout.view.BlurLayout
import org.hf.blurldemo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<FrameLayout>(R.id.main)
        val blurView = findViewById<BlurLayout>(R.id.blurLayout)

        blurView.setupWith(root)
    }
}