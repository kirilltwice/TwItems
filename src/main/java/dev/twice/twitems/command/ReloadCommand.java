package dev.twice.twitems.command;

import dev.twice.twitems.ItemsPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ReloadCommand implements CommandExecutor {

    private final ItemsPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && "reload".equals(args[0])) {
            if (!sender.hasPermission("twitems.reload")) {
                sender.sendMessage(ChatColor.RED + "У вас нет прав на выполнение этой команды");
                return true;
            }

            try {
                plugin.reload();
                sender.sendMessage(ChatColor.GREEN + "Конфигурация перезагружена!");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Ошибка при перезагрузке");
            }
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Использование: /twitems reload");
        return true;
    }
}