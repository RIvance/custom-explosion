package org.ivance.customexplosion.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(WitherSkullEntity.class)
public abstract class WitherSkullEntityMixin extends ExplosiveProjectileEntity {

    protected WitherSkullEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author Ivance
     * @reason Overwrite the onCollision method
     */
    @Overwrite
    public void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.WITHER)).get();
            double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.WITHER)).get();

            Explosion.DestructionType destruction = (
                !this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
            ) ? Explosion.DestructionType.NONE : (
                this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.WITHER)).get()
            ) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), (float) multiplier, createFire, destruction);
            this.discard();
        }
    }
}
