package com.uppsala;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CirclePanel extends JPanel implements MouseListener, MouseMotionListener {
    private final ArrayList<Circle> circles;

    // Håller koll på vilken cirkel som är vald just nu
    private Circle activeCircle = null;

    // För att veta om vi är i "drag-läge" eller "resize-läge"
    private boolean dragging = false;
    private boolean resizing = false;

    // Musens offset relativt cirkelns centrum (vid drag) eller cirkelns radie (vid resize)
    private int offsetX, offsetY;

    // Aktuell färg
    private Color currentColor = Color.RED;  // eller valfri default

    // Färgrutor uppe i hörnet
    private final Rectangle[] colorBoxes = {
            new Rectangle(10, 10, 20, 20),  // Röd ruta
            new Rectangle(35, 10, 20, 20),  // Grön ruta
            new Rectangle(60, 10, 20, 20),  // Blå ruta
            new Rectangle(85, 10, 20, 20)   // Gul ruta
    };
    private final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    public CirclePanel() {
        circles = new ArrayList<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rita färgväljare-rutorna
        for (int i = 0; i < colorBoxes.length; i++) {
            g.setColor(colors[i]);
            Rectangle box = colorBoxes[i];
            g.fillRect(box.x, box.y, box.width, box.height);
        }

        // Rita alla cirklar
        for (Circle c : circles) {
            c.draw(g);
        }
    }

    // --- Metoder för MouseListener ---
    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // 1) Kolla om vi klickade i någon av färgrutorna
        for (int i = 0; i < colorBoxes.length; i++) {
            if (colorBoxes[i].contains(mx, my)) {
                currentColor = colors[i];
                return; // Avsluta, för vi vill inte samtidigt skapa/flytta cirkel
            }
        }

        // 2) Kolla om vi klickar på en cirkel (överst först)
        Circle clicked = null;
        for (int i = circles.size() - 1; i >= 0; i--) {
            Circle c = circles.get(i);
            if (c.onEdge(mx, my)) {
                // Vi är i resize-läge
                clicked = c;
                resizing = true;
                dragging = false;

                // Spara undan radien och hur långt ifrån kanten vi klickade (om du vill)
                // offset för att kunna beräkna ny radie
                // Ex: distans mellan klickpunkten och cirkelns centrum
                // spara ev. Detta om du vill justera radien relativt var du klickade
                break;
            } else if (c.contains(mx, my)) {
                // Vi är i drag-läge
                clicked = c;
                dragging = true;
                resizing = false;

                // Beräkna offset, så att cirkeln inte "hoppar" till muspekarens position
                offsetX = mx - c.getX();
                offsetY = my - c.getY();
                break;
            }
        }

        // Om vi hittade en cirkel
        if (clicked != null) {
            // Ta bort cirkeln från listan och lägg den sist för att få den "överst"
            circles.remove(clicked);
            circles.add(clicked);
            activeCircle = clicked;
        } else {
            // 3) Ingen cirkel träffad -> skapa ny cirkel
            Circle newCircle = new Circle(mx, my, 30, currentColor);
            circles.add(newCircle);
            activeCircle = newCircle;
            // Inget dragging eller resizing behövs här (om du inte vill stödja att
            // man direkt drar ut radien när man klickar, men det är en annan variant).
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Avsluta drag- och resize-lägen
        dragging = false;
        resizing = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    // --- Metoder för MouseMotionListener ---
    @Override
    public void mouseDragged(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (activeCircle != null) {
            if (dragging) {
                // Flytta cirkeln
                activeCircle.setX(mx - offsetX);
                activeCircle.setY(my - offsetY);
                repaint();
            } else if (resizing) {
                // Ändra radien
                // Förslagsvis baserat på distansen mellan muspekaren och cirkelns centrum
                int dx = mx - activeCircle.getX();
                int dy = my - activeCircle.getY();
                int newRadius = (int)Math.sqrt(dx*dx + dy*dy);
                // Du kan även lägga in minsta radie = 5, etc.
                activeCircle.setRadius(Math.max(newRadius, 5));
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
