package database

import Entity.User
import Entity.UserTabel
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.media.session.MediaSession
import app.App
import org.jetbrains.anko.db.*

class DBController(context: Context = App.instance) : ManagedSQLiteOpenHelper(context, DBController.DB_NAME, null, DBController.DB_VERSION) {


    companion object {
        //Database Name
        val DB_NAME : String = "users"
        //Database version
        val DB_VERSION : Int = 3
        val instance by lazy { DBController() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
                UserTabel.name,
                true,
                UserTabel.id to INTEGER + PRIMARY_KEY,
                UserTabel.username to TEXT,
                UserTabel.email to TEXT,
                UserTabel.phonenumber to TEXT,
                UserTabel.token to TEXT
        )
        testData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(UserTabel.name, true)
        onCreate(db)
        }

    fun testData(db: SQLiteDatabase){

        db.insert(
                UserTabel.name,
                UserTabel.id to 1,
                UserTabel.username to "test",
                UserTabel.email to "test@test.dk",
                UserTabel.phonenumber to "22334455",
                UserTabel.token to "123abc"
        )
    }

    fun getUser(currentUsername : String, userEmail : String, userPhonenumber : String, userToken : String) : Boolean {

       try {
           var user: User? = null
           instance.use {
               user = select("User")
                       .where(
                               "(username = {currentUser}) and (email = {userE}) and " +
                               "(phonenumber = {userPhone}) and (token = {userToken})",
                               "currentUser" to currentUsername,
                               "userE" to userEmail,
                               "userPhone" to userPhonenumber,
                               "userToken" to userToken)
                       .parseOpt(
                               rowParser {
                                   id: Int, username: String, email: String, phonenumber: String, token: String ->
                                   User(id, username, email, phonenumber, token)
                               }
                       )
           }
           println(user)
           return user != null
       } catch (ex : SQLiteException) {
           println(ex)
           return false;
       }
}

}
