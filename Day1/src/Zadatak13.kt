import kotlin.math.sqrt
import kotlin.math.pow

fun main() {
    val t1 = Tocak()
    t1.ispisiKoordinate()
    val t2 = Tocak(3.0)
    t2.ispisiKoordinate() // Očekivano: (3.0, 3.0)
    val t3 = Tocak(4.0, 5.0)
    t3.ispisiKoordinate() // Očekivano: (4.0, 5.0)
    t3.transliraj(2.0, -1.0)
    println("Nakon translacije:")
    t3.ispisiKoordinate() // Očekivano: (6.0, 4.0)
    val udaljenost = t3.udaljenost(t2)
    println("Udaljenost između t2 i t3: $udaljenost")

}

class Tocak(private var x: Double, private var y: Double) {

    constructor() : this(0.0, 0.0)
    constructor(kordinate: Double) : this(kordinate, kordinate)
    fun transliraj(dx: Double, dy: Double) {
        x += dx
        y += dy
    }
    fun udaljenost(drugaTocka: Tocak): Double {
        return sqrt((x - drugaTocka.x).pow(2) + (y - drugaTocka.y).pow(2))
    }
    fun ispisiKoordinate() {
        println("Tocka: ($x, $y)")
    }
}