package work.lclpnet.mmofurniture.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import work.lclpnet.mmofurniture.block.BedsideCabinetBlock;
import work.lclpnet.mmofurniture.blockentity.util.OpenableContainer;
import work.lclpnet.mmofurniture.blockentity.util.OpenableViewerCountManager;
import work.lclpnet.mmofurniture.module.BedsideCabinetModule;

public class BedsideCabinetBlockEntity extends BasicLootBlockEntity implements OpenableContainer {

    private final ViewerCountManager stateManager;

    public BedsideCabinetBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BedsideCabinetModule.blockEntityType, blockPos, blockState);
        stateManager = new OpenableViewerCountManager<>(this);
    }

    @Override
    public int size() {
        return 9;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.cfm.bedside_cabinet");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1);
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

    @Override
    public void playOpenSound(BlockState blockState, SoundEvent soundEvent) {
        Vec3i directionVec = blockState.get(BedsideCabinetBlock.DIRECTION).getVector();
        double x = this.pos.getX() + 0.5D + directionVec.getX() / 2.0D;
        double y = this.pos.getY() + 0.5D + directionVec.getY() / 2.0D;
        double z = this.pos.getZ() + 0.5D + directionVec.getZ() / 2.0D;
        World world = this.getWorld();
        if (world != null) {
            world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void setOpen(BlockState blockState, boolean open) {
        World world = this.getWorld();
        if (world != null) {
            world.setBlockState(this.getPos(), blockState.with(BedsideCabinetBlock.OPEN, open), Block.NOTIFY_ALL);
        }
    }
}
