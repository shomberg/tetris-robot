import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisRobot //implements KeyListener
{

	private static boolean[][] field;
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
	private static long startTime;
	//private boolean cont = false;

	public static void main(String[] args) throws AWTException{
		field = new  boolean[20][10];
		boolean[][] f1 = new boolean[20][10];
		boolean[][] f2 = new boolean[20][10];
		f1[9][0] = true;
		System.out.println(Piece.detHeight(f2,f1));
		//		field[0][3] = true;
		//		field[0][4] = true;
		//		field[1][4] = true;
		//		field[0][5] = true;
		//		Piece a = new Piece();
		//		print(a.blocks);
		//		int[] b = a.detOptimal(field, false);
		//		System.out.println(Arrays.toString(b));
		//		field = a.iterate(field, b[0], b[1]);
		//        print(field);
		//        if(true)
		//        	return;
		int top = 0;
		boolean emergent = false;
		Robot r = new Robot();
		r.setAutoWaitForIdle(false);
		delay(3000);
		Piece[] p = {new Piece(), new Piece(), new Piece(), new Piece(), new Piece(), new Piece()};
		for(int i = 1; i < p.length; i++){
			p[i].getNextPiece(i, r);
			print(p[i].blocks);
			System.out.println("------------------------");
		}
		Piece hold = new Piece();
		boolean firstHold = true;

		keyType(r, KeyEvent.VK_SPACE);
		for(int i = 0; i < 3; i++){
			for(int j = 2; j < 7; j++){
				int xCheck = MINX+j*OFFSET + OFFSET/2;
				int yCheck = MINY+(19-i)*OFFSET+OFFSET/2;
				Color c = r.getPixelColor(xCheck, yCheck);
				if(!c.equals(Piece.GRAY1) && !c.equals(Piece.GRAY2)){
					field[i][j] = true;
				}
			}
		}
		startTime = System.currentTimeMillis();

		while(System.currentTimeMillis()-startTime < 120000){// && cont
			System.out.println("start cycle");
			//p.findSelf(r);
			for(int i = 0; i < p.length-1; i++)
				p[i] = p[i+1];
			p[p.length-1] = new Piece();
			p[p.length-1].getNextPiece(p.length-1, r);

			int[] optimal = p[0].detOptimal(field, p, hold, 0, emergent, false, 0, 0);
			//			for(Piece piece : p){
			//				System.out.println("----Block------");
			//				print(piece.blocks);
			//			}
			System.out.println("----------------------");
			print(field);
			System.out.println("----------------------");
			System.out.println(Arrays.toString(optimal));
			System.out.println("make move");
			//Make move 
			if(optimal[0] < 0){
//				System.out.println("before:");
//				print(p[0].blocks);
//				print(hold.blocks);
				keyType(r, KeyEvent.VK_C);
				Piece temp = hold;
				hold = p[0];
				if(firstHold){
					firstHold = false;
					for(int i = 0; i < p.length-1; i++)
						p[i] = p[i+1];
					p[p.length-1] = new Piece();
					p[p.length-1].getNextPiece(p.length-1, r);
				}
				else{
					p[0] = temp;
				}
//				System.out.println("after:");
//				print(p[0].blocks);
//				print(hold.blocks);
				optimal = p[0].detOptimal(field, p, hold, 0, emergent, true, 0, 0);
				System.out.println(Arrays.toString(optimal));
				System.out.println("make move");
			}
			for(int i = 0; i < optimal[0]; i++){
				keyType(r, KeyEvent.VK_UP);
			}
			if(optimal[1] > 0){
				for(int i = 0; i < optimal[1]; i++)
					keyType(r, KeyEvent.VK_RIGHT);
			}
			else if(optimal[1] < 0){
				for(int i = 0; i > optimal[1]; i--)
					keyType(r, KeyEvent.VK_LEFT);
			}
			keyType(r, KeyEvent.VK_SPACE);
			System.out.println("move made");
			delay(250);
			//Reset data
			field = p[0].iterate(field, optimal[0], optimal[1]);
			Piece.clearLines(field);
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
			if(top > 15)
				emergent = false;
			else
				emergent = false;
			//			delay(400);
		}
	}

	private static void keyType(Robot r, int keycode){
		r.keyPress(keycode);
		delay(63);
		r.keyRelease(keycode) ;
		delay(63);
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
