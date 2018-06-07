package com.elegion.tracktor.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistanceMatrixApi {

    @GET("json")
    Call<DistanceMatrixResponse> getDistanceForRoute(@Query("origins") String positions,
                                                     @Query("destinations") String lastPosition,
                                                     @Query("key") String apiKey);
}
