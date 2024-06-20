package com.example.recieve_data_from_other_apps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.recieve_data_from_other_apps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var singleImage: ImageView

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(binding.root)


//        val recievingText = findViewById<TextView>(R.id.rcText)



//        singleImage = findViewById<ImageView>(R.id.iv_single)



        when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                       binding.rcText.text = it
                    }
                } else if (intent.type?.startsWith("image/") == true) {
                    handleSingleImage(intent)
                }
            }

            Intent.ACTION_SEND_MULTIPLE -> {
                if (intent.type?.startsWith("image/") == true) {
                    handleMultipleImages(intent)
                }
            }
        }

    }

    private fun handleSingleImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            binding.ivSingle.setImageURI(it)
        }
    }

    private fun handleMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper)

            for (image in it) {
                val imageView = ImageView(this)
                imageView.setImageURI(image as Uri?)
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )

                binding.viewFlipper.addView(imageView)

            }

            val backBtn = findViewById<Button>(R.id.leftButton)
            binding.leftButton.setOnClickListener {
                viewFlipper.showPrevious()
            }
            val nextBtn = findViewById<Button>(R.id.rightButton)
            binding.rightButton.setOnClickListener {
                viewFlipper.showNext()
            }
        }
    }
}