package gr.rambou.myicarus;

import java.io.Serializable;
import java.util.Date;

public class Lesson implements Serializable{

    private final String ID;
    private final String Title;
    private final double Mark;
    private final String Semester;
    private final Date Statement;
    private final Date Exam;
    private final LessonStatus Status;

    public static enum LessonStatus {

        PASSED, FAILED, NOT_GIVEN
    }

    public Lesson(String id, String title, double mark, String semester, Date statement, Date exam, LessonStatus status) {
        this.ID = id;
        this.Title = title;
        this.Mark = mark;
        this.Semester = semester;
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
        return Semester;
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
        return "ID: [" + ID + "] Μάθημα: [" + Title + "] Εξάμηνο: [" + Semester
                + "ο] Βαθμός: [" + Mark + "] Δήλωση: [" + Statement
                + "] Εξέταση: [" + Exam + "] Κατάσταση: [" + Status + "]";
    }
}
