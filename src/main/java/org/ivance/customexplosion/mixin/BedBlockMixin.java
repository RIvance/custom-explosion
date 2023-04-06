package org.ivance.customexplosion.mixin;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.ivance.customexplosion.gamerule.ExplosionGameRules;
import org.ivance.customexplosion.util.Explosive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {

    private final static String WORLD_CREATE_EXPLOSION = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;";

    @Inject(method = "onUse", cancellable = true, at = @At(value = "INVOKE", target = WORLD_CREATE_EXPLOSION))
    public void onUse(
        BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
        BlockHitResult hit, CallbackInfoReturnable<ActionResult> callback
    ) {
        boolean createFire = world.getGameRules().get(ExplosionGameRules.createFire(Explosive.BLOCK)).get();
        double multiplier = world.getGameRules().get(ExplosionGameRules.multiplier(Explosive.BLOCK)).get();
        Explosion.DestructionType destruction = (
            world.getGameRules().get(ExplosionGameRules.destruction(Explosive.BLOCK)).get()
        ) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;

        world.createExplosion(
            null, DamageSource.badRespawnPoint(), null,
            (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5,
            5.0f * (float) multiplier, createFire, destruction
        );
        callback.setReturnValue(ActionResult.SUCCESS);
    }
}
