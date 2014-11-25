package com.moleboi.snake.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.moleboi.snake.model.Food;
import com.moleboi.snake.model.Snake;
import com.moleboi.snake.model.SnakeSegment;

public class GameBoard extends JPanel implements ActionListener{
	private Timer timer;
	private Snake snake;
	private boolean dead = false;
	private int boardWidth;
	private int boardHeight;
	private Food food;
	private int counter = 0;
	
	public GameBoard(int boardWidth, int boardHeight) {
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.snake = new Snake(24, 24);
		this.food = new Food(boardWidth, boardHeight);
		this.setBackground(Color.white);
		timer = new Timer(300, this);
		timer.start();
		this.setPreferredSize(new Dimension(500, 500));
		
		InputMap im = this.getInputMap();
		im.put(KeyStroke.getKeyStroke("UP"), "GoUp");
		im.put(KeyStroke.getKeyStroke("DOWN"), "GoDown");
		im.put(KeyStroke.getKeyStroke("LEFT"), "GoLeft");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "GoRight");
		im.put(KeyStroke.getKeyStroke("ENTER"), "Restart");
		ActionMap am = this.getActionMap();
		am.put("GoUp", new GoUp());
		am.put("GoDown", new GoDown());
		am.put("GoLeft", new GoLeft());
		am.put("GoRight", new GoRight());
		am.put("Restart", new Restart());
	}
	public int tileWidth() {
		return getSize().width/boardWidth;
	}
	public int tileHeight() {
		return getSize().height/boardHeight;
	}
	private void drawTile(Graphics g, int x, int y, Color color){
		g.setColor(color);
		g.fillRect(x, y, tileWidth(), tileHeight());
	}
	private void drawSnakeTile(Graphics g, int x, int y, Color color){
		g.setColor(color);
		g.fillRect(x+1, y+1, tileWidth()-1, tileHeight()-1);
	}
	public void paint(Graphics g){
		super.paint(g);
		if(dead) {
			String s1 = "GAME OVER";
			String s2 = "Press Enter to restart";
			int sWidth =(int) g.getFontMetrics().getStringBounds(s1, g).getWidth();
			int sHeight =(int) g.getFontMetrics().getStringBounds(s1, g).getHeight();
			int startX = (getWidth() - sWidth)/2;
			int startY = (getHeight() - sHeight)/2;
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString(s1, startX-10, startY);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			int temp = sHeight;
			sWidth =(int) g.getFontMetrics().getStringBounds(s2, g).getWidth();
			sHeight =(int) g.getFontMetrics().getStringBounds(s2, g).getHeight();
			startX = (getWidth() - sWidth)/2;
			startY = (getHeight() - sHeight)/2;
			g.drawString(s2, startX+10, startY + temp);
			return;
		}
		int x = food.getX();
		int y = food.getY();
		drawTile(g, x * tileWidth(), y * tileHeight(), Color.red);
		Iterator<SnakeSegment> it = snake.getBody();
		while(it.hasNext()) {
			SnakeSegment current = it.next();
			x = current.getX();
			y = current.getY();
			drawSnakeTile(g, x * tileWidth(), y * tileHeight(), Color.black);
		}
	}
	public boolean checkForWallCollision() {
		SnakeSegment head = snake.getHead();
		int headX = head.getX();
		int headY = head.getY();
		if(headX == -1 || headX == boardWidth || headY == -1 || headY == boardHeight) {
			return true;
		}
		return false;
	}
	public boolean checkForFoodCollision() {
		SnakeSegment head = snake.getHead();
		int headX = head.getX();
		int headY = head.getY();
		int foodX = food.getX();
		int foodY = food.getY();
		if(headX == foodX && headY == foodY) {
			return true;
		}
		return false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(dead) {
			repaint();
			return;
		}
		if(snake.checkForSelfCollision() || checkForWallCollision()) {
			dead = true;
		}
		if(checkForFoodCollision()) {
			food.respawn();
			counter++;
			int delay = timer.getDelay();
			if(delay != 100) {
				timer.setDelay(delay-2);
			}
		} else {
			snake.dropLast();
		}
		snake.move();
		repaint();
	}
	public void restart() {
		snake = new Snake(24, 24);
		food.respawn();
		timer.restart();
		dead = false;
	}
	class GoUp extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			snake.goUp();
			System.out.println("up");
		}
		
	}
	class GoDown extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			snake.goDown();
			System.out.println("down");
		}
		
	}
	class GoLeft extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			snake.goLeft();
			System.out.println("left");
		}
		
	}
	class GoRight extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			snake.goRight();
			System.out.println("right");
		}
		
	}
	class Restart extends AbstractAction {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			restart();
			System.out.println("restart");
		}
		
	}
	
}
