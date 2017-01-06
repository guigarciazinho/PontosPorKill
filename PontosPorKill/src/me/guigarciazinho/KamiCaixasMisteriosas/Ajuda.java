package me.guigarciazinho.KamiCaixasMisteriosas;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ajuda implements Listener, CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("ajuda")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§f[§bPremium§Craft§f]§c Somente para players.");
				return true;
			}
		}
		
		if(command.getName().equalsIgnoreCase("ajuda")){
			if(sender instanceof Player){
				if(args.length >= 0){
					Player p = (Player) sender;
					ArrayList<String> lorebau = new ArrayList<>();
					lorebau.add("§eClique aqui para entender um pouco mais sobre");
					lorebau.add("§2Pontos e §1§lCaixas §c§lMisteriosas");
					Inventory inv = Bukkit.createInventory(null, 9 * 3, "§b§lPremium§2§lHelp");
					ItemStack espaco = new ItemStack(Material.STAINED_GLASS, 1, (byte) 3);
					ItemMeta espacometa = espaco.getItemMeta();
					espacometa.setDisplayName("§eEspaco elegante §bPremium§2Craft");
					espaco.setItemMeta(espacometa);
					ItemStack bau = new ItemStack(Material.ENDER_CHEST, 1, (byte) 3);
					ItemMeta baumeta = bau.getItemMeta();
					baumeta.setLore(lorebau);
					baumeta.setDisplayName("§eAjuda com §1§lCaixas §c§lMisteriosas");
					bau.setItemMeta(baumeta);
					inv.setItem(0, espaco);
					inv.setItem(2, espaco);
					inv.setItem(3, espaco);
					inv.setItem(4, espaco);
					inv.setItem(5, espaco);
					inv.setItem(6, espaco);
					inv.setItem(7, espaco);
					inv.setItem(8, espaco);
					inv.setItem(1, espaco);
					inv.setItem(18, espaco);
					inv.setItem(19, espaco);
					inv.setItem(20, espaco);
					inv.setItem(21, espaco);
					inv.setItem(22, espaco);
					inv.setItem(23, espaco);
					inv.setItem(24, espaco);
					inv.setItem(25, espaco);
					inv.setItem(26, espaco);
					inv.setItem(13, bau);
					p.openInventory(inv);
					
					return true;
				}
				
			}
		}
		return false;
	}
	
	@EventHandler
	public void aoClicarInv(InventoryClickEvent e){
		if(e.getInventory().getTitle().equalsIgnoreCase("§b§lPremium§2§lHelp")){
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().contains("§eAjuda com §1§lCaixas §c§lMisteriosas")){
				p.closeInventory();
				p.sendMessage("§f[§bPremium§2Craft§f]§e Mate jogadores para ganhar pontos, ao chegar a marca de 50 pontos você poderá trocar seus pontos por uma caixas, a cada abate você recebe 1 ponto.");
				p.sendMessage("§f[§bPremium§2Craft§f]§e Use /pontos verificar, para saber quantos pontos você tem.");
				p.sendMessage("§f[§bPremium§2Craft§f]§e Para trocar seus pontos por uma §1§lCaixa §c§lMisteriosa§e, use /pontos usar.");
				p.sendMessage("§f[§bPremium§2Craft§f]§e Para ver suas §1§lCaixa §c§lMisteriosa§e, use /caixas, e para abri-las basta clicar nelas.");
				
				
			}
		}
	}

}
