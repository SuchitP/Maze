import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Rectangle;

public class Hero{

	private Location loc;
	private int size;
	private Color color;
	private int dir;

	public Hero(Location loc, int size, Color color, int dir){
		this.loc = loc;
		this.size = size;
		this.color = color;
		this.dir = dir;
	}

	public Color getColor(){
		return color;
	}
	public Location getLoc(){
		return loc;
	}
	public void setLoc(Location l){
		loc = l;
	}
	public int getDir(){
		return dir;
	}
	public void setDir(int dir){
		this.dir=dir;
	}

	public void move(int key, char[][] maze){

		int r = getLoc().getR();
		int c = getLoc().getC();

        if (key == 38){
            if(dir == 0){//up
                System.out.println("hi");
                if(r>0 && maze[r-1][c] == ' '){
                    System.out.println("went left");
                    getLoc().setR(-1);
                }
            }
            if(dir == 1){//right
                if(c<maze[0].length-1 && maze[r][c+1] == ' '){
                    System.out.println("went down");
                    getLoc().setC(1);
                }
            }
            if(dir == 2){//down
            System.out.println("hi");
                if(r<maze.length-1 && maze[r+1][c] == ' '){
                    System.out.println("went right");
                    getLoc().setR(1);
                }
            }
            if(dir == 3){//left
                if(c>0 && maze[r][c-1] == ' '){
                    System.out.println("went up");
                    getLoc().setC(-1);
                }
            }
        }

		if (key == 39){
			dir++;
			if(dir>3)
				dir=0;
		}
		if (key == 37){
			dir--;
			if(dir<0)
				dir=3;
		}
	}

	public Rectangle getRect(){
		int r = getLoc().getR();
		int c = getLoc().getC();

		return new Rectangle(c*size+size, r*size+size, size, size);
	}

}