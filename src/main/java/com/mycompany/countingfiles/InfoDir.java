/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.countingfiles;

/**
 *
 * @author Hryhorii Perets
 */
public class InfoDir {
    private final String path;
    private int numberOfFiles;
    
        
    public InfoDir(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }
    
    public void addFile(){
        numberOfFiles++;
    }
    
    @Override
    public String toString(){
        return String.format("%7d|%s",numberOfFiles,path);
    }
}
