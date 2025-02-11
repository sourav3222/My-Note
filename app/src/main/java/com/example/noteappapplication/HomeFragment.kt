package com.example.noteappapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.noteappapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), NoteAdapter.NoteEdit, NoteAdapter.NoteDelete {
    lateinit var binding: FragmentHomeBinding
    lateinit var note: Note


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)

        var notes: List<Note> = NoteDatabase.getDB(requireContext()).getNoteDao().getAllData()

        notes.let {
            var adapter = NoteAdapter(this,this)
            adapter.submitList(notes)

            binding.recyclerView.adapter = adapter

        }



        binding.addBtn.setOnClickListener {



            findNavController().navigate(R.id.action_homeFragment_to_addFragment)

        }



        return binding.root
    }

    override fun onNoteEdit(note: Note) {

        var bundle = Bundle()
        bundle.putInt(AddFragment.Note_ID,note.id)

        findNavController().navigate(R.id.action_homeFragment_to_addFragment, bundle)

    }

    override fun onNoteDelete(note: Note) {

        val noteDao = NoteDatabase.getDB(requireContext()).getNoteDao()

        Thread {
            noteDao.deleteData(note)
            requireActivity().runOnUiThread {
                val updatedNotes = noteDao.getAllData()
                (binding.recyclerView.adapter as NoteAdapter).submitList(updatedNotes)
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }.start()


    }
}


