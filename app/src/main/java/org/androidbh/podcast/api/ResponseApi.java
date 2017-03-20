package org.androidbh.podcast.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by felipearimateia on 20/03/17.
 */

public class ResponseApi<T> {

    private int count;
    @SerializedName("items")
    private List<T> items;
    private String error;
    private String result;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
