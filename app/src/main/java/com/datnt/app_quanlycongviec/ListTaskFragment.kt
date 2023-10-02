package com.datnt.app_quanlycongviec

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.concurrent.TimeUnit


class ListTaskFragment : Fragment() {
    private var rcvTask: RecyclerView? = null
    private var btnAdd: FloatingActionButton? = null
    private var taskAdapter: TaskAdapter? = null
    private var array: ArrayList<Task> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_task, container, false)

        rcvTask = view.findViewById(R.id.rcvTask)
        btnAdd = view.findViewById(R.id.btnAdd)

        taskAdapter = TaskAdapter(array)
        rcvTask?.adapter = taskAdapter

        getData()

        btnAdd?.setOnClickListener {
            findNavController().navigate(R.id.action_listTaskFragment_to_addFragment)
        }

        taskAdapter?.onClickItemRemove = {
            array.removeAt(it)
            taskAdapter?.notifyDataSetChanged()
            Toast.makeText(requireContext(),"Xóa thành công", Toast.LENGTH_LONG).show()
        }
        return view
    }

    // Lấy dữ liệu từ Content Provider
    private fun getData(){
        // Định nghĩa URI của Content Provider và các cột bạn muốn truy vấn
        val uri = TaskContentProvider.contentUri
        val projection = arrayOf(DatabaseHelper.ID,DatabaseHelper.TASK_NAME, DatabaseHelper.TASK_DETAILS, DatabaseHelper.TASK_TIME)

        // Thực hiện truy vấn bằng ContentResolver
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)

        if(cursor != null){
            while (cursor.moveToNext()){
                val taskId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID))
                val taskName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_NAME))
                val taskDetails = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_DETAILS))
                val taskTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_TIME))

                val task = Task(taskId, taskName, taskDetails, taskTime)
                array.add(task)
            }
            taskAdapter?.notifyDataSetChanged()
        }
        cursor?.close()
    }
}