import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisRobot //implements KeyListener
{

	//	public static final int MINX = 216;	//School half
	//	public static final int MINY = 321;	//School half
//	public static final int MINX = 219;	//Home half
//	public static final int MINY = 325;	//Home half
//	public static final int MINX = 218; //Firefox half
//	public static final int MINY = 307; //Firefox half
	public static final int MINX = 215; //Linux half
	public static final int MINY = 323; //Linux half
//	public static final int MINX = 380;	//Home full
//	public static final int MINY = 305;	//Home full
	public static final int OFFSET = 18;
	//private boolean cont = false;

	public static void main(String[] args) throws AWTException{
//		Player p = new Player();
//		delay(3000);
//		p.run(MINX, MINY, OFFSET);
		
		long startTime = System.currentTimeMillis();
		int ret = testRun(-3000,400,-6,-3,40);
		System.out.println("that took: " + (System.currentTimeMillis()-startTime) + " milliseconds");
		System.out.println("cleared: " + ret + " lines");
		
//		boolean[] soFar = new boolean[7];
//		for(int i = 0; i < 21; i++)
//			System.out.println(genPiece(soFar));
	}
	
	public static int testRun(double ho, double li, double la, double he, double pi){
		int sent = 0;
		int combo = 0;
		
		double hole = ho;
		double line = li;
		double lane = la;
		double height = he;
		double piece = pi;
		
		boolean[][] field = new  boolean[20][10];
		int top = 0;
		boolean emergent = false;
		boolean[] soFar = new boolean[7];
		Piece[] p = {new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece)};
		for(int i = 1; i < p.length; i++){
			p[i].setPiece(genPiece(soFar));
			print(p[i].blocks);
			System.out.println("------------------------");
		}

		Piece hold = new Piece(hole,line,lane,height,piece);
		boolean firstHold = true;
		
		for(int pNum = 0; pNum < 200; pNum++){
			System.out.println("start cycle");
			for(int i = 0; i < p.length-1; i++)
				p[i] = p[i+1];
			p[p.length-1] = new Piece(hole,line,lane,height,piece);
			p[p.length-1].setPiece(genPiece(soFar));

			int[] optimal = p[0].detOptimal(field, p, hold, 0, emergent, false, 0, 0);
			System.out.println("----------------------");
			print(field);
			System.out.println("----------------------");
			System.out.println(Arrays.toString(optimal));
			System.out.println("make move");
			//Make move 
			if(optimal[0] < 0){
				Piece temp = hold;
				hold = p[0];
				if(firstHold){
					firstHold = false;
					for(int i = 0; i < p.length-1; i++)
						p[i] = p[i+1];
					p[p.length-1] = new Piece(hole,line,lane,height,piece);
					p[p.length-1].setPiece(genPiece(soFar));
				}
				else{
					p[0] = temp;
				}
				optimal = p[0].detOptimal(field, p, hold, 0, emergent, true, 0, 0);
				System.out.println(Arrays.toString(optimal));
				System.out.println("make move");
			}
			//Reset data
			field = p[0].iterate(field, optimal[0], optimal[1]);
			int cleared = Piece.clearLines(field);
			if(cleared>0){
				combo++;
			}
			else{
				combo = 0;
			}
			switch(cleared){
			case 1:
				System.out.println("single");
				break;
			case 2:
				sent += 1;
				System.out.println("double!");
				break;
			case 3:
				sent += 2;
				System.out.println("triple!");
				break;
			case 4:
				sent += 4;
				System.out.println("TETRIS!");
				break;
			}
			if(combo <= 7){
				sent += (combo)/2;
			}
			else
				sent += 4;
			System.out.println("cleared: " + cleared);
			System.out.println("sent: " + sent);
			top = 0;
			while(true){
				boolean isIn = false;
				for(int i = 0; i < 10; i++)
					isIn = isIn || field[top][i];
				if(!isIn)
					break;
				top++;
				if(top == 20)
					break;
			}
//			if(top > 15)
//				emergent = false;
//			else
//				emergent = false;
//			delay(400);
		}
		return sent;
	}
		
	public static int genPiece(boolean[] soFar){
		Random gen = new Random();
		int num = 0;
		for(int i = 0; i < soFar.length; i++)
			num+=soFar[i]?0:1;
		int[] choose = new int[num];
		num=0;
		for(int i = 0; i < soFar.length; i++)
			if(!soFar[i])
				choose[num++]=i;
		int pick = gen.nextInt(num);
		soFar[choose[pick]]=true;
		if(num==1)
			for(int i = 0; i < soFar.length; i++)
				soFar[i]=false;
		return choose[pick];
	}

	private static void keyType(Robot r, int keycode){
		r.keyPress(keycode);
		delay(65);
		r.keyRelease(keycode) ;
		delay(65);
	}

	private static void delay(int ms){
		try{
			Thread.sleep(ms);
		}
		catch(InterruptedException e){
		}
	}

	private static void print(boolean[][] x){
		for(int i = x.length-1 ; i >= 0; i--){
			for(int j = 0; j < x[0].length; j++){
				System.out.print(x[i][j]?"X":" ");
			}
			System.out.println();
		}
	}

	/*public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			cont = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			cont = false;
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}*/
}
