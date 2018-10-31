package com.texaconnect.texa.network;

import com.texaconnect.texa.BuildConfig;

import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FakeInterceptor implements Interceptor {
    // FAKE RESPONSES.
    private final static String TEACHER_ID_1 = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}";
    private static final int HTTP_UNAUTHORIZED_CODE = 401;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if(BuildConfig.DEBUG) {
            String responseString;
            // Get Request URI.
            final URI uri = chain.request().url().uri();

            String url = uri.getPath();

            if(url.contains("get_product_list")){
//                responseString = DummyRepo.getDummyProduct();
                responseString = "";
            }else {
                responseString = "";
            }

            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        }
        else {
            response = chain.proceed(chain.request());
        }

        return response;
    }
}
