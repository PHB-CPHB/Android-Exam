package exam.app.Entity

data class Friend(
        var displayname : String,
        var email : String,
        var phonenumber : String
)

object FriendTabel {
    val name = "Friend"
    val id = "id"
    var displayname = "displayname"
    var email = "email"
    var phonenumber = "phonenumber"
}