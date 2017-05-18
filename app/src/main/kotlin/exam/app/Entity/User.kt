package exam.app.Entity

data class User(
        val id : Int,
        val username : String,
        val email : String,
        val phonenumber : String,
        val token : String
)


object UserTabel {
    val name = "User"
    val id = "id"
    val username = "username"
    val email = "email"
    val phonenumber = "phonenumber"
    val token = "token"
}

