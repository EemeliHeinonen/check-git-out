package com.eemeliheinonen.gitcheck;


import com.eemeliheinonen.gitcheck.models.Commit;
import com.eemeliheinonen.gitcheck.models.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GitHubApiInterface {

    //Returns 30 repositories by default
    @GET("users/{username}/repos")
    Call<List<Repository>> repositoryList(@Path("username") String username);

    //Get the next page of a user's repositories, by passing in in the 'next' link from the first call's headers
    @GET
    Call<List<Repository>> repositoryListPaginate(@Url String url);

    //Get the 10 latest commits in a repository
    @GET("https://api.github.com/repos/{username}/{repositoryname}/commits?per_page=10")
    Call<List<Commit>> commitList(@Path("username") String username, @Path("repositoryname") String repositoryName);
}
