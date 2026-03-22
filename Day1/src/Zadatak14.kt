import kotlin.random.Random

fun main() {
    val ruka = Ruka()

    println("Bacanje svih kockica:")
    println(ruka.baciSve())
    println("Trenutne vrijednosti kockica: ${ruka.trenutneVrijednosti()}")

    println("\nZaključavanje prvih 3 kockica i bacanje preostalih:")
    ruka.postaviZakljucaneKockice(listOf(0, 1, 2))
    println(ruka.baciSve())
    println("Trenutne vrijednosti kockica: ${ruka.trenutneVrijednosti()}")

    println("\nProvjere rezultata:")
    println("Je li Jamb? ${ruka.jeJamb()}")
    println("Je li Poker? ${ruka.jePoker()}")
    println("Je li Skala? ${ruka.jeSkala()}")

    println("\nOtključavanje svih kockica i ponovno bacanje:")
    ruka.otkljucajSve()
    println(ruka.baciSve())
    println("Trenutne vrijednosti kockica: ${ruka.trenutneVrijednosti()}")
    println("Je li Jamb? ${ruka.jeJamb()}")
    println("Je li Poker? ${ruka.jePoker()}")
    println("Je li Skala? ${ruka.jeSkala()}")
}

class Kockica {
    var vrijednost: Int = 1
        private set

    fun baci(): Int {
        vrijednost = Random.nextInt(1, 7)
        return vrijednost
    }
}

class Ruka {
    private val kockice = Array(6) { Kockica() }
    private val zakljucane = BooleanArray(6) { false }

    fun baciSve(): List<Int> {
        return kockice.mapIndexed { i, kockica ->
            if (!zakljucane[i]) kockica.baci() else kockica.vrijednost
        }
    }

    fun postaviZakljucaneKockice(indeksi: List<Int>) {
        for (i in indeksi) {
            if (i in 0..5) zakljucane[i] = true
        }
    }

    fun otkljucajSve() {
        for (i in zakljucane.indices) {
            zakljucane[i] = false
        }
    }

    fun trenutneVrijednosti(): List<Int> {
        return kockice.map { it.vrijednost }
    }

    fun jeJamb(): Boolean {
        return kockice.all { it.vrijednost == kockice[0].vrijednost }
    }

    fun jePoker(): Boolean {
        val vrijednosti = kockice.groupBy { it.vrijednost }
        return vrijednosti.values.any { it.size >= 4 }
    }

    fun jeSkala(): Boolean {
        val skalaMala = setOf(1, 2, 3, 4, 5)
        val skalaVelika = setOf(2, 3, 4, 5, 6)
        val vrijednosti = kockice.map { it.vrijednost }.toSet()
        return vrijednosti == skalaMala || vrijednosti == skalaVelika
    }
}