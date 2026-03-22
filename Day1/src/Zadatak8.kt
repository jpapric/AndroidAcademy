fun main() {
    var num : Int
    num = 53
    var rezultat : Boolean
    rezultat = isProst(num)
    if(rezultat){
        println("Broj je prost")
    }else{
        println("Broj nije prost")
    }
}

fun isProst(broj : Int) : Boolean{

    if(broj<2){
        return false
    }

    for(i in 2 until broj){
        if(broj % i == 0){
            return false
        }
    }

    return true
}