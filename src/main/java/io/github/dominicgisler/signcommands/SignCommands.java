package io.github.dominicgisler.signcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SignCommands extends JavaPlugin implements Listener {
    private final String SIGN_HEAD = "[command]";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (Objects.requireNonNull(e.getLine(0)).equalsIgnoreCase(SIGN_HEAD)) {
            e.setLine(0, ChatColor.BOLD + SIGN_HEAD);
            for (int i = 1; i <= 3; i++) {
                String line = e.getLine(i);
                if (line != null && line.startsWith("//")) {
                    e.setLine(i, ChatColor.GRAY + line);
                }
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b != null) {
                if (b.getState() instanceof Sign) {
                    Sign s = (Sign) b.getState();
                    if (s.getLine(0).equalsIgnoreCase(ChatColor.BOLD + SIGN_HEAD)) {
                        StringBuilder command = new StringBuilder();
                        for (int i = 1; i <= 3; i++) {
                            String line = s.getLine(i);
                            if (!line.startsWith(ChatColor.GRAY + "//")) {
                                command.append(line);
                            }
                        }
                        Player p = e.getPlayer();
                        p.performCommand(command.toString());
                    }
                }
            }
        }
    }
}
