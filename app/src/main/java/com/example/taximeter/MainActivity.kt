package com.example.taximeter

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.IOException
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,languages)
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(arrayAdapter)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> true
                R.id.location -> {
                    val intent = Intent(this, LocationActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.travel -> {
                    val intent = Intent(this, TravelActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }




        // Référence au bouton
        val btnShowQR: Button = findViewById(R.id.btn_show_qr)

        // Action au clic sur le bouton
        btnShowQR.setOnClickListener {
            showCustomQRCodeDialog("First Name: Karim\nLast Name: EL FATHI\nAge: 35\n" +
                    "Driving Licence: Category B\n" +
                    "City: Essaouira\nTelephone: 0612111314")
        }
    }
    // Fonction pour afficher un AlertDialog personnalisé avec QR Code
    private fun showCustomQRCodeDialog(driverInfo: String) {
        // Générer le QR Code
        val qrCodeBitmap = generateQRCode(driverInfo)
        if (qrCodeBitmap == null) {
            Toast.makeText(this, "Erreur lors de la génération du QR Code", Toast.LENGTH_LONG).show()
            return
        }

        // Charger la mise en page personnalisée
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        // Mettre à jour l'ImageView avec le QR Code
        val qrCodeImageView = dialogView.findViewById<ImageView>(R.id.dialogQRCode)
        qrCodeImageView.setImageBitmap(qrCodeBitmap)

        // Créer et afficher le dialog
        val dialog = AlertDialog.Builder(this, R.style.YellowAlertDialog)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                saveBitmapToGallery(qrCodeBitmap, "QRCode_Infos_Chauffeur")
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        // Appliquer le style des boutons après la création
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                setTextColor(resources.getColor(android.R.color.black))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.apply {
                setTextColor(resources.getColor(android.R.color.black))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }
        }

        dialog.show()
    }

    // Générer un QR Code à partir des informations
    private fun generateQRCode(data: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    // Sauvegarder le QR Code dans la galerie
    private fun saveBitmapToGallery(bitmap: Bitmap, fileName: String): String? {
        val resolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (imageUri == null) {
            Toast.makeText(this, "Erreur : Impossible de sauvegarder l'image.", Toast.LENGTH_SHORT).show()
            return null
        }

        return try {
            resolver.openOutputStream(imageUri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            Toast.makeText(this, "Image sauvegardée dans la galerie.", Toast.LENGTH_SHORT).show()
            imageUri.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Erreur lors de la sauvegarde de l'image.", Toast.LENGTH_SHORT).show()
            null
        }
    }
}