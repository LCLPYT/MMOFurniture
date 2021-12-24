package work.lclpnet.mmofurniture.asm.mixin.common;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.mmofurniture.asm.type.IPlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntity {

    @Shadow
    protected abstract void closeHandledScreen();

    @Override
    public void closeCurrentHandledScreen() {
        this.closeHandledScreen();
    }
}
