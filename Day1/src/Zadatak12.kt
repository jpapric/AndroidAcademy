import kotlin.math.PI

fun main() {
    val jedinicniKrug = Krug()
    println("Jedinični krug:")
    println("Polumjer: ${jedinicniKrug.getPolumjer()}")
    println("Površina: ${jedinicniKrug.izracunajPovrsinu()}")
    println("Opseg: ${jedinicniKrug.izracunajOpseg()}")
    val krugSaPolumjerom = Krug(5.0)
    println("\nKrug sa polumjerom 5.0:")
    println("Polumjer: ${krugSaPolumjerom.getPolumjer()}")
    println("Površina: ${krugSaPolumjerom.izracunajPovrsinu()}")
    println("Opseg: ${krugSaPolumjerom.izracunajOpseg()}")
    krugSaPolumjerom.setPolumjer(3.0)
    println("\nNovi polumjer kruga: ${krugSaPolumjerom.getPolumjer()}")
    println("Nova površina: ${krugSaPolumjerom.izracunajPovrsinu()}")
    println("Novi opseg: ${krugSaPolumjerom.izracunajOpseg()}")

}

class Krug(private var polumjer: Double) {
    constructor() : this(1.0)

    fun izracunajPovrsinu(): Double {
        return PI * polumjer * polumjer
    }

    fun izracunajOpseg(): Double {
        return 2 * PI * polumjer
    }

    fun getPolumjer(): Double {
        return polumjer
    }

    fun setPolumjer(noviPolumjer: Double) {
        if (noviPolumjer > 0) {
            polumjer = noviPolumjer
        } else {
            println("Polumjer mora biti veći od 0.")
        }
    }
}