package work.lclpnet.mmofurniture.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import work.lclpnet.mmofurniture.module.ChairModule;

import java.util.List;

public class SeatEntity extends Entity {

    protected BlockPos source;

    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    private SeatEntity(World world, BlockPos source, double yOffset) {
        this(ChairModule.seatType, world);
        this.source = source;
        this.updatePosition(source.getX() + 0.5, source.getY() + yOffset, source.getZ() + 0.5);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void tick() {
        super.tick();
        if (source == null) {
            source = this.getBlockPos();
        }
        if (!this.world.isClient) {
            if (this.getPassengerList().isEmpty() || this.world.isAir(source)) {
                this.remove();
                world.updateComparators(getBlockPos(), world.getBlockState(getBlockPos()).getBlock());
            }
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.0D;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return true;
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public static ActionResult create(World world, BlockPos pos, double yOffset, PlayerEntity player) {
        if (!world.isClient) {
            List<SeatEntity> seats = world.getNonSpectatingEntities(SeatEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0));
            if (seats.isEmpty()) {
                SeatEntity seat = new SeatEntity(world, pos, yOffset);
                world.spawnEntity(seat);
                player.startRiding(seat, false);
            }
        }
        return ActionResult.SUCCESS;
    }
}
