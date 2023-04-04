package org.smnrpn.cards;

import java.util.ArrayList;

public class Data {
    private int id;
    private String name;
    private String type;
    private String frameType;
    private String desc;
    private int atk;
    private int def;
    private int level;
    private String race;
    private String attribute;
    private CardImages[] card_images;
    private CardSets[] card_sets;
    private CardPrices[] card_prices;

    // Additional response for link monsters
    private int linkval;
    private String[] linkmarkers;


    // Additional response for pendulum monsters
    private int scale;



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFrameType() {
        return frameType;
    }

    public String getDesc() {
        return desc;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getLevel() {
        return level;
    }

    public String getRace() {
        return race;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getImage_url_cropped() {
        return card_images[0].getImage_url_cropped();
    }

    // Get link rank and link markers for link monsters
    public int getLinkVal() {
        return linkval;
    }

    public String[] getLinkMarkers() {
        return linkmarkers;
    }

    // Get scale for pendulum monsters
    public int getScale() {
        return scale;
    }

    public float getCardmarketPrice() {
        return card_prices[0].getCardmarket_price();
    }

    public float getTCGPlayerPrice() {
        return card_prices[0].getTcgplayer_price();
    }

    public float getEbayPrice() {
        return card_prices[0].getEbay_price();
    }

    public float getAmazonPrice() {
        return card_prices[0].getAmazon_price();
    }

    public float getCoolstuffPrice() {
        return card_prices[0].getCoolstuffinc_price();
    }

    public ArrayList<String> getSetNames() {
        ArrayList<String> setNames = new ArrayList<>();

        for (int i = 0; i < card_sets.length; i++) {
            setNames.add(card_sets[i].getSet_name());
        }

        return setNames;
    }

    public ArrayList<String> getSetCodes() {
        ArrayList<String> setCodes = new ArrayList<>();

        for (int i = 0; i < card_sets.length; i++) {
            setCodes.add(card_sets[i].getSet_code());
        }

        return setCodes;
    }

    public ArrayList<String> getSetRarities() {
        ArrayList<String> setRarities = new ArrayList<>();

        for (int i = 0; i < card_sets.length; i++) {
            setRarities.add(card_sets[i].getSet_rarity());
        }

        return setRarities;
    }

    public ArrayList<Float> getSetPrices() {
        ArrayList<Float> setPrices = new ArrayList<>();

        for (int i = 0; i < card_sets.length; i++) {
            setPrices.add(card_sets[i].getSet_price());
        }

        return setPrices;
    }
}
