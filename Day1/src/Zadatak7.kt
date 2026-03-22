fun main() {
    val power : Int
    val num : Int
    power = 3
    num = 3
    var rezultat : Int
    rezultat = izracunPotencije(power,num)
    println("$num na $power = $rezultat")

}

fun izracunPotencije(potencija : Int, broj : Int) : Int{
    var result : Int
    result = 1
    for(i in (1..potencija)){
        result*=broj
    }
    return result
}