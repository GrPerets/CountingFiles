/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.countingfiles;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author Hryhorii Perets
 */
public class Counter implements Runnable, NativeKeyListener{
    private final InfoDir infoDir;
    private final Path output;
    private Thread thread;
    private static int id = 1;
    
    public Counter(InfoDir infoDir, Path output) {
        this.infoDir = infoDir;
        this.output = output;
        thread = new Thread(this);
        thread.start();
        GlobalScreen.addNativeKeyListener(this);
        
                
    }
    
    
    @Override
    public void run() {
        while (!thread.isInterrupted()){
            try {
                Files.walkFileTree(Paths.get(infoDir.getPath()), new SimpleFileVisitor<Path>(){ 
                    @Override
                    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) throws IOException {

                        if(thread.isInterrupted()){
                            return FileVisitResult.TERMINATE;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs){
                        if (attrs.isRegularFile()){
                           infoDir.addFile();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path path, IOException exc){
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                }
                );
            }
            catch (IOException | InvalidPathException ex) {
                Logger.getLogger(Counter.class.getName()).log(Level.SEVERE, null, ex);
            }

            finally {

                System.out.printf("|%3d|%s%n",id++,infoDir);

                try {
                    Files.write(output,String.format("%s%s%d%n",infoDir.getPath(),";",infoDir.getNumberOfFiles()).getBytes(),CREATE,APPEND);
                        
                }
                catch (IOException ex) {
                    Logger.getLogger(Counter.class.getName()).log(Level.SEVERE, null, ex);

                }
                thread.interrupt();
            }
        }
        
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                    thread.interrupt();
                    try {
                       GlobalScreen.unregisterNativeHook();
                        
                    } catch (NativeHookException ex) {
                        Logger.getLogger(Counter.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
		//System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
		//System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }
}
