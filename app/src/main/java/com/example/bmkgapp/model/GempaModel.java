package com.example.bmkgapp.model;

public class GempaModel {

    private String tanggal;
    private String jam;
    private String magnitude;
    private String kedalaman;
    private String wilayah;
    private String coordinates;
    private String shakemap;

    public GempaModel(String tanggal, String jam, String magnitude,
                      String kedalaman, String wilayah,
                      String coordinates, String shakemap) {

        this.tanggal = tanggal;
        this.jam = jam;
        this.magnitude = magnitude;
        this.kedalaman = kedalaman;
        this.wilayah = wilayah;
        this.coordinates = coordinates;
        this.shakemap = shakemap;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getKedalaman() {
        return kedalaman;
    }

    public String getWilayah() {
        return wilayah;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getShakemap() {
        return shakemap;
    }
}
