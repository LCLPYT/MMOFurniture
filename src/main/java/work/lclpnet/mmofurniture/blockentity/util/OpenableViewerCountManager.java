package work.lclpnet.mmofurniture.blockentity.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.mmofurniture.block.BedsideCabinetBlock;
import work.lclpnet.mmofurniture.sound.FurnitureSounds;

public class OpenableViewerCountManager<T extends Inventory & OpenableContainer> extends ViewerCountManager {

    protected final T container;

    public OpenableViewerCountManager(T container) {
        this.container = container;
    }

    @Override
    protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
        boolean open = state.get(BedsideCabinetBlock.OPEN);
        if (!open) {
            container.playOpenSound(state, FurnitureSounds.BLOCK_BEDSIDE_CABINET_OPEN);
            container.setOpen(state, true);
        }
    }

    @Override
    protected void onContainerClose(World world, BlockPos pos, BlockState state) {
    }

    @Override
    protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        boolean open = state.get(BedsideCabinetBlock.OPEN);
        if (open && newViewerCount <= 0) {
            container.playOpenSound(state, FurnitureSounds.BLOCK_BEDSIDE_CABINET_CLOSE);
            container.setOpen(state, false);
        }
    }

    @Override
    protected boolean isPlayerViewing(PlayerEntity player) {
        if (container.isScreenHandler(player.currentScreenHandler)) {
            Inventory inventory = container.getEffectiveInventory(player.currentScreenHandler);
            return inventory == container;
        }
        return false;
    }
}
