package com.nasa.simulador;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("ARTEMIS II MISSION SIMULATOR");
        System.out.println("=================================");

        try {

            OrekitConfig.init();

            TLIPrototype.run();

        } catch (Exception e) {

            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}