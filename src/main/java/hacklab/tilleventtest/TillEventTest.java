package hacklab.tilleventtest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TillEventTest extends JavaPlugin implements Listener {

    LevelStatus ls = new LevelStatus();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("thisPluginIsTillEventTest");
    }

    /*
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e){
        ItemStack i = e.getItem();
        if(i.getType() == Material.WOODEN_HOE || i.getType() == Material.STONE_HOE || i.getType() == Material.IRON_HOE || i.getType() == Material.GOLDEN_HOE || i.getType() == Material.DIAMOND_HOE || i.getType() == Material.NETHERITE_HOE){
            e.getPlayer().sendMessage("お前、耕したな");
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1, 1);
        }
    }
     */

    public void getTill(Player player){
        Till till = Till.getInstance(this, player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Material m = e.getItem().getType();
        Player p = e.getPlayer();
        if(e.getItem() == null)p.sendMessage("素手じゃ耕せねーよ");
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (m == Material.WOODEN_HOE || m == Material.STONE_HOE || m == Material.IRON_HOE || m == Material.GOLDEN_HOE || m == Material.DIAMOND_HOE || m == Material.NETHERITE_HOE) {
                Block b = e.getClickedBlock();
                if (b.getType() == Material.DIRT || b.getType() == Material.GRASS_BLOCK) {

                    //playSound
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                    //metadata
                    Till till = Till.getInstance(this, p);

                    if(b.getType() == Material.DIRT)till.expup(9);
                    if(b.getType() == Material.GRASS_BLOCK)till.expup(19);

                    if(ls.getNeedNextExp(till.getLevel()) <= till.getExp()){
                        till.levelup();
                    }

                    //particle
                    Location loc = e.getClickedBlock().getLocation();
                    loc.add(0.5,1,0.5);
                    loc.getWorld().spawnParticle(
                            Particle.VILLAGER_HAPPY, // パーティクルの種類
                            loc, // 発生させる場所
                            4,// 発生させる数
                            0.3, // 散開させるXの範囲
                            0, // 散開させるYの範囲
                            0.3 // 散開させるZの範囲
                    );
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 0)return false;
        Player p = (Player)sender;

        Till till = Till.getInstance(this, p);

        if(args[0].equals("level")){
            sender.sendMessage(String.valueOf(till.getLevel()));
            sender.sendMessage(String.valueOf(ls.calLevel(till.getExp())) );

            System.out.println(till.getLevel());
            System.out.println(ls.calLevel(till.getExp()));

        }

        return true;
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
