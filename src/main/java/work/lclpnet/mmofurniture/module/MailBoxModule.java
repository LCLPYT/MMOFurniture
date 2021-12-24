package work.lclpnet.mmofurniture.module;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.MMOBlockRegistrar;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.block.MailBoxBlock;
import work.lclpnet.mmofurniture.blockentity.MailBoxBlockEntity;
import work.lclpnet.mmofurniture.inventory.MailBoxScreenHandler;

import java.util.HashSet;
import java.util.Set;

public class MailBoxModule extends AbstractVariantModule {

    public static BlockEntityType<MailBoxBlockEntity> blockEntityType;
    public static final ScreenHandlerType<MailBoxScreenHandler> screenHandlerType = ScreenHandlerRegistry.registerExtended(MMOFurniture.identifier("mail_box"), (syncId, inventory, buf) -> {
        MailBoxBlockEntity mailBoxBlockEntity = (MailBoxBlockEntity) inventory.player.world.getBlockEntity(buf.readBlockPos());
        if (mailBoxBlockEntity == null) throw new IllegalStateException("Crate block entity was not found");
        return new MailBoxScreenHandler(syncId, inventory, mailBoxBlockEntity);
    });
    private static Set<MailBoxBlock> mailBoxBlocks = new HashSet<>();

    public MailBoxModule() {
        super(false);
    }

    @Override
    public void register() {
        super.register();

        blockEntityType = Registry.register(Registry.BLOCK_ENTITY_TYPE, MMOFurniture.identifier("mail_box"),
                BlockEntityType.Builder.create(MailBoxBlockEntity::new, mailBoxBlocks.toArray(new MailBoxBlock[0])).build(null));

        mailBoxBlocks = null;
    }

    @Override
    protected void registerVariant(String variant, Block parent) {
        MailBoxBlock mailBox = new MailBoxBlock(parent);
        new MMOBlockRegistrar(mailBox)
                .register(MMOFurniture.identifier("%s_mail_box", variant), MMOFurniture.ITEM_GROUP);
        mailBoxBlocks.add(mailBox);
    }
}
