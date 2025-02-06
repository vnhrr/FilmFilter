package com.example.filmfilter.Model

data class MovieDetailsResponse(
    val id: Int,
    val credits: CreditsResponse
)

data class CreditsResponse(
    val cast: List<Actor>,
    val crew: List<CrewMember>
)

data class Actor(
    val name: String
)

data class CrewMember(
    val name: String,
    val job: String
)
