package Entity

data class User(
        val id : Int,
        val username : String,
        val password : String
)

object UserTabel {
    val name = "User"
    val id = "id"
    val username = "username"
    val password = "password"
}

