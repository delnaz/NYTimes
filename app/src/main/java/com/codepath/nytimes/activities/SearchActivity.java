package com.codepath.nytimes.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.codepath.nytimes.R;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.adapters.EndlessScrollListener;
import com.codepath.nytimes.fragment.FilterFragment;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterFragment.OnFragmentInteractionListener {

    @BindView(R.id.gvResult) GridView gvResults;

    ArrayList<Article> articles;

    ArticleAdapter adapter;
    String searchQuery;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();
    }

    private void setUpViews() {
        ButterKnife.bind(this);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this,articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                onArticleSearch(page);
                return loading;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                adapter.clear();
                searchQuery = query;
                onArticleSearch(0);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                Log.d("2222", "query" + searchQuery);
                return false;
            }
        });
        MenuItem filter = menu.findItem(R.id.action_filter);
        filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showFragmentDialog();

                return true;
                //  return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

        //    return true;
    }

    public void showFragmentDialog(){
        final FragmentManager fm = getSupportFragmentManager();
        final FilterFragment filterFragment = new FilterFragment();
        filterFragment.show(fm, "filters_fragment");
    }

    public void onArticleSearch(int page){

        if(Utils.isNetworkAvailable(this)) {

            AsyncHttpClient client = new AsyncHttpClient();

            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
            String apiKey = "c1404fc5f4634d87b4ccddaf533f045c";
            RequestParams params = new RequestParams();
            params.put("api-key", apiKey);
            params.put("page", page);

            if (searchQuery != null && !searchQuery.isEmpty()) {
                params.put("q", searchQuery);
            }

            SharedPreferences prefs = getApplicationContext().getSharedPreferences("nytimes", Context.MODE_PRIVATE);
            int sort = prefs.getInt("sort", 0);
            if (sort == 1) {
                params.put("sort", "oldest");
            } else if(sort == 2){
                params.put("sort", "newest");
            }

            int year = prefs.getInt("year", -1);
            int month = prefs.getInt("month", -1);
            int day = prefs.getInt("day", -1);

            if(!(year == -1 || month == -1 || day == -1)){
                month++;
                String one = month < 10 ? "0" : "";
                String two = day < 10 ? "0" : "";
                String begin = year + one + month + two + day ;
                params.put("begin_date", begin);
            }

            String filterQuery = "";
            if(prefs.getBoolean("check1", false)) {
                filterQuery = "\"Arts\"";
            }

            if(prefs.getBoolean("check2", false)) {
                filterQuery = filterQuery + " \"Fashion & Style\"";
            }

            if(prefs.getBoolean("check3", false)) {
                filterQuery = filterQuery + " \"Sports\"";
            }

            if (filterQuery != null && !filterQuery.isEmpty()) {
                params.put("fq", "news_desk:(" + filterQuery + ")");
            }

            loading = true;

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    loading = false;
                    JSONArray articleResults = null;
                    try {
                        articleResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJSONArray(articleResults));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    loading = false;
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else {
            final Context context = this;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("");
            alertDialog
                    .setMessage("NO INTERNET CONNECTION")
                    .setCancelable(true)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog   dialog = alertDialog.create();

            dialog.show();

        }
    }

    @Override
    public void onFragmentInteraction(int year, int month, int day, int sortOrder, boolean ch1, boolean ch2, boolean ch3) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("nytimes", Context.MODE_PRIVATE).edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", day);
        editor.putInt("sort", sortOrder);
        editor.putBoolean("check1", ch1);
        editor.putBoolean("check2", ch2);
        editor.putBoolean("check3", ch3);
        editor.commit();

        articles.clear();
        adapter.notifyDataSetChanged();

        onArticleSearch(0);
    }


}
