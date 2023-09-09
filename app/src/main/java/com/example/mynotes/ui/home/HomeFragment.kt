package com.example.mynotes.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.R
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.MainActivity
import com.example.mynotes.ui.home.adapters.NoteAdapter
import com.example.mynotes.utils.UserDataStore
import com.example.mynotes.utils.applySchedulers
import com.example.mynotes.utils.applySchedulersToClicks
import com.example.mynotes.utils.base.BaseFragment
import com.example.mynotes.utils.showSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CheckResult")
class HomeFragment() : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    HomeContract.View {
    private val TAG="mytag"
    var isFilterSelected=-1

    @Inject
    lateinit var noteAdapter: NoteAdapter


    @Inject
    lateinit var dataStore: UserDataStore

    @Inject
    lateinit var presenter: HomePresenter
    private lateinit var layoutManager: LayoutManager
    private var title: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
                filterNote()
            if (isFilterSelected!= -1){

                filterNoteOnBack(isFilterSelected)
            }



            (activity as MainActivity).searchViewSubject.applySchedulers().subscribe {
                title = it
                if (it.isNotEmpty()) {
                    presenter.searchNote(it)

                } else {
                    presenter.getNotes()



                }
            }



            showToolBar()


            whatLayoutManager().applySchedulers().subscribe {

                layoutManager = it
                if (isFilterSelected!= -1){
                    filterNote()



                    return@subscribe
                }
                if (isFilterSelected == -1){
                    if (title != null) {
                        presenter.searchNote(title!!)
                    }else{
                        presenter.getNotes()
                    }
                }




            }


            btnGoToAdd.clicks().applySchedulersToClicks().subscribe {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)

            }
            noteAdapter.onItemClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddFragment(
                        it.id
                    )
                )


            }
            noteAdapter.onItemLongClickListener {
                showDeleteDialog(it)
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()

    }

    override fun showNotes(notes: List<NoteEntity>) {
        binding.apply {
            
            noteRecycler.apply {
                noteAdapter.setData(notes)
                adapter = noteAdapter
                layoutManager = this@HomeFragment.layoutManager




            }
        }
    }

    override fun showEmpty(isEmpty: Boolean) {
        binding.apply {
            if (isEmpty) {
                noteRecycler.isVisible = false
                emptyNote.isVisible = true
                txtEmpty.isVisible = true
                emptyNote.setImageResource(R.drawable.add_note)
                txtEmpty.text = getString(R.string.emptyNote)

            } else {
                noteRecycler.isVisible = true
                emptyNote.isVisible = false
                txtEmpty.isVisible = false
            }
        }

    }

    override fun noteDeleteMessage() {

        binding.root.showSnackBar(getString(R.string.delete))

    }

    private fun whatLayoutManager(): Observable<LayoutManager> {

        val layoutManagerSubject = BehaviorSubject.createDefault<LayoutManager>(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        )
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.readFromDs().collect {

                layoutManager = if (it) {

                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                } else {

                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

                }


                layoutManagerSubject.onNext(layoutManager)

            }
        }

        return layoutManagerSubject.hide()


    }

    private fun showToolBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.show()

    }

    private fun showDeleteDialog(entity: NoteEntity) {
        MaterialAlertDialogBuilder(requireContext()).also {
            it.create()
            val view = layoutInflater.inflate(R.layout.alert_dialog, null)
            it.setView(view)
            it.setTitle("Delete!")


            it.setPositiveButton(
                "Delete"
            ) { dialog, _ ->
                presenter.deleteNote(entity)
                dialog.dismiss()
            }
            it.setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            it.show()
        }

    }

    override fun emptySearch(empty: Boolean) {
        binding.apply {
            if (empty) {
                noteRecycler.isVisible = false
                emptyNote.isVisible = true
                txtEmpty.isVisible = true
                emptyNote.setImageResource(R.drawable.empty)
                txtEmpty.text = getString(R.string.emptySearch)

            } else {
                noteRecycler.isVisible = true
                emptyNote.isVisible = false
                txtEmpty.isVisible = false
            }
        }
    }

    override fun showNotesFromSearch(notes: List<NoteEntity>) {
        binding.apply {
            noteRecycler.apply {
                noteAdapter.setData(notes)
                adapter = noteAdapter
                layoutManager = this@HomeFragment.layoutManager


            }
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.getNotes()

    }

    private fun filterNote(){

        (activity as MainActivity).filterNoteSubject.applySchedulers().subscribe {

            isFilterSelected=it
            when(isFilterSelected){
                0->{
                    presenter.getNotes()
                }
                1->{

                    presenter.filterAlphabetically()
                }
                2->{
                    presenter.filterByDate()
                }
            }
        }
    }
    private fun filterNoteOnBack(item:Int){
        when(item){
            0->{
                presenter.getNotes()
            }
            1->{

                presenter.filterAlphabetically()
            }
            2->{
                presenter.filterByDate()
            }
        }
    }
}



