import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Threads extends Thread {
    private String name = "defaultName";

    public Threads(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5 * 1000);
            System.out.println(name+" Adlı threadin işi bitti");
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }

    public static int threadIslem(int thread) throws IOException {
        FileReader f = new FileReader("threadWorks.txt");
        BufferedReader in = new BufferedReader(f);
        int satir = 0;
        String txt="";
        while (true){
            satir++;
            if(satir == thread)
                break;
            in.readLine();
        }
        txt = in.readLine();
        int toplam=0;
        for (int j = 0; j < txt.length(); j++) {
            toplam += (int)txt.charAt(j)-'0';
        }
        f.close();
        int sonuc = toplam/thread;
        if (sonuc>50)
            sonuc=toplam - 30*thread;
        if (sonuc<5)
            sonuc=toplam+thread;
        System.out.println(thread+". Thread Toplam İşi: "+sonuc);
        return sonuc;
    }
}