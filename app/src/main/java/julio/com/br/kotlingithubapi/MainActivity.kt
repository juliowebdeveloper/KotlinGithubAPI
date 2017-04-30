package julio.com.br.kotlingithubapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.vicpin.krealmextensions.firstItem
import com.vicpin.krealmextensions.query
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import io.realm.Realm
import io.realm.RealmQuery
import julio.com.br.kotlingithubapi.R.layout.activity_main
import julio.com.br.kotlingithubapi.model.Github
import julio.com.br.kotlingithubapi.service.GitHubService
import julio.com.br.kotlingithubapi.service.ServiceGenerator
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val gitHubgitHubService = ServiceGenerator.createService(GitHubService::class.java)
        Realm.init(this@MainActivity)
        val realm = Realm.getDefaultInstance()


        val savedUser: Github? = RealmQuery.createQuery(realm, Github::class.java).findFirst()
        val savedUser2  = Github().query{ query -> query.equalTo("id", savedUser?.id).findFirst() }
        val savedUser3 = Github().queryFirst();
        Log.e("SAVED USER 3", savedUser3?.name)
        updateViews(savedUser)

       gitHubgitHubService.getGithubUser("juliowebdeveloper").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    user -> realm.beginTransaction()
                    realm.copyToRealm(user)
                    realm.commitTransaction()

                    //Github().save() //Kotlin Realm Extensions
                    updateViews(user)
                },
                 { error -> Log.e("Error", error.message)
                 }
                )


    }


    private fun updateViews(savedUser: Github?) {
        Glide.with(this).load(savedUser?.avatarUrl).into(user_image)
        user_name.text = savedUser?.name
        public_repos.text = "Public Repos: "+ savedUser?.publicRepos.toString()
    }




}
