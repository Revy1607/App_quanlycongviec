package com.datnt.app_quanlycongviec

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddFragment : Fragment() {
    private lateinit var datePickerTask: DatePicker
    private var editTitleTask: EditText? = null
    private var editTextTask: EditText? = null
    private var buttonSave: Button? = null

//    private var databaseHelper: DatabaseHelper? = null
//    private var taskViewModel: TaskViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        datePickerTask = view.findViewById(R.id.datePickerTask)
        editTitleTask = view.findViewById(R.id.editTitleTask)
        editTextTask = view.findViewById(R.id.editTextTask)
        buttonSave = view.findViewById(R.id.buttonSave)


        buttonSave?.setOnClickListener {
            saveTask()
        }
        return view
    }

    private fun saveTask() {
        val taskTitle = editTitleTask?.text.toString()
        val taskText = editTextTask?.text.toString()
        val date = getDatePicker(datePickerTask)

        if (taskTitle.isNotEmpty() && taskText.isNotEmpty() && date.isNotEmpty()) {
            val values = ContentValues()
            values.put(DatabaseHelper.TASK_NAME, taskTitle)
            values.put(DatabaseHelper.TASK_DETAILS, taskText)
            values.put(DatabaseHelper.TASK_TIME, date)

            val uri = context?.contentResolver?.insert(TaskContentProvider.contentUri, values)

            if (uri != null) {
                Toast.makeText(requireContext(), "Add task success", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_addFragment_to_listTaskFragment)
            } else {
                Toast.makeText(requireContext(), "Add task fail", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Please do not leave any section blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun getDatePicker(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}