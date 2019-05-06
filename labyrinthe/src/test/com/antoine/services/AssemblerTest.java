package test.com.antoine.services;

import com.antoine.modele.level.Level;
import com.antoine.modele.level.Level4;
import com.antoine.services.Assembler;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssemblerTest {

    private String path="./config/conf.xml";

    @Test
    public void parse() {
        Assembler ass= new Assembler(path);

        System.out.println("================================================");
    }

    @Test
    public void newInstance() {
        Assembler ass= new Assembler(path);
        Level level= (Level) ass.newInstance("levelApple");
        assertEquals(40, level.getPlayerX());
        assertEquals(50, level.getPlayerY());
        assertTrue((level.getMap() != null));
        assertTrue(level.getPlayer().getImage().getWidth() == 34);
    }

    @Test
    public void newInstance2(){
        Assembler assembler= new Assembler(path);
        Level4 level= (Level4) assembler.newInstance("levelTwilight");
    }
}