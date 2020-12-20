package com.kdron.hungamadatabase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kdron.adapter.MovieListAdapter;
import com.kdron.models.MovieDetail;
import com.kdron.network.ConnectionUtil;
import com.kdron.network.VollyResponse;
import com.kdron.utils.MovieSeachFilter;
import com.kdron.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ConstraintLayout activityRoot;
    SearchView sv;
    RecyclerView rv;
    ProgressDialog progressDialog;
    ImageView iv;
    Context context = this;
    public static final String TAG = MainActivity.class.getName();
    ArrayList<MovieDetail> listMovie = new ArrayList<>();
    MovieListAdapter movieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityRoot = findViewById(R.id.activityRoot);
        sv = findViewById(R.id.sb);
        rv = findViewById(R.id.rv);
        iv = findViewById(R.id.iv);
        setProgressDialog();


        getMovieList();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)) {
                    ArrayList<MovieDetail> filteredList = new ArrayList();
                    for (MovieDetail md : listMovie) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (MovieSeachFilter.isMovieMatch(newText,md.title)) {
                            filteredList.add(md);
                        }
                    }
                    //update recyclerview
                    if (movieListAdapter != null) {
                        movieListAdapter.updateList(filteredList);
                    }

                }else
                {
                    if (movieListAdapter != null) {
                        movieListAdapter.updateList(listMovie);
                    }
                }


                return false;
            }
        });


    }




    private void setProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
    }

    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }

    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }


    private void getMovieList() {
        showProgress();

        ConnectionUtil.callVolleyGetRequest(ConnectionUtil.MOVIE_LIST + "?api_key=" + ConnectionUtil.API, context, new VollyResponse() {
            @Override
            public void onReceive(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectMovie = jsonArray.getJSONObject(i);
                        MovieDetail movieDetail = new MovieDetail();
                        movieDetail.adult = jsonObjectMovie.getBoolean("adult");
                        movieDetail.backdrop_path = jsonObjectMovie.getString("backdrop_path");
                        movieDetail.id = jsonObjectMovie.getInt("id");
                        movieDetail.original_language = jsonObjectMovie.getString("original_language");
                        movieDetail.original_title = jsonObjectMovie.getString("original_title");
                        movieDetail.overview = jsonObjectMovie.getString("overview");
                        movieDetail.popularity = jsonObjectMovie.getInt("popularity");
                        movieDetail.release_date = jsonObjectMovie.getString("release_date");
                        movieDetail.title = jsonObjectMovie.getString("title");
                        movieDetail.video = jsonObjectMovie.getBoolean("video");
                        movieDetail.vote_average = jsonObjectMovie.getInt("vote_average");
                        movieDetail.vote_count = jsonObjectMovie.getInt("vote_count");
                        movieDetail.poster_path = jsonObjectMovie.getString("poster_path");
                        listMovie.add(movieDetail);
                    }


                    rv.setLayoutManager(new LinearLayoutManager(context));
                    movieListAdapter = new MovieListAdapter(listMovie, context);
                    rv.setAdapter(movieListAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                }

                hideProgress();
            }

            @Override
            public void onError() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                hideProgress();

            }

            @Override
            public void onNoInternet() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.noInternate));
                hideProgress();

            }
        });
    }


}