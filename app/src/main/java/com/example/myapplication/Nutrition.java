package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Nutrition extends AppCompatActivity {

    Button btn_Search;
    EditText et_dataInput;
    TextView showInfo;
    String item;
    Integer kcal,protein,fat,carbs,fibre;
    String url;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        btn_Search =findViewById(R.id.btn_SearchID);
        et_dataInput = findViewById(R.id.et_DataInput);
        showInfo = findViewById(R.id.showInfo);
    }

    public void btn_SearchID(View view) {
        String userFoodInput = et_dataInput.getText().toString();
        if (userFoodInput.contains(" ")) {
            String after = userFoodInput.trim().replaceAll(" ", "%20");
            url = "https://api.edamam.com/api/food-database/v2/parser?app_id=bc7b902b&app_key=%201ac553cacc0282856d6d4df80442734a&ingr="+after+"&nutrition-type=cooking" + "Response Body";
        }else {
            url = "https://api.edamam.com/api/food-database/v2/parser?app_id=bc7b902b&app_key=%201ac553cacc0282856d6d4df80442734a&ingr="+userFoodInput+"&nutrition-type=cooking" + "Response Body";
        }

        RequestQueue queue = Volley.newRequestQueue(Nutrition.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Food = response.getJSONArray("parsed");
                    JSONObject firstIndex = Food.getJSONObject(0);
                    JSONObject foodMainIndex = firstIndex.getJSONObject("food");
                    JSONObject nutritionalInformation = foodMainIndex.getJSONObject("nutrients");
                    item = foodMainIndex.getString("label");
                    kcal = nutritionalInformation.getInt("ENERC_KCAL");
                    protein = nutritionalInformation.getInt("PROCNT");
                    fat = nutritionalInformation.getInt("FAT");
                    carbs = nutritionalInformation.getInt("CHOCDF");
                    fibre = nutritionalInformation.getInt("FIBTG");
                    showInfo.setText(""+item+" \n " +"Kcal: "+ kcal+" \n "+"Protein: "+ protein+" \n "+"fat: "+ fat+" \n "+"carbs: "+ carbs+" \n "+"fibre: "+ fibre);
                } catch (JSONException e) {
                    Toast.makeText(Nutrition.this, "Food can't be found, please try something else!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(Nutrition.this, "Please enter a food item in the box above. ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}