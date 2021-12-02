package com.example.thecollectivediet;

import android.content.Context;

import com.example.thecollectivediet.JSON_Marshall_Objects.FoodResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JSONSerializer {

    private String fileName;
    private Context context;

    public JSONSerializer(String fileName, Context context){
        this.fileName = fileName;
        this.context = context;
    }

    //save list for edit meal list of meals
    public void save(List<FoodResult> list) throws IOException, JSONException {

        //Make an array in JSON format
        JSONArray jsonArray = new JSONArray();

        //load with list of FoodResult(s)
        for(FoodResult n : list){
            jsonArray.put(n.convertToJSON());
        }

        //Now write to private disk space of our app
        Writer writer = null;
        try{
            OutputStream out = context.openFileOutput(fileName, context.MODE_PRIVATE);

            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    //load list used in edit meals frag
    public ArrayList<FoodResult> load() throws IOException, JSONException{

        ArrayList<FoodResult> list = new ArrayList<FoodResult>();

        BufferedReader reader = null;
        try{
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null){
                jsonString.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for(int i = 0; i < jsonArray.length(); i++)
            {
                list.add( new FoodResult(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //only happens when list is fresh
        } finally {
            if (reader != null){
                reader.close();
            }
        }

        return list;
    }
}
