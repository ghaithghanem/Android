package com.amier.Activities.models

data class User(
        var _id: String? = null,
    var email: String? = null,
    var nom: String? = null,
    var password: String? = null,
    var prenom: String? = null,
    var photoProfil: String? = null,
    var numt: String? = null
)
data class email(
    var email: String? = null,
)
data class token (
    var email: String? = null,
    var token : String? = null,
    var password: String? = null,
        )

data class ForgotAndToken(
    val succes : Boolean? = null,
    val msg : String? = null,
    val token : String? = null
)




data class UserAndToken (
    val user : User? = null,
    val token : String? = null
)

data class userSignUpResponse (
    val user : User? = null,
    val token : String? = null,
    val reponse : String? = null
)
data class userUpdateResponse (
    val user : User? = null,
    val reponse : String? = null
)
data class ForgetPassword(
    var email: String?= null,
    var code: Int?= null,
    var password: String?= null
)
data class FrogetResponse(
    val message:String? = null
)