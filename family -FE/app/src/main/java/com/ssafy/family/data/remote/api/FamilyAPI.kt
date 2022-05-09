package com.ssafy.family.data.remote.api

import com.ssafy.family.data.remote.res.FamilyRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FamilyAPI {
    @Multipart
    @POST("/api/family/create")
    suspend fun createFamily(
        @PartMap data: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<FamilyRes>

    @Multipart
    @POST("/api/family/join")
    suspend fun joinFamily(
        @PartMap data: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Response<FamilyRes>

    @GET("api/family/code/check/{code}")
    suspend fun checkFamilyCode(@Path("code") code:String):Response<FamilyRes>
}