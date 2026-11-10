package com.example.coffee_work.Activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_work.Adapter.ItemsListCategoryAdapter
import com.example.coffee_work.R
import com.example.coffee_work.ViewModel.MainViewModel

class ItemsListActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items_list)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val listView = findViewById<RecyclerView>(R.id.listView)
        val categoryTxt = findViewById<TextView>(R.id.categoryTxt)
        val backBtn = findViewById<View>(R.id.backBtn)

        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""
        categoryTxt.text = title

        progressBar.visibility = View.VISIBLE
        viewModel.loadItems(id).observe(this, Observer {
            listView.layoutManager = LinearLayoutManager(
                this@ItemsListActivity,

                LinearLayoutManager.VERTICAL,
                false
            )
            listView.adapter = ItemsListCategoryAdapter(it)
            progressBar.visibility = View.GONE
        })

        backBtn.setOnClickListener { finish() }
    }
}