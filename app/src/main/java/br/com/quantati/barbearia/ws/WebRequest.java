package br.com.quantati.barbearia.ws;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import br.com.quantati.barbearia.model.Agendamento;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by carlos on 22/05/17.
 */

public class WebRequest {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String CHAVE = "cragouveia";

    public static final String BASE_URL = "https://barbearia.herokuapp.com/";
    private static final int TIMEOUT = 5000;

    public static final String SAVE = "save";
    public static final String DELETE_BY_ID = "deleteById/%d";
    public static final String FIND_BY_CHAVE = "findByChave/%s";
    public static final String IMAGE = "image/%s/%d";


    private String sendRequest(URL url, String method, String jsonString) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Accept", "application/json");
        if (method.equalsIgnoreCase("GET")) builder.get();
        else builder.post(RequestBody.create(JSON, jsonString));
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.i(String.format("sendRequest(%s)", request.url().toString()), "Error closing InputStream");
        }

        return null;
    }

    public String save(Agendamento agendamento) {
        try {
            agendamento.setChave(CHAVE);
            if (agendamento.isNovo()) agendamento.setId(0L);
            URL url = new URL(BASE_URL + SAVE);
            return sendRequest(url, "POST", new ObjectMapper().writeValueAsString(agendamento));
        }
        catch (Exception e) {
            return null;
        }
    }

    public String list() {
        try {
            URL url = new URL(BASE_URL + String.format(FIND_BY_CHAVE,  CHAVE));
            return sendRequest(url, "GET", null);
        }
        catch (Exception e) {
            return null;
        }
    }

    public String delete(long id) {
        try {
            URL url = new URL(BASE_URL + String.format(DELETE_BY_ID, id));
            return sendRequest(url, "GET", null);
        }
        catch (Exception e) {
            return null;
        }
    }


}
