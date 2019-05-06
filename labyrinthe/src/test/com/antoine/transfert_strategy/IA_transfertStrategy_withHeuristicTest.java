package com.antoine.transfert_strategy;

import com.antoine.contracts.ILevel;
import com.antoine.services.Assembler;
import com.antoine.vue.Test.FrameForTestingLevel;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class IA_transfertStrategy_withHeuristicTest {

    @Test
    public void printPathTest() {

        SwingUtilities.invokeLater(()-> {
            FrameForTestingLevel frame;
            Assembler assembler = new Assembler("./src/main/conf/config/conf.xml");
            ILevel levelTwilight = (ILevel) assembler.newInstance("levelTwilight");

            frame = new FrameForTestingLevel(levelTwilight);
        });
    }


}