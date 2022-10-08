package hacklab.tilleventtest;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

class Till {

    static Gson gson = new Gson();
    private int exp = 0;
    private int level = 1;
    private String name;
    private Player player;


    private Till(){}

    public static Till getInstance(JavaPlugin javaPlugin, Player player){
        if (!player.hasMetadata("Till")) {
            Till till = new Till();

            String jsonString = gson.toJson(gson.toJson(till));

            MetadataValue metadata = new FixedMetadataValue(javaPlugin, jsonString);//metadataを作成する

            player.setMetadata("Till", metadata);//キーをMetaTestにしてMetadataを書き込み

            System.out.println("hello");
            till.player = player;
            return till;
        }else{                                                //↓リストで返す
            String jsonString = player.getMetadata("Till").get(0).asString();
            //System.out.println("gson = " +gson);
            //System.out.println("JsonString = " + jsonString);
            Till till = gson.fromJson(jsonString,Till.class); //Jsonを介してtillに変換
            till.player = player;

            return till;
        }
    }

    public void expup(int value) {
        exp += value;
    }

    public void levelup() {
        level++;
        exp = 0;
    }

    public int getLevel(){
        return level;
    }

    public int getExp(){
        return exp;
    }

    public void setlevel(int lev){
        this.level = lev;
        setTill(player, this);
    }

    private void setTill(Player player,Till till){
        MetadataValue metadata = new FixedMetadataValue((Plugin) till, gson.toJson(till));
        player.setMetadata("Till", metadata);
        System.out.print("LevelSet");
    }

}
