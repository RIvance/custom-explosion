package org.ivance.customexplosion.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import org.ivance.customexplosion.util.Explosive;

import java.util.HashMap;
import java.util.Map;

public class ExplosionGameRules {

    private static final Map<Explosive, GameRules.Key<BooleanRule>> destructionGameRules = new HashMap<>();
    private static final Map<Explosive, GameRules.Key<DoubleRule>> powerMultiplierGameRules = new HashMap<>();
    private static final Map<Explosive, GameRules.Key<BooleanRule>> createFireGameRules = new HashMap<>();

    public static void register(Explosive explosive, GameRules.Category category, boolean destruction, boolean createFire) {
        destructionGameRules.put(explosive, GameRuleRegistry.register(
            explosive.name + "ExplosionDestruction", category, GameRuleFactory.createBooleanRule(destruction)
        ));
        powerMultiplierGameRules.put(explosive, GameRuleRegistry.register(
            explosive.name + "ExplosionMultiplier", category, GameRuleFactory.createDoubleRule(1.0)
        ));
        createFireGameRules.put(explosive, GameRuleRegistry.register(
            explosive.name + "ExplosionCreateFire", category, GameRuleFactory.createBooleanRule(createFire)
        ));
    }

    public static GameRules.Key<BooleanRule> destruction(Explosive explosive) {
        return destructionGameRules.get(explosive);
    }

    public static GameRules.Key<DoubleRule> multiplier(Explosive explosive) {
        return powerMultiplierGameRules.get(explosive);
    }

    public static GameRules.Key<BooleanRule> createFire(Explosive explosive) {
        return createFireGameRules.get(explosive);
    }
}
