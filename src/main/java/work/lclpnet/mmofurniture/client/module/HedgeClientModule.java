package work.lclpnet.mmofurniture.client.module;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import work.lclpnet.mmocontent.client.render.LateBindingRenderLayers;
import work.lclpnet.mmofurniture.module.HedgeModule;
import work.lclpnet.mmofurniture.util.CommonUtil;

public class HedgeClientModule implements IClientModule {

    @Override
    public void registerClient() {
        /* register colors */
        // block colors
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> FoliageColors.getSpruceColor(),
                HedgeModule.getHedgeBlock("spruce"));

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> FoliageColors.getBirchColor(),
                HedgeModule.getHedgeBlock("birch"));

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
                HedgeModule.getHedgeBlock("oak"),
                HedgeModule.getHedgeBlock("jungle"),
                HedgeModule.getHedgeBlock("acacia"),
                HedgeModule.getHedgeBlock("dark_oak"));

        // item colors
        Block[] hedgeBlocks = CommonUtil.OVERWORLD_WOOD_TYPES.stream()
                .map(wood -> HedgeModule.getHedgeBlock(wood.toLowerCase()))
                .toArray(Block[]::new);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
                    return MinecraftClient.getInstance().getBlockColors().getColor(state, null, null, tintIndex);
                }, hedgeBlocks
        );

        /* register layers */
        // needs to use LateBindingRenderLayers, because game options are not yet available on BlockRenderLayerMap register
        LateBindingRenderLayers.bindLate(binder -> {
            // only for fancy or fabulous
            if (MinecraftClient.getInstance().options.graphicsMode.ordinal() <= 0) return;

            for (Block block : hedgeBlocks) binder.accept(block, RenderLayer.getCutout());
        });
    }
}
