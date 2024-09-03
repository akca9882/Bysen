import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Rendering extends JPanel {
    /**
     * Ritar spelaren
     * @param g Graphics2D objektet att rita med
     */
    void drawPlayer(Graphics2D g) {
        int x = Bysen.rooms[Bysen.currRoom][0] + (Bysen.roomSize - Bysen.playerSize) / 2;
        int y = Bysen.rooms[Bysen.currRoom][1] + (Bysen.roomSize - Bysen.playerSize) - 2;

        Path2D player = new Path2D.Double();
        player.moveTo(x, y);
        player.lineTo(x + Bysen.playerSize, y);
        player.lineTo(x + Bysen.playerSize / 2, y - Bysen.playerSize);
        player.closePath();

        g.setColor(Color.white);
        g.fill(player);
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.black);
        g.draw(player);
    }

    /***
     * ritar startskärmermen
     * @param g Graphics2D objektet att rita med
     */
    void drawStartScreen(Graphics2D g) {
        g.setColor(new Color(0xDDFFFFFF, true));
        g.fillRect(0, 0, getWidth(), getHeight() - 60);

        g.setColor(Color.darkGray);
        g.setFont(new Font("SansSerif", Font.BOLD, 48));
        g.drawString("Fånga Bysen!", 160, 240);

        g.setFont(getFont());
        g.drawString("Vänsterklicka för att flytta, Högerklicka för att skjuta", 210, 310);
        g.drawString("Var försiktig väsen kan befinna sig i samma rum som du", 175, 345);
        g.drawString("Klicka för att starta", 310, 380);
    }

    /***
     * ritar rum samt sträcken mellan dem
     * @param g Graphics2D objektet att rita med
     */
    void drawRooms(Graphics2D g) {
        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i < Bysen.links.length; i++) {
            for (int link : Bysen.links[i]) {
                int x1 = Bysen.rooms[i][0] + Bysen.roomSize / 2;
                int y1 = Bysen.rooms[i][1] + Bysen.roomSize / 2;
                int x2 = Bysen.rooms[link][0] + Bysen.roomSize / 2;
                int y2 = Bysen.rooms[link][1] + Bysen.roomSize / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.setColor(Color.orange);
        for (int[] r : Bysen.rooms)
            g.fillOval(r[0], r[1], Bysen.roomSize, Bysen.roomSize);

        if (!Bysen.gameOver) {
            g.setColor(Color.magenta);
            for (int link : Bysen.links[Bysen.currRoom])
                g.fillOval(Bysen.rooms[link][0], Bysen.rooms[link][1], Bysen.roomSize, Bysen.roomSize);
        }

        g.setColor(Color.darkGray);
        for (int[] r : Bysen.rooms)
            g.drawOval(r[0], r[1], Bysen.roomSize, Bysen.roomSize);
    }

    /***
     * ritar medelanden samt antalt pilar
     * @param g
     */
    void drawMessage(Graphics2D g) {
        if (!Bysen.gameOver)
            g.drawString("pilar  " + Bysen.numArrows, 610, 30);

        if (Bysen.messages != null) {
            g.setColor(Color.black);

            // ta bort lika meddelanden
            Bysen.messages = Bysen.messages.stream().distinct().collect(toList());

            // slå ihop max tre
            String msg = Bysen.messages.stream().limit(3).collect(joining(" & "));
            g.drawString(msg, 20, getHeight() - 40);

            // om det finns mer, skriv ut nedanför
            if (Bysen.messages.size() > 3) {
                g.drawString("& " + Bysen.messages.get(3), 20, getHeight() - 17);
            }

            Bysen.messages.clear();
        }
    }

    /***
     * sätter renderHints och kallar på dem olika ritnings metoderna
     * @param gg the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawRooms(g);
        if (Bysen.gameOver) {
            drawStartScreen(g);
        } else {
            drawPlayer(g);
        }
        drawMessage(g);
    }
}
