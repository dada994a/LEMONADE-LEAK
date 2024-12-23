package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.client.ConfigManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.clickgui.cursa.CursaClickGUI;
import club.eridani.cursa.gui.mainmenu.MainMenu;
import club.eridani.cursa.gui.clickgui.sigma.SigmaGui;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import org.lwjgl.input.Keyboard;

@Module(name = "ClickGUI", category = Category.CLIENT, keyCode = Keyboard.KEY_O)
public class ClickGui extends ModuleBase {
    public Setting<String> gui = setting("Type", "Cursa", "Cursa", "Sigma");

    public static ClickGui instance;

    public ClickGui() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;

        if (gui.getValue().equals("Cursa") && !(mc.currentScreen instanceof MainMenu))
            mc.displayGuiScreen(new CursaClickGUI());
        if (gui.getValue().equals("Sigma") && !(mc.currentScreen instanceof SigmaGui))
            mc.displayGuiScreen(new SigmaGui());
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof CursaClickGUI) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }


}
