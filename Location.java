import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Location{

	private int r;
	private int c;

	public Location(int r, int c){
		this.r = r;
		this.c = c;
	}

	public int getR(){
		return r;
	}
	public int getC(){
		return c;
	}

	public void setR(int a){
		r += a;
	}
	public void setC(int b){
		c += b;
	}
	public void setRCoord(int a){
		r = a;
	}
	public void setCCoord(int b){
		c = b;
	}
}