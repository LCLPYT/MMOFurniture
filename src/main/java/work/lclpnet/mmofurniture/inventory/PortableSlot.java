package work.lclpnet.mmofurniture.inventory;

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * Author: MrCrayfish
 */
public class PortableSlot extends Slot {

    public PortableSlot(Inventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return !(block instanceof IPortableInventory || block instanceof ShulkerBoxBlock);
    }
}
