package work.lclpnet.mmofurniture.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.stream.IntStream;

public abstract class BasicLootBlockEntity extends LootableContainerBlockEntity implements SidedInventory {

    private final int[] slots;
    protected DefaultedList<ItemStack> inventory;

    protected BasicLootBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        this.slots = IntStream.range(0, this.size()).toArray();
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    protected boolean addItem(ItemStack stack) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getStack(i).isEmpty()) {
                this.setStack(i, stack);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> it = this.inventory.iterator();
        ItemStack stack;
        do {
            if (!it.hasNext()) {
                return true;
            }
            stack = it.next();
        } while (stack.isEmpty());

        return false;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        if (!this.serializeLootTable(tag)) {
            Inventories.writeNbt(tag, this.inventory);
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if(!this.deserializeLootTable(tag)) {
            Inventories.readNbt(tag, this.inventory);
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return slots;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
