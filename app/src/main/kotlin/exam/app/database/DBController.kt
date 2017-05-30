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
        val DB_VERSION: Int = 16
        val instance by lazy { DBController() }
    }

    override fun onCreate(db: SQLiteDatabase)   {
        db.createTable(
                UserTabel.name,
                true,
                UserTabel.id to INTEGER + PRIMARY_KEY,
                UserTabel.displayName to TEXT,
                UserTabel.email to TEXT,
                UserTabel.phonenumber to TEXT,
                UserTabel.password to TEXT
        )

        db.createTable(
                FriendTabel.name,
                true,
                FriendTabel.id to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FriendTabel.displayname to TEXT,
                FriendTabel.email to TEXT,
                FriendTabel.phonenumber to TEXT

        )

        db.createTable(
                MessageTabel.name,
                true,
                MessageTabel.id to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                MessageTabel.friendEmail to TEXT,
                MessageTabel.friendPhone to TEXT,
                MessageTabel.message to TEXT,
                MessageTabel.status to TEXT
        )



       // testData(db)
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

    fun getUser() : User{
        try {
            var user : User = User("", "", "", "")
            instance.use {
                user = select("User")
                        .parseSingle(
                                rowParser {
                                    id : Int,
                                    displayName : String,
                                    email : String,
                                    phonenumber : String,
                                    password : String
                                    -> User(displayName, email ,phonenumber, password)
                                }
                        )
            }
            return user
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return User("", "", "", "")
        }
    }

    fun insertFriend(friend: Friend) {
//      instance.use {
//           execSQL("INSERT INTO ${FriendTabel.name}  VALUES (NULL, '${friend.displayname}', '${friend.email}', '${friend.phonenumber}');")
//       }
        instance.use {
            insert(
                    FriendTabel.name,
                    FriendTabel.id to null,
                    FriendTabel.displayname to friend.displayname,
                    FriendTabel.email to friend.email,
                    FriendTabel.phonenumber to friend.phonenumber
            )
        }
    }

    fun insertMessage(message: Message) {
       /* instance.use {
            execSQL("INSERT INTO ${MessageTabel.name} VALUES (null, ${message.friendEmail}, ${message.friendPhone}, ${message.message}, ${message.status});")
        }*/
        instance.use {
            insert(
                    MessageTabel.name,
                    MessageTabel.id to null,
                    MessageTabel.friendEmail to message.friendEmail,
                    MessageTabel.friendPhone to message.friendPhone,
                    MessageTabel.message to message.message,
                    MessageTabel.status to message.status.toString()
            )
        }
    }

    fun clearUserTable(){
        instance.use {
            delete("User")
        }
    }

  /*  fun testData(db: SQLiteDatabase) {

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
    }*/

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
                                    phonenumber : String
                                    -> Friend(displayName, email ,phonenumber)
                                }
                        )
            }
            return friends
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return emptyList()
        }

    }

    fun getMessages(): List<Message> {
        try {
            var messages : List<Message> = emptyList()
            instance.use {
                messages = select("Message")
                        .parseList(
                                rowParser {
                                    id : Int,
                                    friendEmail : String,
                                    friendPhone : String,
                                    message : String,
                                    status : String
                                    -> Message(friendEmail, friendPhone, message , Status.valueOf(status))
                                }
                        )
            }
            return messages
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return emptyList()
        }
    }

    fun getMessagesByFriend(phone : String): List<Message> {
        try {
            var messages : List<Message> = emptyList()
            instance.use {
                messages = select("Message")
                        .where("(friendPhone = {fPhone})",
                                "fPhone" to phone)
                        .parseList(
                                rowParser {
                                    id : Int,
                                    friendEmail : String,
                                    friendPhone : String,
                                    message : String,
                                    status : String
                                    -> Message(friendEmail, friendPhone, message , Status.valueOf(status))
                                }
                        )
            }
            return messages
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return emptyList()
        }
    }

    fun getFriendByPhone(phone : String): Friend {
        try {
            var friend : Friend = Friend("", "", "")
            instance.use {
                friend = select("Friend")
                        .where("(phonenumber = {userPhone})",
                                "userPhone" to phone)
                        .parseSingle(
                                rowParser {
                                    id : Int,
                                    displayName : String,
                                    email : String,
                                    phonenumber : String
                                    -> Friend(displayName, email ,phonenumber)
                                }
                        )
            }
            return friend
        } catch (ex: SQLiteException) {
            println("Exception : " + ex)
            return Friend("", "", "")
        }
    }
}
