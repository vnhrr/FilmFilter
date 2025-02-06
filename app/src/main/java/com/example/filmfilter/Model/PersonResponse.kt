data class PersonResponse(
    val results: List<Person>
)

data class Person(
    val id: Int,
    val name: String
)
