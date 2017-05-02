package julio.com.br.kotlingithubapi.service

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Shido on 30/04/2017.
 */
class ServiceGenerator {

    companion object{


        val BASE_URL: String = "https://api.github.com/"


        val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy{
            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }

            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.declaringClass == RealmObject::class.java
            }
        }).create()


        val httpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


        var builder: Retrofit.Builder = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)

        var retrofit = builder.build()


        fun <S> createService(serviceClass: Class<S>): S {
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging)
                httpClient.connectTimeout(20, TimeUnit.SECONDS)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
            return retrofit.create(serviceClass)
        }





    }







    }