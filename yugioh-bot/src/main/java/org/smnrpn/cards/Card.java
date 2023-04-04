package org.smnrpn.cards;

/*
 * This class is used along the Data and the other classes in this package
 * to retrieve the information from the JSON file sent by the Http request.
 * The variables inside these classes represent the inner structure of the file:
 * data[id = ..., name = ..., (other variables inside the Data class), card_images[image_url, image_url_small, image_url_cropped] ...]
 */

import java.util.ArrayList;

public class Card {
    private Data[] data;

    public int getId() {
        return data[0].getId();
    }

    public String getName() {
        return data[0].getName();
    }

    public String getType() {
        return data[0].getType();
    }

    public String getFrameType() {
        return data[0].getFrameType();
    }

    public String getDesc() {
        return data[0].getDesc();
    }

    public int getAtk() {
        return data[0].getAtk();
    }

    public int getDef() {
        return data[0].getDef();
    }

    public int getLevel() {
        return data[0].getLevel();
    }

    public String getRace() {
        return data[0].getRace();
    }

    public String getAttribute() {
        return data[0].getAttribute();
    }
    public String getImage_url_cropped() {
        return data[0].getImage_url_cropped();
    }

    public int getLinkVal() {
        return data[0].getLinkVal();
    }
    public String[] getLinkMarkers() {
        return data[0].getLinkMarkers();
    }

    public int getScale() {
        return data[0].getScale();
    }

    public float getCardmarketPrices() {
        return data[0].getCardmarketPrice();
    }

    public float getTCGPlayerPrices() {
        return data[0].getTCGPlayerPrice();
    }

    public float getEbayPrices() {
        return data[0].getEbayPrice();
    }

    public float getAmazonPrices() {
        return data[0].getAmazonPrice();
    }

    public float getCoolstuffPrices() {
        return data[0].getCoolstuffPrice();
    }

    public ArrayList<String> getSetNames() {
        return data[0].getSetNames();
    }

    public ArrayList<String> getSetCodes() {
        return data[0].getSetCodes();
    }

    public ArrayList<String> getSetRarities() {
        return data[0].getSetRarities();
    }

    public ArrayList<Float> getSetPrices() {
        return data[0].getSetPrices();
    }
}
