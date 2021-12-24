package work.lclpnet.mmofurniture.module;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmofurniture.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractVariantModule implements IModule {

    public static final ImmutableList<String> VARIANTS;

    protected final boolean registerStone, registerNetherWood, registerStrippedWood;

    public AbstractVariantModule() {
        this(true);
    }

    public AbstractVariantModule(boolean registerStone) {
        this(registerStone, true, true);
    }

    public AbstractVariantModule(boolean registerStone, boolean registerNetherWood, boolean registerStrippedWood) {
        this.registerStone = registerStone;
        this.registerNetherWood = registerNetherWood;
        this.registerStrippedWood = registerStrippedWood;
    }

    static {
        List<String> tmp = new ArrayList<>();
        Stream.concat(CommonUtil.OVERWORLD_WOOD_TYPES.stream(), CommonUtil.NETHER_WOOD_TYPES.stream()).forEach(woodType -> {
            tmp.add(woodType);
            tmp.add(String.format("stripped_%s", woodType));
        });

        tmp.addAll(CommonUtil.STONE_TYPES);

        VARIANTS = ImmutableList.copyOf(tmp);
    }

    @Override
    public void register() {
        Consumer<String> woodRegistrar = woodType -> {
            Block parent = Registry.BLOCK.get(new Identifier(getWoodParentPath(woodType)));
            if (Blocks.AIR.equals(parent)) return;

            registerVariant(woodType, parent);
            if (registerStrippedWood) registerVariant(String.format("stripped_%s", woodType), parent);
        };

        CommonUtil.OVERWORLD_WOOD_TYPES.forEach(woodRegistrar);
        if (registerNetherWood) CommonUtil.NETHER_WOOD_TYPES.forEach(woodRegistrar);
        if (registerStone) {
            CommonUtil.STONE_TYPES.forEach(stoneType -> {
                Block parent = Registry.BLOCK.get(new Identifier(stoneType));
                if (!Blocks.AIR.equals(parent)) registerVariant(stoneType, parent);
            });
        }
    }

    protected String getWoodParentPath(String variant) {
        return String.format("%s_planks", variant);
    }

    protected abstract void registerVariant(String variant, Block parent);
}
