package hacklab.tilleventtest;

public class LevelStatus {

    class ExpStatus{
        //シリアル化 ばらばらのデータを一つのオブジェクトにする
        int needNextExp;
        int getExp;
        int cumExp;
    }

    LevelStatus() {
        final double nluxp = 12; //レベル１から２にレベルアップするのに必要な経験値
        final double nlut = 5; //レベルアップのために同レベルの敵を倒す回数
        final float expMag = 1.5f; //レベル差が１のときの倍率
        int level = 25;

        /*
        System.out.println("取得経験値 "+ e.getExp);
        System.out.println("レベルアップに必要な経験値 "+ e.needNextExp);
        System.out.println("必要累積経験値　"+ e.cumExp);

         */
    }

    public int getGetExp(int level){
        ExpStatus e = getExpState(level);
        return e.getExp;
    }

    public int getNeedNextExp(int level){
        ExpStatus e = getExpState(level);
        return e.needNextExp;
    }

    public int getLevel(int exp){
        ExpStatus e;
        //計算式
        int l = 1;
        while(true){
            e = getExpState(l);
            if(exp - e.needNextExp < 0)break;
            exp -= e.needNextExp;
            l ++;
        }

        return l;
    }

    public ExpStatus getExpState(int level){

        final int nluxp = 12; //レベル１から２にレベルアップするのに必要な経験値
        final int nlut = 5; //レベルアップのために同レベルの敵を倒す回数
        final float expMag = 1.5f; //レベル差が１のときの倍率

        ExpStatus obj = new ExpStatus();
        obj.needNextExp = needNextExp(nluxp,nlut,expMag,level);
        obj.getExp = getExp(nluxp,nlut,expMag,level);
        obj.cumExp = cumExp(nluxp,nlut,expMag,level);

        return obj;
    }

    int needNextExp(int a, int b ,float c, int lev){
        return (int) Math.round( a * Math.pow(c, lev-1));
    }

    int getExp(int a, int b ,float c, int lev) {
        if(lev == 1){
            return 2;
        }else if(lev == 2){
            return 4;
        }else if(lev == 3){
            return 5;
        }else if(lev == 4){
            return 8;
        }
        return (int) Math.round( a * Math.pow(c, lev-1-4));
    }

    int cumExp(int a, int b, float c, int lev){
        double d = 0;
        for(int i=1; i<lev; i++){
            d += Math.round( a * Math.pow(c, i-1) );
        }
        return (int) d;
    }
}
