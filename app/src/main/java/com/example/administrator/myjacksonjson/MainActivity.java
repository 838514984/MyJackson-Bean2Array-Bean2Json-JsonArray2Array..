package com.example.administrator.myjacksonjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Person person=new Person();
        person.age=23;
        person.name="qiaohong";
        ArrayList<String> s=new ArrayList<>();
        s.add("2332");
        s.add("wqh");
        person.angle=s;
        JSONArray jsonArray=new JSONArray();
        ObjectMapper objectMapper=new ObjectMapper();

        //将JavaBean转换为数组。
        try {
            String json=objectMapper.writeValueAsString(person);
            JsonNode jsonNode=objectMapper.readTree(json);
            Iterator<String> it= jsonNode.fieldNames();
            while(it.hasNext()){
                String fieldName = it.next();
                JsonNode node = jsonNode.path(fieldName);
                if (node.isNumber()) {
                    jsonArray.put(node.numberValue());
                }
                else if(node.isTextual()){
                    jsonArray.put(node.asText());//toString()有双引号，textvalue()有可能是null
                }
                else if(node.isBoolean()){
                    jsonArray.put(node.booleanValue());
                }
                else if (node.isArray())
                {
                    jsonArray.put(new JSONArray(node.toString()));
                }else if(node.isNull()){
                    jsonArray.put(null);
                }else{
                    JSONObject json1 = new JSONObject(objectMapper.writeValueAsString(node));
                    jsonArray.put(json1);
                }
            }

            Log.e("Xxxx Array: ",jsonArray.toString());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //将json转换成Bean Array
        String peoples="[{\"name\":\"qiaohong\",\"age\":23,\"angle\":[\"2332\",\"wqh\"]},{\"name\":\"巧红\",\"age\":23,\"angle\":[\"小巧\",\"小红\"]}]";
        ObjectMapper objectMapper2=new ObjectMapper();
        try {
            PersonResponse persons=objectMapper2.readValue(peoples,PersonResponse.class);
            for (int i=0;i<persons.size();i++){
                Log.e("xxx",persons.get(i).name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //注解，序列化顺序，注意这里是内部类，所以要用static，不然外部无法到达，转换出错。
    @JsonPropertyOrder({"name","age","angle"})
    public static class Person {

        public String name;
        public int age;
        public ArrayList<String> angle;
    }
    //这种适应直接返回的是数组的内容。
   public static class PersonResponse  extends ArrayList<Person> implements Serializable {

    }
}
