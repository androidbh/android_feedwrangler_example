package org.androidbh.podcast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.androidbh.podcast.api.FeedWranglerApi;
import org.androidbh.podcast.api.ResponseApi;
import org.androidbh.podcast.api.RestAdpter;
import org.androidbh.podcast.entity.Category;
import org.androidbh.podcast.entity.Podcast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list_items)
    RecyclerView recyclerView;

    private Category category;

    private Call<ResponseApi<Podcast>> call;

    private PodcastAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category = getIntent().getParcelableExtra("category");

        setTitle(category.getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PodcastAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPodcasts();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void loadPodcasts() {

        FeedWranglerApi api = RestAdpter.getApi(FeedWranglerApi.class);
        call = api.getCategory(category.getId());

        call.enqueue(new Callback<ResponseApi<Podcast>>() {
            @Override
            public void onResponse(Call<ResponseApi<Podcast>> call, Response<ResponseApi<Podcast>> response) {

                if (response.isSuccessful()) {
                    ResponseApi<Podcast> responseApi = response.body();
                    adapter.items.addAll(responseApi.getItems());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResponseApi<Podcast>> call, Throwable t) {

            }
        });
    }
}
