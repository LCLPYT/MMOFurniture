package work.lclpnet.mmofurniture.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import work.lclpnet.mmofurniture.inventory.MailBoxScreenHandler;
import work.lclpnet.mmofurniture.module.MailBoxModule;

public class MailBoxBlockEntity extends BasicLootBlockEntity implements ExtendedScreenHandlerFactory {

    public MailBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MailBoxModule.blockEntityType, blockPos, blockState);
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.cfm.mail_box");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new MailBoxScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.getPos());
    }
}
