package task5

fun main(){
    val account1=BankAccount("1")
    val account2=BankAccount("2")
    val account3=BankAccount("3")

    account1.deposit(200.0)
    account2.deposit(-50.0)
    account3.deposit(10000.0)

    account1.withdraw(300.0)
    account2.withdraw(20.0)
    account3.withdraw(5000.0)

    println("Balance of account1 (${account1.accountNumber}): ${account1.balance}")
    println("Balance of account2 (${account2.accountNumber}): ${account2.balance}")
    println("Balance of account3 (${account3.accountNumber}): ${account3.balance}")

    println("Total bank accounts created: ${BankAccount.totalAccounts}")
}

object TransactionLogger{
    fun log(message:String){
        println("[Transaction Log] $message")
    }
}

class BankAccount(val accountNumber: String){
    var balance:Double=0.0
        private set

    fun deposit(amount:Double){
        if(amount>0){
            balance+=amount
            TransactionLogger.log("Deposited $amount to $accountNumber. New balance: $balance")
        }else{
            TransactionLogger.log("Invalid deposit amount: $amount")
        }
    }

    fun withdraw(amount:Double){
        if(amount<=balance){
            balance-=amount
            TransactionLogger.log("Withdrew  $amount from account $accountNumber. New balance: $balance")
        }else{
            TransactionLogger.log("Insufficient funds for withdrawal of $amount from $accountNumber. Current balance: $balance")
        }
    }

    companion object{
        var totalAccounts:Int=0
            private set
    }

    init {
        totalAccounts++
        TransactionLogger.log("Bank account $accountNumber created. Total accounts: $totalAccounts.")
    }
}