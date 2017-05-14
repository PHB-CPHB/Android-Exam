package exam.app.database

import exam.app.Entity.User
import exam.app.Entity.UserTabel
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import exam.app.App
import org.jetbrains.anko.db.*

class DBController(context: Context = App.instance) : ManagedSQLiteOpenHelper(context, DBController.DB_NAME, null, DBController.DB_VERSION) {


    companion object {
        //Database Name
        val DB_NAME : String = "users"
        //Database version
        val DB_VERSION : Int = 2
        val instance by lazy { DBController() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
                UserTabel.name,
                true,
                UserTabel.id to INTEGER + PRIMARY_KEY,
                UserTabel.username to TEXT,
                UserTabel.password to TEXT
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
                UserTabel.password to "123"
        )
    }

    fun getUser(currentuser : String, userPassword : String) : Boolean {

       try {
           var user: User? = null
           instance.use {
               user = select("User")
                       .where(
                               "(username = {currentUser}) and (password = {userPassword}) ",
                               "currentUser" to currentuser,
                               "userPassword" to userPassword)
                       .parseOpt(
                               rowParser {
                                   id: Int, username: String, password: String ->
                                   User(id, username, password)
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
