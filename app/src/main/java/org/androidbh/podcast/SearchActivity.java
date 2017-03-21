package org.androidbh.podcast;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.androidbh.podcast.api.ErrorUtils;
import org.androidbh.podcast.api.FeedWranglerApi;
import org.androidbh.podcast.api.ResponseApi;
import org.androidbh.podcast.api.RestAdpter;
import org.androidbh.podcast.entity.Podcast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list_items)
    RecyclerView recyclerView;

    private final FeedWranglerApi api = RestAdpter.getApi(FeedWranglerApi.class);
    private Call<ResponseApi<Podcast>> call;
    private PodcastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PodcastAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(this);
        }

        searchItem.expandActionView();

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        call = api.search(query);
        call.enqueue(new Callback<ResponseApi<Podcast>>() {
            @Override
            public void onResponse(Call<ResponseApi<Podcast>> call, Response<ResponseApi<Podcast>> response) {
                if (response.isSuccessful()) {
                    ResponseApi<Podcast> responseApi = response.body();
                    adapter.items.clear();
                    adapter.items.addAll(responseApi.getItems());
                    adapter.notifyDataSetChanged();
                }
                else {
                    ResponseApi responseApi = ErrorUtils.parseError(response);
                    showMessage(responseApi.getError());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi<Podcast>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.d(TAG,"requisição cancelada");
                }
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
