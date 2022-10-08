package hacklab.tilleventtest;

import com.google.gson.Gson;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TillEventTest extends JavaPlugin implements Listener {

    class Till {
        int exp = 0;
        int level = 1;
        String name;
        void exp(int value){
            exp += value;
        }
        void level(){
            level ++;
        }
    }

    LevelStatus ls = new LevelStatus();

    Gson gson = new Gson();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("thisPluginIsTillEventTest");
    }

    public Till getTill(Player player) {
        Till till = null;

        if (!player.hasMetadata("Till")) {
            Till value = new Till();

            String jsonString = gson.toJson(value);

            MetadataValue metadata = new FixedMetadataValue(this, jsonString);//metadataを作成する

            player.setMetadata("Till", metadata);//キーをMetaTestにしてMetadataを書き込み

            till = value;

            System.out.println("hello");
        }else{                                                //↓リストで返す
            String jsonString = player.getMetadata("Till").get(0).asString();
            //System.out.println("gson = " +gson);
            //System.out.println("JsonString = " + jsonString);
            till = gson.fromJson(jsonString,Till.class); //Jsonを介してtillに変換
        }
        return till;
    }



    public void setTill(Player player,Till till){
        MetadataValue metadata = new FixedMetadataValue(this, gson.toJson(till));
        player.setMetadata("Till", metadata);
        System.out.print("NUKETA");
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getItem() == null)return;
        Material m = e.getItem().getType();
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (m == Material.WOODEN_HOE || m == Material.STONE_HOE || m == Material.IRON_HOE || m == Material.GOLDEN_HOE || m == Material.DIAMOND_HOE || m == Material.NETHERITE_HOE) {
                Block b = e.getClickedBlock();
                if (b.getType() == Material.DIRT || b.getType() == Material.GRASS_BLOCK) {

                    //playSound
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                    //metadata
                    Till till = getTill(p);

                    if(b.getType() == Material.DIRT)till.exp(9);
                    if(b.getType() == Material.GRASS_BLOCK)till.exp(19);

                    if(ls.getNeedNextExp(till.level) <= till.exp){
                        till.level();
                        till.exp = 0;
                    }

                    p.sendMessage(String.valueOf(ls.getGetExp(till.level)));

                    System.out.println(till.exp);
                    setTill(p,till);

                    //particle
                    Location loc = e.getClickedBlock().getLocation();
                    loc.add(0.5,1,0.5);
                    loc.getWorld().spawnParticle(
                            Particle.VILLAGER_HAPPY, // パーティクルの種類
                            loc, // 発生させる場所
                            8,// 発生させる数
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

        Till till = getTill((Player)sender);

        if(args[0].equals("level")){
            sender.sendMessage(String.valueOf(till.level));
            sender.sendMessage(String.valueOf(ls.getLevel(till.exp)) );

            System.out.println(String.valueOf(till.level));
            System.out.println(String.valueOf(ls.getLevel(till.exp)) );

        }

        return true;
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
