package com.prestigepoint.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.prestigepointnew.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aartek on 19/11/16.
 */

public class CourseCategoryTitleTopicActivity extends Activity
{

    ListView CourseCategoryTitleTopicList;
    ProgressBar progressBar;
    ArrayList<GetSet> courseCategoryTitleTopicList = new ArrayList<GetSet>();
    GetSet data;

    String COURSE_CATEGORY_TITLE_TOPIC = "http://admin.prestigepoint.in/getCourseCategoryTitleTopic.do";
    GetSet model;
    CourseCategoryTitleTopicAdapter courseCatgryTitleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.course_category_title_topic_activity);

        data = (GetSet) getIntent().getSerializableExtra("model");

        CourseCategoryTitleTopicList = (ListView)findViewById(R.id.CourseCategoryTitleTopicList);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getALLCourseCategoryTitleTopic();

        CourseCategoryTitleTopicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                GetSet getData = (GetSet) parent.getItemAtPosition(position);

                Intent intentQuestn = new Intent(view.getContext(), CourseTitleSubCategoryActivity.class);
                intentQuestn.putExtra("model", getData);
                startActivity(intentQuestn);

            }
        });

    }


    private void getALLCourseCategoryTitleTopic() {

        HashMap<String, String> params1 = new HashMap<String, String>();

        params1.put("courseTitleId", data.getCourseTitleId());
        System.out.println("=========" + data.getCourseTitleId());

        JsonObjectRequest req = new JsonObjectRequest(COURSE_CATEGORY_TITLE_TOPIC, new JSONObject(params1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("<><><>" + response.toString());

                        try {

                            String respon = response.getString("response");

                            if (respon.equals("200")) {

                                JSONArray jArray = response.getJSONArray("DATA");
                                if (jArray.length() != 0) {
                                    for (int i = 0; i < jArray.length(); i++) {

                                        JSONObject jObj = jArray.getJSONObject(i);

                                        String strTitleCategoryTopicId = jObj.getString("titleCategoryTopicId");
                                        System.out.println("====<><><>==========" + strTitleCategoryTopicId);
                                        String strTitleCategoryTopicName = jObj.getString("titleCategoryTopicName");
                                        System.out.println("====<><><>==========" + strTitleCategoryTopicName);

                                        model = new GetSet();
                                        model.setStrTitleCategoryTopicId(strTitleCategoryTopicId);
                                        model.setStrTitleCategoryTopicName(strTitleCategoryTopicName);
                                        courseCategoryTitleTopicList.add(model);

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Data is Not Available!!", Toast.LENGTH_LONG).show();

                                }

                                courseCatgryTitleAdapter = new CourseCategoryTitleTopicAdapter(CourseCategoryTitleTopicActivity.this, courseCategoryTitleTopicList);
                                CourseCategoryTitleTopicList.setAdapter(courseCatgryTitleAdapter);
                                progressBar.setVisibility(View.GONE);



                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                System.out.println("<><><>" + error.toString());


            }
        });
        int socketTimeout = 60000;
        req.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(req);



    }


    class CourseCategoryTitleTopicAdapter extends BaseAdapter {

        private Context context;

        ArrayList<GetSet> CourseCategryTitleTopicList;
        GetSet data;
        Activity activity;


        public CourseCategoryTitleTopicAdapter(Context context, ArrayList<GetSet> CourseCategryTitleTopicList) {
            this.context = context;
            this.CourseCategryTitleTopicList = CourseCategryTitleTopicList;
        }

        @Override
        public int getCount() {
            return CourseCategryTitleTopicList.size();
        }

        @Override
        public Object getItem(int position) {
            return CourseCategryTitleTopicList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return CourseCategryTitleTopicList.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.course_category_title_topic_adapter, null);
            }

            TextView coursecatgryTitleTopicName = (TextView) convertView.findViewById(R.id.tvtitleCategoryTopicName);
            data = CourseCategryTitleTopicList.get(position);
            coursecatgryTitleTopicName.setText(data.getStrTitleCategoryTopicName());


            return convertView;
        }
    }
}
