package work.lclpnet.mmofurniture.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;
import work.lclpnet.mmofurniture.client.gui.screen.DoorMatScreen;

public class DoorMatItem extends BlockItem {

    public DoorMatItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        boolean placedTileEntity = super.postPlacement(pos, world, player, stack, state);

        if (world.isClient && !placedTileEntity && player != null)
            openDoorMatScreen(world, pos);

        return placedTileEntity;
    }

    @Environment(EnvType.CLIENT)
    protected void openDoorMatScreen(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DoorMatBlockEntity)
            MinecraftClient.getInstance().openScreen(new DoorMatScreen((DoorMatBlockEntity) blockEntity));
    }
}
