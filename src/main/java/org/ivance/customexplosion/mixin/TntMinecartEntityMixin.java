package org.ivance.customexplosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityMixin extends Entity {

    public TntMinecartEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author Ivance
     * @reason Overwrite the explode method
     */
    @Overwrite
    public void explode(double velocity) {
        if (!this.world.isClient) {
            double d = Math.sqrt(velocity);
            if (d > 5.0) {
                d = 5.0;
            }

            boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.TNT)).get();
            double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.TNT)).get();

            Explosion.DestructionType destruction = (
                this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.TNT)).get()
            ) ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE;

            this.world.createExplosion(
                this, this.getX(), this.getY(), this.getZ(),
                (float) ((4.0 + this.random.nextDouble() * 1.5 * d) * multiplier),
                createFire, destruction
            );
        }
    }
}
