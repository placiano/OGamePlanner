package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class ActionCost {
    private final long time;

    private final int metal;
    private final int crystal;
    private final int deuterium;
    private final int darkMatter;

    public ActionCost(long time, int metal, int crystal, int deuterium, int darkMatter) {
        this.time = time;
        this.metal = metal;
        this.crystal = crystal;
        this.deuterium = deuterium;
        this.darkMatter = darkMatter;
    }

    public long getTime() {
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

    public ActionCost plus(ActionCost other) {
        return new ActionCost(this.time + other.time, this.metal + other.metal, this.crystal + other.crystal, this.deuterium + other.deuterium, this.darkMatter + other.darkMatter);
    }
}
