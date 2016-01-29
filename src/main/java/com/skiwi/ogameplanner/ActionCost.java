package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class ActionCost {
    private final int time;

    private final int metal;
    private final int crystal;
    private final int deuterium;
    private final int darkMatter;

    public ActionCost(int time, int metal, int crystal, int deuterium, int darkMatter) {
        this.time = time;
        this.metal = metal;
        this.crystal = crystal;
        this.deuterium = deuterium;
        this.darkMatter = darkMatter;
    }

    public int getTime() {
        return time;
    }

    public int getMetal() {
        return metal;
    }

    public int getCrystal() {
        return crystal;
    }

    public int getDeuterium() {
        return deuterium;
    }

    public int getDarkMatter() {
        return darkMatter;
    }
}
