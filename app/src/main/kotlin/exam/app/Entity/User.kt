package exam.app.Entity

import com.google.firebase.auth.FirebaseUser

data class User(
        val fireBUser : FirebaseUser,
        val phonenumber : String,
        val password : String
) {
    //TODO If users need any methodes
}




object UserTabel {
    val id = "id"
    val name = "User"
    val token = "token"
    val displayName = "displayName"
    val email = "email"
    val phonenumber = "phonenumber"
    val password = "password"

}

