package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;

@Module(name = "HudEditor" , category = Category.CLIENT)
public class HUD extends ModuleBase {
    public static HUD INSTANCE;

    public HUD() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            disable();
            return;
        }

    }

    @Override
    public void onTick() {

    }
}
