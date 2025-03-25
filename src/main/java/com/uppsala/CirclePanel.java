package com.uppsala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CirclePanel extends JPanel implements MouseListener, MouseMotionListener {
    private final ArrayList<Circle> circles;

    // Cirkeln som just nu interageras med
    private Circle activeCircle = null;

    // Flaggor för flytt- eller storleksändringsläge
    private boolean dragging = false;
    private boolean resizing = false;

    // Musens avstånd till cirkelns mittpunkt vid drag
    private int offsetX, offsetY;

    // Vald färg för nya cirklar
    private Color currentColor = Color.RED;

    // Färgrutor uppe i hörnet
    private final Rectangle[] colorBoxes = {
            new Rectangle(10, 10, 20, 20),
            new Rectangle(35, 10, 20, 20),
            new Rectangle(60, 10, 20, 20),
            new Rectangle(85, 10, 20, 20)
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

        // Rita färgrutorna
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

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        // Kontrollera om en färgruta klickats
        for (int i = 0; i < colorBoxes.length; i++) {
            if (colorBoxes[i].contains(mx, my)) {
                currentColor = colors[i];
                return;
            }
        }

        // Kontrollera om en cirkel har klickats
        Circle clicked = null;
        for (int i = circles.size() - 1; i >= 0; i--) {
            Circle c = circles.get(i);
            if (c.onEdge(mx, my)) {
                clicked = c;
                resizing = true;
                dragging = false;
                break;
            } else if (c.contains(mx, my)) {
                clicked = c;
                dragging = true;
                resizing = false;
                offsetX = mx - c.getX();
                offsetY = my - c.getY();
                break;
            }
        }

        // Om en cirkel träffades, flytta den sist i listan för att rita överst
        if (clicked != null) {
            circles.remove(clicked);
            circles.add(clicked);
            activeCircle = clicked;
        } else {
            // Skapa ny cirkel
            Circle newCircle = new Circle(mx, my, 30, currentColor);
            circles.add(newCircle);
            activeCircle = newCircle;
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        resizing = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (activeCircle != null) {
            if (dragging) {
                activeCircle.setX(mx - offsetX);
                activeCircle.setY(my - offsetY);
                repaint();
            } else if (resizing) {
                int dx = mx - activeCircle.getX();
                int dy = my - activeCircle.getY();
                int newRadius = (int) Math.sqrt(dx * dx + dy * dy);
                activeCircle.setRadius(Math.max(newRadius, 5)); // Minimumradie 5
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
