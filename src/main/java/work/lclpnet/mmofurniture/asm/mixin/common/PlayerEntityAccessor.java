package work.lclpnet.mmofurniture.asm.mixin.common;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {

    @Invoker("closeHandledScreen")
    void invokeCloseHandledScreen();
}
