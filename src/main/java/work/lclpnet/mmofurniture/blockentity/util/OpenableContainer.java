package work.lclpnet.mmofurniture.blockentity.util;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvent;

public interface OpenableContainer {

    void playOpenSound(BlockState blockState, SoundEvent soundEvent);

    void setOpen(BlockState blockState, boolean open);

    default boolean isScreenHandler(ScreenHandler screenHandler) {
        return screenHandler instanceof GenericContainerScreenHandler;
    }

    default Inventory getEffectiveInventory(ScreenHandler screenHandler) {
        return ((GenericContainerScreenHandler) screenHandler).getInventory();
    }
}
