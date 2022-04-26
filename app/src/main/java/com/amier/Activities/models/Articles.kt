package com.amier.Activities.models

data class Articles(
    var _id: String? = null,
    var nom: String? = null,
    var description: String? = null,
    var addresse: DoubleArray? = null,
    var photo: String? = null,
    var dateCreation: String? = null,
    var dateModif: String? = null,
    var type: String? = null,
    var user: User? = null,
    var question: Question? = null,
    var __v: Int? = null,
    var articles: MutableList<Articles>? = null,
)
