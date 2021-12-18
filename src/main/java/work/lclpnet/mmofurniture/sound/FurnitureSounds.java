package work.lclpnet.mmofurniture.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.asm.mixin.common.SoundEventAccessor;
import work.lclpnet.mmofurniture.MMOFurniture;

import java.util.ArrayList;
import java.util.List;

public class FurnitureSounds {

    private static List<SoundEvent> sounds = new ArrayList<>();

    public static final SoundEvent BLOCK_CABINET_OPEN = register("block.cabinet.open"),
            BLOCK_CABINET_CLOSE = register("block.cabinet.close"),
            BLOCK_BEDSIDE_CABINET_OPEN = register("block.bedside_cabinet.open"),
            BLOCK_BEDSIDE_CABINET_CLOSE = register("block.bedside_cabinet.close"),
            BLOCK_BLINDS_OPEN = register("block.blinds.open"),
            BLOCK_BLINDS_CLOSE = register("block.blinds.close");

    public static SoundEvent register(String name) {
        Identifier loc = MMOFurniture.identifier(name);
        SoundEvent event = new SoundEvent(loc);
        sounds.add(event);
        return event;
    }

    public static void init() {
        if (sounds == null) throw new IllegalStateException("Sounds are already initialized");
        sounds.forEach(sound -> Registry.register(Registry.SOUND_EVENT, ((SoundEventAccessor) sound).getId(), sound));
        sounds = null;
    }
}
