package io.monteirodev.giftideas.api;

import io.monteirodev.giftideas.BuildConfig;
import io.monteirodev.giftideas.model.ActiveListings;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class Etsy {

    // the requestInterceptor adds the etsyApiKey
    private static RequestInterceptor getInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addEncodedQueryParam("api_key", BuildConfig.ETSY_API_KEY);
            }
        };
    }

    /* get Api interface
     *
     * .create(interface to create an object from)
     */
    private static Api getApi(){
        // set REST Adapter
        return new RestAdapter.Builder()
                .setEndpoint("https://openapi.etsy.com/v2")
                .setRequestInterceptor(getInterceptor())
                .build()
                .create(Api.class);
    }

    public static void getActiveListings(Callback<ActiveListings> callback){
        getApi().activeListings("Images,Shop", callback);
    }
}
