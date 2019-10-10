/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.countingfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hryhorii Perets
 */
public class CountingFilesMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("Запустите приложение используя формат: CountingFiles paths.txt results.txt");
            System.exit(0);
        }
                
        
        
        Path input=null;
        Path output=null;
        try {
            input = Paths.get("C:\\Users\\grperets\\Documents\\NetBeansProjects",args[0]);
            output = Paths.get("C:\\Users\\grperets\\Documents\\NetBeansProjects",args[1]);
        }
        catch (InvalidPathException ex){
            Logger.getLogger(CountingFilesMain.class.getName()).log(Level.SEVERE, null, ex);
        }               
          
        List<String> lines = null;
        try {
            if (Files.deleteIfExists(output)){
                Files.createFile(output);
            }  
            lines = Files.readAllLines(input);
        }
        catch (IOException ex) {
            Logger.getLogger(CountingFilesMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Counter counter=null;
        Thread thread;
        if(lines!=null){
            for(String path:lines){
                
                    counter = new Counter(new InfoDir(path),output);
                    //GlobalScreen.addNativeKeyListener(counter);
                    thread = new Thread(counter,"path");
                    thread.start();
                    
                    
                    //    counter.run();
                
            }
        } else {
            System.out.println("В исходном файле отсутствуют пути каталогов");
            System.exit(0);
        }
        
        /*
        try {
			//GlobalScreen.registerNativeHook();
                        GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		*/
    }
    
}
