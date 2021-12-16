package work.lclpnet.mmofurniture.module;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmofurniture.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVariantModule implements IModule {

    public static final ImmutableList<String> VARIANTS;

    static {
        List<String> tmp = new ArrayList<>();
        CommonUtil.WOOD_TYPES.forEach(woodType -> {
            tmp.add(woodType);
            tmp.add(String.format("stripped_%s", woodType));
        });

        tmp.addAll(CommonUtil.STONE_TYPES);

        VARIANTS = ImmutableList.copyOf(tmp);
    }

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
