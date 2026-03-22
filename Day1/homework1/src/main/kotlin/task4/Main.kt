package task4

fun main(){
    val testInputs = listOf(
        " John_doe123 ",
        "  ab  ",
        "valid_user1",
        "no spaces",
        "123invalid",
        "_startsWithUnderscore",
        "toolongusernamethatexceedslimit",
        "ok_usr"
    )

    for (input in testInputs) {
        val prepared = prepareUsername(input)
        val isValid = isValid(prepared)
        println("Original: $input\nPrepared: $prepared\nValid: $isValid\n")
    }

    print("\nEnter username: ")
    val userInput = readln()
    val prepared = prepareUsername(userInput)
    val valid = isValid(prepared)
    println("Prepared: \"$prepared\"")
    println("Valid: $valid")
}

fun prepareUsername(uName:String):String{
    return uName.trim().lowercase()
}

fun isValid(userName:String):Boolean{
    if(userName.isBlank()) return false
    if(userName.length<5 || userName.length>15) return false
    if(!userName[0].isLetter()) return false
    if(userName.contains(' ')) return false
    if(!userName.all { it.isLetterOrDigit() || it=='_' }) return false
    return true
}