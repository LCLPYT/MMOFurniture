package work.lclpnet.mmofurniture.asm.mixin.client;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.mmofurniture.blockentity.IUpdatePacketReceiver;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    @Final
    private ClientConnection connection;

    @Inject(
            method = "onBlockEntityUpdate(Lnet/minecraft/network/packet/s2c/play/BlockEntityUpdateS2CPacket;)V",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket packet, CallbackInfo ci, BlockPos blockPos) {
        if (this.client.world == null) return;

        this.client.world.getBlockEntity(blockPos, packet.getBlockEntityType()).ifPresent(blockEntity -> {
            if (blockEntity instanceof CommandBlockBlockEntity && this.client.currentScreen instanceof CommandBlockScreen)
                return;

            if (blockEntity instanceof IUpdatePacketReceiver)
                ((IUpdatePacketReceiver) blockEntity).onDataPacket(connection, packet);
        });
    }
}
