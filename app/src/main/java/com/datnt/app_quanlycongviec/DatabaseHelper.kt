package com.datnt.app_quanlycongviec

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "tasksDB"
        const val DATABASE_VERSION = 2
        const val TABLE_NAME = "taskTBL"
        const val ID = "id"
        const val TASK_NAME = "task_name"
        const val TASK_DETAILS = "task_details"
        const val TASK_TIME = "task_time"
    }

    //Tảo bảng
    override fun onCreate(db: SQLiteDatabase?) {
        val createTbl = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TASK_NAME TEXT, $TASK_DETAILS TEXT, $TASK_TIME TEXT)"
        db?.execSQL(createTbl)
    }

    //khi nâng cấp db mới sẽ xóa bảng cũ và tạo bảng mới
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}

//Lấy tất cả task
//    fun getAllTask(): List<Task>{
//        val taskList = mutableListOf<Task>()
//        val db = writableDatabase
//        val selectQuery = "SELECT * FROM $TASK_NAME"
//
//        //Thực hiện truy vấn
//        //Cursor là một đối tượng được sử dụng để duyệt và truy xuất dữ liệu từ cơ sở dữ liệu sau khi bạn đã thực hiện truy vấn
//        val cursor = db.rawQuery(selectQuery, null)
//
//        if(cursor != null){
//            //Nếu cursor khác null di chuyển cursor đến vị trí đầu tiên
//            if(cursor.moveToFirst()){
//                //sử dụng do while để lấy dữ liệu và kiểm tra dữ liệu có sẵn hay không
//                do {
//                    val id: Int = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
//                    val name: String = cursor.getString(cursor.getColumnIndex(TASK_NAME))
//                    val details: String = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
//                    val time: String = cursor.getString(cursor.getColumnIndex(TASK_TIME))
//                    val task = Task(id = id, name = name, details = details, time = time)
//                    taskList.add(task)
//                }while (cursor.moveToNext())
//            }
//        }
//        cursor.close()
//        return taskList
//    }
//
//    //dữ liệu có được thêm vào hay không sẽ được trả về kiểu boolean
//    fun addTask(task: Task): Boolean {
//        //tạo đối tượng csdl
//        val db = this.writableDatabase
//        val value = ContentValues()
//        value.put(TASK_NAME, task.name)
//        value.put(TASK_DETAILS, task.details)
//        value.put(TASK_TIME, task.time)
//        val success = db.insert(TABLE_NAME, null, value)
//        db.close()
//        //ktr dữ liệu có đc chèn vào hay không
//        return Integer.parseInt("$success") != -1
//    }
//
//    fun deleteTask(_id: Int): Boolean{
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(ID, _id)
//        val _success = db.delete(TABLE_NAME, "id=$_id", null)
//        db.close()
//        return Integer.parseInt("$_success") != -1
//    }
