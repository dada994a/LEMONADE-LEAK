package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.common.annotations.Parallel;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashSet;
import java.util.Set;

@Parallel
@Module(name = "AutoEzzz", category = Category.MISC)
public class AutoEz extends ModuleBase {

    private final Set<EntityLivingBase> messagedEnemies = new HashSet<>(); // 送信済みの敵を記録
    private EntityLivingBase lastAttackedEnemy;

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase enemy = mc.player.getLastAttackedEntity();

        if (enemy != null) {
            if (enemy.isDead) {
                double distance = mc.player.getDistance(enemy);
                if (distance <= 30.0 && !messagedEnemies.contains(enemy)) {
                    mc.player.sendChatMessage("Ezzz " + enemy.getName() + " is an idiot" + " Lemonade on Top!!");
                    messagedEnemies.add(enemy); // 送信済みの敵としてセットに追加
                    lastAttackedEnemy = enemy;
                }
            } else {
                // 敵が生き返った場合、セットから削除
                if (messagedEnemies.contains(enemy)) {
                    messagedEnemies.remove(enemy);
                }
            }
        }
    }
}
