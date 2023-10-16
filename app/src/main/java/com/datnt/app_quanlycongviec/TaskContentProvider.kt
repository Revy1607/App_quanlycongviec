package com.datnt.app_quanlycongviec

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri

class TaskContentProvider : ContentProvider() {
    private lateinit var databaseHelper: DatabaseHelper
    companion object {
        private const val AUTHORITY = "com.datnt.app_quanlycongviec.TaskContentProvider"
        private const val TASK_PATH = "taskTBL"
        private const val TASK = 1
        private const val TASK_ID = 2
        val contentUri: Uri = Uri.parse("content://$AUTHORITY/$TASK_PATH")

    }
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TASK_PATH, TASK)
        addURI(AUTHORITY, "$TASK_PATH/#", TASK_ID)
    }


    override fun onCreate(): Boolean {

        databaseHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val db = databaseHelper.readableDatabase
        val cursor: Cursor?

        return when(uriMatcher.match(p0)){
            TASK ->{
                cursor = db?.query(
                    DatabaseHelper.TABLE_NAME, p1, p2, p3, null, null, p4
                )
                cursor?.setNotificationUri(context?.contentResolver, p0)
                return cursor
            }
            else -> null
        }
    }

    override fun getType(p0: Uri): String? {
        return when(uriMatcher.match(p0)){
            TASK -> "vnd.android.cursor.dir/$AUTHORITY.$TASK_PATH"
            else -> null
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val db = databaseHelper.writableDatabase

        return when(uriMatcher.match(p0)){
            TASK ->{
                val id = db.insert(DatabaseHelper.TABLE_NAME, null, p1)
                if(id > 0){
                    val uris = ContentUris.withAppendedId(TaskContentProvider.contentUri, id)
                    context?.contentResolver?.notifyChange(p0, null)
                    return uris
                } else {
                    throw SQLException("Failed to insert row into $p0")
                }
            }
            else -> null
        }
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val db = databaseHelper.writableDatabase

        return when(uriMatcher.match(p0)){
            TASK ->{
                val rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME, p1, p2)
                if(rowsDeleted > 0){
                    context?.contentResolver?.notifyChange(p0, null)
                }
                rowsDeleted
            }
            else -> throw IllegalArgumentException("Unknown URI: $p0")
        }
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val db = databaseHelper.writableDatabase

        return when(uriMatcher.match(p0)){
            TASK ->{
                val rowsUpdate = db.update(DatabaseHelper.TABLE_NAME, p1, p2, p3)
                if(rowsUpdate > 0){
                    context?.contentResolver?.notifyChange(p0, null)
                }
                rowsUpdate
            }
            else -> throw  IllegalArgumentException("Unknown URI: $p0")
        }
    }

}