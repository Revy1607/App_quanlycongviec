package com.datnt.app_quanlycongviec

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.number.IntegerWidth

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "tasksDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "taskTBL"
        private const val ID = "id"
        private const val TASK_NAME = "task_name"
        private const val TASK_DETAILS = "task_details"
        private const val TASK_TIME = "task_time"
    }

    //Tảo bảng
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT, $TASK_TIME TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    //khi nâng cấp db mới sẽ xóa bảng cũ và tạo bảng mới
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    //Lấy tất cả task
    fun getAllTask(): List<Task>{
        val taskList = mutableListOf<Task>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TASK_NAME"

        //Thực hiện truy vấn
        //Cursor là một đối tượng được sử dụng để duyệt và truy xuất dữ liệu từ cơ sở dữ liệu sau khi bạn đã thực hiện truy vấn
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor != null){
            //Nếu cursor khác null di chuyển cursor đến vị trí đầu tiên
            if(cursor.moveToFirst()){
                //sử dụng do while để lấy dữ liệu và kiểm tra dữ liệu có sẵn hay không
                do {
                    val id: Int = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    val name: String = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                    val details: String = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
                    val time: String = cursor.getString(cursor.getColumnIndex(TASK_TIME))
                    val task = Task(id = id, name = name, details = details, time = time)
                    taskList.add(task)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    //dữ liệu có được thêm vào hay không sẽ được trả về kiểu boolean
    fun addTask(task: Task): Boolean {
        //tạo đối tượng csdl
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(TASK_NAME, task.name)
        value.put(TASK_DETAILS, task.details)
        value.put(TASK_TIME, task.time)
        val success = db.insert(TABLE_NAME, null, value)
        db.close()
        //ktr dữ liệu có đc chèn vào hay không
        return Integer.parseInt("$success") != -1
    }

    fun deleteTask(_id: Int): Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, _id)
        val _success = db.delete(TABLE_NAME, "id=$_id", null)
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}