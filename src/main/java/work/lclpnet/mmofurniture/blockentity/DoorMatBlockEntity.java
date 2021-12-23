package work.lclpnet.mmofurniture.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.module.DoorMatModule;

public class DoorMatBlockEntity extends BlockEntity implements IUpdatePacketReceiver {

    private String message = null;

    public DoorMatBlockEntity() {
        super(DoorMatModule.blockEntityType);
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
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (tag.contains("Message", 8)) { // TAG_STRING
            this.message = tag.getString("Message");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if (this.message != null) tag.putString("Message", this.message);
        return super.toTag(tag);
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 0, this.toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    @Override
    public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
        this.fromTag(this.getCachedState(), packet.getCompoundTag());
    }
}
