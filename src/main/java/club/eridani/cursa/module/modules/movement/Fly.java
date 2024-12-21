package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Module(name = "Fly", category = Category.MOVEMENT)
public class Fly extends ModuleBase {

    Setting<Double> speed = setting("Speed", 1.0D, 0.1D, 5.0D);

    @Override
    public void onTick() {
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.allowFlying = true;

        // Adjust the player's motion based on input
        double speedMultiplier = speed.getValue();
        mc.player.motionY = ((mc.player.movementInput.jump ? 1 : 0) + (mc.player.movementInput.sneak ? -1 : 0)) * speedMultiplier;

        // Handle horizontal movement
        if (mc.player.movementInput.moveForward != 0 || mc.player.movementInput.moveStrafe != 0) {
            float yaw = mc.player.rotationYaw * (float) Math.PI / 180.0F;
            double forward = mc.player.movementInput.moveForward * speedMultiplier;
            double strafe = mc.player.movementInput.moveStrafe * speedMultiplier;
            mc.player.motionX += -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
            mc.player.motionZ += Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
        }
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.allowFlying = mc.player.isCreative();
    }
}
