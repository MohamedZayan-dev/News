package com.example.android.news;

public class NewsInfoActivity {
    private String mSection;
    private String mTitle;
    private String mDate;
    private String mUrl;

    public NewsInfoActivity(String Section, String Title, String Date, String Url){
        mSection=Section;
        mTitle=Title;
        mDate=Date;
        mUrl=Url;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmTitle() {
        return mTitle;
    }
public String getmUrl(){
        return mUrl;
}
}
