package com.kdron.hungamadatabase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kdron.adapter.ItemListAdapter;
import com.kdron.models.Items;
import com.kdron.models.MovieDetail;
import com.kdron.network.ConnectionUtil;
import com.kdron.network.VollyResponse;
import com.kdron.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {


    ImageView imagePoster;
    TextView txtMovieName;
    TextView txtLanguage;
    TextView txtGenre;
    TextView txtDetails;
    androidx.recyclerview.widget.RecyclerView rvCast;
    ProgressBar progressCast;
    androidx.recyclerview.widget.RecyclerView rv_crew;
    ProgressBar progressCrew;
    androidx.recyclerview.widget.RecyclerView rv_similar_movies;
    ProgressBar progressMovies;

    MovieDetail movieDetail;
    ProgressDialog progressDialog;
    Context context = this;

    public static final String INTENT_MOVIE_DETAILS = "intent_movie_details";
    public static final String TAG = MovieDetailActivity.class.getName();
    ArrayList<Items> listCast = new ArrayList<>();
    ArrayList<Items> listCrew = new ArrayList<>();
    ArrayList<Items> listSimilarMovies = new ArrayList<>();
    ImageView imgYoutube;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        movieDetail = (MovieDetail) getIntent().getSerializableExtra(INTENT_MOVIE_DETAILS);
        setProgressDialog();

        imagePoster = findViewById(R.id.image_poster);
        txtMovieName = findViewById(R.id.txt_movie_name);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtGenre = findViewById(R.id.txtGenre);
        txtDetails = findViewById(R.id.txtDetails);
        rvCast = findViewById(R.id.rvCast);
        progressCast = findViewById(R.id.progressCast);
        rv_crew = findViewById(R.id.rv_crew);
        progressCrew = findViewById(R.id.progressCrew);
        rv_similar_movies = findViewById(R.id.rv_similar_movies);
        progressMovies = findViewById(R.id.progressMovies);
        imgYoutube = findViewById(R.id.imgYoutube);


        txtMovieName.setText(movieDetail.title);
        txtDetails.setText(movieDetail.overview);



        getMovieDetails();
        getCastDetails();
        getSimilarMovies();
        getVideoDetail();

        Glide.with(context).
                load(ConnectionUtil.IMG_URL + movieDetail.poster_path)
                .placeholder(R.drawable.ic_place_holder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePoster);
    }

    private void getMovieDetails() {
        showProgress();

        ConnectionUtil.callVolleyGetRequest(ConnectionUtil.MOVIE_DETAILS + "/" + movieDetail.id + "?api_key=" + ConnectionUtil.API, context, new VollyResponse() {
            @Override
            public void onReceive(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray("genres");
                    String geners = "";
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObjectGeners = jsonArray.getJSONObject(i);
                        geners = geners + jsonObjectGeners.getString("name");
                        if (i < jsonArray.length()) {
                            geners = geners + ",";
                        }
                    }
                    txtGenre.setText(geners);

                    JSONArray jsonArrayLanguage = jsonObject.getJSONArray("spoken_languages");
                    String language = "";
                    for (int i = 0; i < jsonArrayLanguage.length(); i++) {
                        JSONObject jsonObjectLanguage = jsonArrayLanguage.getJSONObject(i);
                        language = jsonObjectLanguage.getString("english_name");

                        if (i < jsonArrayLanguage.length()) {
                            language = language + ",";
                        }
                    }

                    txtLanguage.setText(language);

                    getCastDetails();


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


    private void getCastDetails() {

        progressCast.setVisibility(View.VISIBLE);
        progressCrew.setVisibility(View.VISIBLE);

        ConnectionUtil.callVolleyGetRequest(ConnectionUtil.MOVIE_DETAILS + "/" + movieDetail.id + "/credits?api_key=" + ConnectionUtil.API, context, new VollyResponse() {
            @Override
            public void onReceive(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArrayCast = jsonObject.getJSONArray("cast");
                    JSONArray jsonArrayCrew = jsonObject.getJSONArray("crew");

                    listCast.clear();
                    for (int i = 0; i < jsonArrayCast.length(); i++) {
                        JSONObject jsonObjectCast = jsonArrayCast.getJSONObject(i);
                        Items items = new Items();


//                        cast.adult = jsonObjectCast.getBoolean("adult");
//                        cast.gender = jsonObjectCast.getInt("gender");
                        items.id = jsonObjectCast.getInt("id");
//                        cast.known_for_department = jsonObjectCast.getString("known_for_department");
                        items.name = jsonObjectCast.getString("name");
//                        cast.original_name = jsonObjectCast.getString("original_name");
//                        cast.popularity = jsonObjectCast.getDouble("popularity");
                        items.poster = jsonObjectCast.getString("profile_path");
//                        cast.cast_id = jsonObjectCast.getInt("cast_id");
//                        cast.character = jsonObjectCast.getString("character");
//                        cast.credit_id = jsonObjectCast.getString("credit_id");
//                        cast.order = jsonObjectCast.getInt("order");


                        //temp  fix
                        boolean isPresent = false;
                        for (int j = 0; j < listCast.size(); j++) {

                            if (listCast.get(j).id == items.id) {
                                isPresent = true;
                            }
                        }

                        if (!isPresent) {
                            listCast.add(items);
                        }

                    }

                    listCrew.clear();

                    for (int i = 0; i < jsonArrayCrew.length(); i++) {
                        JSONObject jsonObjectCrew = jsonArrayCrew.getJSONObject(i);
                        Items items = new Items();
                        items.name = jsonObjectCrew.getString("name");
                        items.poster = jsonObjectCrew.getString("profile_path");

                        //temp  fix
                        boolean isPresent = false;
                        for (int j = 0; j < listCrew.size(); j++) {

                            if (listCrew.get(j).id == items.id) {
                                isPresent = true;
                            }
                        }

                        if (!isPresent) {
                            listCrew.add(items);
                        }


                        //  listCrew.add(items);

                    }


                    rvCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    ItemListAdapter itemListAdapterCast = new ItemListAdapter(listCast, context);
                    rvCast.setAdapter(itemListAdapterCast);


                    rv_crew.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    ItemListAdapter itemListAdapterCrew = new ItemListAdapter(listCrew, context);
                    rv_crew.setAdapter(itemListAdapterCrew);


                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                }
                progressCast.setVisibility(View.GONE);
                progressCrew.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                progressCast.setVisibility(View.GONE);
                progressCrew.setVisibility(View.GONE);


            }

            @Override
            public void onNoInternet() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.noInternate));
                progressCast.setVisibility(View.GONE);
                progressCrew.setVisibility(View.GONE);

            }
        });
    }


    private void getSimilarMovies() {

        progressMovies.setVisibility(View.VISIBLE);

        ConnectionUtil.callVolleyGetRequest(ConnectionUtil.MOVIE_DETAILS + "/" + movieDetail.id + "/similar?api_key=" + ConnectionUtil.API, context, new VollyResponse() {
            @Override
            public void onReceive(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray("results");


                    listSimilarMovies.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCast = jsonArray.getJSONObject(i);
                        Items items = new Items();

                        items.id = jsonObjectCast.getInt("id");
                        items.name = jsonObjectCast.getString("title");
                        items.poster = jsonObjectCast.getString("poster_path");

                        listSimilarMovies.add(items);

                    }


                    rv_similar_movies.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    ItemListAdapter itemListAdapterCrew = new ItemListAdapter(listSimilarMovies, context);
                    rv_similar_movies.setAdapter(itemListAdapterCrew);


                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                }
                progressMovies.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                progressMovies.setVisibility(View.GONE);


            }

            @Override
            public void onNoInternet() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.noInternate));
                progressMovies.setVisibility(View.GONE);


            }
        });
    }

    private void getVideoDetail() {


        ConnectionUtil.callVolleyGetRequest(ConnectionUtil.MOVIE_DETAILS + "/" + movieDetail.id + "/videos?api_key=" + ConnectionUtil.API, context, new VollyResponse() {
            @Override
            public void onReceive(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectVideo = jsonArray.getJSONObject(i);
                        String site = jsonObjectVideo.getString("site");

                        if (site.equalsIgnoreCase("youtube")) {

                            String key = jsonObjectVideo.getString("key");

                            imgYoutube.setVisibility(View.VISIBLE);
                            imagePoster.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));

                                }
                            });

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));
                }
                progressMovies.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.errorMsg));


            }

            @Override
            public void onNoInternet() {
                ToastUtil.toastCenterShort(context, context.getResources().getString(R.string.noInternate));


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


}