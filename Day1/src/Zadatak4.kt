fun main() {
    val char : String
    var isVowel : Boolean
    char = readln()
    isVowel = when (char){
        "a"->true
        "e"->true
        "i"->true
        "o"->true
        "u"->true
        else -> { isVowel = false; false}
    }
    if(isVowel){
        println("Znak je samoglasnik")
    }else{
        println("Znak je suglasnik")
    }
}