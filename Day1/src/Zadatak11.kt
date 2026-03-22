fun main() {
    val rijeci = listOf("lozinka123", "Slaba", "JakaLozinka1", "12345678", "TestPass8")
    var rezultat : Int

    for (rijec in rijeci) {
        rezultat = izbrojiZnakove(rijec)
        println("Rijec $rijec ima $rezultat jedinstvenih znakova")
    }

}

fun izbrojiZnakove(rijec: String): Int {
    val jedinstveniZnakovi = mutableListOf<Char>()

    for (char in rijec) {
        if (!jedinstveniZnakovi.contains(char)) {
            jedinstveniZnakovi.add(char)
        }
    }

    return jedinstveniZnakovi.size
}