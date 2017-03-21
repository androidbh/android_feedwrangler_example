package org.androidbh.podcast.api;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by felipearimateia on 3/21/17.
 */

public class ErrorUtils {

    public static ResponseApi parseError(Response<?> response) {

        Converter<ResponseBody, ResponseApi> converter =
                RestAdpter.retrofit()
                        .responseBodyConverter(ResponseApi.class, new Annotation[0]);

        ResponseApi error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ResponseApi();
        }

        return error;
    }
}
