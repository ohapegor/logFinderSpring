package ru.ohapegor.logFinder.userInterface.entities;


public class Style {



    public static Style of(String color){
        return new Style(color);
    }

    public Style(String color) {
        this.color = color;
        setColorScheme();
    }

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    int R;
    int G;
    int B;

    public void setR(int r) {
        R = r;
    }
    public void setG(int g) {
        G = g;
    }
    public void setB(int b) {
        B = b;
    }

    public int getR() {
        return R;
    }
    public int getG() {
        return G;
    }
    public int getB() {
        return B;
    }


    public void setColorScheme() {
        if (color == null || color.length() != 6) {
            color = "000000";
        }

        try {
            R = Integer.parseInt(color.substring(0, 2), 16);
            G = Integer.parseInt(color.substring(2, 4), 16);
            B = Integer.parseInt(color.substring(4, 6), 16);
        }catch (Exception e){
            color = "000000";
            setColorScheme();
        }
    }

    public void setColorRGB(){
        if (R < 0) R = 0;
        if (R > 255) R = 255;
        if (G < 0) G = 0;
        if (G > 255) G = 255;
        if (B < 0) B = 0;
        if (B > 255) B = 255;

        color = String.format("%02x",R)+ String.format("%02x",G)+ String.format("%02x",B);
    }

    @Override
    public String toString() {
        return "Style{" +
                "color='" + color + '\'' +
                ", R=" + R +
                ", G=" + G +
                ", B=" + B;
    }




}
