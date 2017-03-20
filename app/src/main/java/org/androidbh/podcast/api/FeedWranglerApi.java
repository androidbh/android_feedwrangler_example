package org.androidbh.podcast.api;

import org.androidbh.podcast.entity.Category;
import org.androidbh.podcast.entity.Podcast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by felipearimateia on 20/03/17.
 */

public interface FeedWranglerApi {

    @GET("api/v2/podcasts/categories")
    Call<ResponseApi<Category>> getCategories();

    @GET("api/v2/podcasts/category/{id}")
    Call<ResponseApi<Podcast>>getCategory(@Path("id") int id);
}
