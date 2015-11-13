package mainPKG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Builder{

	private static final long serialVersionUID = 1L;

	private BufferedImage img;
	private int node[][];
	private JFrame window;
	private JPanel panel;

	public int nodeCount;

	public Builder() {

		window = new JFrame("Diamond Square Algorith Demo");
		window .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(512, 512);
		window.setVisible(true);

		panel = new JPanel();
		panel.setVisible(true);
		window.add(panel);

		img = new BufferedImage(512,512, BufferedImage.TYPE_3BYTE_BGR);

		node = new int[512][512];

		nodeCount = 512*512;

		System.out.println(nodeCount + " Nodes Loaded!");

	}

	public void renderImage(boolean save) {

		//Paint the pixels
		for (int x = 0; x < 512; x++) {
			for (int y = 0; y < 512; y++) {
				int tempNode = node[x][y];
				Color pixColor;
				if(tempNode > 127) {
					tempNode = tempNode - 128;
					pixColor = new Color(255 - (tempNode * 2), 255, 0);	
				} else if(tempNode < 128 || tempNode > 0){ 
					pixColor = new Color(255, (tempNode * 2), 0);
				} else {
					pixColor = new Color(0,0,255);
				}

				img.setRGB(x, y, pixColor.getRGB());
			}
		}

		if(save == true) {
			File file = new File("terrain.png");
			try {
				ImageIO.write(img,"png",file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Graphics2D g2d = (Graphics2D) panel.getGraphics();

		g2d.drawImage(img, 0, 0, null);

	}

	public void reset() {
		for (int x = 0; x < 512; x++) {
			for (int y = 0; y < 512; y++) {
				node[x][y] = 128;
			}
		}

		node[0][0] = 255;
		node[511][0] = 0;
		node[0][511] = 255;
		node[511][511] = 0;System.out.println("Nodes Left : " + nodeCount);
	}

	public void genAnim(int StartX, int StartY, int EndX, int EndY) {
		//The exact same to diamond square but waits and draws as it goes
		if(StartX != EndX) {

			if(nodeCount % 10000 == 0) {
				System.out.println("Nodes Done : " + ((512*512) - nodeCount));
			}

			nodeCount--;

			//Get some deviation
			Random ranGen = new Random();
			int ranRange = 128;
			int ranInt = ranGen.nextInt(ranRange);
			int deviation =  ranInt - (ranRange / 2);
			//Set the target node
			int nodeX = (((EndX - StartX)+1) / 2) + StartX;
			int nodeY = (((EndY - StartY)+1) / 2) + StartY;

			node[nodeX][nodeY] = (/*add together the corner nodes and divide by 4 to get an average, then  add the devaiation*/(node[StartX][StartY] + node[StartX][EndY] + node[EndX][StartY] + node[EndX][EndY]) / 4)+ deviation;

			if(node[nodeX][nodeY] < 0) {
				node[nodeX][nodeY] = 0;
			} else if(node[nodeX][nodeY] > 255) {
				node[nodeX][nodeY] = 255;
			}

			if(nodeCount % 100 == 0) {
				renderImage(false);
			}

			if((EndX - StartX) > 1) {	
				//Start the algorithm within the other nodes
				genAnim(StartX, StartY, nodeX, nodeY);
				genAnim(StartX, nodeY, nodeX, EndY);
				genAnim(nodeX, StartY, EndX, nodeY);
				genAnim(nodeX,nodeY, EndX, EndY);
			}
		}

	}


	public void DiamondSquare(int StartX, int StartY, int EndX, int EndY) {

		if(StartX != EndX) {

			if(nodeCount % 10000 == 0) {
				System.out.println("Nodes Done : " + ((512*512) - nodeCount));
			}

			nodeCount--;

			//Get some deviation
			Random ranGen = new Random();
			int ranRange = 128;
			int ranInt = ranGen.nextInt(ranRange);
			int deviation =  ranInt - (ranRange / 2);
			//Set the target node
			int nodeX = (((EndX - StartX)+1) / 2) + StartX;
			int nodeY = (((EndY - StartY)+1) / 2) + StartY;

			node[nodeX][nodeY] = (/*add together the corner nodes and divide by 4 to get an average, then  add the devaiation*/(node[StartX][StartY] + node[StartX][EndY] + node[EndX][StartY] + node[EndX][EndY]) / 4)+ deviation;

			if(node[nodeX][nodeY] < 0) {
				node[nodeX][nodeY] = 0;
			} else if(node[nodeX][nodeY] > 255) {
				node[nodeX][nodeY] = 255;
			}

			if((EndX - StartX) > 1) {	
				//Start the algorithm within the other nodes
				DiamondSquare(StartX, StartY, nodeX, nodeY);
				DiamondSquare(StartX, nodeY, nodeX, EndY);
				DiamondSquare(nodeX, StartY, EndX, nodeY);
				DiamondSquare(nodeX,nodeY, EndX, EndY);
			}
		}


	}

	public void generateRiver(int givenX, int givenY) {

		int smallestX = givenX;
		int smallestY = givenY;
		int smallestValue = node[givenX][givenY];


	}

	public void smoothAll(int num) {
		int count = 0;
		while (count < num) {
			if(count % 4 == 0) {
				for(int x = 1; x < 511; x++) {
					for(int y = 1; y < 511; y++) {
						if(node[x][y] < 75 && node[x][y] != 0) {
							node[x][y]--;
						} else if (node[x][y] > 180 && node[x][y] != 255) {
							node[x][y]++;
						}

						int average = (node[x-1][y-1] + node[x-1][y] + node[x-1][y+1] + node[x][y+1] + node[x][y-1] + node[x+1][y+1] + node[x+1][y] + node[x+1][y-1]) / 9;
						if(node[x][y] < average) {
							node[x][y]++;
						} else if(node[x][y] > average) {
							node[x][y]--;
						}
					}
				}
			} else if (count % 4 == 1) {
				for(int x = 510; x > 1; x--) {
					for(int y = 1; y < 511; y++) {
						if(node[x][y] < 75 && node[x][y] != 0) {
							node[x][y]--;
						} else if (node[x][y] > 180 && node[x][y] != 255) {
							node[x][y]++;
						}

						int average = (node[x-1][y-1] + node[x-1][y] + node[x-1][y+1] + node[x][y+1] + node[x][y-1] + node[x+1][y+1] + node[x+1][y] + node[x+1][y-1]) / 9;
						if(node[x][y] < average) {
							node[x][y]++;
						} else if(node[x][y] > average) {
							node[x][y]--;
						}
					}
				}
			} else if (count % 4 == 2) {
				for(int x = 1; x < 511; x++) {
					for(int y = 510; y > 1; y--) {
						if(node[x][y] < 75 && node[x][y] != 0) {
							node[x][y]--;
						} else if (node[x][y] > 180 && node[x][y] != 255) {
							node[x][y]++;
						}

						int average = (node[x-1][y-1] + node[x-1][y] + node[x-1][y+1] + node[x][y+1] + node[x][y-1] + node[x+1][y+1] + node[x+1][y] + node[x+1][y-1]) / 9;
						if(node[x][y] < average) {
							node[x][y]++;
						} else if(node[x][y] > average) {
							node[x][y]--;
						}
					}
				}
			} else if (count % 4 == 3) {
				for(int x = 510; x > 1; x--) {
					for(int y = 510; y > 1; y--) {
						if(node[x][y] < 75 && node[x][y] != 0) {
							node[x][y]--;
						} else if (node[x][y] > 180 && node[x][y] != 255) {
							node[x][y]++;
						}

						int average = (node[x-1][y-1] + node[x-1][y] + node[x-1][y+1] + node[x][y+1] + node[x][y-1] + node[x+1][y+1] + node[x+1][y] + node[x+1][y-1]) / 9;
						if(node[x][y] < average) {
							node[x][y]++;
						} else if(node[x][y] > average) {
							node[x][y]--;
						}
					}
				}
			}
			count ++;
		}
	}


}
