package gr.rambou.myicarus;

import java.util.Date;

public class Lesson {

    private final String ID;
    private final String Title;
    private final double Mark;
    private final String Cemester;
    private final Date Statement;
    private final Date Exam;
    private final LessonStatus Status;

    public static enum LessonStatus {

        PASSED, FAILED, NOT_GIVEN
    };

    public Lesson(String id, String title, double mark, String cemester, Date statement, Date exam, LessonStatus status) {
        this.ID = id;
        this.Title = title;
        this.Mark = mark;
        this.Cemester = cemester;
        this.Statement = statement;
        this.Exam = exam;
        this.Status = status;
    }

    public String Get_ID() {
        return ID;
    }

    public String Get_Title() {
        return Title;
    }

    public String Get_Cemester() {
        return Cemester;
    }

    public double Get_Mark() {
        return Mark;
    }

    public Date Get_Statement() {
        return Statement;
    }

    public Date Get_Exam() {
        return Exam;
    }

    public LessonStatus Get_Status() {
        return Status;
    }

    @Override
    public String toString() {
        return "ID: [" + ID + "] Μάθημα: [" + Title + "] Εξάμηνο: [" + Cemester
                + "ο] Βαθμός: [" + Mark + "] Δήλωση: [" + Statement
                + "] Εξέταση: [" + Exam + "] Κατάσταση: [" + Status + "]";
    }
}
