package com.example.mlseriescamera.helpers

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager

import com.example.mlseriescamera.R
import com.example.mlseriescamera.databinding.ActivityImageHelperBinding

class ImageHelperActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivityImageHelperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityImageHelperBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_image_helper)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkAndRequestPermissions()

        println("debug")
    }

    private fun checkAndRequestPermissions() {
        val permissionStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@ImageHelperActivity,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            ContextCompat.checkSelfPermission(
                this@ImageHelperActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }


        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this@ImageHelperActivity,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST_CODE
                )
            }
            else{
                ActivityCompat.requestPermissions(
                    this@ImageHelperActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }


        } else {
            // izin verilmis, resimleri oku
            loadImages()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // izin verildi, resimleri okuma islemini baslatabilirsin
                    loadImages()
                } else {
                    // izin reddedildi,
                    // kullanıcıya izin verimezse yapacağınız işlemleri buraya ekle
                }
                return
            }
        }
    }


    private fun loadImages() {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        val imageUris = mutableListOf<Uri>()

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri: Uri = Uri.withAppendedPath(uri, id.toString())

                // Burada contentUri kullanarak resim dosyasını okuyabilir veya görüntüleyebilirsiniz
                println("Resim adi: $name, URI: $contentUri")
                imageUris.add(contentUri)
            }
        }

        binding.imageRecyclerView.layoutManager = GridLayoutManager(this@ImageHelperActivity,3)
        binding.imageRecyclerView.adapter = ImageAdapter(imageUris)
    }
}