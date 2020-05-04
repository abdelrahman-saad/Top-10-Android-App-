package com.example.top10downloader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplication {
    private static final String TAG = "ParseApplication";
    private ArrayList<FeedEntry> applications;

    public ParseApplication() {
        this.applications = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public boolean parse(String XmlData) {
        boolean status = true;
        FeedEntry CurrentRecord = null;
        boolean inEntry = false;
        String textVal = "";
        boolean gotImage = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(XmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            CurrentRecord = new FeedEntry();
                        } else if (("image".equalsIgnoreCase(tagName)) && inEntry) {
                            String imageRes = xpp.getAttributeValue(null, "height");
                            if (imageRes != null) {
                                gotImage = "53".equalsIgnoreCase(imageRes);
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textVal = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("Entry".equalsIgnoreCase(tagName)) {
                                applications.add(CurrentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName))
                                CurrentRecord.setName(textVal);
                            else if ("artist".equalsIgnoreCase(tagName))
                                CurrentRecord.setArtist(textVal);
                            else if ("releaseDate".equalsIgnoreCase(tagName))
                                CurrentRecord.setReleaseDate(textVal);
                            else if ("summary".equalsIgnoreCase(tagName))
                                CurrentRecord.setSummary(textVal);
                            else if ("image".equalsIgnoreCase(tagName)) {
                                if (gotImage)
                                    CurrentRecord.setImageURL(textVal);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
