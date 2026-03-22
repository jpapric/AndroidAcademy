fun main() {
    val num1: Int
    val num2: Int
    val num3: Int
    num1 = readln().toInt()
    num2 = readln().toInt()
    num3 = readln().toInt()
    if (num1 > num2 && num1 > num3) {
        println("$num1")
    } else if (num2 > num1 && num2 > num3) {
        println("$num2")
    } else {
        println("$num3")
    }
}