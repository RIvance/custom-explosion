package org.ivance.customexplosion;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;

public class CustomExplosion implements ModInitializer {
    @Override
    public void onInitialize() {
        ExplosionGameRules.register(Explosive.CREEPER, GameRules.Category.MOBS, true, false);
        ExplosionGameRules.register(Explosive.WITHER, GameRules.Category.MOBS, true, false);
        ExplosionGameRules.register(Explosive.GHAST_FIREBALL, GameRules.Category.MOBS, true, true);
        ExplosionGameRules.register(Explosive.TNT, GameRules.Category.MISC, true, false);
        ExplosionGameRules.register(Explosive.BLOCK, GameRules.Category.MISC, true, true);
    }
}
