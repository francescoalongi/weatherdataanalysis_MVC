package Controller;

import Model.Station;
import Utils.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicDataAcquirer {

    private ScheduledExecutorService scheduler;

    public PeriodicDataAcquirer(String path, Station station) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new DownloadDataFromFile(path, station), 0, 5, TimeUnit.MINUTES);
    }

    public void close() {
        scheduler.shutdownNow();
    }

}

class DownloadDataFromFile implements Runnable {

    private String path;
    private Station station;
    private String firstTwoLine;

    DownloadDataFromFile(String path, Station station) {
        this.path = path;
        this.station = station;
    }

    private String getFirstTwoLine(Scanner scanner) {
        return scanner.nextLine() + scanner.nextLine();
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            Scanner scanner = new Scanner(url.openStream());
            if (firstTwoLine.equals(getFirstTwoLine(scanner)) && !scanner.hasNext())
                return; //if file is not updated return
            while (scanner.hasNext()) {
                String line = scanner.nextLine(); //I think it would start from 3rd line
                // idea: scan all lines and save data in a map (or a stack? smth like --> insert(stack.pop(),stack.pop(),...)) and only at the end run the insert query
                // I think map is better!

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}