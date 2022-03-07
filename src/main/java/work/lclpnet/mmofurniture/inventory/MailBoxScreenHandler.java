package work.lclpnet.mmofurniture.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import work.lclpnet.mmofurniture.blockentity.MailBoxBlockEntity;
import work.lclpnet.mmofurniture.module.MailBoxModule;

public class MailBoxScreenHandler extends ScreenHandler {

    private final MailBoxBlockEntity mailBoxBlockEntity;

    public MailBoxScreenHandler(int syncId, PlayerInventory playerInventory, MailBoxBlockEntity mailBoxBlockEntity) {
        super(MailBoxModule.screenHandlerType, syncId);
        this.mailBoxBlockEntity = mailBoxBlockEntity;
        mailBoxBlockEntity.onOpen(playerInventory.player);

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(mailBoxBlockEntity, x, 8 + x * 18, 18));
        }

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 50 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 108));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.mailBoxBlockEntity.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            clickedStack = slotStack.copy();
            if (index < this.mailBoxBlockEntity.size()) {
                if (!this.insertItem(slotStack, this.mailBoxBlockEntity.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(slotStack, 0, this.mailBoxBlockEntity.size(), false)) {
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
        this.mailBoxBlockEntity.onClose(player);
    }

    public MailBoxBlockEntity getMailBoxBlockEntity() {
        return mailBoxBlockEntity;
    }
}
