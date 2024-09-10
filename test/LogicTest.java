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
        assert(logic.insideRoom(1,1,0,0));
    }

    @Test
    void startNewGame() {
        logic.startNewGame();
        robot.keyPress(KeyEvent.VK_ENTER);
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
    void throwNet() {
    }

    @Test
    void rooms(){
        assertEquals(new int[][] {{334, 20}, {609, 220}, {499, 540}, {169, 540}, {62, 220},
                {169, 255}, {232, 168}, {334, 136}, {435, 168}, {499, 255}, {499, 361},
                {435, 447}, {334, 480}, {232, 447}, {169, 361}, {254, 336}, {285, 238},
                {387, 238}, {418, 336}, {334, 393}}, Logic.rooms);
    }

    @Test
    void links(){
        assertEquals(new int[][] {{4, 7, 1}, {0, 9, 2}, {1, 11, 3}, {4, 13, 2}, {0, 5, 3},
                {4, 6, 14}, {7, 16, 5}, {6, 0, 8}, {7, 17, 9}, {8, 1, 10}, {9, 18, 11},
                {10, 2, 12}, {13, 19, 11}, {14, 3, 12}, {5, 15, 13}, {14, 16, 19},
                {6, 17, 15}, {16, 8, 18}, {19, 10, 17}, {15, 12, 18}}, Logic.links);
    }
}