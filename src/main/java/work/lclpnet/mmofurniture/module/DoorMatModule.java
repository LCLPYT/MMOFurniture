package work.lclpnet.mmofurniture.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
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
                .register(MMOFurniture.identifier("door_mat"), ItemGroup.DECORATIONS);

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("door_mat"),
                BlockEntityType.Builder.create(DoorMatBlockEntity::new, doorMatBlock).build(null));
    }
}
