package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.BedsideCabinetBlock;
import work.lclpnet.mmofurniture.blockentity.BedsideCabinetBlockEntity;

import java.util.HashSet;
import java.util.Set;

public class BedsideCabinetModule extends AbstractVariantModule {

    public static BlockEntityType<BedsideCabinetBlockEntity> blockEntityType;
    private static Set<BedsideCabinetBlock> bedsideCabinetBlocks = new HashSet<>();

    @Override
    public void register() {
        super.register();

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("bedside_cabinet"),
                FabricBlockEntityTypeBuilder.create(BedsideCabinetBlockEntity::new, bedsideCabinetBlocks.toArray(new BedsideCabinetBlock[0])).build(null));

        bedsideCabinetBlocks = null;
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        BedsideCabinetBlock cabinet = new BedsideCabinetBlock(parent);

        new MMOBlockRegistrar(cabinet)
                .register(MMOFurniture.identifier("%s_bedside_cabinet", variant), MMOFurniture.ITEM_GROUP);

        bedsideCabinetBlocks.add(cabinet);
    }
}
