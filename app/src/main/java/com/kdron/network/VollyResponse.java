package com.kdron.network;

public interface VollyResponse {

    void onReceive(String response);

    void onError();

    void onNoInternet();

}

