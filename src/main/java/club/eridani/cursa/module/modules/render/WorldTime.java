package club.eridani.cursa.module.modules.render;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module(name = "WorldTime", category = Category.RENDER)
public class WorldTime extends ModuleBase {

    private final Minecraft mc = Minecraft.getMinecraft();
    private Setting<Integer> worldTime; // クライアント側で固定する時間

    public WorldTime() {
        worldTime = setting("WorldTime", 18000, 0, 24000); // クライアント側の時間を設定（0から24000）
    }

    @Override
    public void onEnable() {
        if (mc.world != null) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.world != null) {
            setClientWorldTime(worldTime.getValue());
        }
    }

    private void setClientWorldTime(int time) {
        World world = mc.world;
        if (world != null) {
            world.setTotalWorldTime(time);
            world.setWorldTime(time);
        }
    }
}
