package algonquin.cst2335.zhao0251;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.zhao0251.databinding.ActivityMainBinding;

/** This page is the first page of the application.
 * @author yingyi
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    protected String cityName;
    protected RequestQueue queue = null;
    Bitmap image;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            queue = Volley.newRequestQueue(this);
            ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

            setContentView(binding.getRoot());

            binding.getForecast.setOnClickListener(click ->{
                cityName = binding.editText.getText().toString();
                String stringURL = null;
                try {
                    stringURL = new StringBuilder()
                            .append("https://api.openweathermap.org/data/2.5/weather?q=")
                            .append(URLEncoder.encode(cityName, "UTF-8"))
                            .append("&appid=7e943c97096a9784391a981c4d878b22&units=metric").toString();
                } catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,(response)->{
                    try{
                        JSONObject coord = response.getJSONObject("coord");
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);

                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");

                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");

                        //String pathname = getFilesDir() + "/" + iconName + ".png";
                        //File file = new File(pathname);
                        //if(file.exists()){
                       //     image = BitmapFactory.decodeFile(pathname);
                       // }else{
                        String imageUrl =  "http://openweathermap.org/img/w/" + iconName + ".png";
                        ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    try{
                                        image = bitmap;
                                        image.compress(Bitmap.CompressFormat.PNG, 100,
                                                MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                        binding.icon.setImageBitmap(image);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } //end of onResponse
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) ->{
                                //Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                            });
                            queue.add(imgReq);
                        //}//end of else
                        runOnUiThread(()->{
                            binding.temp.setText("The current temperature is " + current);
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.minTemp.setText("The min temperature is " + min);
                            binding.minTemp.setVisibility(View.VISIBLE);
                            binding.maxTemp.setText("The max temperature is " + max);
                            binding.maxTemp.setVisibility(View.VISIBLE);
                            binding.humidity.setText("The humidity is " + humidity + "%");
                            binding.humidity.setVisibility(View.VISIBLE);
                            binding.icon.setImageBitmap(image);
                            binding.icon.setVisibility(View.VISIBLE);
                            binding.description.setText(description);
                            binding.description.setVisibility(View.VISIBLE);
                        });
                    } catch (JSONException e){
                        throw new RuntimeException(e);
                    }
                }, (error) ->{
                });
                queue.add(request);
            });
        }
    }