package task3

fun main(){
    val steps = listOf(4500, 12000, 8000, 15000, 3000, 11000, 9500)
    var totalSteps:Int=0

    for(step in steps){
        totalSteps+=step
    }

    println("Total steps for the week: $totalSteps")

    var index:Int=0

    while(index < steps.size){
        if(steps[index]>10000){
            println("First day with more than 10,000 steps: Day ${index + 1}")
            break
        }
        index++
    }
}