package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.ChairBlock;
import work.lclpnet.mmofurniture.entity.SeatEntity;

public class ChairModule extends AbstractVariantModule {

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

        super.register();
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        new MMOBlockRegistrar(new ChairBlock(parent))
                .register(MMOFurniture.identifier("%s_chair", variant), MMOFurniture.ITEM_GROUP);
    }
}
