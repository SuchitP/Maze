import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Maze extends JPanel implements KeyListener{

	JFrame frame;
	int numCols = 42;
	int numRows = 18;
	char[][] maze = new char[18][42];
	ArrayList<Wall> walls;
	boolean draw3D = false;
	boolean win  = false;
	int dir = 1;
	int size = 10;
	Hero hero;
	Location loc = new Location(2, 0);
	ArrayList<Color> colors = new ArrayList<>();
	Location checkPoint;
	int checkPointUsed = 0;
	int level = 1;
	int hammer = 3;
	Color color;


	public Maze(){
		hero = new Hero(loc, 10, Color.RED, dir);
		setBoard();

		frame = new JFrame("A-Mazing Program");
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setVisible(true);
		frame.addKeyListener(this);

	}//Maze
	public void setBoard(){

		try{
			File fileName = new File("maze" + level + ".txt");
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			String text;
			int r = 0;

			while((text = input.readLine()) != null){
				for(int c = 0; c < text.length(); c++)
					maze[r][c] = text.charAt(c);
				r++;
			}
		}catch(IOException ioe){}

		if(draw3D){
			createWalls();
		}
	}

	public void paintComponent(Graphics g){
        //setBoard();
		super.paintComponent(g);//eraser
		int size = 10;
		Graphics2D g2 = (Graphics2D)g;

		if(maze[hero.getLoc().getR()+1][hero.getLoc().getC()] == 'e'){
			System.out.println("WINNER");
			g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
			g2.setColor(Color.RED);
			if(level == 1)
				level = 2;
            	//g2.drawString("You Beat the First Level! Press any button to go to the second level.", 100, frame.getHeight()/2);
			else if(level == 2)
				level = 3;
            	//g2.drawString("You Beat the Second Level! Press any button to go to the third level.", 100, frame.getHeight()/2);
            else if(level == 3){
				level = 4;
				System.out.println("2");
			}
			hero.getLoc().setRCoord(2);
			hero.getLoc().setCCoord(0);
			hero.setDir(1);
			checkPointUsed = 0;
			hammer = 3;
			setBoard();
		}

		if(!draw3D){
			g2.setColor(Color.BLACK);
			g2.fillRect(0,0, 1000, 800);
			g2.setColor(Color.GRAY);
			for(int c = 0; c < maze[0].length;c++){
				for(int r = 0; r < maze.length; r++){
					try{
						if(maze[r+1][c] == 'e'){
							g2.setColor(Color.YELLOW);
							g2.fillRect(c*size, r*size, size, size);
							g2.setColor(Color.GRAY);
						}
						else if(maze[r][c] == ' ') //|| maze[r][c] == 'e')
							g2.fillRect(c*size, r*size, size, size);
						else
							g2.drawRect(c*size, r*size, size, size);
					}catch(Exception e){}
				}
			}
			try{
				if(checkPointUsed>0 && maze[checkPoint.getR()][checkPoint.getC()] == ' '){
					g2.setColor(Color.BLUE);
					g2.fillRect(checkPoint.getC()*size, checkPoint.getR()*size, size, size);
				}
			}catch(Exception e){}

			dir = hero.getDir();
			g2.setColor(Color.RED);
			g2.fillRect(hero.getLoc().getC()*size, hero.getLoc().getR()*size, size, size);
		}
		else{
			for(int i = 0; i < walls.size(); i++){
				/*//g2.setColor(Color.GRAY);
				String temp = walls.get(i).returnType();
				System.out.println(colors.size());
				System.out.println(walls.size());

				g2.setColor(colors.get(i));

				//else
				//	g2.setColor(new Color(255-50,255-50,255-50));//Color.BLACK);
				g2.fillPolygon(walls.get(i).getCols(), walls.get(i).getRows(), 4);

				g2.setColor(Color.BLACK);
				if(!(temp.equals("Front")))
					g2.drawPolygon(walls.get(i).getCols(), walls.get(i).getRows(), 4);*/

				g2.setColor(walls.get(i).returnColor());
				g2.fillPolygon(walls.get(i).getPoly());
				g2.setColor(Color.BLACK);
				g2.drawPolygon(walls.get(i).getPoly());
			}
		}
		g2.setFont(new Font("TimesRoman", Font.BOLD, 16));
		g2.setColor(Color.BLUE);
        g2.drawString("To set a one-time checkpoint, press \"c\"(one per level)",450,frame.getHeight()/10);
        g2.drawString("To go back to the checkpoint, press \"c\" again(can only use once)",450,frame.getHeight()/8);
        g2.drawString("Press space bar to switch to and from 3D",450,frame.getHeight()/30);
        g2.drawString("Hammers Left(Press \"h\" to use): " + hammer,450,frame.getHeight()/15);

		g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
		g2.setColor(Color.YELLOW);
		if(level == 4)
           	g2.drawString("You Beat All 3 Levels!", 100, frame.getHeight()/2);


	}//paintComponent

	public void keyReleased(KeyEvent e){
	}//keyReleased
	public void keyPressed(KeyEvent e){
		hero.move(e.getKeyCode(), maze);

		if(e.getKeyCode() == 32){
			draw3D = !draw3D;
		}
		if(draw3D){
			createWalls();
		}

		if(e.getKeyCode() == 67 && checkPointUsed == 0){//press c and checkpoint not set
			checkPoint = new Location(hero.getLoc().getR(), hero.getLoc().getC());
			checkPointUsed = 1;
		}
		else if(e.getKeyCode() == 67 && checkPointUsed == 1){//press c to go back to checkpoint that has been set

			System.out.println("in");
			hero.getLoc().setRCoord(checkPoint.getR());
			hero.getLoc().setCCoord(checkPoint.getC());
			System.out.println("out");

			checkPointUsed = 2;
		}
		try{
			if(maze[hero.getLoc().getR()][hero.getLoc().getC()+1] == '#' && hero.getDir() == 1 && e.getKeyCode() == 72 && hammer > 0){//use hammer (right)
				maze[hero.getLoc().getR()][hero.getLoc().getC()+1] = ' ';
				hammer--;
				System.out.println(hammer);
			}
			if(maze[hero.getLoc().getR()+1][hero.getLoc().getC()] == '#' && hero.getDir() == 2 && e.getKeyCode() == 72 && hammer > 0){//use hammer (down)
				maze[hero.getLoc().getR()+1][hero.getLoc().getC()] = ' ';
				hammer--;
				System.out.println(hammer);
			}
			if(maze[hero.getLoc().getR()][hero.getLoc().getC()-1] == '#' && hero.getDir() == 3 && e.getKeyCode() == 72 && hammer > 0){//use hammer (left)
				maze[hero.getLoc().getR()][hero.getLoc().getC()-1] = ' ';
				hammer--;
				System.out.println(hammer);
			}
			if(maze[hero.getLoc().getR()-1][hero.getLoc().getC()] == '#' && hero.getDir() == 0 && e.getKeyCode() == 72 && hammer > 0){//use hammer (up)
				maze[hero.getLoc().getR()-1][hero.getLoc().getC()] = ' ';
				hammer--;
				System.out.println(hammer);
			}
		}catch(Exception exception){}

		repaint();
	}//keyPressed
	public void keyTyped(KeyEvent e){

	}//keyTyped

	public void createWalls(){
		walls = new ArrayList<Wall>();

		for(int n = 0; n < 5; n++){
			walls.add(getLeftPath(n));
			walls.add(getRightPath(n));
		}

		floorsAndCeilings();

		int rr = hero.getLoc().getR();
		int cc = hero.getLoc().getC();
		int dir = hero.getDir();
		switch(dir){
			case 0:
				if(maze[rr-1][cc] == '#'){
					System.out.println("POG");
				}
				for(int n = 0; n < 5; n++){
					try{
						if(maze[rr-n][cc] == '#'){
							walls.add(wallInFront(n));
							n = 6;
						}
						else{
							if(maze[rr-n][cc-1] == '#'){
								walls.add(getLeft(n));

							}
							else{
								walls.add(getLeftPath(n));
								walls.add(getLeftTriangle(n));
								walls.add(getLeftFloorTriangle(n));
							}
							if(maze[rr-n][cc+1] == '#'){
								walls.add(getRight(n));

							}
							else{
								walls.add(getRightPath(n));
								walls.add(getRightTriangle(n));
								walls.add(getRightFloorTriangle(n));
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
			case 1:
				for(int n = 0; n < 5; n++){
					try{
						if(maze[rr][cc+n] == '#'){
							walls.add(wallInFront(n));
							n = 6;
						}
						else{
							if(maze[rr-1][cc+n] == '#'){
								walls.add(getLeft(n));

							}
							else if(maze[rr-1][cc+n] == ' '){
								walls.add(getLeftPath(n));
								walls.add(getLeftTriangle(n));
								walls.add(getLeftFloorTriangle(n));
							}
							if(maze[rr+1][cc+n] == '#'){
								walls.add(getRight(n));

							}
							else if(maze[rr+1][cc+n] == ' '){
								walls.add(getRightPath(n));
								walls.add(getRightTriangle(n));
								walls.add(getRightFloorTriangle(n));
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
			case 2:
				for(int n = 0; n < 5; n++){
					try{
						if(maze[rr+n][cc] == '#'){
							walls.add(wallInFront(n));
							n = 6;
						}
						else{
							if(maze[rr+n][cc+1] == '#'){
								walls.add(getLeft(n));

							}
							else{
								walls.add(getLeftPath(n));
								walls.add(getLeftTriangle(n));
								walls.add(getLeftFloorTriangle(n));
							}
							if(maze[rr+n][cc-1] == '#'){
								walls.add(getRight(n));

							}
							else{
								walls.add(getRightPath(n));
								walls.add(getRightTriangle(n));
								walls.add(getRightFloorTriangle(n));
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
			case 3:
				for(int n = 0; n < 5; n++){
					try{
						if(maze[rr][cc-n] == '#'){
							walls.add(wallInFront(n));
							n = 6;
						}
						else{
							if(maze[rr+1][cc-n] == '#'){
								walls.add(getLeft(n));

							}
							else{
								walls.add(getLeftPath(n));
								walls.add(getLeftTriangle(n));
								walls.add(getLeftFloorTriangle(n));
							}
							if(maze[rr-1][cc-n] == '#'){
								walls.add(getRight(n));

							}
							else{
								walls.add(getRightPath(n));
								walls.add(getRightTriangle(n));
								walls.add(getRightFloorTriangle(n));
							}
						}
					}catch(ArrayIndexOutOfBoundsException e){}
				}
				break;
		}
	}//CreateWalls

	public void floorsAndCeilings(){

		for(int n = 0; n < 5; n++){
			walls.add(getCeil(n));
			walls.add(getFloor(n));
		}
	}//Floors and Ceilings

	public Wall getLeft(int n){//trapezoid
		int[] rLocs = new int[] {100+50*n, 150+50*n, 700-50*n, 750-50*n};
		int[] cLocs = new int[] {100+50*n, 150+50*n, 150+50*n, 100+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Left", 0);
	}//Left

	public Wall getLeftPath(int n){//Rect
		int[] rLocs = new int[] {150+50*n, 150+50*n, 700-50*n, 700-50*n};
		int[] cLocs = new int[] {100+50*n, 150+50*n, 150+50*n, 100+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Left Path", 0);
	}//Left Path

	public Wall getRight(int n){//trapezoid
		int[] rLocs = new int[] {700-50*n, 750-50*n, 100+50*n, 150+50*n};
		int[] cLocs = new int[] {700-50*n, 750-50*n, 750-50*n, 700-50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right", 0);
	}//Right

	public Wall getRightPath(int n){//Rect
		int[] rLocs = new int[] {700-50*n, 700-50*n, 150+50*n, 150+50*n};
		int[] cLocs = new int[] {700-50*n, 750-50*n, 750-50*n, 700-50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right Path", 0);
	}//Right Path
	public Wall getCeil(int n){//trapezoid
		int[] rLocs = new int[] {100+50*n, 100+50*n, 150+50*n, 150+50*n};
		int[] cLocs = new int[] {100+50*n, 750-50*n, 700-50*n, 150+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Ceiling", 0);
	}//Ceiling
	public Wall getFloor(int n){//trapezoid
		int[] rLocs = new int[] {700-50*n, 700-50*n, 750-50*n, 750-50*n};
		int[] cLocs = new int[] {150+50*n, 700-50*n, 750-50*n, 100+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Floor", 0);
	}//Floor

	public Wall wallInFront(int n){//rect
		int[] rLocs = new int[] {100+50*n, 750-50*n, 750-50*n, 100+50*n};
		int[] cLocs = new int[] {100+50*n, 100+50*n, 750-50*n, 750-50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Front", 0);
	}//Floor

	public Wall getRightTriangle(int n){//triangle

		int[] rLocs = new int[] {100+50*n, 150+50*n, 150+50*n};
		int[] cLocs = new int[] {750-50*n, 700-50*n, 750-50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right Triangle", 0);
	}//Floor

	public Wall getRightFloorTriangle(int n){//triangle

		int[] rLocs = new int[] {700-50*n, 700-50*n, 750-50*n};
		int[] cLocs = new int[] {700-50*n, 750-50*n, 750-50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right Triangle", 0);
	}//Floor

	public Wall getLeftTriangle(int n){//triangle
		int[] rLocs = new int[] {100+50*n, 150+50*n, 150+50*n};
		int[] cLocs = new int[] {100+50*n, 100+50*n, 150+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right Triangle", 0);
	}//Floor

	public Wall getLeftFloorTriangle(int n){//triangle

		int[] rLocs = new int[] {700-50*n, 700-50*n, 750-50*n};
		int[] cLocs = new int[] {100+50*n, 150+50*n, 100+50*n};

		return new Wall(rLocs, cLocs, 255-50*n, 255-50*n, 255-50*n, "Right Triangle", 0);
	}//Floor
	public static void main(String[]args){
		Maze app = new Maze();
	}//main
}