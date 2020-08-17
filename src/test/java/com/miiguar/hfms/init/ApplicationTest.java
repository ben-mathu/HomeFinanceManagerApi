package com.miiguar.hfms.init;

import com.miiguar.hfms.data.daraja.models.AccessToken;
import org.junit.Test;

/**
 * @author bernard
 */
public class ApplicationTest {
    public static final String TAG = ApplicationTest.class.getSimpleName();

    @Test
    public void getMpesaApiAccessToken_Returns_Access_Token() {
        Application app = new Application();
        AccessToken accessToken;
        try {
//            accessToken = app.fetchDarajaAccessToken();
//            assertNotEquals(accessToken.getAccessToken(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}