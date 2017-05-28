package exam.app.Entity

data class Friend(
        var displayname : String,
        var email : String?,
        var phonenumber : String?
) {

    //TODO Make all with the friend list
    fun getFriends() : List<Friend>{
        return emptyList();
    }
/*
    var messagesText : String
        //TODO: Make the get to take a a list and place | for every message and a ; for every element inside the list
        get() : String = "..."
        set(value: String) { messages = value.split("|").map { Message(it.split(";")) }.toList() }
        */
}

object FriendTabel {
    val id = "id"
    val name = "Friend"
    var displayname = "displayname"
    var email = "email"
    var phonenumber = "phonenumber"
    var message = "message"
}