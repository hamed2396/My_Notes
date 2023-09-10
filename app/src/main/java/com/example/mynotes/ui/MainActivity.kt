package com.example.mynotes.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.databinding.FilerDialogBinding
import com.example.mynotes.utils.UserDataStore
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private val navHost by lazy { supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment }
    private var state: Boolean = false
    private lateinit var job: Job
    val searchViewSubject = PublishSubject.create<String>()
    val filterNoteSubject = PublishSubject.create<Int>()
    private var selectedItem = -1



    @Inject
    lateinit var dataStore: UserDataStore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)

        binding?.apply {

            lifecycleScope.launch {
                dataStore.readFromDs().collect {

                    state = it
                    //avoid user to see layoutManager icon change
                    mainView.visibility = View.INVISIBLE
                    delay(50)
                    mainView.visibility = View.VISIBLE
                    //
                }
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {


        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu.findItem(R.id.menu_search)
        val layout = menu.findItem(R.id.menu_layoutManager)
        if (state) {
            layout.setIcon(R.drawable.grid)
        } else {
            layout.setIcon(R.drawable.linear)
        }

        val searchView = search.actionView as SearchView
        searchView.queryHint = "search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchViewSubject.onNext(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        job = CoroutineScope(Dispatchers.Main).launch {
            when (item.itemId) {

                R.id.menu_layoutManager -> {


                    dataStore.readFromDs().collect {
                        state = if (it) {
                            item.setIcon(R.drawable.linear)
                            false
                        } else {
                            item.setIcon(R.drawable.grid)

                            true
                        }
                        dataStore.writeOnDs(state)
                        job.cancel()

                    }


                }

                R.id.menu_filter -> {
                    showFilterDialog()
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun showFilterDialog() {

        val binding = FilerDialogBinding.inflate(layoutInflater)
        val radioGroup = binding.radioGroup
        MaterialAlertDialogBuilder(this).also {
            if (selectedItem != -1) {

                radioGroup.check(selectedItem)
            }

            it.create()
            it.setView(binding.root)
            val dialog = it.show()
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                selectedItem = checkedId
                when (checkedId) {
                    R.id.radioAllNote -> {
                        filterNoteSubject.onNext(0)
                    }

                    R.id.radioTextAlphabetically -> {
                        filterNoteSubject.onNext(1)
                    }

                    R.id.radioDateDesc -> {
                        filterNoteSubject.onNext(2)
                    }
                }
                dialog.dismiss()


            }

        }


    }


}