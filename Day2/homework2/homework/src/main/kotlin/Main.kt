fun main() {

    val restaurant = Restaurant("Kod Kotlinera")

    val waiter1 = Waiter("Ivan", 800.0, 200.0)
    val chef1 = Chef("Marko", 1200.0, "Italian")

    restaurant.addEmployee(waiter1)
    restaurant.addEmployee(chef1)

    val dish1 = Dish("Pizza", 10.0, false)
    val dish2 = Dish("Salad", 5.0, true)

    val order1 = Order(1, mutableListOf(dish1, dish2))
    order1.waiter = waiter1
    order1.status = OrderStatus.DONE

    val order2 = Order(2, mutableListOf(dish2))
    order2.waiter = waiter1
    order2.status = OrderStatus.IN_PROGRESS

    restaurant.addOrder(order1)
    restaurant.addOrder(order2)

    println("Welcome ${restaurant.name}")

    restaurant.printEmployees()

    restaurant.orders.forEach { order ->
        println("Order ${order.id}: ${order.dishes.map { it.name }}")
    }

    println("Total revenue: ${restaurant.totalRevenue()}")

    val bigOrders = restaurant.filterOrders {
        it.totalPrice() > 8
    }

    println("Big orders: ${bigOrders.size}")

    val grouped = restaurant.orders.groupBy { it.status }
    println(grouped)

}

interface Payable {
    fun calculateSalary(): Double
}

abstract class Employee(
    val name: String,
    protected var baseSalary: Double
) : Payable {

    abstract fun work()

    override fun calculateSalary(): Double {
        return baseSalary
    }
}

class Waiter(
    name: String,
    baseSalary: Double,
    var tips: Double
) : Employee(name, baseSalary) {

    override fun work() {
        println("$name is serving customers.")
    }

    override fun calculateSalary(): Double {
        return baseSalary + tips
    }
}

class Chef(
    name: String,
    baseSalary: Double,
    val specialty: String
) : Employee(name, baseSalary) {

    override fun work() {
        println("$name is cooking $specialty dishes.")
    }
}

data class Dish(
    val name: String,
    val price: Double,
    val isVegetarian: Boolean
)

enum class OrderStatus {
    CREATED, IN_PROGRESS, DONE
}

class Order(
    val id: Int,
    val dishes: MutableList<Dish>
) {
    lateinit var waiter: Waiter
    var note: String? = null
    var status: OrderStatus = OrderStatus.CREATED

    fun totalPrice(): Double {
        return dishes.sumOf { it.price }
    }
}

class Restaurant(
    val name: String
) {
    private val employees = mutableListOf<Employee>()
    val orders = mutableListOf<Order>()

    fun addEmployee(employee: Employee) {
        employees.add(employee)
    }

    fun addOrder(order: Order) {
        orders.add(order)
    }

    fun filterOrders(predicate: (Order) -> Boolean): List<Order> {
        return orders.filter(predicate)
    }

    fun totalRevenue(): Double {
        return orders
            .filter { it.status == OrderStatus.DONE }
            .sumOf { it.totalPrice() }
    }

    fun printEmployees() {
        employees.forEach {
            it.work()
            println("${it.name} earns ${it.calculateSalary()}")
        }
    }
}