package game;


import java.awt.Font;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoMouseWheelHandler;
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

import character.Character;

public class GameWindow  implements MinuetoKeyboardHandler, MinuetoMouseHandler, MinuetoMouseWheelHandler {

	// The event queue
	private MinuetoEventQueue eventQueue;		// The Minueto queue that will hold
	
	// Window to draw stuff to
	private MinuetoWindow window;			// The Minueto window
	
	// Font used to draw on the screen
	private MinuetoFont font;
	
	// Images of text
	private MinuetoImage circle = new MinuetoCircle(20, MinuetoColor.RED, true);
	
	// Should be keep the window open
	private boolean open;
	
	//Character
	MinuetoText characterInfo;
	
	//Font
	private MinuetoFont characterInfoFont = new MinuetoFont(MinuetoFont.Serif, 14, true, false);
	
	
	public GameWindow() {
		characterInfo = new MinuetoText(GodCharacter.getInstance().getCharacter().toString(), characterInfoFont, MinuetoColor.BLACK);
		this.window = new MinuetoFrame(800, 600, true);
	}
	
	private void initialize() {
		// Build the event quue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the window handler with the event queue.
		//this.window.registerWindowHandler(this, eventQueue);
		this.window.registerKeyboardHandler(this, eventQueue);
		this.window.registerMouseHandler(this, eventQueue);
		this.window.registerMouseWheelHandler(this, eventQueue);
		
		this.window.setVisible(true);
		this.window.setTitle("Minueto From Swing");
	}

	public void start() {		
		initialize();
		
		// Keep window open
		open = true;
		
		// Game/rendering loop
		while(open) {
		
			// Clear the window.
			this.window.clear(MinuetoColor.WHITE);
			this.window.draw(characterInfo, 100, 10);
//			this.window.draw(circle, 400, 10);
			
			
			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
			
			// Render all graphics in the back buffer.
			this.window.render();
		}	
		
		// Close our window
		this.window.close();
	}
	
	public void handleKeyPress(int value)
	{
		switch(value)
		{
			case MinuetoKeyboard.KEY_ENTER:
				this.window.clear(MinuetoColor.WHITE);
				this.window.draw(circle, 400, 20);
//				System.exit(0);
				break;
				
			default:
				//Ignore all
			
		}
	}
	
	public void handleKeyRelease(int value) {
	//Do nothing on key release
	}

	public void handleKeyType(char key) {
	//Do nothing on key type
	}
	
	public void handleMousePress(int x, int y, int button)
	{
	      System.out.println("Mouse click on button " + button + " at " + x + "," + y);
	}

	@Override
	public void handleMouseMove(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseRelease(int x, int y, int button) 
	{
		System.out.println("Mouse release on button " + button + " at " + x + "," + y);
	}

	@Override
	public void handleMouseWheelRotate(int i) {
		System.out.println(i);
		
	}
}
