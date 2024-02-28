package br.com.autogeral;

import javax.net.ssl.*;

import br.com.autogeral.auth.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class Main {

    private static final String URL_LOGIN = "https://cloud-api.questor.com.br/home/LoginModulo";
    private static final String URL_LOGOUT = "https://cloud-api.questor.com.br/home/Logout";

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL_LOGIN).newBuilder();
        urlBuilder.addQueryParameter("LogonName", "questor@AUTO.GERAL");
        urlBuilder.addQueryParameter("PlainPassword", "masterkey");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        String token = null;
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println(responseBody);
            if (response.code() != 200) {
                System.out.println("Erro ao fazer login");
                return;
            }

            Gson gson = new GsonBuilder().create();
            LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);


            token = loginResponse.getToken();
            System.out.println("Value: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlBuilder = HttpUrl.parse(URL_LOGIN).newBuilder();
        urlBuilder.addQueryParameter("LogonName", "questor@AUTO.GERAL");
        urlBuilder.addQueryParameter("PlainPassword", "masterkey");
        url = urlBuilder.build().toString();

        request = new Request.Builder()
                .url(url)
                .build();


    }
}