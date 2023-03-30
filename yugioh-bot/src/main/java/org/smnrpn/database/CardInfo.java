package org.smnrpn.database;

import java.util.Arrays;

public class CardInfo {
    private final Card card;
    private String cardInfo;

    private final String LINE_BREAK = "\n\n";

    public CardInfo(Card card) {
        this.card = card;
    }

    public String makeLink() {
        cardInfo = "<b>Name:</b> " + card.getName() + "\n" +
            "<b>Attribute:</b> " + card.getAttribute() + "\n" +
            "<b>Link value:</b> " + card.getLinkVal() + "\n" +
            "<b>Link markers:</b> " + Arrays.toString(card.getLinkMarkers()) + "\n" +
            "<b>Atk:</b> " + card.getAtk() + LINE_BREAK +
            card.getRace() + " / " + card.getType() + LINE_BREAK +
            "<b>Card text:</b>" + "\n" +
            card.getDesc();

        return cardInfo;
    }

    public String makePendulum() {
        cardInfo = "<b>Name:</b> " + card.getName() + "\n" +
            "<b>Attribute:</b> " + card.getAttribute() + "\n" +
            "<b>Level:</b> " + card.getLevel() + "\n" +
            "<b>Scale:</b> " + card.getScale() + "\n" +
            "<b>Atk:</b> " + card.getAtk() + "\n" +
            "<b>Def:</b> " + card.getDef() + LINE_BREAK +
            card.getRace() + " / " + card.getType() + LINE_BREAK +
            "<b>Card text:</b>" + "\n" +
            card.getDesc();

        return cardInfo;
    }

    public String makeSpellTrap() {
        cardInfo = "<b>Name:</b> " + card.getName() + LINE_BREAK +
            card.getRace() + " " + card.getType() + LINE_BREAK +
            "<b>Card text:</b>" + "\n" +
            card.getDesc();

        return cardInfo;
    }

    public String makeMonster() {
        cardInfo = "<b>Name:</b> " + card.getName() + "\n" +
            "<b>Attribute:</b> " + card.getAttribute() + "\n" +
            "<b>Level:</b> " + card.getLevel() + "\n" +
            "<b>Atk:</b> " + card.getAtk() + "\n" +
            "<b>Def:</b> " + card.getDef() + LINE_BREAK +
            card.getRace() + " / " + card.getType() + LINE_BREAK +
            "<b>Card text:</b>" + "\n" +
            card.getDesc();

        return cardInfo;
    }
}
