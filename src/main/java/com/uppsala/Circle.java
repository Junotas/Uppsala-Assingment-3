package com.uppsala;

import java.awt.*;

public class Circle {
    private int x;
    private int y;
    private int r;
    private final Color color;

    public Circle(int x, int y, int r, Color color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = color;
    }

    // Ritar cirkeln baserat på dess mittpunkt och radie
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
    }

    // Returnerar true om punkten (mx, my) ligger inuti cirkeln
    public boolean contains(int mx, int my) {
        int dx = mx - x;
        int dy = my - y;
        return (dx * dx + dy * dy) <= r * r;
    }

    // Returnerar true om punkten (mx, my) ligger nära cirkelns kant
    public boolean onEdge(int mx, int my) {
        int dx = mx - x;
        int dy = my - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return Math.abs(dist - r) < 5;
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setRadius(int r) { this.r = r; }

    public int getX() { return x; }
    public int getY() { return y; }
}
