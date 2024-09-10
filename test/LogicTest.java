import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class LogicTest {
    Logic logic;
    Robot robot;
    @BeforeEach
    void setUp() throws AWTException {
        logic = new Logic();
        Logic.rand.setSeed(0);
        robot = new Robot();
    }

    @Test
    void handleMouseClick() {
        Bysen.main(null);
        robot.delay(1000);
        robot.mouseMove(1920/2, 1080/2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_A);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_K);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_E);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(1000);
        assertFalse(Logic.gameOver);
    }

    @Test
    void insideRoom() {
        assertTrue(logic.insideRoom(1,1,0,0));
        assertFalse(logic.insideRoom(50,50,0,0));
    }

    @Test
    void startNewGame() {
        logic.startNewGame();
        assertFalse(Logic.gameOver);
        assertEquals(3,Logic.numArrows);
        assertEquals(0,Logic.currRoom);
    }

    @Test
    void tooClose() {
        logic.startNewGame();
        assertTrue(logic.tooClose(0));
        assertTrue(logic.tooClose(1));
        assertFalse(logic.tooClose(2));
    }

    @Test
    void situationBysen() {
        logic.startNewGame();
        Logic.currRoom=8;
        logic.situation();
        assertEquals("Bysen lockar Spelaren att gå vilse",Logic.messages.getFirst());
    }

    @Test
    void situationTroll(){
        logic.startNewGame();
        Logic.currRoom=9;
        logic.situation();
        assertEquals("Spelaren faller ner i trollringen",Logic.messages.getFirst());
    }

    @Test
    void situationVittra(){
        logic.startNewGame();
        Logic.currRoom=11;
        logic.situation();
        assertEquals("Vittran kör iväg Spelaren till ett slumpat rum",Logic.messages.getFirst());
        assertEquals(17,Logic.currRoom);
    }

    @Test
    void situationVette(){
        logic.startNewGame();
        Logic.currRoom=14;
        logic.situation();
        assertEquals("Vätten sände en sjukdom på Spelaren", Logic.messages.getFirst());
    }

    @Test
    void throwNetCatch() {
        logic.startNewGame();
        Logic.currRoom=7;
        logic.throwNet(8);
        assertEquals("Spelaren vinner! Spelaren har fångat Bysen!", Logic.messages.getFirst());

    }

    @Test
    void throwNetMiss(){
        logic.startNewGame();
        Logic.currRoom=7;
        logic.throwNet(6);
        assertEquals("Spelaren råkade se Bysen och han bara försvann", Logic.messages.getFirst());
    }

    @Test
    void throwNetMovedTo() {
        logic.startNewGame();
        Logic.currRoom = 9;
        logic.throwNet(10);
        assertEquals("Spelaren väckte Bysen och han är inte glad!", Logic.messages.getFirst());
    }

    @Test
    void throwNetNoMessage() {
        logic.startNewGame();
        Logic.numArrows = -1;

        for (int i = 0; i < 6; i++) {
            logic.throwNet(15);
        }
        Logic.messages.clear();
        logic.throwNet(15);

        assertTrue(Logic.messages.isEmpty());
        }

    @Test
    void throwNetNoArrows() {
        logic.startNewGame();
        Logic.numArrows = 1;
        logic.throwNet(0);
        assertEquals("Oops! Spelaren har inga inga nät kvar.", Logic.messages.getFirst());
    }
}