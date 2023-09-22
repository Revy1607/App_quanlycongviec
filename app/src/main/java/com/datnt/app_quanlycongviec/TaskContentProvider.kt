package com.datnt.app_quanlycongviec

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class TaskContentProvider: ContentProvider() {
    companion object {
        const val AUTHORITY = "com.datnt.TaskContentProvider"
        const val TASK_TABLE = "taskTBL"
        // Định nghĩa mã (code) cho URI của bạn
        const val TASK = 1
        val contentUri = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        contentUri.addURI(AUTHORITY, TASK_TABLE, TASK)
    }

    private var dbHelper: DatabaseHelper? = null
    override fun onCreate(): Boolean {
        // Thêm URI vào UriMatcher
        dbHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val db = dbHelper?.writableDatabase
        var cursor: Cursor? = null
        cursor = when(contentUri.match(p0)){
            TASK -> db?.query(TASK_TABLE, p1, p2, p3, null, null, p4)
            else -> null
        }
        return cursor
    }

    override fun getType(p0: Uri): String? {
        return when(contentUri.match(p0)){
            TASK -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$TASK_TABLE"
            else -> null
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val db = dbHelper?.writableDatabase
        val id = db?.insert(TASK_TABLE, null, p1)
        context?.contentResolver?.notifyChange(p0, null)
        return Uri.withAppendedPath(p0, id.toString())
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

}