package club.eridani.cursa.module.modules.combat;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.GUIManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.event.events.render.RenderEvent;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.eridani.cursa.utils.*;
import net.minecraft.block.BlockAnvil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@Module(name = "Blocker", category = Category.COMBAT)
public class Blocker extends ModuleBase {
    public Setting<Boolean> cevBreaker = setting("CevBreaker", true);
    public Setting<Boolean> Anvil = setting("Anvil", true);
    public Setting<Boolean> packetPlace = setting("PacketPlace", true);
    public Setting<String> swing = setting("Swing", "Mainhand", "Mainhand", "Offhand", "None");
    public Setting<Boolean> render = setting("Render", true);
    public Setting<Integer> cevRed = setting("CevRed", 255, 0, 255);
    public Setting<Integer> cevGreen = setting("CevGreen", 0, 0, 255);
    public Setting<Integer> cevBlue = setting("CevBlue", 0, 0, 255);
    public Setting<Integer> cevAlpha = setting("CevAlpha", 70, 0, 255);
    private List<BlockPos> cevPositions = new ArrayList<>();

    @Override
    public void onDisable() {
        cevPositions = new ArrayList<>();
    }

    @Override
    public void onTick() {
        if (cevBreaker.getValue()) blockCev();
    }

    @Override
    public void onRenderWorld(RenderEvent event) {
        if (!render.getValue()) return;

        for (BlockPos pos : cevPositions) {
            CursaTessellator.prepare(GL_QUADS);
            int color = new Color(cevRed.getValue(), cevGreen.getValue(), cevBlue.getValue(), cevAlpha.getValue()).getRGB();
            CursaTessellator.drawFullBox(pos.add(0, 1, 0), 1f, color);
            CursaTessellator.release();
        }
    }

    public void blockCev() {
        BlockPos playerPos = EntityUtil.getEntityPos(mc.player);
        BlockPos[] offsets = new BlockPos[]{
                new BlockPos(0, 2, 0), // cev
                new BlockPos(1, 1, 0),
                new BlockPos(-1, 1, 0),
                new BlockPos(0, 1, 1),
                new BlockPos(0, 1, -1),
                new BlockPos(1, 1, 1),
                new BlockPos(1, 1, -1),
                new BlockPos(-1, 1, 1),
                new BlockPos(-1, 1, -1),
        };

        for (BlockPos offset : offsets) {
            BlockPos offsetPos = playerPos.add(offset);
            Entity crystal = getCrystal(offsetPos);
            if (Objects.isNull(crystal)) continue;
            BlockPos crystalPos = EntityUtil.getEntityPos(crystal).add(0, -1, 0);
            if (!BlockUtil.getBlock(crystalPos).equals(Blocks.AIR) && !BlockUtil.getBlock(crystalPos).equals(Blocks.OBSIDIAN))
                continue;

            if (!BlockUtil.getBlock(playerPos.add(0, 2, 0)).equals(Blocks.AIR)){
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, playerPos.getY() + 0.21, mc.player.posZ, false));
            }
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));

            if (!swing.getValue().equals("None"))
                mc.player.connection.sendPacket(new CPacketAnimation(swing.getValue().equals("Mainhand") ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));

            if (!cevPositions.contains(crystalPos)) cevPositions.add(crystalPos);
        }

        // アンビルブロッカー
        if (Anvil.getValue()) {
            if (isAnvilAbove(playerPos) || isFallingAnvilAbove(playerPos)) {
                int obby = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);
                if (obby != -1) {
                    InventoryUtil.push();
                    mc.player.inventory.currentItem = obby;
                    mc.playerController.updateController();

                    BlockInteractionHelper.placeBlock(playerPos.add(0, 2, 0), packetPlace.getValue());
                    InventoryUtil.pop();
                }
            }
        }

        Iterator<BlockPos> iterator = cevPositions.iterator();
        while(iterator.hasNext()){
            BlockPos pos = iterator.next();
            if (!Objects.isNull(getCrystal(pos))) continue;

            int obby = InventoryUtil.getBlockHotbar(Blocks.OBSIDIAN);
            if (obby == -1) continue;

            InventoryUtil.push();
            mc.player.inventory.currentItem = obby;
            mc.playerController.updateController();

            if (BlockUtil.getBlock(pos).equals(Blocks.AIR)) {
                BlockInteractionHelper.placeBlock(pos, packetPlace.getValue());
                BlockInteractionHelper.rightClickBlock(pos , EnumFacing.UP , packetPlace.getValue());
            } else {
                BlockInteractionHelper.placeBlock(pos.add(0 , 1 , 0), packetPlace.getValue());
                InventoryUtil.pop();
            }

            iterator.remove();
        }
    }
    private boolean isAnvilAbove(BlockPos playerPos) {
        BlockPos anvilPos1 = playerPos.add(0,1,0);// 頭上1マス
        BlockPos anvilPos2 = playerPos.add(0, 2, 0); // 頭上2マス
        BlockPos anvilPos3 = playerPos.add(0, 3, 0); // 頭上3マス
        return BlockUtil.getBlock(anvilPos2) instanceof BlockAnvil || BlockUtil.getBlock(anvilPos3) instanceof BlockAnvil || BlockUtil.getBlock(anvilPos1) instanceof BlockAnvil;
    }
    private boolean isFallingAnvilAbove(BlockPos playerPos) {
        return mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityFallingBlock)
                .anyMatch(entity -> {
                    EntityFallingBlock fallingBlock = (EntityFallingBlock) entity;
                    return fallingBlock.getBlock().getBlock() instanceof BlockAnvil &&
                            (fallingBlock.getPosition().equals(playerPos.add(0, 1, 0)) ||
                            fallingBlock.getPosition().equals(playerPos.add(0, 2, 0)) ||
                                    fallingBlock.getPosition().equals(playerPos.add(0, 3, 0)));//落下中のアンビル検知
                });
    }

    private Entity getCrystal(BlockPos pos) {
        return mc.world.loadedEntityList.stream()
                .filter(e -> e instanceof EntityEnderCrystal)
                .filter(e -> EntityUtil.getEntityPos(e).add(0, -1, 0).equals(pos))
                .min(Comparator.comparing(PlayerUtil::getDistance)).orElse(null);
    }
}
