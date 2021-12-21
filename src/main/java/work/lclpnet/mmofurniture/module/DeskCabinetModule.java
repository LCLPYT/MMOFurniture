package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.DeskCabinetBlock;
import work.lclpnet.mmofurniture.blockentity.DeskCabinetBlockEntity;

import java.util.HashSet;
import java.util.Set;

public class DeskCabinetModule extends AbstractVariantModule {

    public static BlockEntityType<DeskCabinetBlockEntity> blockEntityType;
    private static Set<DeskCabinetBlock> bedsideCabinetBlocks = new HashSet<>();

    @Override
    public void register() {
        super.register();

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("desk_cabinet"),
                BlockEntityType.Builder.create(DeskCabinetBlockEntity::new, bedsideCabinetBlocks.toArray(new DeskCabinetBlock[0])).build(null));

        bedsideCabinetBlocks = null;
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        int variantIdx = VARIANTS.indexOf(variant);
        if (variantIdx == -1) return;

        DeskCabinetBlock cabinet = new DeskCabinetBlock(parent, (short) variantIdx);

        new MMOBlockRegistrar(cabinet) // only supports up to 2^16 - 2 variants
                .register(MMOFurniture.identifier("%s_desk_cabinet", variant), ItemGroup.DECORATIONS);

        bedsideCabinetBlocks.add(cabinet);
    }
}
