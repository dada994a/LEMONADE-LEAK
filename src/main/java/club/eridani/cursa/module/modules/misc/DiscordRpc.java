package club.eridani.cursa.module.modules.misc;

import club.eridani.cursa.Cursa;
import club.eridani.cursa.client.RPCManager;
import club.eridani.cursa.common.annotations.Module;
import club.eridani.cursa.module.Category;
import club.eridani.cursa.module.ModuleBase;
import club.eridani.cursa.setting.Setting;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.util.Objects;

@Module(name = "DiscordRpc", category = Category.MISC)
public class DiscordRpc extends ModuleBase {

    public Setting<String> icon = setting("Icon", "lemonade", "lemonade", "kikyou", "Elegg", "hutao", "hotaru");

    public static RPCManager INSTANCE;
    private Thread _thread = null;


    public static void init() {
        INSTANCE = new RPCManager();
    }

    public void onEnable() {
        if (icon.getValue().equals("lemonade")) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "1298061385640706150";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageText = "";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    presence.details = "Welcome Lemonade";
                    presence.state = getDetails();
                    presence.largeImageKey = "logo";
                    presence.largeImageText = Cursa.MOD_VERSION;
                    lib.Discord_UpdatePresence(presence);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();
        }
        if (icon.getValue().equals("kikyou")) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "1299653580093128768";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageText = "";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    presence.details = "Welcome Lemonade";
                    presence.state = getDetails();
                    presence.largeImageKey = "logo";
                    presence.largeImageText = Cursa.MOD_VERSION;
                    lib.Discord_UpdatePresence(presence);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();

        }
        if (icon.getValue().equals("Elegg")) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "1299660889682870312";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageText = "";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    presence.details = "Welcome Lemonade";
                    presence.state = getDetails();
                    presence.largeImageKey = "logo";
                    presence.largeImageText = Cursa.MOD_VERSION;
                    lib.Discord_UpdatePresence(presence);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();

        }
        if (icon.getValue().equals("hutao")) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "1299660935807504394";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageText = "";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    presence.details = "Welcome Lemonade";
                    presence.state = getDetails();
                    presence.largeImageKey = "logo";
                    presence.largeImageText = Cursa.MOD_VERSION;
                    lib.Discord_UpdatePresence(presence);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();

        }
        if (icon.getValue().equals("hotaru")) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "1299660986479017994";
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            presence.largeImageText = "";
            _thread = new Thread(() ->
            {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    presence.details = "Welcome Lemonade";
                    presence.state = getDetails();
                    presence.largeImageKey = "logo";
                    presence.largeImageText = Cursa.MOD_VERSION;
                    lib.Discord_UpdatePresence(presence);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler");

            _thread.start();

        }

    }


    public void onDisable() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        _thread = null;
    }

    public String getDetails() {
        return Objects.isNull(mc.player) ? "MainMenu" : mc.player.getName();
    }

    public String getState() {
        return Objects.isNull(mc.player) ? "" : Objects.isNull(mc.getCurrentServerData()) ? "SinglePlayer" : mc.getCurrentServerData().serverIP;
    }
}