package org.ivance.customexplosion.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireballEntity.class)
public class FireBallEntityMixin extends AbstractFireballEntity {

    @Shadow private int explosionPower;

    public FireBallEntityMixin(EntityType<? extends AbstractFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author Ivance
     * @reason Overwrite the onCollision method
     */
    @Overwrite
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.GHAST_FIREBALL)).get();
            double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.GHAST_FIREBALL)).get();

            Explosion.DestructionType destruction = (
                !this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
            ) ? Explosion.DestructionType.NONE : (
                this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.GHAST_FIREBALL)).get()
            ) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

            this.world.createExplosion(
                null, this.getX(), this.getY(), this.getZ(),
                this.explosionPower * (float) multiplier, createFire, destruction
            );
            this.discard();
        }
    }
}
