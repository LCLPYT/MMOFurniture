package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.ChairBlock;
import work.lclpnet.mmofurniture.entity.SeatEntity;
import work.lclpnet.mmofurniture.util.CommonUtil;

public class ChairModule implements IModule {

    public static EntityType<SeatEntity> seatType;

    @Override
    public void register() {
        seatType = Registry.register(
                Registry.ENTITY_TYPE,
                MMOFurniture.identifier("seat"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, SeatEntity::new)
                        .dimensions(EntityDimensions.changing(0.0F, 0.0F))
                        .trackRangeChunks(3)
                        .trackedUpdateRate(Integer.MAX_VALUE)
                        .forceTrackedVelocityUpdates(false)
                        .build()
        );

        CommonUtil.WOOD_TYPES.forEach(woodType -> {
            Block parent = Registry.BLOCK.get(new Identifier(String.format("%s_planks", woodType)));
            if (Blocks.AIR.equals(parent)) return;

            registerChair(woodType, parent);
            registerChair(String.format("stripped_%s", woodType), parent);
        });

        CommonUtil.STONE_TYPES.forEach(stoneType -> {
            Block parent = Registry.BLOCK.get(new Identifier(stoneType));
            if (!Blocks.AIR.equals(parent)) registerChair(stoneType, parent);
        });
    }

    private void registerChair(String name, Block parent) {
        new MMOBlockRegistrar(new ChairBlock(parent))
                .register(MMOFurniture.identifier(String.format("%s_chair", name)), ItemGroup.DECORATIONS);
    }
}
