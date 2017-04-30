package julio.com.br.kotlingithubapi.service

import julio.com.br.kotlingithubapi.model.Github
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by Shido on 30/04/2017.
 */
interface GitHubService {

    @GET("users/{username}")
    fun getGithubUser(@Path("username") username: String): Observable<Github>



}