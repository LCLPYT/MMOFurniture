package work.lclpnet.mmofurniture.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.asm.type.IPlayerEntity;
import work.lclpnet.mmofurniture.block.CabinetBlock;
import work.lclpnet.mmofurniture.block.CrateBlock;
import work.lclpnet.mmofurniture.blockentity.util.OpenableContainer;
import work.lclpnet.mmofurniture.blockentity.util.OpenableViewerCountManager;
import work.lclpnet.mmofurniture.inventory.CrateScreenHandler;
import work.lclpnet.mmofurniture.module.CratesModule;

import java.util.UUID;

public class CrateBlockEntity extends BasicLootBlockEntity implements IUpdatePacketReceiver, ExtendedScreenHandlerFactory, OpenableContainer {

    private UUID ownerUuid;
    private boolean locked;
    private final ViewerCountManager stateManager;

    public CrateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CratesModule.blockEntityType, blockPos, blockState);
        stateManager = new OpenableViewerCountManager<>(this);
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.cfm.crate");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        if (this.locked && !this.ownerUuid.equals(playerInventory.player.getUuid())) {
            playerInventory.player.sendMessage(new TranslatableText("container.isLocked", this.getDisplayName()), true);
            playerInventory.player.playSound(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return null;
        }
        return new CrateScreenHandler(syncId, playerInventory, this, this.locked);
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public CrateBlockEntity setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        BlockEntityUtil.sendUpdatePacket(this);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator())
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator())
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
    }

    public void scheduledTick() {
        if (!this.removed)
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
    }

    public void removeUnauthorisedPlayers() {
        if (world != null && this.locked) {
            int x = this.pos.getX();
            int y = this.pos.getY();
            int z = this.pos.getZ();

            for (PlayerEntity playerEntity : world.getNonSpectatingEntities(PlayerEntity.class, new Box((float) x - 5.0F, (float) y - 5.0F, (float) z - 5.0F, (float) (x + 1) + 5.0F, (float) (y + 1) + 5.0F, (float) (z + 1) + 5.0F))) {
                if (playerEntity.currentScreenHandler instanceof CrateScreenHandler) {
                    Inventory crateInventory = ((CrateScreenHandler) playerEntity.currentScreenHandler).getCrateBlockEntity();
                    if (this == crateInventory && !playerEntity.getUuid().equals(ownerUuid)) {
                        ((IPlayerEntity) playerEntity).closeCurrentHandledScreen();
                    }
                }
            }
        }
    }

    @Override
    public void playOpenSound(BlockState blockState, SoundEvent soundEvent) {
        Vec3i directionVec = blockState.get(CabinetBlock.DIRECTION).getVector();
        double x = this.pos.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.pos.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.pos.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        World world = this.getWorld();
        if (world != null) {
            world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.75F, world.random.nextFloat() * 0.1F + 0.7F);
        }
    }

    @Override
    public void setOpen(BlockState blockState, boolean open) {
        World world = this.getWorld();
        if (world != null) {
            world.setBlockState(this.getPos(), blockState.with(CrateBlock.OPEN, open), 3);
        }
    }

    @Override
    public boolean isScreenHandler(ScreenHandler screenHandler) {
        return screenHandler instanceof CrateScreenHandler;
    }

    @Override
    public Inventory getEffectiveInventory(ScreenHandler screenHandler) {
        return ((CrateScreenHandler) screenHandler).getCrateBlockEntity();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.readData(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        this.writeData(tag);
        super.writeNbt(tag);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.writeData(new NbtCompound());
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
        NbtCompound compound = packet.getNbt();
        if (compound != null)
            this.readData(compound);
    }

    private void readData(NbtCompound compound) {
        if (compound.containsUuid("OwnerUUID")) this.ownerUuid = compound.getUuid("OwnerUUID");
        if (compound.contains("Locked", NbtType.BYTE)) this.locked = compound.getBoolean("Locked");
    }

    private NbtCompound writeData(NbtCompound compound) {
        if (this.ownerUuid != null) compound.putUuid("OwnerUUID", this.ownerUuid);
        compound.putBoolean("Locked", this.locked);
        return compound;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return !locked;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return !locked;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.getPos());
    }
}
