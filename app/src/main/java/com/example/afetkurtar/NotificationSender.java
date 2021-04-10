package com.example.afetkurtar;

import android.app.Activity;
import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NotificationSender extends Activity {
    Context context;
    RequestQueue queue;
    String api_key_header_value = "key=AAAA5VKNhzk:APA91bGhZd1yaXmsMFvgpRQFREmNFd92pPnFhdMfQuWe3WBnppWSFtFxZK9QmgLOqVz26gK9U507kZl9M7OAExkjaWJiNLj-NPHtF7JHKNBNFFWndBwvYrx4dcBKVE_chcZroUlqF_Jn";
    String url = "https://fcm.googleapis.com/fcm/send";

    public NotificationSender(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void sendToTeam(String ID,String mesaj){
        JSONObject obj = new JSONObject();
        try {
            obj.put("teamID",ID); //DEGISECEK
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/personnelUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String cevap;
                        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                        try {
                            cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);

                            while (cevap.indexOf(",{") > -1) {
                                list.add(new JSONObject(cevap.substring(0, cevap.indexOf(",{"))));
                                cevap = cevap.substring(cevap.indexOf(",{") + 1);
                            }
                            list.add(new JSONObject(cevap));
                           // getTokensandSend(list,ID,mesaj);
                            addVolunteer(list,ID,mesaj);
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);

    }

    public void addVolunteer(ArrayList<JSONObject> perarr,String ID,String mesaj){
        JSONObject obj = new JSONObject();
        try {
            obj.put("assignedTeamID",ID); //DEGISECEK
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ASAGISI DEGISECEK -- TEST AMACLI YAPILDI

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/volunteerUser/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String cevap;
                        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                        try {
                            cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);

                            while (cevap.indexOf(",{") > -1) {
                                list.add(new JSONObject(cevap.substring(0, cevap.indexOf(",{"))));
                                cevap = cevap.substring(cevap.indexOf(",{") + 1);
                            }
                            list.add(new JSONObject(cevap));
                            getTokensandSend(perarr,list,ID,mesaj);
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);

    }

    public void getTokensandSend(ArrayList<JSONObject> perarr,ArrayList<JSONObject> perarr2,String ID,String mesaj){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, "https://afetkurtar.site/api/users/read.php", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String cevap;
                        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                        try {
                            cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);

                            while (cevap.indexOf(",{") > -1) {
                                list.add(new JSONObject(cevap.substring(0, cevap.indexOf(",{"))));
                                cevap = cevap.substring(cevap.indexOf(",{") + 1);
                            }
                            list.add(new JSONObject(cevap));
                            ArrayList<String> Strarr = new ArrayList<>();
                            /*
                            for(JSONObject x: perarr){
                                for(JSONObject y: list){
                                    if(x.getString("personnelID").equals(y.getString("userID"))){
                                        Strarr.add(y.getString("userToken"));
                                    }
                                }
                            }
                            */
                            for(JSONObject y: list) {
                                boolean check = true;
                                for (JSONObject x : perarr) {
                                    if (x.getString("personnelID").equals(y.getString("userID"))) {
                                        Strarr.add(y.getString("userToken"));
                                        check=false;
                                    }
                                }
                                if(check) {
                                    for (JSONObject x : perarr2) {
                                        if (x.getString("volunteerID").equals(y.getString("userID"))) {
                                            Strarr.add(y.getString("userToken"));
                                        }
                                    }
                                }
                            }
                            for(String str : Strarr){
                                if(str.length()>10) {
                                    if(!str.equals(MainActivity.userInfo.getString("userToken"))) {
                                        sendNotificationWithData("Takım " +ID +" Yeni Mesaj", mesaj, "Yeni Mesaj", "Yeni Mesaj", str);
                                    }
                                }
                            }

                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }



    public void sendNotification(String Title, String Body, String ID){

        JSONObject obj = new JSONObject();
        try {
            obj.put("userID",ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, "https://afetkurtar.site/api/users/search.php", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String cevap;
                        try {
                            cevap = response.getString("records");
                            cevap = cevap.substring(1, cevap.length() - 1);

                            JSONObject tmp = new JSONObject(cevap);
                            System.out.println(response.toString() + "*****************************************");
                            if(tmp.getString("userToken").length()>10 && tmp.getString("userID").equals(ID))
                            sendNotificationWithData(Title,Body,"","",tmp.getString("userToken"));

                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }
    public void sendNotificationWithData(String Ntitle, String Nbody, String Dtitle, String Dbody, String ID){
        try {
            JSONObject data = new JSONObject();

            data.put("title", Ntitle);
            data.put("body", Nbody);

            JSONObject data2 = new JSONObject();

            data2.put("title", Dtitle);
            data2.put("body", Dbody);

            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("data", data2);
            // notification_data.put("to",MainActivity.userInfo.getString("userToken"));
            notification_data.put("to",ID);

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
