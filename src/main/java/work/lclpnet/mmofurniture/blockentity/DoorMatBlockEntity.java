package work.lclpnet.mmofurniture.blockentity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.module.DoorMatModule;

public class DoorMatBlockEntity extends BlockEntity implements IUpdatePacketReceiver {

    private String message = null;

    public DoorMatBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(DoorMatModule.blockEntityType, blockPos, blockState);
    }

    public void setMessage(String message) {
        if (this.message == null) {
            this.message = message;
            BlockEntityUtil.sendUpdatePacket(this);
        }
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("Message", NbtType.STRING))
            this.message = tag.getString("Message");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        if (this.message != null) tag.putString("Message", this.message);
        super.writeNbt(tag);
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        final NbtCompound tag = new NbtCompound();
        this.writeNbt(tag);
        return tag;
    }

    @Override
    public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
        final NbtCompound nbt = packet.getNbt();
        if (nbt != null) this.readNbt(nbt);
    }
}
