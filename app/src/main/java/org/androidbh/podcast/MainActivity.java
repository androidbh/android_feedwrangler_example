package org.androidbh.podcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidbh.podcast.api.ErrorUtils;
import org.androidbh.podcast.api.FeedWranglerApi;
import org.androidbh.podcast.api.ResponseApi;
import org.androidbh.podcast.api.RestAdpter;
import org.androidbh.podcast.entity.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.list_items)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CategoryAdapter adapter;
    private Call<ResponseApi<Category>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCategories();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void openSearhc(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void loadCategories() {

        FeedWranglerApi api = RestAdpter.getApi(FeedWranglerApi.class);
        call = api.getCategories();

        call.enqueue(new Callback<ResponseApi<Category>>() {
            @Override
            public void onResponse(Call<ResponseApi<Category>> call, Response<ResponseApi<Category>> response) {

                if (response.isSuccessful()) {
                    ResponseApi responseApi = response.body();
                    adapter.items.addAll(responseApi.getItems());
                    adapter.notifyDataSetChanged();
                }
                else {
                    ResponseApi responseApi = ErrorUtils.parseError(response);
                    showMessage(responseApi.getError());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi<Category>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.d(TAG,"requisição cancelada" );
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
