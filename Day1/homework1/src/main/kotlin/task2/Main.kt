package task2

fun main(){

    var code:Int=2
    var price:Float= 0.0f
    var insertedMoney:Float=2.0f
    val drink = when(code){
        1 -> {
            price=1.0f
            "Water"
        }
        2 -> {
            price=1.7f
            "Cola"
        }
        3 -> {
            price=1.3f
            "Juice"
        }
        4 -> {
            price=1.5f
            "Coffee"
        }
        else -> "Error"
    }

    if (insertedMoney >= price) {
        val change = insertedMoney - price
        println("Dispensing: $drink")
        println("Change: $change €")
    } else {
        val missing = price - insertedMoney
        println("Not enough money for $drink.")
        println("Missing: $missing €")
    }

}