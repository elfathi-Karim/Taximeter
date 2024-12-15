package com.example.taximeter

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TravelActivity : AppCompatActivity() {

    private lateinit var recyclerViewProducts: RecyclerView
    lateinit var productsAdapter: TravelsAdapter
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    lateinit var imageSearch: ImageView
    lateinit var editSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_travel)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerViewTravels)
        toolBar = findViewById(R.id.toolbar)
        imageSearch = toolBar.findViewById(R.id.imageSearch)
        editSearch = toolBar.findViewById(R.id.editSearch)
        setSupportActionBar(toolBar)



        val items = listOf(
            Travel("Essaouira", 4.5f, "365.99 DH" , R.drawable.logo2),
            Travel("Marrakech", 4f, "450.88 DH", R.drawable.logo2),
            Travel("Safi", 5f, "400.99 DH", R.drawable.logo2),
            Travel("Agadir", 4F, "600.06 DH", R.drawable.logo2),
            Travel("Casablanca", 5f, "330.89 DH", R.drawable.logo2),
            Travel("Tanger", 4.5f, "700.99 DH", R.drawable.logo2),
        )

        productsAdapter = TravelsAdapter(items)

        recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(this@TravelActivity)
            adapter = productsAdapter
        }

        imageSearch.setOnClickListener {
            val search = editSearch.text.toString()
            productsAdapter.filter.filter(search)
        }
    }
}