package br.com.autogeral.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    public void testParse() {
        String json = "{\"Token\":\"abc123\"}";
        Gson gson = new GsonBuilder().create();
        LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);
        assertNotNull(loginResponse);
        assertEquals("abc123", loginResponse.getToken());
    }

}