package work.lclpnet.mmofurniture.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import work.lclpnet.mmofurniture.entity.SeatEntity;

public class SeatRenderer extends EntityRenderer<SeatEntity> {

    public SeatRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(SeatEntity entity) {
        return null;
    }

    @Override
    protected void renderLabelIfPresent(SeatEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

    }
}
