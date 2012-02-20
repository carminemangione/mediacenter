package com.mangione.mediacenter.model.imdb;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * User: carminemangione
 * Date: Dec 27, 2009
 * Time: 3:53:05 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class IMDBSearchResult {
    private String movieName;
    private String description;
    private String rating;


    public IMDBSearchResult(final VideoFile videoFile, final IMDBSearchResultListener listener) throws Exception {
        new Thread() {
            public void run() {
                try {
                    if (videoFile.getImdbFile().exists()) {
                        Properties properties = new Properties();
                        FileReader fileReader = new FileReader(videoFile.getImdbFile());
                        properties.load(fileReader);
                        IMDBSearchResult.this.movieName = properties.getProperty("Title", "no title");
                        IMDBSearchResult.this.description = properties.getProperty("Description", "no description");
                        IMDBSearchResult.this.rating = properties.getProperty("Rating", "no rating");
                        fileReader.close();

                    } else {
                        HttpUnitOptions.setScriptingEnabled(false);
                        WebConversation wc = new WebConversation();
                        String request = "http://www.imdb.com/find?s=all&q=" + videoFile.getIMDBvideoName();
                        System.out.println("request = " + request);
                        WebResponse resp = wc.getResponse(request);
                        System.out.println("resp = " + resp.getText());
                        Document document = resp.getDOM();
                        IMDBSearchResult.this.movieName = getContentForAttribute(document, "title");
                        IMDBSearchResult.this.description = getContentForAttribute(document, "description");
                        IMDBSearchResult.this.rating = getRating(document);
                        writeProperties(videoFile);
                    }
                    listener.searchFinished(IMDBSearchResult.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public String getMovieName() {
        return movieName;
    }


    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Title: " + movieName + "\n" + "Description: " + "\n" + "Rating: " + rating;
    }

    private void writeProperties(VideoFile videoFile) throws IOException {
        Properties properties = new Properties();
        properties.put("Description", description);
        properties.put("Title", movieName);
        properties.put("Rating", rating);
        BufferedWriter bw = new BufferedWriter(new FileWriter(videoFile.getImdbFile()));
        properties.store(bw, null);
        bw.close();
    }

    private String getContentForAttribute(Document document, String attributeName) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile("//*[@name='" + attributeName + "']");
        NodeList nodeset = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        NamedNodeMap map = nodeset.item(0).getAttributes();
        return map.getNamedItem("content").getNodeValue();
    }


    private String getRating(Document document) throws Exception {

//        XPath xpath = XPathFactory.newInstance().newXPath();
//        XPathExpression expr = xpath.compile("//*[@class='general rating']/*[@class='meta']/B");
//        NodeList nodeset = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
//        Node node = nodeset.item(0);
//        Node node1 = node.getChildNodes().item(0);
//        if (node1 != null)
//            return node1.getNodeValue();
//        else
        return "";
    }

}
