package club.eridani.cursa;

import club.eridani.cursa.client.*;
import club.eridani.cursa.concurrent.event.EventManager;
import club.eridani.cursa.concurrent.event.Listener;
import club.eridani.cursa.concurrent.event.Priority;
import club.eridani.cursa.event.events.client.InitializationEvent;
import club.eridani.cursa.module.modules.client.ClickGui;
import club.eridani.cursa.tasks.Tasks;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static club.eridani.cursa.concurrent.TaskManager.*;

public class Cursa {

    public static final String MOD_NAME = "Lemonade";
    public static final String MOD_VERSION = "0.0.1";

    public static final String AUTHOR = "LEMONADE TEAM";
    public static final String GITHUB = "https://github.com/Sm0kyday0/LemonadeClient";

    public static String CHAT_SUFFIX = "ʟᴇᴍᴏɴᴀᴅᴇ";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    private static Cursa instance;

    public Cursa() {
        instance = this;
    }

    @Listener(priority = Priority.HIGHEST)
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @Listener(priority = Priority.HIGHEST)
    public void initialize(InitializationEvent.Initialize event) {
        try {
            long tookTime = runTiming(() -> {
                Display.setTitle(MOD_NAME + "-" + MOD_VERSION);
                FontManager.init();
                log.info("Loading Module Manager");
                // ModuleManager is partial parallel loadable
                ModuleManager.init();
                // Parallel load managers
                runBlocking(it -> {
                    it.launch(GUIManager::init);
                    it.launch(CommandManager::init);
                    it.launch(FriendManager::init);
                    it.launch(ConfigManager::init);
                    it.launch(RPCManager::init);
                });
            });

            log.info("Took " + tookTime + "ms to launch LemonadeClient!");
        } finally {

        }
    }

    @Listener(priority = Priority.HIGHEST)
    public void postInitialize(InitializationEvent.PostInitialize event) {
        launch(Tasks.LoadConfig);
        ClickGui.instance.disable();
    }

    public static boolean isMainThread(Thread thread) {
        return thread == mainThread;
    }

    public static EventManager EVENT_BUS = new EventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    public static Cursa getInstance() {
        if (instance == null) instance = new Cursa();
        return instance;
    }
}
