package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.TableBlock;
import work.lclpnet.mmofurniture.util.CommonUtil;

public class TableModule implements IModule {

    @Override
    public void register() {
        CommonUtil.WOOD_TYPES.forEach(woodType -> {
            Block parent = Registry.BLOCK.get(new Identifier(String.format("%s_planks", woodType)));
            if (Blocks.AIR.equals(parent)) return;

            registerTable(woodType, parent);
            registerTable(String.format("stripped_%s", woodType), parent);
        });

        CommonUtil.STONE_TYPES.forEach(stoneType -> {
            Block parent = Registry.BLOCK.get(new Identifier(stoneType));
            if (!Blocks.AIR.equals(parent)) registerTable(stoneType, parent);
        });
    }

    private void registerTable(String name, Block parent) {
        new MMOBlockRegistrar(new TableBlock(parent))
                .register(MMOFurniture.identifier(String.format("%s_table", name)), ItemGroup.DECORATIONS);
    }
}
