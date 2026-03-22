fun main() {
    val rijeci = listOf("lozinka123", "Slaba", "JakaLozinka1", "12345678", "TestPass8")
    var rezultat : Int

    for (rijec in rijeci) {
        rezultat = izbrojiSamoglasnike(rijec)
        println("Rijec $rijec ima $rezultat samoglasnika")
    }

}

fun izbrojiSamoglasnike(rijec: String): Int {
    var sum : Int
    sum = 0
    val vowels = setOf('a','e','i','o','u')
    for (letter in rijec) {
        if(letter in vowels) {
            sum++
        }
    }
    return sum
}