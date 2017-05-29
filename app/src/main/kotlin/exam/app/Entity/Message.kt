package exam.app.Entity

data class Message(
        var friendEmail : String,
        var friendPhone : String,
        var message : String,
        var status : Status
)

object MessageTabel{
    val id = "id"
    val name = "Message"
    val friendEmail = "friendEmail"
    val friendPhone = "friendPhone"
    val message = "message"
    val status = "status"
}
