package work.lclpnet.mmofurniture.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import work.lclpnet.mmocontent.networking.MMONetworking;
import work.lclpnet.mmofurniture.MMOFurniture;
import work.lclpnet.mmofurniture.client.gui.widget.button.IconButtonWidget;
import work.lclpnet.mmofurniture.inventory.CrateScreenHandler;
import work.lclpnet.mmofurniture.network.packet.LockCratePacket;

import java.util.UUID;

public class CrateScreen extends HandledScreen<CrateScreenHandler> {

    private static final Identifier GUI_TEXTURE = MMOFurniture.identifier("textures/gui/container/crate.png");
    private static final Identifier ICONS_TEXTURE = MMOFurniture.identifier("textures/gui/icons.png");

    private IconButtonWidget button;
    private boolean locked;
    private final PlayerEntity player;

    public CrateScreen(CrateScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.player = inventory.player;
    }

    @Override
    protected void init() {
        super.init();
        this.button = this.addDrawableChild(new IconButtonWidget(x + backgroundWidth + 2, this.y + 17, new TranslatableText("gui.button.cfm.lock"),
                button -> MMONetworking.sendPacketToServer(new LockCratePacket()), ICONS_TEXTURE, 0, 0));
        this.updateLockButton();
    }

    @Override
    public void handledScreenTick() {
        if (this.locked != this.handler.getCrateBlockEntity().isLocked()) {
            this.locked = this.handler.getCrateBlockEntity().isLocked();
            this.updateLockButton();
        }
    }

    private void updateLockButton() {
        this.locked = this.handler.getCrateBlockEntity().isLocked();
        this.button.setIcon(ICONS_TEXTURE, this.locked ? 0 : 16, 0);
        this.button.setMessage(new TranslatableText(this.locked ? "gui.button.cfm.locked" : "gui.button.cfm.unlocked"));
        UUID ownerUuid = this.handler.getCrateBlockEntity().getOwnerUuid();
        this.button.visible = ownerUuid == null || this.player.getUuid().equals(ownerUuid);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        if (this.button.isMouseOver(mouseX, mouseY))
            this.renderTooltip(matrices, new TranslatableText(this.locked ? "gui.button.cfm.locked" : "gui.button.cfm.unlocked"), mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title.getString(), 8.0F, 6.0F, 0x404040);
        this.textRenderer.draw(matrices, this.playerInventoryTitle.getString(), 8.0F, (float) (this.backgroundHeight - 96 + 2), 0x404040);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int startX = (this.width - this.backgroundWidth) / 2;
        int startY = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, startX, startY, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
