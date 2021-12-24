package work.lclpnet.mmofurniture.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.asm.type.IPlayerEntity;
import work.lclpnet.mmofurniture.block.CabinetBlock;
import work.lclpnet.mmofurniture.block.CrateBlock;
import work.lclpnet.mmofurniture.inventory.CrateScreenHandler;
import work.lclpnet.mmofurniture.module.CratesModule;
import work.lclpnet.mmofurniture.sound.FurnitureSounds;

import java.util.UUID;

public class CrateBlockEntity extends BasicLootBlockEntity implements IUpdatePacketReceiver, ExtendedScreenHandlerFactory {

    private UUID ownerUuid;
    private boolean locked;
    private int playerCount;

    public CrateBlockEntity() {
        super(CratesModule.blockEntityType);
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.mmofurniture.crate");
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
        if (!player.isSpectator()) {
            if (this.playerCount < 0) {
                this.playerCount = 0;
            }
            this.playerCount++;

            BlockState blockState = this.getCachedState();
            boolean open = blockState.get(CrateBlock.OPEN);
            if (!open) {
                this.playLidSound(blockState, FurnitureSounds.BLOCK_CABINET_OPEN);
                this.setLidState(blockState, true);
            }

            this.scheduleTick();
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) this.playerCount--;
    }

    private void scheduleTick() {
        if (this.world != null)
            this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
    }

    public void onScheduledTick() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        World world = this.getWorld();
        if (world != null) {
            this.updatePlayerCount(world, this, x, y, z);
            if (this.playerCount > 0) {
                this.scheduleTick();
            } else {
                BlockState blockState = this.getCachedState();
                if (!(blockState.getBlock() instanceof CrateBlock)) {
                    this.markRemoved();
                    return;
                }

                boolean open = blockState.get(CrateBlock.OPEN);
                if (open) {
                    this.playLidSound(blockState, FurnitureSounds.BLOCK_CABINET_CLOSE);
                    this.setLidState(blockState, false);
                }
            }
        }
    }

    private void updatePlayerCount(World world, Inventory inventory, int x, int y, int z) {
        this.playerCount = 0;
        for (PlayerEntity playerEntity : world.getNonSpectatingEntities(PlayerEntity.class, new Box((float) x - 5.0F, (float) y - 5.0F, (float) z - 5.0F, (float) (x + 1) + 5.0F, (float) (y + 1) + 5.0F, (float) (z + 1) + 5.0F))) {
            if (playerEntity.currentScreenHandler instanceof CrateScreenHandler) {
                Inventory crateInventory = ((CrateScreenHandler) playerEntity.currentScreenHandler).getCrateBlockEntity();
                if (inventory == crateInventory) {
                    this.playerCount++;
                }
            }
        }
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

    private void playLidSound(BlockState blockState, SoundEvent soundEvent) {
        Vec3i directionVec = blockState.get(CabinetBlock.DIRECTION).getVector();
        double x = this.pos.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.pos.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.pos.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        World world = this.getWorld();
        if (world != null) {
            world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.75F, world.random.nextFloat() * 0.1F + 0.7F);
        }
    }

    private void setLidState(BlockState blockState, boolean open) {
        World world = this.getWorld();
        if (world != null) {
            world.setBlockState(this.getPos(), blockState.with(CrateBlock.OPEN, open), 3);
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.readData(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        this.writeData(tag);
        return super.toTag(tag);
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.writeData(new CompoundTag());
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 0, this.toInitialChunkDataTag());
    }

    @Override
    public void onDataPacket(ClientConnection connection, BlockEntityUpdateS2CPacket packet) {
        CompoundTag compound = packet.getCompoundTag();
        this.readData(compound);
    }

    private void readData(CompoundTag compound) {
        if (compound.containsUuid("OwnerUUID")) this.ownerUuid = compound.getUuid("OwnerUUID");
        // 1 =: TAG_BYTE
        if (compound.contains("Locked", 1)) this.locked = compound.getBoolean("Locked");
    }

    private CompoundTag writeData(CompoundTag compound) {
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
