package kia.part1.two

class User private constructor(val nickname: String) {

    companion object {

        fun newSubscribingUser(email: String) =
            User(email.substringBefore('@'))

        fun newSocialUser(accountId: Int) =

            User(getNameFromSocialNetwork(accountId))
    }
}

fun getNameFromSocialNetwork(accountId: Int): String {
    return "Test"
}

fun main() {
    val subscribingUser = User.newSubscribingUser(
        "bob@gma il . com "
    )
    val socialUser = User.newSocialUser(4)
    println(subscribingUser.nickname)
// bob
}
