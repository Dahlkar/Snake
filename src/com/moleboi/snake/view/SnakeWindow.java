package com.moleboi.snake.view;

import javax.swing.JFrame;

import com.moleboi.snake.model.Snake;

public class SnakeWindow extends JFrame {
	
	public SnakeWindow() {
		add(new GameBoard(30, 30));
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new SnakeWindow();

	}

}
