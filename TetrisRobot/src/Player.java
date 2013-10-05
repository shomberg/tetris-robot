import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Player {

	private static boolean[][] field;
	private static long startTime;
	
	public void run(int minx, int miny, int offset) throws AWTException{
		int count = 0;
		double hole = -75000;
		double line = 10000;
		double lane = -150;
		double height = -75;
		double piece = 1000;
		field = new  boolean[20][10];
		int top = 0;
		boolean emergent = false;
		Robot r = new Robot();
		r.setAutoWaitForIdle(false);
		Piece[] p = {new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece), new Piece(hole,line,lane,height,piece)};
		for(int i = 1; i < p.length; i++){
			p[i].getNextPiece(i, r);
			print(p[i].blocks);
			System.out.println("------------------------");
		}
		Piece hold = new Piece(hole,line,lane,height,piece);
		boolean firstHold = true;

		keyType(r, KeyEvent.VK_SPACE);
		for(int i = 0; i < 3; i++){
			for(int j = 2; j < 7; j++){
				int xCheck = minx+j*offset + offset/2;
				int yCheck = miny+(19-i)*offset+offset/2;
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
			p[p.length-1] = new Piece(hole,line,lane,height,piece);
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
				count++;
				hold = p[0];
				if(firstHold){
					firstHold = false;
					for(int i = 0; i < p.length-1; i++)
						p[i] = p[i+1];
					p[p.length-1] = new Piece(hole,line,lane,height,piece);
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
//			if(top > 15)
//				emergent = false;
//			else
//				emergent = false;
//			delay(400);
			count++;
		}
		System.out.println("count: " + count);
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
}
