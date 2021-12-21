package work.lclpnet.mmofurniture.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.inventory.MailBoxScreenHandler;

public class MailBoxScreen extends HandledScreen<MailBoxScreenHandler> {

    private static final Identifier GUI_TEXTURE = MMOFurniture.identifier("textures/gui/container/mail_box.png");

    public MailBoxScreen(MailBoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 132;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        if (client == null) return;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(GUI_TEXTURE);
        int startX = (this.width - this.backgroundWidth) / 2;
        int startY = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, startX, startY, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title.getString(), 8.0F, 6.0F, 0x404040);
        this.textRenderer.draw(matrices, this.playerInventory.getDisplayName().getString(), 8.0F, (float) (this.backgroundHeight - 96 + 2), 0x404040);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
