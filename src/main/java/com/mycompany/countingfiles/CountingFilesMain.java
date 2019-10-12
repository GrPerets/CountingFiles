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
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

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
            System.out.println("Запустите приложение используя формат: countingfiles paths.txt results.txt");
            System.exit(0);
        }
                
                
        Path input=null;
        Path output=null;
        try {
            input = Paths.get(args[0]);
            output = Paths.get(args[1]);
        }
        catch (InvalidPathException ex){
            Logger.getLogger(CountingFilesMain.class.getName()).log(Level.SEVERE, null, ex);
        }               
          
        List<String> lines = null;
        
        //Удалить файл результатов, если существует. Создать новый.
        try {
            if (Files.deleteIfExists(output)){
                Files.createFile(output);
            }  
            lines = Files.readAllLines(input);
        }
        catch (IOException ex) {
            Logger.getLogger(CountingFilesMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        //Спам логи jnativehook отключение
        //Очистить предыдущие конфигурации ведения журнала.
        LogManager.getLogManager().reset();

        //Получить регистратор для "org.jnativehook" и отключить уровень.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        
        if((lines!=null)&&(!lines.isEmpty())) {
            System.out.println("Press ESC to exit");
            for(String path:lines){
                new Counter(new InfoDir(path),output);
            }
            try {
			GlobalScreen.registerNativeHook();
                       // GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}
            
        } else {
            System.out.println("Недоступен исходный файл или отсутствуют пути каталогов");
        }
    
    }
    
}
