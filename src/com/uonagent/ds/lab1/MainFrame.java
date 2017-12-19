package com.uonagent.ds.lab1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MainFrame extends JFrame {
  JPanel menuBar = new JPanel();
  JButton lineButton = new JButton("Отрезок");
  JButton ellipseButton = new JButton("Эллипс");
  JButton rectangleButton = new JButton("Многоугольник");
  JButton curveButton = new JButton("Кривая Безье");
  JFrame lineFrame;
  JFrame ellipseFrame;
  JFrame rectangleFrame;
  JFrame curveFrame = new JFrame("Создать кривую Безье");
  JPanel canvasPanel = new JPanel();
  DefaultListModel listModel = new DefaultListModel();
  JList list = new JList(listModel);
  boolean[][] canvasMatrix = new boolean[600][780];

  private class Point {
    Point() {
      x = -1;
      y = -1;
    }

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    int x;
    int y;
  }

  private class Line {
    Line(Line l) {
      start = l.start;
      end = l.end;
    }

    Point start;
    Point end;
  }

  private void clearMatrix() {
    for (int i = 0; i < 600; ++i) {
      for (int j = 0; j < 780; ++j) {
        canvasMatrix[i][j] = false;
      }
    }
  }

  private void put(int x, int y) {
    if (x >= 0 && x < 600 && y >= 0 && y < 780) {
      canvasMatrix[x][y] = true;
    }
  }

  private void Bresenham(int xStart, int yStart, int xEnd, int yEnd) {
    int a, b, sign;
    a = yEnd - yStart;
    b = xStart - xEnd;
    if (Math.abs(a) > Math.abs(b)) {
      sign = 1;
    } else {
      sign = -1;
    }
    int signA, signB;
    if (a < 0) {
      signA = -1;
    } else {
      signA = 1;
    }
    if (b < 0) {
      signB = -1;
    } else {
      signB = 1;
    }
    int f = 0;
    canvasMatrix[xStart][yStart] = true;
    int x = xStart, y = yStart;
    if (sign == -1) {
      while (x != xEnd || y != yEnd) {
        f += a * signA;
        if (f > 0) {
          f -= b * signB;
          y += signA;
        }
        x -= signB;
        if (y >= 0 && y < 780 && x >= 0 && x < 600) {
          canvasMatrix[x][y] = true;
        }
      }
    } else {
      while (x != xEnd || y != yEnd) {
        f += b * signB;
        if (f > 0) {
          f -= a * signA;
          x -= signB;
        }
        y += signA;
        if (y >= 0 && y < 780 && x >= 0 && x < 600) {
          canvasMatrix[x][y] = true;
        }
      }
    }
  }

  private void bezierGoGo(ArrayList<Point> sourcePoints) {
    ArrayList<Point> finalPoints = new ArrayList<>();

    for (double t = 0; t <= 1; t += 0.01)
      finalPoints.add(calculateBezierFunction(t, sourcePoints));
    drawCurve(finalPoints);
  }

  private Point calculateBezierFunction(double t, ArrayList<Point> srcPoints) {
    double x = 0;
    double y = 0;

    int n = srcPoints.size() - 1;
    for (int i = 0; i <= n; i++) {
      x += fact(n) / (fact(i) * fact(n - i)) * srcPoints.get(i).x * Math.pow(t, i) * Math.pow(1 - t, n - i);
      y += fact(n) / (fact(i) * fact(n - i)) * srcPoints.get(i).y * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }
    return new Point((int) x, (int) y);
  }

  private double fact(double arg) {
    if (arg < 0) throw new RuntimeException("negative argument.");
    if (arg == 0) return 1;

    double rezult = 1;
    for (int i = 1; i <= arg; i++)
      rezult *= i;
    return rezult;
  }

  private void drawCurve(ArrayList<Point> points) {
    for (int i = 1; i < points.size(); i++) {
      int x1 = (int) (points.get(i - 1).x);
      int y1 = (int) (points.get(i - 1).y);
      int x2 = (int) (points.get(i).x);
      int y2 = (int) (points.get(i).y);
      Bresenham(x1, y1, x2, y2);
    }
  }

  public MainFrame() {
    super("FotoShop 3000+ Super");
    setPreferredSize(new Dimension(600, 820));
    setResizable(false);
    setFocusableWindowState(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setLayout(new BorderLayout());
    clearMatrix();

    canvasPanel = new JPanel() {
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        for (int i = 0; i < 600; ++i) {
          for (int j = 0; j < 780; ++j) {
            if (canvasMatrix[i][j]) {
              g.setColor(Color.BLACK);
            } else {
              g.setColor(Color.WHITE);
            }
            g.drawLine(i, j, i, j);
          }
        }
      }
    };

    lineButton.addActionListener((ActionEvent e) -> {
      if (e.getSource() == lineButton) {
        lineFrame = new JFrame("Создать отрезок");
        lineFrame.setSize(new Dimension(480, 320));
        lineFrame.setLayout(new BorderLayout());
        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.PAGE_AXIS));
        JTextField enterXS = new JTextField("Введите X начала");
        JTextField enterYS = new JTextField("Введите Y начала");
        JTextField enterXE = new JTextField("Введите X конца");
        JTextField enterYE = new JTextField("Введите Y конца");
        linePanel.add(enterXS);
        linePanel.add(enterYS);
        linePanel.add(enterXE);
        linePanel.add(enterYE);
        JPanel buttons = new JPanel();
        JButton lineOk = new JButton("OK");
        JButton lineCancel = new JButton("Отмена");
        lineOk.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == lineOk) {
            int xStart = new Integer(enterXS.getText());
            int yStart = new Integer(enterYS.getText());
            int xEnd = new Integer(enterXE.getText());
            int yEnd = new Integer(enterYE.getText());
            clearMatrix();
            Bresenham(xStart, yStart, xEnd, yEnd);
          }
          canvasPanel.repaint();
          lineFrame.dispatchEvent(new WindowEvent(lineFrame, WindowEvent.WINDOW_CLOSING));
        });
        lineCancel.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == lineCancel) {
            lineFrame.dispatchEvent(new WindowEvent(lineFrame, WindowEvent.WINDOW_CLOSING));
          }
        });
        buttons.add(lineOk);
        buttons.add(lineCancel);
        lineFrame.add(buttons, BorderLayout.SOUTH);
        lineFrame.add(linePanel, BorderLayout.NORTH);
        lineFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lineFrame.setLocationRelativeTo(this);
        lineFrame.setResizable(false);
        lineFrame.setVisible(true);
      }
    });

    ellipseButton.addActionListener((ActionEvent e) -> {
      if (e.getSource() == ellipseButton) {
        ellipseFrame = new JFrame("Создать эллипс");
        ellipseFrame.setSize(new Dimension(480, 320));
        ellipseFrame.setLayout(new BorderLayout());
        JPanel ellipsePanel = new JPanel();
        ellipsePanel.setLayout(new BoxLayout(ellipsePanel, BoxLayout.PAGE_AXIS));
        JTextField centerX = new JTextField("Введите координату X центра эллипса");
        JTextField centerY = new JTextField("Введите координату Y центра эллипса");
        JTextField paramA = new JTextField("Введите параметр A");
        JTextField paramB = new JTextField("Введите параметр B");
        ellipsePanel.add(centerX);
        ellipsePanel.add(centerY);
        ellipsePanel.add(paramA);
        ellipsePanel.add(paramB);
        JPanel buttons = new JPanel();
        JButton lineOk = new JButton("OK");
        JButton lineCancel = new JButton("Отмена");
        lineOk.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == lineOk) {
            clearMatrix();
            int a = new Integer(paramA.getText());
            int b = new Integer(paramB.getText());
            int xS = new Integer(centerX.getText());
            int yS = new Integer(centerY.getText());
            int x = 0;
            int y = b;
            long delta = 4 * b * b * ((x + 1) * (x + 1)) + a * a * ((2 * y - 1) * (2 * y - 1)) - 4 * a * a * b * b;
            while (a * a * (2 * y - 1) > 2 * b * b * (x + 1)) {
              put(xS + x, yS + y);
              put(xS + x, yS - y);
              put(xS - x, yS + y);
              put(xS - x, yS - y);
              if (delta < 0) {
                ++x;
                delta += 4 * b * b * (2 * x + 3);
              } else {
                ++x;
                delta = delta - 8 * a * a * (y - 1) + 4 * b * b * (2 * x + 3);
                --y;
              }
            }
            delta = b * b * ((2 * x + 1) * (2 * x + 1)) + 4 * a * a * ((y + 1) * (y + 1)) - 4 * a * a * b * b;
            while (y + 1 != 0) {
              put(xS + x, yS + y);
              put(xS + x, yS - y);
              put(xS - x, yS + y);
              put(xS - x, yS - y);
              if (delta < 0) {
                --y;
                delta += 4 * a * a * (2 * y + 3);
              } else {
                --y;
                delta = delta - 8 * b * b * (x + 1) + 4 * a * a * (2 * y + 3);
                ++x;
              }
            }
          }
          canvasPanel.repaint();
          ellipseFrame.dispatchEvent(new WindowEvent(ellipseFrame, WindowEvent.WINDOW_CLOSING));
        });
        lineCancel.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == lineCancel) {
            ellipseFrame.dispatchEvent(new WindowEvent(ellipseFrame, WindowEvent.WINDOW_CLOSING));
          }
        });
        buttons.add(lineOk);
        buttons.add(lineCancel);
        ellipseFrame.add(buttons, BorderLayout.SOUTH);
        ellipseFrame.add(ellipsePanel, BorderLayout.NORTH);
        ellipseFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ellipseFrame.setLocationRelativeTo(this);
        ellipseFrame.setResizable(false);
        ellipseFrame.setVisible(true);
      }
    });

    rectangleButton.addActionListener((ActionEvent e) -> {
      if (e.getSource() == rectangleButton) {
        rectangleFrame = new JFrame("Закрашенный прямоугольник");
        JTextArea label = new JTextArea("Создан закрашенный многоугольник,\n" +
            "Координаты точек - (20,30),(300,300),(500,700)");
        JButton button = new JButton("OK");
        rectangleFrame.setLayout(new BorderLayout());
        rectangleFrame.add(label, BorderLayout.NORTH);
        rectangleFrame.add(button, BorderLayout.SOUTH);
        button.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == button) {
            rectangleFrame.dispatchEvent(new WindowEvent(rectangleFrame, WindowEvent.WINDOW_CLOSING));
          }
        });
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        p1.x = 20;
        p1.y = 30;
        p2.x = 300;
        p2.y = 300;
        p3.x = 500;
        p3.y = 700;
        ArrayList<Point> arrayList = new ArrayList<>();
        arrayList.add(p1);
        arrayList.add(p2);
        arrayList.add(p3);
        clearMatrix();
        for (int i = 0; i < arrayList.size(); ++i) {
          int j;
          if (i + 1 == arrayList.size()) {
            j = 0;
          } else {
            j = i + 1;
          }
          Bresenham(arrayList.get(i).x, arrayList.get(i).y, arrayList.get(j).x, arrayList.get(j).y);
        }
        for (int y = 0; y < 780; ++y) {
          for (int x = 0; x < 600 - 1; ++x) {
            canvasMatrix[x + 1][y] = canvasMatrix[x][y] ^ canvasMatrix[x + 1][y];
          }
        }
        canvasPanel.repaint();
        rectangleFrame.setSize(new Dimension(480, 320));
        rectangleFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        rectangleFrame.setLocationRelativeTo(this);
        rectangleFrame.setResizable(false);
        rectangleFrame.setVisible(true);
      }
    });

    curveButton.addActionListener((ActionEvent e) -> {
      if (e.getSource() == curveButton) {
        JTextArea label = new JTextArea("Кривая Безье");
        JButton button = new JButton("OK");
        curveFrame.setLayout(new BorderLayout());
        curveFrame.add(label, BorderLayout.NORTH);
        curveFrame.add(button, BorderLayout.SOUTH);
        button.addActionListener((ActionEvent e1) -> {
          if (e1.getSource() == button) {
            curveFrame.dispatchEvent(new WindowEvent(curveFrame, WindowEvent.WINDOW_CLOSING));
          }
        });
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        p1.x = 20;
        p1.y = 30;
        p2.x = 300;
        p2.y = 300;
        p3.x = 500;
        p3.y = 700;
        ArrayList<Point> arrayList = new ArrayList<>();
        arrayList.add(p1);
        arrayList.add(p2);
        arrayList.add(p3);
        clearMatrix();
        bezierGoGo(arrayList);
        canvasPanel.repaint();
        curveFrame.setSize(new Dimension(480, 320));
        curveFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        curveFrame.setLocationRelativeTo(this);
        curveFrame.setResizable(false);
        curveFrame.setVisible(true);
      }
    });

    menuBar.setPreferredSize(new Dimension(600, 40));
    menuBar.add(lineButton);
    menuBar.add(ellipseButton);
    menuBar.add(rectangleButton);
    menuBar.add(curveButton);
    add(menuBar, BorderLayout.NORTH);

    add(canvasPanel);
    setLocationRelativeTo(this);
    pack();
    setVisible(true);
  }
}
