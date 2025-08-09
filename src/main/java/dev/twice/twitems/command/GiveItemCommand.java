package dev.twice.twitems.command;

import dev.twice.twitems.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GiveItemCommand implements CommandExecutor, TabCompleter {

    private final ItemService itemService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /giveitem <игрок> <предмет>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден");
            return true;
        }

        var itemOpt = itemService.getItemByMaterial(args[1]);
        if (itemOpt.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Предмет не найден");
            return true;
        }

        var item = itemOpt.get();
        var remaining = target.getInventory().addItem(item);

        if (!remaining.isEmpty()) {
            remaining.values().forEach(drop ->
                    target.getWorld().dropItem(target.getLocation(), drop));
        }

        sender.sendMessage(ChatColor.GREEN + "Предмет выдан игроку " + target.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return itemService.getMaterialNames().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}