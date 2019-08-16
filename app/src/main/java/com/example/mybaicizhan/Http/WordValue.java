package com.example.mybaicizhan.Http;

import android.provider.Settings;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WordValue {
    private String word=null,psE=null,pronE=null,psA=null,pronA=null,pos=null,orig=null,trans=null;

    public ArrayList<String> posArrayList = null;
    public ArrayList<Sentence> sentenceArrayList = null;


    public WordValue(String word, String psE, String pronE, String psA,
                     String pronA, String pos,String orig,String trans) {
        this.word = ""+word;
        this.psE = ""+psE;
        this.pronE = ""+pronE;
        this.psA = ""+psA;
        this.pronA = ""+pronA;
        this.pos = pos;
        this.orig = orig;
        this.trans = trans;
    }

    public WordValue() {
        this.word = "";      //防止空指针异常
        this.psE = "";
        this.pronE = "";
        this.psA = "";
        this.pronA = "";
        this.pos = "";
        this.orig = "";
        this.trans = "";
    }

    /*
    static public class Pos{
        private String pos = "";
        private String acceptation = "";
        public Pos(){}
        public Pos(String p,String a){
            pos = p;
            acceptation = a;
        }

        public String getAcceptation() { return acceptation; }
        public String getPos() { return pos; }
        public void setPos(String p){pos = p;}
        public void setAcceptation(String a){acceptation = a;}
    }
    */

    static public class Sentence{
        private String orig = "";
        private String trans = "";
        public Sentence(){}
        public Sentence(String o,String t){
            orig = o;
            trans = t;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }
        public void setTrans(String trans){
            this.trans = trans;
        }
        public String getOrig(){
            return orig;
        }
        public String getTrans(){
            return trans;
        }
    }

    public ArrayList<String> getPosArrayList(){
        if(posArrayList == null){
            posArrayList = new ArrayList<>();
            String[] array = pos.split("\n");
            for(String item : array){
                posArrayList.add(item);
            }
        }
        return posArrayList;
    }

    public ArrayList<Sentence> getSentenceArrayList(){
        if(sentenceArrayList == null){
            sentenceArrayList = new ArrayList<>();
            String[] origArray = orig.split("@#@");
            String[] transArray = trans.split("@#@");
            for(int i = 0;i < origArray.length;i++){
                sentenceArrayList.add(new Sentence(origArray[i],transArray[i]));
            }
        }
        return sentenceArrayList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPsE() {
        return psE;
    }

    public void setPsE(String psE) {
        this.psE = psE;
    }

    public String getPronE() {
        return pronE;
    }

    public void setPronE(String pronE) {
        this.pronE = pronE;
    }

    public String getPsA() {
        return psA;
    }

    public void setPsA(String psA) {
        this.psA = psA;
    }

    public String getPronA() {
        return pronA;
    }

    public void setPronA(String pronA) {
        this.pronA = pronA;
    }

    public String getPos(){
        return pos;
    }

    public void setPos(String p){
        pos = p;
    }

    public String getOrig(){
        return orig;
    }

    public void setOrig(String o){
        orig = o;
    }

    public String getTrans(){
        return trans;
    }

    public void setTrans(String t){
        trans = t;
    }

    public void printInfo(){
        System.out.println(this.word);
        System.out.println(this.psE);
        System.out.println(this.pronE);
        System.out.println(this.psA);
        System.out.println(this.pronA);
        System.out.println(this.pos);
        System.out.println(this.orig);
        System.out.println(this.trans);
        /*
        if(posArrayList != null){
            int len = posArrayList.size();
            for(int i = 0;i < len;i++){
                System.out.println(posArrayList.get(i).getPos()+posArrayList.get(i).getAcceptation());
            }
        }
        if(sentenceArrayList != null){
            int len =sentenceArrayList.size();
            for(int i = 0;i < len;i++){
                System.out.println(sentenceArrayList.get(i).getOrig()+sentenceArrayList.get(i).getTrans());
            }
        }
        */
    }


}
