package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.HedgeBlock;

import javax.annotation.Nullable;

public class HedgeModule extends AbstractVariantModule {

    public static TagKey<Block> tag;

    public HedgeModule() {
        super(false, false, false);
    }

    @Override
    public void register() {
        super.register();
        tag = TagKey.of(Registry.BLOCK_KEY, MMOFurniture.identifier("fences/hedge"));
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new HedgeBlock(parent))
                .register(getIdentifier(variant), MMOFurniture.ITEM_GROUP);
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
