package dev.walshy.hardcoreslimefun;

import dev.walshy.hardcoreslimefun.events.AndroidEvents;
import dev.walshy.hardcoreslimefun.events.Events;
import dev.walshy.hardcoreslimefun.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

/*
 * Ideas:
 *   + Reset research on death
 *   + Chance to reset all research on death
 *   + Research fail chance
 *   + Android fail chance (farm/mine)
 *   * Enchanter fail chance
 *   * Disenchant fail chance
 *   * Block placer fail chance
 *   * Reactors actually explode
 *   * Explosives can damage/knockback
 *   * Ancient altar can fail and instead output some rubbish item :^)
 */
public class HardcoreSlimefun extends JavaPlugin {

    private static HardcoreSlimefun instance;

    @Override
    public void onEnable() {
        setInstance(this);
        saveDefaultConfig();

        Config.INSTANCE.load(this.getConfig());

        if (getServer().getPluginManager().getPlugin("Slimefun") == null) {
            getLogger().severe("no lo tienes instalado Slimefun! Este complemento no estará habilitado...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getConfig().getBoolean("auto-update") &&
            getDescription().getVersion().startsWith("Build")) {
        }

        new HardcoreMetrics(this).start();

        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new AndroidEvents(), this);

        if (getCommand("hardcoresf") != null) {
            getCommand("hardcoresf").setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!sender.hasPermission("hardcoreslimefun.admin")) {
            sender.sendMessage("§cNo tienes permiso para administrar HardcoreSlimefun.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            boolean next = !Config.INSTANCE.isEnabled();
            getConfig().set("enabled", next);
            saveConfig();
            Config.INSTANCE.load(getConfig());
            sender.sendMessage("§6[HardcoreSlimefun] §eMecánicas " + (next ? "§aACTIVADAS" : "§cDESACTIVADAS"));
            return true;
        }

        if (args.length > 0 && (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("enable"))) {
            getConfig().set("enabled", true);
            saveConfig();
            Config.INSTANCE.load(getConfig());
            sender.sendMessage("§6[HardcoreSlimefun] §aMecánicas ACTIVADAS.");
            return true;
        }

        if (args.length > 0 && (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable"))) {
            getConfig().set("enabled", false);
            saveConfig();
            Config.INSTANCE.load(getConfig());
            sender.sendMessage("§6[HardcoreSlimefun] §cMecánicas DESACTIVADAS.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            Config.INSTANCE.load(getConfig());
            sender.sendMessage("§6[HardcoreSlimefun] §aConfiguración recargada. Estado actual: " + (Config.INSTANCE.isEnabled() ? "§aACTIVADO" : "§cDESACTIVADO"));
            return true;
        }

        sender.sendMessage("§6[HardcoreSlimefun] §eEstado actual: " + (Config.INSTANCE.isEnabled() ? "§aACTIVADO" : "§cDESACTIVADO"));
        sender.sendMessage("§7Uso: /hardcoresf <toggle | on | off | reload>");
        return true;
    }

    @Override
    public void onDisable() {
        setInstance(null);
    }

    public static HardcoreSlimefun getInstance() {
        return instance;
    }

    private static void setInstance(@Nullable HardcoreSlimefun plugin) {
        instance = plugin;
    }
}
