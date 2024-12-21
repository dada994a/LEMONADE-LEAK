package club.eridani.cursa.gui.mainmenu;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.gui.mainmenu.particle.ParticleManager;
import club.eridani.cursa.utils.AnimationUtils;
import club.eridani.cursa.utils.ClickUtils;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

public class MainMenu extends GuiScreen {

    private ResourceLocation background1, background2, background3;
    private int animatedX, animatedY;
    private List<CustomButton> buttons;
    private ParticleManager pm;
    private int tick, bg;

    // 追加: By Lemonade Teamのテキストの位置を保存
    private int textX, textY, textWidth;

    public MainMenu() {
        background1 = new ResourceLocation("lemonade/background/mainmenu1.png");
        background2 = new ResourceLocation("lemonade/background/mainmenu2.png");
        background3 = new ResourceLocation("lemonade/background/mainmenu3.png");
    }

    @Override
    public void initGui() {
        tick = 0;
        buttons = new LinkedList<>();
        pm = new ParticleManager();
        buttons.add(new CustomButton("SinglePlayer", new ResourceLocation("lemonade/icon/singleplayer.png"), new GuiWorldSelection(this)));
        buttons.add(new CustomButton("MultiPlayer", new ResourceLocation("lemonade/icon/multiplayer.png"), new GuiMultiplayer(this)));
        buttons.add(new CustomButton("Language", new ResourceLocation("lemonade/icon/language.png"), new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager())));
        buttons.add(new CustomButton("Settings", new ResourceLocation("lemonade/icon/setting.png"), new GuiOptions(this, mc.gameSettings)));
        buttons.add(new CustomButton("Quit", new ResourceLocation("lemonade/icon/quit.png"), null));
        super.initGui();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        tick++;
        if (tick % (20 * 30) == 0) {
            bg = (bg + 1) % 3;
        }

        ResourceLocation[] background = new ResourceLocation[]{background1, background2, background3};
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        ScaledResolution sr = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(background[bg]);
        drawModalRectWithCustomSizedTexture(-this.animatedX / 4, -this.animatedY / 3, 0, 0, sr.getScaledWidth() / 3 * 4, sr.getScaledHeight() / 3 * 4, sr.getScaledWidth() / 3 * 4, sr.getScaledHeight() / 3 * 4);

        int xOffset = sr.getScaledWidth() / 2 - 180;
        for (CustomButton cb : buttons) {
            cb.drawScreen(xOffset, sr.getScaledHeight() / 2 - 20, mouseX, mouseY);
            xOffset += 80;
        }

        // By Lemonade Teamのテキストの描画
        textX = sr.getScaledWidth() - FontManager.jelloLargeFont.getStringWidth("By Lemonade Team") - 4;
        textY = sr.getScaledHeight() - 12;
        FontManager.jelloLargeFont.drawString("By Lemonade Team", textX, textY, 0xd0ffffff);
        textWidth = FontManager.jelloLargeFont.getStringWidth("By Lemonade Team");

        super.drawScreen(mouseX, mouseY, partialTicks);
        pm.render(mouseX, mouseY, sr);
        animatedX += ((mouseX - animatedX) / 1.8) + 0.1;
        animatedY += ((mouseY - animatedY) / 1.8) + 0.1;
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (CustomButton cb : buttons) {
            cb.onClicked(mouseX, mouseY, mouseButton);
        }

        //By Lemonade Teamをクリックしたときの処理
        if (mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + FontManager.jelloLargeFont.getHeight()) {
            // 指定したリンクを開く処理
            openLink("https://discord.gg/zzyw8shjHu");
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    // リンクを開くためのメソッドを追加
    private void openLink(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomButton {

        private final ResourceLocation resource;
        private final GuiScreen parent;
        private float animatedSize;
        private int posX, posY;
        private final String name;

        public CustomButton(String name, ResourceLocation resource, GuiScreen parent) {
            this.resource = resource;
            this.parent = parent;
            this.name = name;
        }

        public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
            if (ClickUtils.isMouseHovering(posX, posY, 48, 48, mouseX, mouseY)) {
                animatedSize = AnimationUtils.animate(animatedSize, 30);
                FontManager.jelloFont.drawCenteredString(name, posX + 25, posY + 60, -1);
            } else animatedSize = AnimationUtils.animate(animatedSize, 25);
            GL11.glColor4f(1, 1, 1, 0.75f);
            mc.getTextureManager().bindTexture(resource);
            Gui.drawModalRectWithCustomSizedTexture(posX - (int) animatedSize / 2 + 25, posY - (int) animatedSize / 2 + 25, 0, 0, (int) (animatedSize * 1.5f), (int) (animatedSize * 1.5f), animatedSize * 1.5f, animatedSize * 1.5f);
            this.posX = posX;
            this.posY = posY;
        }

        public void onClicked(int mouseX, int mouseY, int mouseButton) {
            if (ClickUtils.isMouseHovering(posX, posY, 48, 48, mouseX, mouseY)) {
                if (parent == null) mc.shutdown();
                mc.displayGuiScreen(parent);
            }
        }
    }
}
