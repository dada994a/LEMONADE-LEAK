package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.entity.Entity;

@Module(name = "Esp", category = Category.RENDER)
public class Esp extends ModuleBase {

    @Override
    public void onRenderWorld(RenderEvent event) {
        for (Entity entity : mc.world.loadedEntityList) {
            entity.setGlowing(true); // 一時的に発光をオンにする
        }
    }

    @Override
    public void onDisable() {
        // モジュールが無効化されたときに発光をオフにする
        for (Entity entity : mc.world.loadedEntityList) {
            entity.setGlowing(false);
        }
    }
}
