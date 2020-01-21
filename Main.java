package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedImage img = null;
		File f = null;
		
	    try{
	        f = new File("/home/pi/Java/image-to-music/.jpg");
	        img = ImageIO.read(f);
	    }catch(IOException e){
	        System.out.println(e);
	    }

	    int width = img.getWidth();
	    int height = img.getHeight();
	      
	    ArrayList<Integer> arr = new ArrayList<Integer>();

	    for(int y = 0; y < height; y++){
	        for(int x = 0; x < width; x++){
	            int p = img.getRGB(x,y);
	            int a = (p>>24)&0xff;
		        int r = (p>>16)&0xff;
		        int g = (p>>8)&0xff;
		        int b = p&0xff;
		        System.out.printf("R: %d / G: %d / B: %d | Sum: %d\n", r, g, b, r + g + b);
	            arr.add(Map((float)(r + g + b)));
	        }
	    }
	    
     	MidiChannel[] mChannels = null;
		try{
			Synthesizer midiSynth = MidiSystem.getSynthesizer(); 
			midiSynth.open();
     		//Instrument[] instr = midiSynth.getAvailableInstruments();
     		mChannels = midiSynth.getChannels();
			mChannels[0].programChange(1);
		} catch (MidiUnavailableException e) {System.out.println("Midi Unavailable");}
		
		// Play music
		for (int i = 0; i < arr.size(); i++) {
		     	mChannels[0].noteOn(arr.get(i), 100);
		     	
		     	try { Thread.sleep(100);
		     	} catch( InterruptedException e ) {System.out.println("Exception");}
		     	
		     	mChannels[0].noteOff(arr.get(i));
		}
	}
	
	static int Map(float x) {
		// y = (x - a) / (b - a) * (d - c) + c
		float ans = (x / 765) * (68 - 52) + 52;
		return Math.round(ans);
	}

}
