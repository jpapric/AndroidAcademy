fun main() {
    val lozinke = listOf("lozinka123", "Slaba", "JakaLozinka1", "12345678", "TestPass8")

    for (lozinka in lozinke) {
        if (jeJakaLozinka(lozinka)) {
            println("Lozinka '$lozinka' je dovoljno jaka.")
        } else {
            println("Lozinka '$lozinka' nije dovoljno jaka.")
        }
    }

}

fun jeJakaLozinka(lozinka: String): Boolean {
    if (lozinka.length < 8) {
        return false
    }

    var imaVelikoSlovo = false
    var imaBroj = false

    for (char in lozinka) {
        if (char.isUpperCase()) {
            imaVelikoSlovo = true
        }
        if (char.isDigit()) {
            imaBroj = true
        }
    }

    return imaVelikoSlovo && imaBroj
}