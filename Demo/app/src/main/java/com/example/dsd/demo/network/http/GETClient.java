package com.example.dsd.demo.network.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * todo
 * Created by im_dsd on 2019/4/1
 */
public class GETClient {
    public static void main(String[] args) throws Exception {
        InetAddress addr = InetAddress.getByName("api.funshareapp.com");
        Socket socket = new Socket(addr, 80);
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send an HTTP request to the web server
        String path = "/v3/feeds/hi/trending/next/?read_tag=5c9cc44037c6a471937c48b8&auto_refresh=0&os=android&lng=116.3300499&timezone=GMT%2B08%3A00&channel=GooglePlay&ui_lang=hi&ntype=WIFI&pkg=com.fun.share&version=1.2.9&mac=ZYRYzR9NSH%2BRAEF4ig0fgdkM8x%2FsPACDdB63Wex3LmY%3D&vcode=53&operator=unknown&os_v=28&android_id=xonFwkH0ErflVKPgDVt4ww%3D%3D&lang=hi&brand=google&device=Pixel+XL&aid=1c14ca69-7513-43c5-a1c3-fa317f1ad440&did=1c14ca69-7513-43c5-a1c3-fa317f1ad440&lat=39.9758108";
        out.println("GET " + path + " HTTP/1.1");
        out.println("Host: api.funshareapp.com");
        out.println("Authorization: HIN eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X3R5cGUiOiJhbmRyb2lkIiwiZXhwIjowLCJ1c2VyX2lkIjoiMTgzNTQwOTUiLCJ1c2VybmFtZSI6IiJ9.L4katENPV8ZtyRZ-T6P2O2ZmMMYG_P_VcFTLi5cSwi0");
        out.println("Accept-Encoding: gzip");
        out.println("Connection: Close");
        // has a empty line
        out.println();
        // read the response
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        socket.close();
    }
}