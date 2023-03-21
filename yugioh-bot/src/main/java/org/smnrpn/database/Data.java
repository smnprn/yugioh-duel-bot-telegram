package org.smnrpn.database;

import org.smnrpn.database.CardImages;

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
}
