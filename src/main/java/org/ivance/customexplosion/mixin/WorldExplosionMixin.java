package org.ivance.customexplosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.ivance.customexplosion.util.ExplosionConfig;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldExplosionMixin {

    @Shadow public abstract GameRules getGameRules();

    @Shadow protected abstract Explosion.DestructionType getDestructionType(GameRules.Key<GameRules.BooleanRule> gameRuleKey);

    private World self() {
        return (World) (Object) this;
    }

    private ExplosionConfig getExplosionConfig(Explosive explosive, double power) {
        Explosion.DestructionType destructionType = Explosion.DestructionType.KEEP;
        if (this.getGameRules().getBoolean(ExplosionGameRules.destruction(explosive))) {
            switch (explosive.explosionSourceType) {
                case MOB -> {
                    if (this.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                        destructionType = this.getDestructionType(GameRules.MOB_EXPLOSION_DROP_DECAY);
                    }
                }
                case BLOCK -> destructionType = this.getDestructionType(GameRules.BLOCK_EXPLOSION_DROP_DECAY);
                case TNT -> destructionType = this.getDestructionType(GameRules.TNT_EXPLOSION_DROP_DECAY);
            }
        }
        return new ExplosionConfig(
            destructionType,
            (float) (power * this.getGameRules().get(ExplosionGameRules.multiplier(explosive)).get()),
            this.getGameRules().getBoolean(ExplosionGameRules.createFire(explosive))
        );
    }

    private ExplosionConfig getExplosionConfig(World.ExplosionSourceType explosionSourceType, Entity entity, float power, boolean createFire) {
        return switch (explosionSourceType) {
            case NONE -> ExplosionConfig.keep(power, createFire);
            case BLOCK -> this.getExplosionConfig(Explosive.BLOCK, power);
            case MOB -> {
                if (this.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    if (entity instanceof CreeperEntity) {
                        yield getExplosionConfig(Explosive.CREEPER, power);
                    } else if (entity instanceof WitherEntity || entity instanceof WitherSkullEntity) {
                        yield getExplosionConfig(Explosive.WITHER, power);
                    } else if (entity instanceof FireballEntity fireball) {
                        if (fireball.getOwner() instanceof GhastEntity) {
                            yield getExplosionConfig(Explosive.GHAST_FIREBALL, power);
                        }
                    }
                    yield new ExplosionConfig(this.getDestructionType(GameRules.MOB_EXPLOSION_DROP_DECAY), power, createFire);
                } else {
                    yield ExplosionConfig.keep(power, createFire);
                }
            }
            case TNT -> this.getExplosionConfig(Explosive.TNT, power);
        };
    }

    /**
     * @author Ivance
     * @reason Overwrite explosion
     */
    @Overwrite
    public Explosion createExplosion(
        @Nullable Entity entity, @Nullable DamageSource damageSource,
        @Nullable ExplosionBehavior behavior,
        double x, double y, double z, float power, boolean createFire,
        World.ExplosionSourceType explosionSourceType, boolean particles
    ) {
        ExplosionConfig explosionConfig = getExplosionConfig(explosionSourceType, entity, power, createFire);
        Explosion explosion = new Explosion(
            self(), entity, damageSource, behavior, x, y, z,
            explosionConfig.power(), explosionConfig.createFire(), explosionConfig.destructionType()
        );
        explosion.collectBlocksAndDamageEntities();
        explosion.affectWorld(particles);
        return explosion;
    }
}
