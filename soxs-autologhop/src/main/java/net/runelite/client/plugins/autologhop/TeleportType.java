package net.runelite.client.plugins.autologhop;

public enum TeleportType {

    ROYAL_SEED_POD("Royal Seed Pod"),
    ROW_GRAND_EXCHANGE("RoW to GE");

    private final String name;

    TeleportType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
