fun main() {
    val num : Int
    var sum : Int
    sum = 0
    num = readln().toInt()
    for(i in (1..num)){
        sum+=i
    }
    println("Suma je:  $sum")
}