package dev.twice.twitems.command;

import dev.twice.twitems.Main;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GiveItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /giveitem <игрок> <предмет>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден");
            return true;
        }

        String materialName = args[1];

        CompletableFuture.supplyAsync(() -> {
            return Main.getInstance().getConfigManager().getItemByMaterial(materialName);
        }).thenAcceptAsync(item -> {
            if (item == null) {
                sender.sendMessage(ChatColor.RED + "Предмет не найден");
                return;
            }

            Map<Integer, ItemStack> remainingItems = target.getInventory().addItem(item);

            if (!remainingItems.isEmpty()) {
                for (ItemStack remaining : remainingItems.values()) {
                    target.getWorld().dropItem(target.getLocation(), remaining);
                }
            }

            sender.sendMessage(ChatColor.GREEN + "Успешно выдал предмет " + target.getName());
        }, runnable -> Bukkit.getScheduler().runTask(Main.getInstance(), runnable));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }

        if (args.length == 2) {

            return Main.getInstance().getConfigManager().getAllItems().stream()
                    .map(item -> item.getType().name())
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}