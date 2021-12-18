package work.lclpnet.mmofurniture.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import work.lclpnet.mmofurniture.blockentity.CrateBlockEntity;
import work.lclpnet.mmofurniture.module.CratesModule;

import java.util.Objects;

public class CrateScreenHandler extends ScreenHandler {

    protected CrateBlockEntity crateBlockEntity;

    public CrateScreenHandler(int syncId, PlayerInventory inventory, CrateBlockEntity crateBlockEntity, boolean locked) {
        super(CratesModule.screenHandlerType, syncId);

        this.crateBlockEntity = Objects.requireNonNull(crateBlockEntity);
        crateBlockEntity.onOpen(inventory.player);

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new PortableSlot(crateBlockEntity, x + y * 9, 8 + x * 18, 18 + y * 18));
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.crateBlockEntity.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            clickedStack = slotStack.copy();
            if (index < this.crateBlockEntity.size()) {
                if (!this.insertItem(slotStack, this.crateBlockEntity.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(slotStack, 0, this.crateBlockEntity.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return clickedStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.crateBlockEntity.onClose(player);
    }

    public CrateBlockEntity getCrateBlockEntity() {
        return crateBlockEntity;
    }
}
