package work.lclpnet.mmofurniture;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import work.lclpnet.mmofurniture.module.*;
import work.lclpnet.mmofurniture.network.FurnitureNetworking;
import work.lclpnet.mmofurniture.sound.FurnitureSounds;

import java.util.Set;

public class MMOFurniture implements ModInitializer {

    public static final String MOD_ID = "mmofurniture";

    public static final Set<IModule> MODULES = ImmutableSet.of(
            new TableModule(),
            new ChairModule(),
            new CoffeeTableModule(),
            new CabinetModule(),
            new BedsideCabinetModule(),
            new DeskModule(),
            new DeskCabinetModule(),
            new SofaModule(),
            new BlindsModule(),
            new UpgradedFenceModule(),
            new UpgradedGateModule(),
            new CratesModule(),
            new ParkBenchModule(),
            new MailBoxModule(),
            new HedgeModule()
    );

    @Override
    public void onInitialize() {
        FurnitureNetworking.registerPackets();
        FurnitureNetworking.registerServerPacketHandlers();

        MODULES.forEach(IModule::register);
        FurnitureSounds.init();
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Identifier identifier(String format, Object... args) {
        return identifier(String.format(format, args));
    }
}
