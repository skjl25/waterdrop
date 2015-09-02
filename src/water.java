import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import processing.core.*;
import java.util.*;
import java.lang.String;
import javax.sound.sampled.*; 
import krister.Ess.*;


public class water extends PApplet {
	
	AudioInputStream audioInputStream;
	SourceDataLine sourceDataLine;
	boolean stopCapture = false;
	boolean speakerSwitch=false;
	boolean black=false;
	boolean squeeze = false;
	int prevSize=0;
	int speakerA [] = new int [1000000];
	int speakerB [] = new int [1000000];
	int speakerA_size [] = new int [1000000];
	int speakerB_size [] = new int [1000000];
	int index=0; 
	AudioStream myStream;
	AudioInput myInput;

	boolean inputReady=false;
	float[] streamBuffer;

		  
	  
		public void setup() {
//			while(true)
			speakerA=new int[1000000];
			speakerB=new int[1000000];
			for(int i=0; i < 10000; i++)
			{
				speakerA_size[i] = 10;
				speakerB_size[i] = 10;
			}
		//	captureAudio();
			size(1080, 640);
			background(0);
			noStroke();
			frameRate(30);
			
			Ess.start(this);

			  // create a new AudioInput
			  myInput=new AudioInput(); 

			  // create a new AudioStream
			  myStream=new AudioStream(myInput.size);
			  streamBuffer=new float[myInput.size];

			  // start
			  myStream.start();
			  myInput.start();  		
		}
	  
