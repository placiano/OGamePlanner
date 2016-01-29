package com.skiwi.ogameplanner;

/**
 * @author Frank van Heeswijk
 */
public class ServerSettings {
    private final int economySpeed;
    private final int fleetSpeed;

    public ServerSettings(int economySpeed, int fleetSpeed) {
        this.economySpeed = economySpeed;
        this.fleetSpeed = fleetSpeed;
    }

    public int getEconomySpeed() {
        return economySpeed;
    }

    public int getFleetSpeed() {
        return fleetSpeed;
    }
}
