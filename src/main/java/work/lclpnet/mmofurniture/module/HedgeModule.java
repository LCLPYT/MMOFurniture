package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.HedgeBlock;

import javax.annotation.Nullable;

public class HedgeModule extends AbstractVariantModule {

    public static Tag<Block> tag;

    public HedgeModule() {
        super(false, false, false);
    }

    @Override
    public void register() {
        super.register();
        tag = TagRegistry.block(MMOFurniture.identifier("fences/hedge"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new HedgeBlock(parent))
                .register(getIdentifier(variant), ItemGroup.DECORATIONS);
    }

    @Override
    protected String getWoodParentPath(String variant) {
        return String.format("%s_leaves", variant);
    }

    @NotNull
    protected static Identifier getIdentifier(String variant) {
        return MMOFurniture.identifier("%s_hedge", variant);
    }

    @Nullable
    public static Block getHedgeBlock(String woodType) {
        Block block = Registry.BLOCK.get(getIdentifier(woodType));
        return Blocks.AIR.equals(block) ? null : block;
    }
}
