package exam.app.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


/**
 * Created by Mikkel on 15/05/2017.
 */
class FirebaseActions {
    val TAG = "FirebaseActions"

    fun sendMessageToUser(user : String, message : String) {

    }

    fun dbCall( fUser : FirebaseUser, user : String, message : String){
        val db = FirebaseDatabase.getInstance()
        var myRef = db.getReference(user)
        myRef.child(fUser.uid).setValue(message)
    }
}