package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.DoorMatBlock;
import work.lclpnet.mmofurniture.blockentity.DoorMatBlockEntity;

public class DoorMatModule implements IModule {

    public static BlockEntityType<DoorMatBlockEntity> blockEntityType;

    @Override
    public void register() {
        DoorMatBlock doorMatBlock = new DoorMatBlock();

        new MMOBlockRegistrar(doorMatBlock)
                .register(MMOFurniture.identifier("door_mat"), MMOFurniture.ITEM_GROUP);

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("door_mat"),
                FabricBlockEntityTypeBuilder.create(DoorMatBlockEntity::new, doorMatBlock).build(null));
    }
}
