package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.CrateBlock;
import work.lclpnet.mmofurniture.blockentity.CrateBlockEntity;
import work.lclpnet.mmofurniture.inventory.CrateScreenHandler;

import java.util.HashSet;
import java.util.Set;

public class CratesModule extends AbstractVariantModule {

    public static BlockEntityType<CrateBlockEntity> blockEntityType;
    public static final ScreenHandlerType<CrateScreenHandler> screenHandlerType = ScreenHandlerRegistry.registerExtended(MMOFurniture.identifier("crate"), (syncId, inventory, buf) -> {
        CrateBlockEntity crateBlockEntity = (CrateBlockEntity) inventory.player.world.getBlockEntity(buf.readBlockPos());
        if (crateBlockEntity == null) throw new IllegalStateException("Crate block entity was not found");
        return new CrateScreenHandler(syncId, inventory, crateBlockEntity, crateBlockEntity.isLocked());
    });
    private static Set<CrateBlock> crateBlocks = new HashSet<>();

    public CratesModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("crate"),
                BlockEntityType.Builder.create(CrateBlockEntity::new, crateBlocks.toArray(new CrateBlock[0])).build(null));

        crateBlocks = null;
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        CrateBlock crateBlock = new CrateBlock();

        new MMOBlockRegistrar(crateBlock)
                .register(MMOFurniture.identifier("%s_crate", variant), MMOFurniture.ITEM_GROUP);

        crateBlocks.add(crateBlock);
    }
}
