package club.eridani.cursa.module.modules.client;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.FontManager;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.render.RenderOverlayEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.RenderHelper;

import java.awt.Color;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Module(name = "ActiveModuleList", category = Category.CLIENT)
public class ActiveModuleList extends ModuleBase {

    Setting<String> listPos = setting("ListPos", "RightTop", listOf("RightTop", "RightDown", "LeftTop", "LeftDown"));

    // モジュールごとのアニメーション開始時刻を管理するマップ
    private final Map<ModuleBase, Long> moduleAnimationMap = new HashMap<>();
    private static final long ANIMATION_DURATION = 500; // アニメーションの継続時間（ミリ秒）

    @Override
    public void onRender(RenderOverlayEvent event) {

        int startX = RenderHelper.getStart(event.getScaledResolution(), listPos.getValue()).x;
        int startY = RenderHelper.getStart(event.getScaledResolution(), listPos.getValue()).y;

        if (mc.player.getActivePotionEffects().size() > 0 && listPos.getValue().equals("RightTop")) {
            startY += 26;
        }

        int index = 0;

        // モジュールリストを幅順にソート
        List<ModuleBase> moduleList = Cursa.MODULE_BUS.getModules().stream()
                .sorted(Comparator.comparing(it -> -FontManager.getWidth(it.name))).collect(Collectors.toList());

        for (ModuleBase module : moduleList) {
            // 初回表示時のアニメーション開始時間を登録
            if (!moduleAnimationMap.containsKey(module)) {
                moduleAnimationMap.put(module, System.currentTimeMillis());
            }

            // アニメーションの進行度を計算
            long elapsedTime = System.currentTimeMillis() - moduleAnimationMap.get(module);
            float animationProgress = Math.min(1.0f, (float) elapsedTime / ANIMATION_DURATION);

            // アルファ値を計算してフェードイン効果を実現
            int alpha = (int) (255 * animationProgress);
            int baseColor = GUIManager.isRainbow() ? rainbow(index * 100) : GUIManager.getColor3I();
            int color = (baseColor & 0x00FFFFFF) | (alpha << 24); // アルファチャンネルの調整

            index++;

            // listPosの設定に従い、描画位置を調整
            switch (listPos.getValue()) {
                case "RightDown": {
                    FontManager.draw(module.name, startX - FontManager.getWidth(module.name), startY - FontManager.getHeight() * index, color);
                    break;
                }
                case "LeftTop": {
                    FontManager.draw(module.name, startX, startY + 3 + FontManager.getHeight() * (index - 1), color);
                    break;
                }
                case "LeftDown": {
                    FontManager.draw(module.name, startX, startY - FontManager.getHeight() * index, color);
                    break;
                }
                default: {
                    FontManager.draw(module.name, startX - FontManager.getWidth(module.name), startY + 3 + FontManager.getHeight() * (index - 1), color);
                    break;
                }
            }
        }

        // リストから削除されたモジュールのエントリをマップからも削除
        moduleAnimationMap.keySet().removeIf(module -> !moduleList.contains(module));
    }

    // レインボーカラー生成メソッド
    public int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }
}
