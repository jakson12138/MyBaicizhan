package com.example.mybaicizhan.XML;

import android.util.Log;

import com.example.mybaicizhan.Http.WordValue;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class XMLReader {

    static private final String TAG = "XMLReader";

    public XMLReader(){}

    public WordValue parseXMLWithPullForWord(String xmlData){
        WordValue wordValue = null;

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            String word=null,psE=null,pronE=null,psA=null,pronA=null,
                    pos="",orig="",trans="";
            //ArrayList<WordValue.Pos> posArrayList = new ArrayList<>();
            //ArrayList<WordValue.Sentence> sentenceArrayList = new ArrayList<>();
            boolean isE = true; //英式发音

            while(eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:{
                        if("key".equals(nodeName)){
                            word = xmlPullParser.nextText();
                        }
                        else if("ps".equals(nodeName)){
                            if(isE){
                                psE = xmlPullParser.nextText();
                            }
                            else{
                                psA = xmlPullParser.nextText();
                            }
                        }
                        else if("pron".equals(nodeName)){
                            if(isE){
                                pronE = xmlPullParser.nextText();
                                isE = false;
                            }
                            else{
                                pronA = xmlPullParser.nextText();
                            }
                        }
                        else if("pos".equals(nodeName)){
                            pos += xmlPullParser.nextText();
                        }
                        else if("acceptation".equals(nodeName)){
                            pos += xmlPullParser.nextText() + "\n";
                        }
                        else if("orig".equals(nodeName)){
                            orig += xmlPullParser.nextText() + "@#@";
                        }
                        else if("trans".equals(nodeName)){
                            trans += xmlPullParser.nextText() + "@#@";
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if("dict".equals(nodeName)){
                            if(psE == null || pronE == null || psA == null || pronA == null){
                                return null;
                            }
                            wordValue = new WordValue(word,psE,pronE,psA,pronA,pos,orig,trans);
                            wordValue.printInfo();
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return wordValue;
    }

}
