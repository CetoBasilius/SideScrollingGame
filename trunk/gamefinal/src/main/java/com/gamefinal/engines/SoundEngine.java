package com.gamefinal.engines;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.gamefinal.global.Global;

public class SoundEngine {


   // Each sound effect has its own clip, loaded with its own sound file.
   private Clip clip;
   
   public SoundEngine(){
	   Init();
   }
   
   private void SoundEffect(URL url) {
      try {
         // Set up an audio input stream piped from the sound file.
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         // Get a clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioInputStream);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void play(int soundID) {
	   SoundEffect(Global.getGlobals().soundFileNames[soundID]);
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
   }

   private void Init(){
	   for(int soundIndex=0;soundIndex<Global.getGlobals().soundFileNames.length;soundIndex++){
		   if(Global.getGlobals().soundFileNames[soundIndex]!=null){
			   SoundEffect(Global.getGlobals().soundFileNames[soundIndex]);
			   clip.setFramePosition(0);
			   clip.start();
		   }
	   }
   }
}
