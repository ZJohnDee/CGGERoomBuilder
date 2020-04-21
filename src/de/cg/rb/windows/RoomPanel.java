package de.cg.rb.windows;

import de.cg.rb.ctrl.GlobalSettings;
import de.cg.rb.ctrl.Main;
import de.cg.rb.ctrl.RoomObject;
import de.cg.rb.ctrl.RoomSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ConcurrentModificationException;

public class RoomPanel extends JPanel {

    public static int offsetX = 0;
    public static int offsetY = 0;

    private int startMouseX = 0;
    private int startMouseY = 0;

    public RoomPanel() {
        super();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                mousePressedEvent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseReleasedEvent(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.DARK_GRAY);

        g.fillRect(0, 0, getWidth(), getHeight());


        g.setColor(Color.GREEN);
        try {
            for (RoomObject ro : RoomSettings.roomObjects) {
                if (ro.gameObject.displayAsRect) {
                    g.fillRect(ro.x-offsetX, ro.y-offsetY, ro.w, ro.h);
                } else {
                    g.drawImage(ro.gameObject.img.getScaledInstance(ro.w, ro.h, Image.SCALE_REPLICATE), ro.x-offsetX, ro.y-offsetY, null);
                }

            }
        } catch (ConcurrentModificationException e) {
            System.out.println("CCMException");
        }

        //Drawing the grid
        g.setColor(Color.GRAY);
        int grid = GlobalSettings.gridSize;
        if (grid > 2) {
            for (int y = 0; y<(getHeight()+offsetY)/grid; y++) {
                for (int x = 0; x<(getWidth()+offsetX)/grid; x++) {
                    g.drawRect(x * (grid) - offsetX, y * grid - offsetY, grid, grid);
                }
            }
        }

    }

    private void mousePressedEvent(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {

            if (RoomSettings.isInit) {
                var selected = GlobalSettings.selected;
                if (selected >= 0) {
                    var obj = GlobalSettings.initializedObjects.get(selected);
                    RoomObject ro;

                    int grid = GlobalSettings.gridSize;

                    int x = (e.getX()+offsetX)/grid*grid;
                    int y = (e.getY()+offsetY)/grid*grid;
                    int w = obj.width;
                    int h = obj.height;

                    ro = new RoomObject(x, y, w, h, obj);
                    RoomSettings.roomObjects.add(ro);

                    System.out.println("Object placed at " + x + " / " + y);
                }
            }

        }

        else if (e.getButton() == MouseEvent.BUTTON3) {
            startMouseX = e.getX();
            startMouseY = e.getY();
        }
    }

    private void mouseReleasedEvent(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            int mx = e.getX();
            int my = e.getY();

            int dx = startMouseX - mx;
            int dy = startMouseY - my;

            offsetX += dx;
            offsetY += dy;
        }
    }
}
