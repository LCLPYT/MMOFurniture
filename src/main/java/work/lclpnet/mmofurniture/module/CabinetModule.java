package work.lclpnet.mmofurniture.module;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.CabinetBlock;
import work.lclpnet.mmofurniture.blockentity.CabinetBlockEntity;

import java.util.HashSet;
import java.util.Set;

public class CabinetModule extends AbstractVariantModule {

    public static BlockEntityType<CabinetBlockEntity> tileEntityType;

    private static Set<CabinetBlock> cabinetBlocks = new HashSet<>();

    @Override
    public void register() {
        super.register();

        tileEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("cabinet"),
                BlockEntityType.Builder.create(CabinetBlockEntity::new, cabinetBlocks.toArray(new CabinetBlock[0])).build(null));

        cabinetBlocks = null;
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        CabinetBlock cabinet;

        new MMOBlockRegistrar(cabinet = new CabinetBlock(parent))
                .register(MMOFurniture.identifier("%s_cabinet", variant), ItemGroup.DECORATIONS);

        cabinetBlocks.add(cabinet);
    }
}
