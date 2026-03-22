package task1

fun main(){
    val name:String="John"
    val surname:String="Doe"
    var email:String?=null
    var age:Int?=23

    println("Email length: ${email?.length}")

    email = "john@gmail.com"

    println("Email length: ${email?.length}")

    email?.let {
        println("Email length: $it")
    }
}