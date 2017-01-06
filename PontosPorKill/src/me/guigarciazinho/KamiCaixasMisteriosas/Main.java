package me.guigarciazinho.KamiCaixasMisteriosas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static File configFile;
	public static YamlConfiguration configPontos;
	private static Main instance;

	@Override
	public void onEnable() {
		instance = this;
		System.out.println("==================");
		System.out.println("Plugin KamiCaixasMisteriosas foi habilitado");
		System.out.println("Criado por kami, guigarciazinho");
		System.out.println("==================");
		getCommand("caixas").setExecutor(new GUI());
		getServer().getPluginManager().registerEvents(new GUI(), this);
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		configFile = new File(getDataFolder(), "Pontos.yml");
		if (!configFile.exists() && getConfig().getBoolean("Use MySQL") == false) {
			saveResource("Pontos.yml", false);
		} else if (getConfig().getBoolean("Use MySQL") == true) {
			bd BD = new bd();
			BD.criar();
		}
		configPontos = YamlConfiguration.loadConfiguration(configFile);
		try {
			configPontos.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Desabilitando plugin...");
		getLogger().info("Desabilitado com sucesso!");
		getLogger().info("Criado por kami, guigarciazinho");
		HandlerList.unregisterAll();
	}

	public static Main getInstance() {
		return instance;
	}

	String prefixo = getConfig().getString("Prefixo").replace("&", "§");

	@EventHandler
	public void aoMatarJogar(PlayerDeathEvent e) {
		bd BD = new bd();
		Player m = e.getEntity();
		Player p = m.getKiller();
		if (p instanceof Player) {
			if (getConfig().getBoolean("Use MySQL") == true) {
				if (BD.criarReg(p.getUniqueId(), p.getName()) == false) {
					if (BD.checar(m.getUniqueId()) == true) {

						BD.win(p.getUniqueId());
						p.sendMessage(prefixo
								+ getConfig().getString("Matou").replace("&", "§").replace("@player", m.getName()));
						m.sendMessage(prefixo
								+ getConfig().getString("Morreu").replace("&", "§").replace("@player", p.getName()));
						return;
					} else {
						p.sendMessage(prefixo + getConfig().getString("Matou mas nao ganhou").replace("&", "§")
								.replace("@player", m.getName()));
						m.sendMessage(prefixo + getConfig().getString("Morreu mas nao perdeu").replace("&", "§"));
						return;
					}

				} else {
					p.sendMessage(prefixo + getConfig().getString("Inicio contagem").replace("&", "§"));
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
					return;
				}

			}

			if (!configPontos.contains("Jogadores." + p.getUniqueId())) {
				configPontos.set("Jogadores." + p.getUniqueId() + ".Nome", p.getName());
				configPontos.set("Jogadores." + p.getUniqueId() + ".Pontos", 5);
				configPontos.set("Jogadores." + p.getUniqueId() + ".Caixas", 3);
				p.sendMessage(prefixo + getConfig().getString("Inicio contagem").replace("&", "§"));
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				try {
					configPontos.save(configFile);
				} catch (IOException d) {
					d.printStackTrace();
				}
				return;

			} else if (configPontos.contains("Jogadores." + p.getUniqueId())) {
				int pontos = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Pontos") + 1;
				int pts = configPontos.getInt("Jogadores." + m.getUniqueId() + ".Pontos");
				if (pts > 0) {
					configPontos.set("Jogadores." + p.getUniqueId() + ".Pontos", pontos);
					p.sendMessage(
							prefixo + getConfig().getString("Matou").replace("&", "§").replace("@player", m.getName()));
					try {
						configPontos.save(configFile);
					} catch (IOException d) {
						d.printStackTrace();
					}
					return;
				} else if (pts <= 0) {
					p.sendMessage(prefixo + getConfig().getString("Matou mas nao ganhou").replace("&", "§")
							.replace("@player", m.getName()));
					return;
				}

			}

		}
		if (p instanceof Player) {
			if (!configPontos.contains("Jogadores." + m.getUniqueId())) {
				return;
			} else if (configPontos.contains("Jogadores." + m.getUniqueId())) {
				int pontos = configPontos.getInt("Jogadores." + m.getUniqueId() + ".Pontos") - 1;
				int pts = configPontos.getInt("Jogadores." + m.getUniqueId() + ".Pontos");
				if (pts > 0) {
					configPontos.set("Jogadores." + m.getUniqueId() + ".Pontos", pontos);
					m.sendMessage(prefixo
							+ getConfig().getString("Morreu").replace("&", "§").replace("@player", p.getName()));
				} else if (pts <= 0) {
					m.sendMessage(prefixo + getConfig().getString("Morreu mas nao perdeu").replace("&", "§"));

				}
			}

		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if (command.getName().equalsIgnoreCase("pontos")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(prefixo + " Somente para players.");
				return true;
			}

			if (sender instanceof Player) {

				if (args.length == 0 || args.length > 1) {
					return true;
				}

				bd BD = new bd();
				int pts = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Pontos");
				if (args.length == 1) {
					if ("usar".equalsIgnoreCase(args[0])) {
						ArrayList<String> cabeca = new ArrayList<>();
						ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
						SkullMeta m = (SkullMeta) item.getItemMeta();
						m.setOwner(p.getName());
						if (getConfig().getBoolean("Use MySQL") == false) {
							cabeca.add("§aVocê tem:§b " + pts + "§a pontos");
						} else {
							cabeca.add("§aVocê tem:§b " + BD.checarPontos(p.getUniqueId()) + "§a pontos");
						}
						m.setLore(cabeca);
						m.setDisplayName("§a" + p.getName());
						item.setItemMeta(m);
						ItemStack bau = new ItemStack(Material.ENDER_CHEST, 1, (byte) 3);
						ItemMeta baumeta = bau.getItemMeta();
						baumeta.setDisplayName("§1Caixa §cMisteriosa");
						ArrayList<String> lore = new ArrayList<>();
						lore.add("§cCusto: §e50 pontos");
						baumeta.setLore(lore);
						bau.setItemMeta(baumeta);
						Inventory inv = Bukkit.createInventory(null, 9 * 1, "§c§lComprar§c §1§lCaixas");
						inv.setItem(0, bau);
						inv.setItem(8, item);
						p.openInventory(inv);

					}
				}
			}
		}

		return false;
	}

	@EventHandler
	public void aoClicarLoja(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equalsIgnoreCase("§c§lComprar§c §1§lCaixas")) {

			if (getConfig().getBoolean("Use MySQL") == true) {
				e.setCancelled(true);
				if (e.getCurrentItem().getItemMeta() == null) {
					return;
				}
				bd BD = new bd();
				if (getConfig().getBoolean("Use MySQL") == true) {
					e.setCancelled(true);
					Player p = (Player) e.getWhoClicked();
					if (BD.checarCaixas(p.getUniqueId()) <= 43) {
						if (BD.checarPontos(p.getUniqueId()) >= 50) {
							if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§1Caixa §cMisteriosa")) {
								BD.comprou(p.getUniqueId());
								p.closeInventory();
								p.sendMessage(prefixo + "§2 Baú adquirido com sucesso");
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
								return;
							}

						} else {
							p.sendMessage(prefixo + "§c Você não tem pontos suficientes para comprar uma caixa.");
							p.closeInventory();
							return;
						}

					} else {
						p.sendMessage(prefixo
								+ "§c Você atingiu o limite de caixas. Abra ao menos uma, antes de comprar outra.");
						p.closeInventory();
						return;
					}

				}
			}

			if (getConfig().getBoolean("Use MySQL") == false) {
				Player p = (Player) e.getWhoClicked();
				int pontos1 = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Pontos");
				int pontos = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Pontos") - 50;
				int caixa = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Caixas") + 1;
				int caixacheck = configPontos.getInt("Jogadores." + p.getUniqueId() + ".Caixas");
				e.setCancelled(true);
				if (caixacheck <= 43) {
					if (pontos1 >= 50) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§1Caixa §cMisteriosa")) {
							configPontos.set("Jogadores." + p.getUniqueId() + ".Pontos", pontos);
							configPontos.set("Jogadores." + p.getUniqueId() + ".Caixas", caixa);
							p.closeInventory();
							p.sendMessage(prefixo + "§2 Baú adquirido com sucesso");
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
							try {
								configPontos.save(configFile);
							} catch (IOException d) {
								d.printStackTrace();
							}
							return;

						}
					}
				} else if (pontos1 <= 49 || configPontos.getString("Jogadores." + p.getUniqueId()) == null) {
					p.sendMessage(prefixo
							+ " §cVocê não tem pontos suficientes ou ainda não começou a contagem. Para dar inicio a contagem mate algum jogador. ");
					return;
				} else {
					p.sendMessage(
							prefixo + " §cVocê atingiu o limite de caixas. Abra ao menos uma antes de comprar outra.");
					return;
				}
			}

		}
	}
}
