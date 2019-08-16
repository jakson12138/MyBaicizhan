package com.example.mybaicizhan.Http;

public class SentencePerDay {

    private String sid;
    private String tts;
    private String content;
    private String fenxiang_img;
    private String picture1;
    private String picture2;

    public String getContent() {
        return content;
    }
    public String getTts(){
        return tts;
    }
    public String getFenxiang_img(){
        return fenxiang_img;
    }
    public void setTts(String t){
        tts = t;
    }
    public void setContent(String c){
        content = c;
    }
    public void setFenxiang_img(String f){
        fenxiang_img = f;
    }
    public String getPicture1(){
        return picture1;
    }
    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }
    public String getPicture2(){
        return picture2;
    }
    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getSid(){
        return sid;
    }
    public void setSid(String s){
        sid = s;
    }

    public void printInfo(){
        System.out.println(this.tts);
        System.out.println(this.content);
        System.out.println(this.fenxiang_img);
    }
}
