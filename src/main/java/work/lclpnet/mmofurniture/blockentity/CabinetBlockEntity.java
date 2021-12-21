package work.lclpnet.mmofurniture.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import work.lclpnet.mmofurniture.block.CabinetBlock;
import work.lclpnet.mmofurniture.module.CabinetModule;
import work.lclpnet.mmofurniture.sound.FurnitureSounds;

public class CabinetBlockEntity extends BasicLootBlockEntity {

    private int playerCount;

    public CabinetBlockEntity() {
        super(CabinetModule.blockEntityType);
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.mmofurniture.cabinet");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, syncId, playerInventory, this, 2);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.playerCount < 0) {
                this.playerCount = 0;
            }
            this.playerCount++;

            BlockState blockState = this.getCachedState();
            boolean open = blockState.get(CabinetBlock.OPEN);
            if (!open) {
                this.playDoorSound(blockState, FurnitureSounds.BLOCK_CABINET_OPEN);
                this.setDoorState(blockState, true);
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
            this.playerCount = ChestBlockEntity.countViewers(world, this, x, y, z); //Gets a count of players around using this inventory
            if (this.playerCount > 0) {
                this.scheduleTick();
            } else {
                BlockState blockState = this.getCachedState();
                if (!(blockState.getBlock() instanceof CabinetBlock)) {
                    this.markRemoved();
                    return;
                }

                boolean open = blockState.get(CabinetBlock.OPEN);
                if (open) {
                    this.playDoorSound(blockState, FurnitureSounds.BLOCK_CABINET_CLOSE);
                    this.setDoorState(blockState, false);
                }
            }
        }
    }

    private void playDoorSound(BlockState blockState, SoundEvent soundEvent) {
        Vec3i directionVec = blockState.get(CabinetBlock.DIRECTION).getVector();
        double x = this.pos.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.pos.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.pos.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        World world = this.getWorld();
        if (world != null) {
            world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    private void setDoorState(BlockState blockState, boolean open) {
        World world = this.getWorld();
        if (world != null) {
            world.setBlockState(this.getPos(), blockState.with(CabinetBlock.OPEN, open), 3);
        }
    }
}
