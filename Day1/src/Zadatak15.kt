enum class Boja {
    SRCE, PIK, TREF, KARO
}

enum class Vrijednost(val snaga: Int) {
    DVA(2), TRI(3), CETIRI(4), PET(5), SEST(6), SEDAM(7),
    OSAM(8), DEVET(9), DESET(10), ZANDAR(11), DAMA(12), KRALJ(13), AS(14)
}

data class Karta(val boja: Boja, val vrijednost: Vrijednost) {
    override fun toString(): String {
        return "$vrijednost $boja"
    }
}

fun main() {
    val igra = IgraWar()
    igra.igra()
}

class Spil {
    private val karte = Array(52) { i ->
        val boja = Boja.values()[i / 13]
        val vrijednost = Vrijednost.values()[i % 13]
        Karta(boja, vrijednost)
    }.toMutableList()

    init {
        promijesaj()
    }

    fun promijesaj() {
        karte.shuffle()
    }

    fun podijeliKartu(): Karta? {
        return if (karte.isNotEmpty()) karte.removeAt(0) else null
    }

    fun velicina(): Int {
        return karte.size
    }
}

class IgraWar {
    private val spil = Spil()

    fun igra() {
        var bodoviIgrac1 = 0
        var bodoviIgrac2 = 0

        println("Početak igre War!")

        while (spil.velicina() >= 2) {
            val karta1 = spil.podijeliKartu()
            val karta2 = spil.podijeliKartu()

            if (karta1 != null && karta2 != null) {
                println("Igrač 1 vuče: $karta1")
                println("Igrač 2 vuče: $karta2")

                when {
                    karta1.vrijednost.snaga > karta2.vrijednost.snaga -> {
                        println("Igrač 1 osvaja rundu!")
                        bodoviIgrac1++
                    }
                    karta2.vrijednost.snaga > karta1.vrijednost.snaga -> {
                        println("Igrač 2 osvaja rundu!")
                        bodoviIgrac2++
                    }
                    else -> {
                        println("Neriješeno!")
                    }
                }
                println()
            }
        }

        println("Kraj igre!")
        println("Igrač 1 ima $bodoviIgrac1 bodova.")
        println("Igrač 2 ima $bodoviIgrac2 bodova.")

        when {
            bodoviIgrac1 > bodoviIgrac2 -> println("Igrač 1 pobjeđuje!")
            bodoviIgrac2 > bodoviIgrac1 -> println("Igrač 2 pobjeđuje!")
            else -> println("Igra je neriješena!")
        }
    }
}