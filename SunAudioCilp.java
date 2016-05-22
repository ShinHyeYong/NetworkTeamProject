package client;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class SunAudioCilp{
	private String filename;
	private Player player;

	public SunAudioCilp(String filename){
		this.filename = filename;
	}

	public void close(){
		if (player != null)
			player.close();
	}

	public void play(){
		try{
			InputStream fis = this.getClass().getResourceAsStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}catch(Exception e){
			System.out.println("Problem playing file" + filename);
			System.out.println(e);
		}
		new Thread(){
			public void run(){
				try{ player.play(); }
				catch(Exception e) { System.out.println(e); }
			}
		}.start();
	}
}



/*import javazoom.jl.player.Player;

public class Audio{
	private String filename;
	private Player player;

	public Audio(String filename){
		this.filename = filename;
	}

	public void close(){
		if (player != null)
			player.close();
	}

	public void play(){
		try{
			InputStream fis = this.getClass().getResourceAsStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}catch(Exception e){
			System.out.println("Problem playing file" + filename);
			System.out.println(e);
		}
		new Tread(){
			public void run(){
				try{ player.play(); }
				catch(Exception e) { System.out.println(e); }
			}
		}.start();
	}
}*/