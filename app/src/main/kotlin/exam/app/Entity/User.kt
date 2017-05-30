package exam.app.Entity

import com.google.firebase.auth.FirebaseUser

data class User(
        val displayName : String,
        val email : String,
        val phonenumber : String,
        val password : String
)



object UserTabel {
    val id = "id"
    val name = "User"
    val displayName = "displayName"
    val email = "email"
    val phonenumber = "phonenumber"
    val password = "password"

}

