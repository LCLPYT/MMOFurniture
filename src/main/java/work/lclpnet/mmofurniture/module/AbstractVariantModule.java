package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmofurniture.util.CommonUtil;

public abstract class AbstractVariantModule implements IModule {

    @Override
    public void register() {
        CommonUtil.WOOD_TYPES.forEach(woodType -> {
            Block parent = Registry.BLOCK.get(new Identifier(getWoodParentPath(woodType)));
            if (Blocks.AIR.equals(parent)) return;

            registerVariant(woodType, parent);
            registerVariant(String.format("stripped_%s", woodType), parent);
        });

        CommonUtil.STONE_TYPES.forEach(stoneType -> {
            Block parent = Registry.BLOCK.get(new Identifier(stoneType));
            if (!Blocks.AIR.equals(parent)) registerVariant(stoneType, parent);
        });
    }

    protected String getWoodParentPath(String variant) {
        return String.format("%s_planks", variant);
    }

    protected abstract void registerVariant(String variant, Block parent);
}
