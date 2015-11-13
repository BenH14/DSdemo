package mainPKG;

public class Controller {

	private Builder build;

	public Controller() {

		build = new Builder();

	}

	public void getSettings() {

		boolean end = false;

		System.out.println("Fractal Landscapes Demo");

		while(end == false) {

			System.out.println("Enter Command");
			String input = IBIO.inputString("- ");

			switch(input) {
			case "exit":
				end = true;
				System.exit(0);
				break;
			case "gen":
				build.reset();
				build.DiamondSquare(0,0, 511, 511);
				build.renderImage(true);
				break;
			case "smooth":
				System.out.println("How many passes?");
				build.smoothAll(IBIO.inputInt("- "));
				build.renderImage(true);
				break;
			case "smoothanim":
				System.out.println("How many passes?");

				for (int x = IBIO.inputInt("- "); x > 0; x--) {
					try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
					build.smoothAll(1);
					build.renderImage(false);
				}
				break;
			case "genanim":
				build.genAnim(0,0, 511, 511);
			case "reset":
				build.reset();
				build.renderImage(true);
				break;
			default:
				System.out.println("Invalid Command");
				break;
			}
			
			build.nodeCount = 512*512;
		}
	}

}
