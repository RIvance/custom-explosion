package org.ivance.customexplosion.util;

import net.minecraft.world.World;

public enum Explosive {
    CREEPER("creeper", World.ExplosionSourceType.MOB),
    WITHER("wither", World.ExplosionSourceType.MOB),
    GHAST_FIREBALL("ghastFireBall", World.ExplosionSourceType.MOB),
    TNT("tnt", World.ExplosionSourceType.TNT),
    BLOCK("block", World.ExplosionSourceType.BLOCK);

    public final String name;
    public final World.ExplosionSourceType explosionSourceType;

    Explosive(String name, World.ExplosionSourceType explosionSourceType) {
        this.name = name;
        this.explosionSourceType = explosionSourceType;
    }
}
