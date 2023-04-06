package org.ivance.customexplosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin extends Entity {

    private static final String WORLD_CREATE_EXPLOSION = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;";

    public WitherEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "mobTick", at = @At(value = "INVOKE_ASSIGN", target = WORLD_CREATE_EXPLOSION))
    void explode(CallbackInfo callback) {
        boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.WITHER)).get();
        double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.WITHER)).get();

        Explosion.DestructionType destruction = (
            !this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
        ) ? Explosion.DestructionType.NONE : (
            this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.WITHER)).get()
        ) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

        this.world.createExplosion(
            this, this.getX(), this.getEyeY(), this.getZ(),
            7.0f * (float) multiplier, createFire, destruction
        );
    }

}
