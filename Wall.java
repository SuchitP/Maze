import java.awt.Color;
import java.awt.Rectangle;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Wall
{
	private int wallConstant = 50;
	int [] rows;
	int [] cols;
	int size = 0;
	Color color;
	String type;

	public Wall(int rows[], int cols[], int r, int g, int b, String type, int size)
	{
		this.rows = rows;
		this.cols = cols;
		if(r>255)
			r = 255;
		if(g>255)
			g = 255;
		if(b>255)
			b = 255;
		if(r<0)
			r = 0;
		if(g<0)
			r = 0;
		if(b<0)
			r = 0;
		this.color = new Color(r, g, b);
		this.size = size;
		this.type = type;
	}//constructor

	public int [] getRows(){return rows;}

	public int [] getCols(){return cols;}

	public Color returnColor(){return color;}

	public int returnSize(){return size;}

	public String returnType(){
		return type;
	}
	public Polygon getPoly(){
		return new Polygon(cols, rows, cols.length);
	}
}//Wall
