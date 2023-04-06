package org.ivance.customexplosion.util;

public enum Explosive {
    CREEPER("creeper"),
    WITHER("wither"),
    GHAST_FIREBALL("ghastFireBall"),
    TNT("tnt"),
    BLOCK("block");

    public final String name;

    Explosive(String name) {
        this.name = name;
    }
}
