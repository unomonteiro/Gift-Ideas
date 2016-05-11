package io.monteirodev.giftideas.api;

import io.monteirodev.giftideas.model.ActiveListings;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Api {

    /* variables to access etsy
     * Protocol = GET
     * URI = "/listings/active"
     *
     * @Query // including used parameters in activeListings()
     * String includes // name parameter like a variable
     *
     * callback will be typed to Java class that network response will
     * be converted into by Retrofit.
     * Retrofit automatically parses the network response, and
     * creates an object of the type you want
     * with all the data set on it
     */
    @GET("/listings/active")
    void activeListings(@Query("includes") String includes,
        Callback<ActiveListings> callback);
}
