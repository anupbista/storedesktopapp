package Modals;

import java.time.LocalDate;
import java.time.LocalTime;

public class QRCode {
    private String name;
    private String subject;
    private LocalTime time;
    private LocalDate date;

    public QRCode(String name, String subject, LocalTime time, LocalDate date) {
        this.name = name;
        this.subject = subject;
        this.time = time;
        this.date = date;
    }
}
