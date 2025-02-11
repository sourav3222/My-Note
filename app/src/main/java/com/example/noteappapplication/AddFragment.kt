package com.example.noteappapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.noteappapplication.databinding.FragmentAddBinding
import java.util.Calendar


class AddFragment : Fragment() {
    lateinit var binding: FragmentAddBinding
    lateinit var note: Note
    var showTime: String? = null
    var showDate: String? = null
    var noteId = 0

    companion object {

        var Note_ID = "note_Id"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        binding = FragmentAddBinding.inflate(inflater,container,false)



        if (noteId != 0) {

            note = NoteDatabase.getDB(requireContext()).getNoteDao()
                .loadAllByIds(listOf<Int>(noteId))[0]

            binding.apply {

                titleET.setText(note.title)
                timeBtn.setText(note.time)
                dateBtn.setText(note.date)



            }

        }




        binding.dateBtn.setOnClickListener{
            PickADate()
        }



        binding.timeBtn.setOnClickListener{
            pickATime()
        }


        binding.submitBtn.setOnClickListener {
            val titleStr = binding.titleET.text.toString()
            val timeStr = showTime ?: "00:00"
            val dateStr = showDate ?: "00/00/0000"

            val note = Note(title = titleStr,  time = timeStr, date = dateStr)

            if (noteId == 0){
                NoteDatabase.getDB(requireContext()).getNoteDao().insertData(note)
            }else{
                note.id = noteId
                NoteDatabase.getDB(requireContext()).getNoteDao().updateData(note)
            }



            findNavController().navigate(R.id.action_addFragment_to_homeFragment)

        }







        return binding.root
    }

    private fun pickATime() {
        val calendar = java.util.Calendar.getInstance()

        val minute = calendar.get(java.util.Calendar.MINUTE)
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)

        val timePicker = TimePickerDialog(
            requireActivity(), TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                showTime = "$hourOfDay : $minute"
                binding.timeBtn.text = showTime


            }, hour, minute, false

        )
        timePicker.show()

    }

    private fun PickADate() {
        val calender = Calendar.getInstance()

        val day = calender.get(Calendar.DAY_OF_MONTH)
        val month = calender.get(Calendar.MONTH)
        val year = calender.get(Calendar.YEAR)

        val showDatePicker = DatePickerDialog(
            requireActivity(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                showDate = "$dayOfMonth/${month + 1}/$year"
                binding.dateBtn.text = showDate

            }, year, month, day

        )

        showDatePicker.show()


    }


}