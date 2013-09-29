import java.awt.*;
import java.util.*;

public class Piece {

	public int minX;
	public int maxX;
	public boolean[][] blocks;
	public static Color GRAY1 = new Color(47, 47, 47);
	public static Color GRAY2 = new Color(43, 43, 43);
	public static Color GRAY3 = new Color(153, 153, 153);
	public static Color BLACK1 = new Color(255, 255, 255);
	//First piece                        1                                    2                              3                          4                     5
	public static Color[] BLUE1 = {new Color(50, 213, 255), new Color(40, 149, 187), new Color(40, 149, 187), new Color(40, 149, 187), new Color(40, 149, 187), new Color(40, 149, 187)};	//Long
	public static Color[] BLUE2 = {new Color(72, 131, 255), new Color(72, 131, 255), new Color(72, 131, 255), new Color(72, 131, 255), new Color(72, 131, 255), new Color(72, 131, 255)};	//L
	public static Color[] YELLOW1 = {new Color(255, 218, 60), new Color(255, 204, 46), new Color(255, 204, 46), new Color(255, 204, 46), new Color(255, 204, 46), new Color(255, 204, 46)};	//Square
	public static Color[] ORANGE1 = {new Color(255, 162, 46), new Color(255, 162, 46), new Color(255, 162, 46), new Color(255, 162, 46), new Color(255, 162, 46), new Color(255, 162, 46)};	//L
	public static Color[] GREEN1 = {new Color(142, 238, 53), new Color(142, 238, 53), new Color(142, 238, 53), new Color(142, 238, 53), new Color(142, 238, 53), new Color(142, 238, 53)};	//Z
	public static Color[] RED1 = {new Color(255, 78, 106), new Color(255, 78, 106), new Color(255, 78, 106), new Color(255, 78, 106), new Color(255, 78, 106), new Color(255, 78, 106)};		//Z
	public static Color[] PURPLE1 = {new Color(235, 80, 205), new Color(235, 80, 205), new Color(235, 80, 205), new Color(235, 80, 205), new Color(235, 80, 205), new Color(235, 80, 205)};	//T
	public boolean blockRot = true;

	public final int recursionDepth = 5;
	public int pieceType;

	public Piece(){
		minX = 0;
		maxX = 0;
		blocks = new boolean[5][5];
		pieceType = -1;
		//		blocks[3][1] = true;
		//		blocks[3][2] = true;
		//		blocks[3][3] = true;
		//		blocks[3][4] = true;
		//		blockRot = false;
	}

