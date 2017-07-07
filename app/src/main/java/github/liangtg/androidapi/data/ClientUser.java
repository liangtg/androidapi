package github.liangtg.androidapi.data;

import android.text.TextUtils;

import com.github.liangtg.base.util.PreferenceData;
import com.google.gson.Gson;

/**
 * Created by liangtg on 17-7-4.
 */

public class ClientUser {
    private static final String KEY_USER = "client_user";
    private static LoginResponse curUser;

    public static void init() {
        String text = PreferenceData.getInstance().getString(KEY_USER, "");
        if (!TextUtils.isEmpty(text)) {
            curUser = new Gson().fromJson(text, LoginResponse.class);
        }
    }

    public static void setCurUser(LoginResponse response) {
        curUser = response;
        if (null != curUser) {
            PreferenceData.getInstance().edit().putString(KEY_USER, new Gson().toJson(response)).commit();
        } else {
            PreferenceData.getInstance().edit().remove(KEY_USER).commit();
        }
    }

}
