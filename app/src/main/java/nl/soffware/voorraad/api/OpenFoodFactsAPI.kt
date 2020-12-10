package nl.soffware.voorraad.api

import com.squareup.moshi.Moshi
import nl.soffware.voorraad.service.OpenFoodFactsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class OpenFoodFactsAPI {
    companion object {
        // The base url off the api.
        private const val baseUrl = "https://world.openfoodfacts.org/api/v0/"

        /**
         * @return [OpenFoodFactsService] The service class off the retrofit client.
         */
        fun createApi(): OpenFoodFactsService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()


            val moshi = Moshi.Builder()
                .build()
            // Create an OkHttpClient to be able to make a log of the network traffic and add the api key to all requests
            // Create the Retrofit instance
            val movieApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            // Return the Retrofit TheMovieDBAPIService
            return movieApi.create(OpenFoodFactsService::class.java)
        }
    }
}