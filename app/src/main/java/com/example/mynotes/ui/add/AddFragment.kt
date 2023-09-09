package com.example.mynotes.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynotes.R
import com.example.mynotes.data.models.entity.NoteEntity
import com.example.mynotes.databinding.FragmentAddBinding
import com.example.mynotes.utils.applySchedulersToClicks
import com.example.mynotes.utils.showSnackBar
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CheckResult")
class AddFragment : Fragment(),
    AddContract.View, View.OnClickListener {
    @Inject
    lateinit var presenter: AddPresenter
    private val binding by lazy { FragmentAddBinding.inflate(layoutInflater) }
    private var backGroundSelected = false

    @Inject
    lateinit var entity: NoteEntity
    private var colorHex: Int = 0
    private var noteId = 0
    private val args by navArgs<AddFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideToolBar()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // hideToolBar()
            setupView()
            args.noteId.let { noteId = it }
            if (noteId > 0) {
                presenter.getSingleNote(noteId)
            }
            val color: Int = R.color.Gunmetal
            btnDone.clicks().applySchedulersToClicks().subscribe {
                entity.title = edtTitle.text.toString()
                entity.description = edtDesc.text.toString()
                entity.id = noteId
                entity.colorHex = if (colorHex > 0) colorHex else color
                presenter.saveNote(entity)
                findNavController().popBackStack()


            }
        }

    }

    private fun setupView() {

        binding.backYellow.setOnClickListener(this)
        binding.backYellow.foreground =
            ContextCompat.getDrawable(requireContext(), R.drawable.ripple)
        binding.backGreen.setOnClickListener(this)
        binding.backGreen.foreground =
            ContextCompat.getDrawable(requireContext(), R.drawable.ripple)
        binding.backRed.setOnClickListener(this)
        binding.backRed.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ripple)
        binding.backBlue.setOnClickListener(this)
        binding.backBlue.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.ripple)
    }


    override fun noteSavedMessage() {
        binding.root.showSnackBar(getString(R.string.save), anchorView = binding.btnDone)
    }


    override fun showSingleNote(entity: NoteEntity) {
        binding.apply {
            edtTitle.setText(entity.title)
            edtDesc.setText(entity.description)
            colorHex = entity.colorHex


        }
    }

    override fun onClick(v: View) {
        binding.apply {
            if (backGroundSelected) {
                binding.backYellow.alpha = 1f
                binding.backRed.alpha = 1f
                binding.backBlue.alpha = 1f
                binding.backGreen.alpha = 1f
                backGroundSelected = false
            }
            when (v.id) {
                R.id.backYellow -> {

                    colorHex = R.color.ripeMango

                    binding.backYellow.alpha = .5f
                    backGroundSelected = true

                }

                R.id.backBlue -> {
                    colorHex =
                        R.color.moonstone
                    binding.backBlue.alpha = .5f
                    backGroundSelected = true

                }


                R.id.backGreen -> {
                    colorHex =
                        R.color.caribbeanGreen
                    binding.backGreen.alpha = .5f
                    backGroundSelected = true

                }

                R.id.backRed -> {
                    colorHex =
                        R.color.awesome

                    binding.backRed.alpha = .5f
                    backGroundSelected = true

                }
            }
        }

    }

    private fun hideToolBar() {
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.hide()


    }


}