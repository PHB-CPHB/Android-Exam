package exam.app.Entity

data class User(
        val id : Int = 0,
        val username : String,
        val email : String,
        val phonenumber : String
)

object UserTabel {
    val name = "User"
    val id = "id"
    val username = "username"
    val email = "email"
    val phonenumber = "phonenumber"
}

