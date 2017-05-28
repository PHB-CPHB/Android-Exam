package exam.app.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import exam.app.App
import exam.app.Entity.*
import org.jetbrains.anko.db.*

class DBController(context: Context = App.instance) : ManagedSQLiteOpenHelper(context, DBController.DB_NAME, null, DBController.DB_VERSION) {


    companion object {
        //Database Name
        val DB_NAME: String = "users"
        //Database version
        val DB_VERSION: Int = 4
        val instance by lazy { DBController() }
    }

    override fun onCreate(db: SQLiteDatabase)   {
        db.createTable(
                UserTabel.name,
                true,
                UserTabel.id to INTEGER + PRIMARY_KEY,
                UserTabel.displayName to TEXT,
                UserTabel.email to TEXT + UNIQUE,
                UserTabel.phonenumber to TEXT,
                UserTabel.password to TEXT
        )

        db.createTable(
                FriendTabel.name,
                true,
                FriendTabel.id to INTEGER + PRIMARY_KEY,
                FriendTabel.displayname to TEXT,
                FriendTabel.email to TEXT + UNIQUE,
                FriendTabel.phonenumber to INTEGER

        )

        db.createTable(
                MessageTabel.name,
                true,
                MessageTabel.id to INTEGER + PRIMARY_KEY,
                MessageTabel.toUser to TEXT,
                MessageTabel.fromUser to TEXT,
                MessageTabel.message to TEXT
        )



        testData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(UserTabel.name, true)
        db.dropTable(FriendTabel.name, true)
        db.dropTable(MessageTabel.name, true)
        onCreate(db)
    }

    /**
     * Inserts the user in the phones database
     */
    fun insertUser(user: User) {
        instance.use {
            insert(
                    UserTabel.name,
                    UserTabel.id to 1,
                    UserTabel.displayName to user.displayName,
                    UserTabel.email to user.email,
                    UserTabel.phonenumber to user.phonenumber,
                    UserTabel.password to user.password

            )
        }
    }

    fun insertFriend(friend: Friend) {
        instance.use {
            insert(
                    FriendTabel.name,
                    FriendTabel.id to 1,
                    FriendTabel.displayname to friend.displayname,
                    FriendTabel.email to friend.email,
                    FriendTabel.phonenumber to friend.phonenumber
            )
        }
    }

    fun insertMessage(message: Message) {
        instance.use {
            insert(
                    MessageTabel.name,
                    MessageTabel.id to 1,
                    MessageTabel.toUser to message.toUser,
                    MessageTabel.fromUser to message.fromUser,
                    MessageTabel.message to message.message
            )
        }
    }

    fun clearUserTable(){
        instance.use {
            delete("User")
        }
    }

    fun testData(db: SQLiteDatabase) {

        db.insert(
                UserTabel.name,
                UserTabel.id to 1,
                UserTabel.displayName to "test",
                UserTabel.email to "test@test.dk",
                UserTabel.phonenumber to "22334455",
                UserTabel.password to "1234"

        )

        db.insert(
                FriendTabel.name,
                FriendTabel.id to 1,
                FriendTabel.displayname to "Phillip",
                FriendTabel.email to "philliphb@gmail.com",
                FriendTabel.phonenumber to 28732722,
                FriendTabel.message to null
        )

        db.insert(
                FriendTabel.name,
                FriendTabel.id to 2,
                FriendTabel.displayname to "Hazem",
                FriendTabel.email to null,
                FriendTabel.phonenumber to 52929055,
                FriendTabel.message to null
        )

        db.insert(
                FriendTabel.name,
                FriendTabel.id to 3,
                FriendTabel.displayname to "Kasper",
                FriendTabel.email to "kasper.vetter@live.dk",
                FriendTabel.phonenumber to null,
                FriendTabel.message to null
        )
    }

/*
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
                                   id: Int, displayName: String, email: String, phonenumber: String, token: String ->
                                   User(firebase, phonenumber, password)
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
*/

    /**
     * Get all the friends on the phone.
     */
    fun getFriends(): List<Friend> {
        try {
        var friends : List<Friend> = emptyList()
            instance.use {
                friends = select("Friend")
                        .parseList(
                                rowParser {
                                    id : Int,
                                    displayName : String,
                                    email : String,
                                    phonenumber : Int,
                                    message : List<Message>
                                    -> Friend(displayName, email ,phonenumber)
                                }
                        )
            }
            return friends
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return emptyList();
        }

    }
}
