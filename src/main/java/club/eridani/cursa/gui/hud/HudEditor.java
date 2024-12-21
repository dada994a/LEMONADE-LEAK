package club.eridani.cursa.gui.hud;

import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.ModuleManager;
import club.eridani.cursa.gui.clickgui.sigma.Component;
import club.eridani.cursa.gui.clickgui.sigma.component.BindButton;
import club.eridani.cursa.gui.clickgui.sigma.component.BooleanButton;
import club.eridani.cursa.gui.clickgui.sigma.component.ModeButton;
import club.eridani.cursa.gui.clickgui.sigma.component.NumberSlider;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.utils.ColorUtil;
import club.eridani.cursa.utils.RenderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HudEditor {
    public static HudEditor INSTANCE;
    private List<Component> components = new ArrayList<>();
    private ModuleBase settingModule;
    private float offset, targetOffset;

    public HudEditor() {
        INSTANCE = this;
    }



    public void setSettingModule(ModuleBase module) {
        this.settingModule = module;
        components.clear();
        module.getSettings().forEach(s -> {
            if (s.getValue() instanceof Boolean) components.add(new BooleanButton(s));
            if (s.getValue() instanceof String) components.add(new ModeButton(s));
            if (s.getValue() instanceof Integer || s.getValue() instanceof Double || s.getValue() instanceof Float)
                components.add(new NumberSlider(s));
        });
        components.add(new BindButton(module));
    }


    public void update() {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        components.forEach(c -> c.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        components.forEach(c -> c.mouseReleased(mouseX, mouseY, state));
    }

    public void keyTyped(char typedChar, int key) {
        components.forEach(c -> c.keyTyped(typedChar, key));
    }


    public void scroll(float mouseX, float mouseY) {
        int dWheel = org.lwjgl.input.Mouse.getDWheel();
        if (dWheel < 0) targetOffset -= 15;
        else if (dWheel > 0) targetOffset += 15;
        offset += (targetOffset - offset) * 0.3;
        if (targetOffset > 0) targetOffset = 0;
    }


    public List<Component> getComponents() {
        return components;
    }
}
