package com.rr;

import com.rr.core.Downloader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Get download url
        String url = null;
        if (args == null || args.length == 0) {
            Scanner scan = new Scanner(System.in);
            while(url == null) {
                System.out.printf("Please input download url:");
                url = scan.next();
            }
        } else {
            url = args[0];
        }
        //System.out.println(url);
        Downloader downloader = new Downloader();
        downloader.download(url);
        System.out.println("Download successfully!");
    }
}
