package com.example.thriftlysa

import android.util.Log

class ThriftlyRepository {

    private val api =
        ApiClient.api

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {

        return try {

            Log.d(
                "ThriftlyAPI",
                "Sending registration request"
            )

            val response =
                api.register(
                    RegisterRequest(
                        name,
                        email,
                        password
                    )
                )

            if (response.isSuccessful) {

                response.body()
                    ?: RegisterResponse(
                        false,
                        "Empty server response."
                    )

            } else {

                RegisterResponse(
                    false,
                    "Server error: ${response.code()}"
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ThriftlyAPI",
                "Registration error",
                e
            )

            RegisterResponse(
                false,
                "Could not connect to the server."
            )
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        return try {

            Log.d(
                "ThriftlyAPI",
                "Sending login request"
            )

            val response =
                api.login(
                    LoginRequest(
                        email,
                        password
                    )
                )

            if (response.isSuccessful) {

                response.body()
                    ?: LoginResponse(
                        false,
                        "Empty server response.",
                        null,
                        null,
                        null
                    )

            } else {

                LoginResponse(
                    false,
                    "Server error: ${response.code()}",
                    null,
                    null,
                    null
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ThriftlyAPI",
                "Login error",
                e
            )

            LoginResponse(
                false,
                "Could not connect to the server.",
                null,
                null,
                null
            )
        }
    }

    suspend fun getListings():
            ListingsResponse {

        return try {

            Log.d(
                "ThriftlyAPI",
                "GET listings"
            )

            val response =
                api.getListings()

            if (response.isSuccessful) {

                response.body()
                    ?: ListingsResponse(
                        false,
                        "Empty server response.",
                        emptyList()
                    )

            } else {

                ListingsResponse(
                    false,
                    "Server error.",
                    emptyList()
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ThriftlyAPI",
                "Listings error",
                e
            )

            ListingsResponse(
                false,
                "Could not load listings.",
                emptyList()
            )
        }
    }

    suspend fun createListing(
        request: CreateListingRequest
    ): CreateListingResponse {

        return try {

            Log.d(
                "ThriftlyAPI",
                "POST create listing"
            )

            val response =
                api.createListing(
                    request
                )

            if (response.isSuccessful) {

                response.body()
                    ?: CreateListingResponse(
                        false,
                        "Empty server response.",
                        null
                    )

            } else {

                CreateListingResponse(
                    false,
                    "Server error.",
                    null
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ThriftlyAPI",
                "Create listing error",
                e
            )

            CreateListingResponse(
                false,
                "Could not create listing.",
                null
            )
        }
    }

    suspend fun toggleFavourite(
        userId: Int,
        listingId: Int
    ): FavouriteResponse {

        return try {

            Log.d(
                "ThriftlyAPI",
                "Toggling favourite"
            )

            val response =
                api.toggleFavourite(
                    FavouriteRequest(
                        userId,
                        listingId
                    )
                )

            if (response.isSuccessful) {

                response.body()
                    ?: FavouriteResponse(
                        false,
                        false,
                        "Empty server response."
                    )

            } else {

                FavouriteResponse(
                    false,
                    false,
                    "Server error."
                )
            }

        } catch (e: Exception) {

            Log.e(
                "ThriftlyAPI",
                "Favourite error",
                e
            )

            FavouriteResponse(
                false,
                false,
                "Could not update favourite."
            )
        }
    }
}