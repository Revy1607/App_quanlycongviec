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


class AddFragment : Fragment() {
    private var datePickerTask: DatePicker? = null
    private var editTextTask: EditText? = null
    private var buttonSave: Button? = null
    private var databaseHelper: DatabaseHelper? = null
    private var taskContentProvider: TaskContentProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        datePickerTask = view?.findViewById(R.id.datePickerTask)
        editTextTask = view?.findViewById(R.id.editTextTask)
        buttonSave = view?.findViewById(R.id.buttonSave)

        databaseHelper = context?.let { DatabaseHelper(it) }
        buttonSave?.setOnClickListener {
            saveTask()
        }


        return view
    }

    private fun saveTask() {
        val taskText = editTextTask?.text.toString()
        val year = datePickerTask?.year
        val month = datePickerTask?.month
        val day = datePickerTask?.dayOfMonth

        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.TASK_NAME, taskText)
        contentValues.put(DatabaseHelper.TASK_TIME, "$year-$month-$day")


    }

}