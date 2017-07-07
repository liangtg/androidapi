package github.liangtg.androidapi.data;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by liangtg on 17-7-4.
 */

public interface DataService {

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(@Field("account") String account, @Field("pass") String pass);

}
