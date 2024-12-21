package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Module(name = "Speed", category = Category.MOVEMENT)
public class Speed extends ModuleBase {

    public Setting<Double> speed = setting("Speed", 2.5, 1.0, 3.5);

    @Override
    public void onTick() {
        if (mc.player == null) return;
        float forward = mc.player.moveForward;
        float strafe = mc.player.moveStrafing;
        float yaw = mc.player.rotationYaw;
        if (forward != 0 || strafe != 0) {
            float angle = yaw * ((float) Math.PI / 180);
            double motionX = 0;
            double motionZ = 0;
            if (forward > 0) {
                motionX += -Math.sin(angle) * speed.getValue();
                motionZ += Math.cos(angle) * speed.getValue();
            } else if (forward < 0) {
                motionX += Math.sin(angle) * speed.getValue();
                motionZ += -Math.cos(angle) * speed.getValue();
            }
            if (strafe > 0) {
                motionX += Math.cos(angle) * speed.getValue();
                motionZ += Math.sin(angle) * speed.getValue();
            } else if (strafe < 0) {
                motionX += -Math.cos(angle) * speed.getValue();
                motionZ += -Math.sin(angle) * speed.getValue();
            }
            mc.player.motionX = motionX;
            mc.player.motionZ = motionZ;
            double totalMotion = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if (totalMotion > speed.getValue()) {
                mc.player.motionX = (motionX / totalMotion) * speed.getValue();
                mc.player.motionZ = (motionZ / totalMotion) * speed.getValue();
            }
        } else {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }
}