		public void draw() {

			if (squeeze == true)
			{
	       	  	for (int i=0; i < index-1 ; i++)
	       	  	{		
							
	       	  		if((speakerA_size[i] >= 400) || (speakerB_size[i] >= 400))
	       	  		{
	       	  			//do nothing
		        	}
	       	  		else
	       	  		{
	       	  			if ((speakerA[i] == 1) && (speakerB[i] == 1))
	       	  			{	
	       	  				fill(0, 255, 255);
	       	  				ellipse(540, 320, speakerA_size[i], speakerA_size[i]/2);
	       	  				speakerA_size[i] = speakerA_size[i] + 1;
	       	  			}
	       	  			
	       	  			//if speaker A speaks
	       	  			else if(speakerA[i] == 1)
	       	  			{
	       	  				fill(0, 255, 0);
	       	  				ellipse(540, 320, speakerA_size[i], speakerA_size[i]/2);
	       	  				speakerA_size[i] = speakerA_size[i] + 1;
	       	  			}
	       	  			
	       	  			//if speaker B speaks
	       	  			else if(speakerB[i] == 1)
	       	  			{
	       	  				fill(0, 0, 255);
	       	  				ellipse(540, 320, speakerB_size[i], speakerB_size[i]/2);
	       	  				speakerB_size[i] = speakerB_size[i] + 1;
	       	  			}		
	       	  			//if no one speaks
	       	  			else
	       	  			{
	       	  				fill(0, 0, 0);
	       	  				ellipse(540, 320, speakerB_size[i], speakerB_size[i]/2);
	       	  				speakerB_size[i] = speakerB_size[i] + 1;
	       	  			}
	       	  		}
	       	  	}	
			}
			else
			{
	       	  	for (int i=0; i < index-1 ; i++)
	       	  	{		
							
	       	  		if((speakerA_size[i] >= 400) || (speakerB_size[i] >= 400))
	       	  		{
	       	  			//do nothing
		        	}
	       	  		else
	       	  		{
	       	  			if ((speakerA[i] == 1) && (speakerB[i] == 1))
	       	  			{	
	       	  				fill(0, 255, 255);
	       	  				ellipse(540, 320, speakerA_size[i], speakerA_size[i]);
	       	  				speakerA_size[i] = speakerA_size[i] + 1;
	       	  			}
	       	  			
	       	  			//if speaker A speaks
	       	  			else if(speakerA[i] == 1)
	       	  			{
	       	  				fill(0, 255, 0);
	       	  				ellipse(540, 320, speakerA_size[i], speakerA_size[i]);
	       	  				speakerA_size[i] = speakerA_size[i] + 1;
	       	  			}
	       	  			
	       	  			//if speaker B speaks
	       	  			else if(speakerB[i] == 1)
	       	  			{
	       	  				fill(0, 0, 255);
	       	  				ellipse(540, 320, speakerB_size[i], speakerB_size[i]);
	       	  				speakerB_size[i] = speakerB_size[i] + 1;
	       	  			}		
	       	  			//if no one speaks
	       	  			else
	       	  			{
	       	  				fill(0, 0, 0);
	       	  				ellipse(540, 320, speakerB_size[i], speakerB_size[i]);
	       	  				speakerB_size[i] = speakerB_size[i] + 1;
	       	  			}
	       	  		}
	       	  	}	
			}


		  // overlay an average of the samples in the input buffer
		  float inc=myInput.buffer.length/256.0f;
		  for (int i=0;i<256;i++) {
			  int y=(int)(100+myInput.buffer[(int)(i*inc)]*100);
			  if(y>190)
			  {
				  squeeze = false;
				  System.out.println("testing thread");
				  try {
					  Thread.sleep(500);
				  } catch (InterruptedException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  
				  if (black == true)
				  {		  
					  squeeze = false;
					  black = false;	
					  if(speakerSwitch==false)
					  {	
						  speakerSwitch=true;
					  }
					  else
					  {
						  speakerSwitch=false;						
					  }
				  }
				  else
				  {
					  black = true;
					  speakerA[index] = 0;
					  speakerB[index] = 0;
				  }
			  }
			  else if(y>130 && y<190)
			  {
				  squeeze = true;
			  }
			  else
				  squeeze = false;
			 }
		  	//if no one speaks
		  	if (black == true)
		  	{
			  	fill(0, 0, 0);
			  	ellipse(540, 320, speakerB_size[index], speakerB_size[index]);
				speakerB_size[index] = speakerB_size[index] + 1;	
		  	}
		  	//if someone speaks
		  	else if(speakerSwitch==false)
		  	{	
		  		index++;
		  		speakerA[index]=1;
		  		speakerB[index]=0;	
		  		if (squeeze == true)
		  		{
			  		fill(0, 255, 0);
			  		ellipse(540, 320, speakerA_size[index], speakerA_size[index]/2);
			  		speakerA_size[index] = speakerA_size[index] + 1;	
		  		}
		  		else
		  		{
			  		fill(0, 255, 0);
			  		ellipse(540, 320, speakerA_size[index], speakerA_size[index]);
			  		speakerA_size[index] = speakerA_size[index] + 1;	
		  		}
		  		
		  	}
		  	else if(speakerSwitch==true)
		  	{	
			  index++;
			  speakerA[index]=0;
			  speakerB[index]=1;	
		  		if (squeeze == true)
		  		{
			  		fill(0, 0, 255);
			  		ellipse(540, 320, speakerB_size[index], speakerB_size[index]/2);
			  		speakerB_size[index] = speakerB_size[index] + 1;	
		  		}
		  		else
		  		{
			  		fill(0, 0, 255);
			  		ellipse(540, 320, speakerB_size[index], speakerB_size[index]);
			  		speakerB_size[index] = speakerB_size[index] + 1;	

		  		}
		  	}
		  	index++;
			rect(25, 25, 50, 50);
		}
		
		void audioStreamWrite(AudioStream theStream) {
			  // block until we have some input
			  while (!inputReady); 
			  
			  System.arraycopy(streamBuffer,0,myStream.buffer,0,streamBuffer.length);
			   
			  inputReady=false;
			}

			void audioInputData(AudioInput theInput) {
			  System.arraycopy(myInput.buffer,0,streamBuffer,0,myInput.size);
			  
			  inputReady=true;
			}

			// we are done, clean up Ess

			public void stop() {
			  Ess.stop();
			  super.stop();
			}
}
