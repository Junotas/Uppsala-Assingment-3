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

    // Ritar cirkeln
    public void draw(Graphics g) {
        g.setColor(color);
        // Notera att "ovalen" börjar i (x-r, y-r) och har diameter 2r
        g.fillOval(x - r, y - r, 2*r, 2*r);
    }

    // Kollar om en punkt (mx, my) är innanför cirkeln
    public boolean contains(int mx, int my) {
        int dx = mx - x;
        int dy = my - y;
        return (dx*dx + dy*dy) <= r*r;
    }

    // Kollar om en punkt (mx, my) ligger ungefär på cirkelns kant
    // (du kan välja en viss tolerans, t ex ±5 pixlar från radien)
    public boolean onEdge(int mx, int my) {
        int dx = mx - x;
        int dy = my - y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return Math.abs(dist - r) < 5;
    }

    // Setters och getters för x, y, r, color osv.
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setRadius(int r) { this.r = r; }
    public int getX() { return x; }
    public int getY() { return y; }

}
