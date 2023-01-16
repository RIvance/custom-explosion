package org.ivance.customexplosion.util;

import net.minecraft.world.GameRules;
import net.minecraft.world.explosion.Explosion;

public record ExplosionConfig(
    Explosion.DestructionType destructionType,
    float power, boolean createFire
) {
    public static ExplosionConfig keep(float power, boolean createFire) {
        return new ExplosionConfig(Explosion.DestructionType.KEEP, power, createFire);
    }
}
