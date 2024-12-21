package club.eridani.cursa.gui.hud.component;

import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.gui.hud.Hud;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

@Module(name = "PhaseTime", category = Category.HUD)
public class PhaseTime extends Hud {
    private final Minecraft mc = Minecraft.getMinecraft();
    private long timeInside = 0;
    private long lastTimeCheck = System.currentTimeMillis();

    @Override
    public void renderHud() {
        // プレイヤーの位置
        BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        boolean isInsideBlock = !mc.world.isAirBlock(playerPos); // ブロック内かどうかを確認
        if (isInsideBlock) {
            long currentTime = System.currentTimeMillis();
            timeInside += currentTime - lastTimeCheck;
            lastTimeCheck = currentTime;

            // 表示する文字列を準備
            String str = "Phase Time: " + String.format("%.1f", timeInside / 1000.0);
            RenderUtil.drawString(str, getX(), getY(), Color.WHITE.getRGB(), true); // 文字を白色で描画
            setSize(RenderUtil.getStringWidth(str), RenderUtil.getStringHeight()); // サイズを設定
        } else {
            // ブロック外の場合、表示を消す
            timeInside = 0; // タイマーをリセット
            lastTimeCheck = System.currentTimeMillis();
            setSize(0, 0); // サイズを0に設定して非表示にする
        }
    }
}