	public Piece copySelf(){
		Piece ret = new Piece();
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				ret.blocks[i][j] = blocks[i][j];
			}
		}
		ret.blockRot = blockRot;
		return ret;
	}

	public void getNextPiece(int num, Robot r){
		blocks = new boolean[5][5];
		int xCheck = TetrisRobot.MINX;
		int yCheck = TetrisRobot.MINY;
		xCheck += 234;	//Home 453
		yCheck += 49;	//Home 374
		if(num > 1){
			yCheck += 67; //Home 441
		}
		if(num > 2){
			yCheck += 55; //Home 496, 548, 600
			yCheck += 52*(num-3);
		}
		Color c = r.getPixelColor(xCheck, yCheck);
		if(c.equals(BLUE1[num-1])){
			blocks[3][1] = true;
			blocks[3][2] = true;
			blocks[3][3] = true;
			blocks[3][4] = true;
			blockRot = false;
			System.out.println("Long");
			pieceType = 0;
		}
		else if(c.equals(BLUE2[num-1])){
			blocks[3][1] = true;
			blocks[2][1] = true;
			blocks[2][2] = true;
			blocks[2][3] = true;
			blockRot = true;
			System.out.println("Blue L");
			pieceType = 1;
		}
		else if(c.equals(YELLOW1[num-1])){
			blocks[3][2] = true;
			blocks[3][3] = true;
			blocks[2][2] = true;
			blocks[2][3] = true;
			blockRot = false;
			System.out.println("Square");
			pieceType = 2;
		}
		else if(c.equals(ORANGE1[num-1])){
			blocks[3][3] = true;
			blocks[2][1] = true;
			blocks[2][2] = true;
			blocks[2][3] = true;
			blockRot = true;
			System.out.println("Orange L");
			pieceType = 3;
		}
		else if(c.equals(GREEN1[num-1])){
			blocks[2][1] = true;
			blocks[2][2] = true;
			blocks[3][2] = true;
			blocks[3][3] = true;
			blockRot = true;
			System.out.println("Green Z");
			pieceType = 4;
		}
		else if(c.equals(RED1[num-1])){
			blocks[3][1] = true;
			blocks[3][2] = true;
			blocks[2][2] = true;
			blocks[2][3] = true;
			blockRot = true;
			System.out.println("Red Z");
			pieceType = 5;
		}
		else if(c.equals(PURPLE1[num-1])){
			blocks[3][2] = true;
			blocks[2][1] = true;
			blocks[2][2] = true;
			blocks[2][3] = true;
			blockRot = true;
			System.out.println("T");
			pieceType = 6;
		}
	}

	public void findSelf(Robot r){
		blocks = new boolean[5][5];
		//Find which blocks are being used
		for(int i = 16; i < 20; i++){
			for(int j = 3; j <= 6; j++){
				int xCheck = TetrisRobot.MINX+j*TetrisRobot.OFFSET;
				int yCheck = TetrisRobot.MINY+(20-i)*TetrisRobot.OFFSET;
				Color c = r.getPixelColor(xCheck, yCheck);
				if(!c.equals(GRAY1) && !c.equals(GRAY2)){
					blocks[i-16][j-3] = true;
					if(c.equals(BLUE1[0]) || c.equals(YELLOW1[0]))
						blockRot = false;
					else
						blockRot = true;
				}
			}
		}
		//Move down all blocks to the appropriate height
		boolean[] rows = new boolean[5];
		for(int row = 0; row < 5; row++){
			for(int j = 0; j < 5; j++){
				rows[row] = rows[row] || blocks[row][j];
			}
		}
		if(rows[0]){
			for(int i = 0; i< 4; i++){
				for(int j = 0; j < 5; j++){
					blocks[i][j] = blocks[i+1][j];
				}
			}
			for(int j = 0; j < 5; j++)
				blocks[4][j] = false;
		}
	}

	public int[] detOptimal(boolean[][] field, Piece[] p, Piece hold, int depth, boolean emergent, boolean caught, int linesSent, int combo){
		int[] ret = new int[7];
		boolean first = true;
		if(!caught){
			Piece[] newPieces = Arrays.copyOf(p, p.length);
			newPieces[depth] = hold;
			ret = newPieces[depth+1].detOptimal(field, newPieces, this, depth, emergent, true, linesSent, combo);
			ret[0] = -1;
			first = false;
		}
		for(int rot = 0; rot < 4; rot++){
			for(int trans = -5; trans <= 5; trans++){
				if(!inBounds(trans))
					continue;
				int[] test;
				boolean[][] newField = iterate(field, 0, trans);
				if(depth == 1)
					test = detMoveValue(field, newField, rot, trans, emergent, linesSent, hold.pieceType);
				else{

					int cleared = clearLines(newField);
					int sent = 0;
					switch(cleared){
					case 2: sent += 1; break;
					case 3: sent += 2; break;
					case 4: sent += 3; break;
					}
					if(cleared > 0){
						sent += 0;//(combo+1)/2;
					}
					test = p[depth+1].detOptimal(newField, p, hold, depth+1, emergent, false, linesSent+sent, sent>0?combo+1:0);
				}
				if(smaller(test, ret, emergent) || first){
					ret = test;
					ret[0] = rot;
					ret[1] = trans;
					first = false;
				}
			}
			rotate();
		}
		return ret;
	}

	public static int[] detMoveValue(boolean[][] field, boolean[][] newField, int rot, int trans, boolean emergent, int cleared, int type){
		//boolean[][] newField = iterate(field, 0, trans);
		int[] ret = new int[7];
		ret[0] = rot;
		ret[1] = trans;
		if(emergent){
			ret[2] = detTotHeight(field);
			ret[3] = detHoleNum(field);
			ret[4] = detClearLanes(field);
			ret[5] = detHeight(field, newField);
		}
		else{
			ret[3] = cleared + clearLines(newField);
			ret[2] = detHoleNum(newField);
			ret[4] = detClearLanes(newField);
			ret[5] = detTotHeight(newField);
			ret[6] = type==0?1:0;
		}
		return ret;
	}

	public static int detTotHeight(boolean[][] field){
		int[] top = new int[10];
		for(int j = 0; j < 10; j++){
			for(int i = 19; i >= 0; i--){
				if(field[i][j]){
					top[j] = i;
					break;
				}
			}
		}

		int avg = 0;
		int height;
		for(height = 19; height >= 0; height--){
			boolean done = false;
			for(int j = 0; j < 10; j++){
				done = field[height][j] || done;
			}
			if(done)
				break;
		}

		for(int j = 0; j < 9; j++){
			avg += top[j];
		}
		avg /= 9;

		int ret = 2*Math.abs(avg-height);
		if(height < 4)
			ret += (4-height)/2;
		else
			ret += (height-4)*5;

		return ret;
	}

	public static int detHoleNum(boolean[][] field){
		int ret = 0;
		for(int j = 0; j < 10; j++){
			int height;
			for(height = 19; height>= 0 && !field[height][j]; height--);
			boolean isOne = false;
			for(int i = 19; i >= 0; i--){
				isOne = isOne || field[i][j];
				if(isOne && !field[i][j])
					ret += 1;
			}
		}
		return ret;
	}

	public static int detClearLanes(boolean[][] field){
		//		int tot = 0;
		//		for(int i = 0; i < 20; i++){
		//			for(int j = 0; j < 10; j++){
		//				tot += field[i][j]?1:0;
		//			}
		//		}

		int[] top = new int[10];
		for(int j = 0; j < 10; j++){
			for(int i = 19; i >= 0; i--){
				if(field[i][j]){
					top[j] = i;
					break;
				}
			}
		}


		int min = 9;
		//		for(int j = 0; j < 10; j++){
		//			if(top[j] < top[min]){
		//				min = j;
		//			}
		//		}

		int diff = 0;
		for(int j = 1; j < 9; j++){
			if(j+1 != min)
				diff += Math.abs(top[j]-top[j+1]);
			if(j-1 != min)
				diff += Math.abs(top[j] - top[j-1]);
		}
		//
		//		if(min != 0 && min != 1){
		diff += Math.abs(top[0]-top[1]); 
		//		}
		//
		//		if (min != 9 && min != 8){
		//			diff += Math.abs(top[9]-top[8]); 
		//		}
		//
		//		if(min == 0 || min == 9)
		//			diff -= 10000;
		//
		//		for(int i = top[min]+1; i <= top[min]+4 && i < 20; i++){
		//			boolean ripe = true;
		//			for(int j = 0; j < 10; j++){
		//				if(j == min)
		//					continue;
		//				ripe = ripe || field[i][j];
		//			}
		//			if(ripe)
		//				diff -= 15;
		//		}

//		boolean clearRight = true;
//		for(int i = 0; i < 20; i++){
//			clearRight = clearRight && !field[i][9];
//		}
//		if(clearRight)
//			diff -= 1000;

		return diff;
	}

	public static int detHeight(boolean[][] oldField, boolean[][] newField){
		for(int i = 19; i >=0; i--){
			for(int j = 0; j < 10; j++){
				if(newField[i][j] && !oldField[i][j]){
					return i;
				}
			}
		}
		return 0;
	}

	public boolean inBounds(int trans){
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if(blocks[i][j] && (j+trans+2 < 0 || j+trans+2 >= 10))
					return false;
			}
		}
		return true;
	}

	public void rotate(){
		boolean[][] ret = new boolean[5][5];
		if(blockRot){	//If the piece rotates around a particular block
			for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					ret[4-j][i] = blocks[i][j];
				}
			}
		}
		else{	//If the piece rotates around a point between four blocks
			for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					if(5-j < 5)
						ret[5-j][i] = blocks[i][j];
				}
			}
		}
		blocks = ret;
	}

	public boolean[][] iterate(boolean[][] field, int rot, int trans){
		//Copy field to a new array to return
		boolean[][] ret = new boolean[field.length][field[0].length];
		for(int i = 0; i < field.length; i++){
			for(int j = 0; j < field[0].length; j++){
				ret[i][j] = field[i][j];
			}
		}
		for(int i = 0; i < rot; i++){
			rotate();
		}
		//Determine the vertical drop distance in y
		int y = 0;
		boolean done = false;
		for(y = 15; y > -5; y--){	//Iterate through height of bottom of block
			for(int i = 0; i < 5; i++){		//Iterate vertically through block array
				for(int j = 0; j < 5; j++){	//Iterate horizontally across the block array
					if(y + i >= 0 && j + 2 + trans >= 0  && j + 2 + trans < 10 && ret[y+i][j+2+trans] && blocks[i][j]){
						//block checked within field bounds, a block intersects an existing one in the field
						y++;	//go up one level to prevent intersection
						done = true;
						break;
					}
					if(y + i < 0 && j + 2 + trans >= 0  && j + 2 + trans < 10 &&  blocks[i][j]){
						//block checked is below the field's bottom bound and exists
						y++;	//go up one level to prevent going past the bottom
						done = true;
						break;
					}
				}
				if(done)
					break;
			}
			if(done)
				break;
		}
		//Fill the new array with piece after the resulting drop
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if(y + i >= 0 && j + 2 + trans >= 0 && j + 2 + trans < 10 && blocks[i][j]){
					try{
						ret[y+i][j+2+trans] = true;
					}
					catch(Exception e){
						//						System.out.println("y, i, j, trans, vals");
						//				c		System.out.println(y + " " + i + " " + j + " " + trans + " " + (y+i) + " " + (j + 2 + trans));
					}
				}
			}
		}
		return ret;
	}

	public static int clearLines(boolean[][] field){
		int ret = 0;
		for(int i = 0; i < 20; i++){	//Iterate through possible full rows
			boolean clear = true;
			for(int j = 0; j < 10; j++){	//Check each block in the row
				clear = clear && field[i][j];
			}
			if(clear){
				for(int r = i; r < 19; r++){	//Move everything down from row i upward
					for(int j = 0; j < 10; j++){
						field[r][j] = field[r+1][j];
					}
				}
				ret++;
				i--;
			}
		}
		return ret;
	}

	private static boolean smaller(int[] move1, int[] move2, boolean emergent){
		if(emergent){
			if(move1[2] < move2[2])
				return false;
			if(move1[2] > move2[2])
				return true;
			if(move1[3] < move2[3])
				return false;
			if(move1[3] > move2[3])
				return true;
			if(move1[4] > move2[4])
				return false;
			if(move1[4] < move2[4])
				return true;
			if(move1[5] < move2[5])
				return false;
			if(move1[5] > move2[5])
				return true;
		}
		else{
			//			int[] ranks = {1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
			//			int check1 = -1, check2 = -1;
			//			for(int i = 0; i < 5; i++){
			//				if(ranks[i] == move1[3])
			//					check1 = i;
			//				if(ranks[i] == move2[3])
			//					check2 = i;
			//			}

			//			int[] weights = {0, -10, 15, 55, 100, 150, 200};
			//			int check1 = weights[move1[3]];
			//			int check2 = weights[move2[3]];
			int check1 = move1[3];
			int check2 = move2[3];

			double holeWeight = -75000;
			double lineWeight = 10000;
			double laneWeight = -150;
			double heightWeight = -75;
			double pieceWeight = 1000;
			//			holeWeight*move[2]+lineWeight*check1+laneWeight*move[4]+heightWeight*move[5]
			double weight1 = holeWeight*move1[2]+lineWeight*check1+laneWeight*move1[4]+heightWeight*Math.pow(move1[5], 2)+pieceWeight*move1[6];
			double weight2 = holeWeight*move2[2]+lineWeight*check2+laneWeight*move2[4]+heightWeight*Math.pow(move2[5], 2)+pieceWeight*move2[6];

			return (weight1 > weight2);

			//			if(check1 > 2 || check2 > 2){
			//				if(check1 < check2)
			//					return false;
			//				if(check1 > check2)
			//					return true;
			//			}
			//			if(move1[2] > move2[2])
			//				return false;
			//			if(move1[2] < move2[2])
			//				return true;
			//			if(check1 < check2)
			//				return false;
			//			if(check1 > check2)
			//				return true;
			//			if(move1[5] > move2[5])
			//				return false;
			//			if(move1[5] < move2[5])
			//				return true;
			//			if(move1[4] < move2[4])
			//				return false;
			//			if(move1[4] > move2[4])
			//				return true;
		}
		return false;
	}
}
