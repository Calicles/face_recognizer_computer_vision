package test.com.antoine.modele.level;

import com.antoine.entity.Boss;
import com.antoine.entity.Player;
import com.antoine.modele.level.Level4;
import com.antoine.services.Assembler;
import org.junit.Test;

import static org.junit.Assert.*;

public class Level4Test {

    private String path= "./config/conf.xml";

    @Test
    public void start() {
        Assembler assembleur= new Assembler(path);
        Level4 level= (Level4) assembleur.newInstance("levelTwilight");
        Boss boss= level.getBoss();
        for(int i= 0; i < 15; i++) {
            System.out.println(boss.getX() + "        " + boss.getY());
            boss.memorizeMoves();
            boss.think();
        }
        Player player= (Player) level.getPlayer();

        System.out.println(player.getX());
    }
}