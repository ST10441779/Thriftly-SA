package com.example.thriftlysa

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("register.php")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("listings.php")
    suspend fun getListings():
            Response<ListingsResponse>

    @POST("create_listing.php")
    suspend fun createListing(
        @Body request: CreateListingRequest
    ): Response<CreateListingResponse>

    @POST("favourite.php")
    suspend fun toggleFavourite(
        @Body request: FavouriteRequest
    ): Response<FavouriteResponse>
}