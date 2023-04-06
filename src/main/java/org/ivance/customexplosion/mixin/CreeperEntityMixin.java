package org.ivance.customexplosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends Entity {

    @Shadow private int explosionRadius;

    @Shadow protected abstract void spawnEffectsCloud();

    public CreeperEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private final static String WORLD_CREATE_EXPLOSION = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;";

    @Inject(method = "explode", at = @At(value = "INVOKE", target = WORLD_CREATE_EXPLOSION))
    public void explode(CallbackInfo ci) {
        boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.CREEPER)).get();
        double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.CREEPER)).get();

        Explosion.DestructionType destruction = (
            !this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
        ) ? Explosion.DestructionType.NONE : (
            this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.CREEPER)).get()
        ) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

        this.world.createExplosion(
            this, this.getX(), this.getY(), this.getZ(),
            (float) this.explosionRadius * (float) multiplier, createFire, destruction
        );

        this.discard();
        this.spawnEffectsCloud();
    }
}
