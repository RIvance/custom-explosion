package org.ivance.customexplosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity {

    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    
    /**
     * @author Ivance
     * @reason Overwrite the explode method
     */
    @Overwrite
    private void explode() {
        boolean createFire = this.world.getGameRules().get(ExplosionGameRules.createFire(Explosive.TNT)).get();
        double multiplier = this.world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.TNT)).get();
        Explosion.DestructionType destruction = (
            this.world.getGameRules().get(ExplosionGameRules.destruction(Explosive.TNT)).get()
        ) ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE;

        this.world.createExplosion(
            this, this.getX(), this.getBodyY(0.0625), this.getZ(),
            4.0f * (float) multiplier, createFire, destruction
        );
    }
}
