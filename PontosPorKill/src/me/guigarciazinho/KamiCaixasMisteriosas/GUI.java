package me.guigarciazinho.KamiCaixasMisteriosas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class GUI implements CommandExecutor, Listener {

	public String prefixo = Main.getInstance().getConfig().getString("Prefixo").replace("&", "§");
	public static int task1;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		bd BD = new bd();
		Player p = (Player) sender;
		if (command.getName().equalsIgnoreCase("caixas")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(prefixo + " Somente para players.");
				return true;
			}
		}

		if (command.getName().equalsIgnoreCase("caixas")) {
			if (sender instanceof Player) {
				if (args.length > 0) {
					sender.sendMessage(prefixo + " Uso incorreto.");
					sender.sendMessage(prefixo + " §c Use /pontos usar para trocar seus pontos por caixas.");
					sender.sendMessage(prefixo + " §c Use /caixas para ir até o menu de caixas.");
					return true;
				}
			}
		}

		if (command.getName().equalsIgnoreCase("caixas")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					Inventory inv = Bukkit.createInventory(null, 9 * 5, "§1§lCaixas §c§lMisteriosas");
					int caixas = Main.configPontos.getInt("Jogadores." + p.getUniqueId().toString() + ".Caixas");

					ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
					SkullMeta m = (SkullMeta) item.getItemMeta();
					m.setOwner(p.getName());
					m.setDisplayName("§a" + p.getName());
					ArrayList<String> lore2 = new ArrayList<>();
					lore2.add("§3Essas §1Caixas §cMisteriosas§3 são suas.");
					lore2.add("§6Clique nelas para §2abrir§6.");
					m.setLore(lore2);
					item.setItemMeta(m);
					inv.setItem(44, item);
					if (Main.getInstance().getConfig().getBoolean("Use MySQL") == false) {
						for (int i = 0; i < caixas; i++) {
							ItemStack item2 = new ItemStack(Material.ENDER_CHEST, 1, (byte) 3);
							ItemMeta item2meta = item2.getItemMeta();
							item2meta.setDisplayName("§1Caixa §cMisteriosa " + prefixo);
							ArrayList<String> lore = new ArrayList<>();
							lore.add("§eO que será que tem dentro?");
							lore.add("§3Abra e descubra.");
							item2meta.setLore(lore);
							item2.setItemMeta(item2meta);
							inv.setItem(i, item2);
						}
						p.openInventory(inv);
					} else {
						for (int i = 0; i < BD.checarCaixas(p.getUniqueId()); i++) {
							ItemStack item2 = new ItemStack(Material.ENDER_CHEST, 1, (byte) 3);
							ItemMeta item2meta = item2.getItemMeta();
							item2meta.setDisplayName("§1Caixa §cMisteriosa " + prefixo);
							ArrayList<String> lore = new ArrayList<>();
							lore.add("§eO que será que tem dentro?");
							lore.add("§3Abra e descubra.");
							item2meta.setLore(lore);
							item2.setItemMeta(item2meta);
							inv.setItem(i, item2);

						}
						p.openInventory(inv);
					}

					return true;
				}
			}
		}

		return false;
	}

	public int xt = 1;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void aoClicarInv(InventoryClickEvent e) {
		bd BD = new bd();
		final Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getTitle().equalsIgnoreCase("§1§lCaixas §c§lMisteriosas")) {
			e.setCancelled(true);
			if (e.getCurrentItem().getItemMeta() == null) {
				return;
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§1Caixa §cMisteriosa " + prefixo)) {
				p.closeInventory();
				if (Main.getInstance().getConfig().getBoolean("Use MySQL") == false) {
					int caixa = Main.configPontos.getInt("Jogadores." + p.getUniqueId().toString() + ".Caixas") - 1;
					Main.configPontos.set("Jogadores." + p.getUniqueId() + ".Caixas", caixa);
					int caixas = Main.configPontos.getInt("Jogadores." + p.getUniqueId().toString() + ".Caixas");
					p.sendMessage(prefixo + "§e Você abriu uma caixa, agora restam: §c" + caixas);
					try {
						Main.configPontos.save(Main.configFile);
					} catch (IOException erro) {
						erro.printStackTrace();
					}
				} else {
					BD.usarCaixas(p.getUniqueId());
					p.sendMessage(
							prefixo + "§e Você abriu uma caixa, agora restam: §c" + BD.checarCaixas(p.getUniqueId()));
				}
				List<ItemStack> materials = new ArrayList<>();
				for (String itens : Main.getInstance().getConfig().getStringList("Itens")) {
					String[] s = itens.split("; ");
					int id = Integer.parseInt(s[0]);
					int quantidade = Integer.parseInt(s[1]);
					ItemStack i = new ItemStack(id, quantidade);
					if (s.length >= 4) {
					int encantamento = Integer.parseInt(s[2]);
					int level = Integer.parseInt(s[3]);
					i.addUnsafeEnchantment(Enchantment.getById(encantamento), level);
					materials.add(i);
					}else{
						materials.add(i);
					}
					
				}
				final Inventory inv2 = Bukkit.createInventory(null, 9 * 3, "§eSorteio §a" + p.getName());
				ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (byte) 3);
				ItemMeta item2meta = item2.getItemMeta();
				item2meta.setDisplayName("Spacer" + prefixo);
				item2.setItemMeta(item2meta);
				inv2.setItem(0, item2);
				inv2.setItem(2, item2);
				inv2.setItem(3, item2);
				inv2.setItem(4, item2);
				inv2.setItem(5, item2);
				inv2.setItem(6, item2);
				inv2.setItem(7, item2);
				inv2.setItem(8, item2);
				inv2.setItem(1, item2);
				inv2.setItem(18, item2);
				inv2.setItem(19, item2);
				inv2.setItem(20, item2);
				inv2.setItem(21, item2);
				inv2.setItem(22, item2);
				inv2.setItem(23, item2);
				inv2.setItem(24, item2);
				inv2.setItem(25, item2);
				inv2.setItem(26, item2);
				p.openInventory(inv2);
				task1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
					int index = 0;
					int slot = 9;
					int slot2 = 17;
					int repeticao = 0;

					@Override
					public void run() {
						if (index == 11) {
							index = 0;
						}
						materials.get(index);
						ItemStack item = new ItemStack(materials.get(index));
						inv2.setItem(slot, item);
						inv2.setItem(slot2, item);
						if (slot == 17) {
							slot = 9;

						}
						if (slot2 == 9) {
							slot2 = 17;

						}
						inv2.setItem(13, item);
						repeticao++;
						slot2--;
						slot++;
						index++;
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
						p.openInventory(inv2);
						if (repeticao == 50) {

							ItemStack barreira = new ItemStack(Material.AIR, 1, (byte) 3);
							inv2.setItem(9, barreira);
							inv2.setItem(10, barreira);
							inv2.setItem(11, barreira);
							inv2.setItem(12, barreira);
							inv2.setItem(14, barreira);
							inv2.setItem(15, barreira);
							inv2.setItem(16, barreira);
							inv2.setItem(17, barreira);
							int random = new Random().nextInt(10);
							ItemStack item2 = new ItemStack(materials.get(random));
							inv2.setItem(13, item2);
							if (inv2.getItem(13) != null) {
								ItemStack premio = inv2.getItem(13);
								p.getInventory().addItem(premio);
								p.sendMessage(
										prefixo + "§e Fim do sorteio, o premio foi adicionado no seu inventário.");
								p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 3, 1);
								p.playSound(p.getLocation(), Sound.FIREWORK_TWINKLE2, 3, 1);
							} else {
								p.sendMessage(prefixo + "§e Que pena. Parece que você não ganhou §cnada");
							}
							Bukkit.getScheduler().cancelTask(task1);
						}
					}
				}, 0, 2);
			}
		}
	}

	@EventHandler
	public void aoAbrirInv(InventoryOpenEvent e) {
		Player p = (Player) e.getPlayer();
		if (e.getInventory().getTitle().equalsIgnoreCase("§1§lCaixas §c§lMisteriosas")) {
			p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);

		}
	}

	@EventHandler
	public void aoClicarInve(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equalsIgnoreCase("§eSorteio §a" + p.getName())) {
			e.setCancelled(true);
		}

	}
	


}
