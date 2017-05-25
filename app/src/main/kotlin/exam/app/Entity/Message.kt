package exam.app.Entity

data class Message(
        var toUser : String,
        var fromUser : String,
        var message : String
) {
    constructor(parts: List<String>) : this(parts[0], parts[1], parts[2])
}

object MessageTabel{
    val id = "id"
    val name = "Message"
    val toUser = "toUser"
    val fromUser = "fromUser"
    val message = "message"
}
