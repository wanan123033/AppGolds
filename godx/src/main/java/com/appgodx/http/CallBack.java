package com.appgodx.http;

import java.io.IOException;


public interface CallBack<T> {
    void onFailure(Call<T> call, IOException e);
    void onResponse(Call<T> call, T response) throws IOException;
}
