package club.eridani.cursa.module.modules.movement;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;

@Module(name = "BoatFly", category = Category.MOVEMENT)
public class BoatFly extends ModuleBase {

    Setting<Double> speed = setting("Speed", 1.0D, 0.1D, 5.0D);
    Setting<Double> verticalSpeed = setting("Vertical Speed", 1.0D, 0.0D, 5.0D); // 上下移動速度の設定

    @Override
    public void onTick() {
        // プレイヤーがボートに乗っているか確認
        if (mc.player.getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat) {
            // 上下の移動速度を設定
            if (mc.player.movementInput.jump) {
                // ジャンプ入力がある場合、上昇速度を適用
                mc.player.getRidingEntity().motionY = verticalSpeed.getValue();
            } else if (mc.player.movementInput.sneak && verticalSpeed.getValue() > 0) {
                // スニーク入力があり、上下移動速度が0より大きい場合、落下速度を適用
                mc.player.getRidingEntity().motionY = -verticalSpeed.getValue();
            } else {
                // 入力がないか、上下移動速度が0の場合、上下の動きをリセット
                mc.player.getRidingEntity().motionY = 0;
            }

            // 水平移動の処理
            float yaw = mc.player.rotationYaw * (float) Math.PI / 180.0F;
            double forward = mc.player.movementInput.moveForward * speed.getValue();
            double strafe = mc.player.movementInput.moveStrafe * speed.getValue();

            // 移動入力がある場合
            if (mc.player.movementInput.moveForward != 0 || mc.player.movementInput.moveStrafe != 0) {
                mc.player.getRidingEntity().motionX += -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
                mc.player.getRidingEntity().motionZ += Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
            } else {
                // 移動入力がない場合、ボートを停止
                mc.player.getRidingEntity().motionX = 0;
                mc.player.getRidingEntity().motionZ = 0;
            }
        }
    }

    @Override
    public void onDisable() {
        // モジュールが無効になったときにボートの動きをリセット
        if (mc.player.getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat) {
            mc.player.getRidingEntity().motionX = 0;
            mc.player.getRidingEntity().motionY = 0;
            mc.player.getRidingEntity().motionZ = 0;
        }
    }
}
