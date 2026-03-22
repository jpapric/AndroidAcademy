fun main() {
    val topLimit: Int
    val bottomLimit: Int
    var num : Int
    topLimit = readln().toInt()
    bottomLimit = readln().toInt()
    do{
        num = readln().toInt()
    }while (num > topLimit || num < bottomLimit)
    var maxDdigit : Int
    maxDdigit = 0
    var tempNum = num
    while (tempNum > 0) {
        val digit = tempNum % 10
        if (digit > maxDdigit) {
            maxDdigit = digit
        }
        tempNum /= 10
    }
    println("Najveća znamenka u broju $num je: $maxDdigit")

}