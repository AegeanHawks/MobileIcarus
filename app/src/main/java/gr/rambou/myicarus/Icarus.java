package gr.rambou.myicarus;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class Icarus implements Serializable {

    private final String Username, Password;
    private String StudentFullName, ID, StudentName, Surname;
    public Map<String, String> Cookies;
    //private Document Page;
    private ArrayList<Lesson> Succeed_Lessons, All_Lessons, Exams_Lessons;


    public enum SendType {

        OFFICE, COURIER, FAX
    }

    public enum PaperType {

        bebewsh_spoudwn, analutikh_ba8mologia, analutikh_ba8mologia_ptuxio_me_ba8mo, analutikh_ba8mologia_ptuxio_xwris_ba8mo,
        stratologia, diagrafh, antigrafo_ptuxiou, plhrw_proupo8eseis_apokthseis_ptuxiou, praktikh_askhsh, stegastiko_epidoma,
        allo
    }

    public Icarus(String username, String password) {
        this.Username = username;
        this.Password = password;
        this.StudentFullName = null;
        this.Cookies = null;
    }

    public boolean login() {

        try {
            //ενεργοποιούμε το SSL
            enableSSLSocket();

            //Εκτελέμε ερώτημα GET μέσω της JSoup για να συνδεθούμε
            Connection.Response res = Jsoup
                    .connect("https://icarus-icsd.aegean.gr/authentication.php")
                    .data("username", Username, "pwd", Password)
                    .method(Connection.Method.POST)
                    .execute();

            //Παίρνουμε τα cookies
            Cookies = res.cookies();

            //Αποθηκεύουμε το περιεχόμενο της σελίδας
            Document Page = res.parse();

            //Ελέγχουμε αν συνδεθήκαμε
            Elements name = Page.select("div#header_login").select("u");

            if (name.hasText()) {
                //Παίρνουμε το ονοματεπώνυμο του φοιτητή
                StudentFullName = name.html();

                //Παίρνουμε τον Αριθμό Μητρώου του φοιτητή
                Pattern r = Pattern.compile("[0-9-/-]{5,16}");

                String line = Page.select("div[id=\"stylized\"]").get(1).select("h2").text().trim();
                Matcher m = r.matcher(line);
                if (m.find()) {
                    ID = m.group(0);
                }

                //Παίρνουμε τους βαθμούς του φοιτητή
                LoadMarks(Page);

                return true;
            }

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
            Log.v("Icarus Class", ex.toString());
        }

        return false;
    }

    public boolean SendRequest(String fatherName, Integer cemester, String address, String phone, String send_address, SendType sendtype, String[] papers) {

        if (papers.length != 11) {
            return false;
        }

        String sendmethod;
        if (sendtype.equals(SendType.FAX)) {
            sendmethod = "με τηλεομοιοτυπία (fax) στο τηλέφωνο:";
        } else if (sendtype.equals(SendType.COURIER)) {
            sendmethod = "με courier, με χρέωση παραλήπτη, στη διεύθυνση:";
        } else {
            sendmethod = "από την Γραμματεία του Τμήματος, την επομένη της αίτησης";
        }

        //We create the Data to be Send
        MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
        mpEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addTextBody("aitisi_student_id", getID());
        mpEntity.addTextBody("aitisi_surname", getSurname());
        mpEntity.addTextBody("aitisi_name", getStudentName());
        mpEntity.addTextBody("aitisi_father", fatherName);
        mpEntity.addTextBody("aitisi_semester", cemester.toString());
        mpEntity.addTextBody("aitisi_address", address);
        mpEntity.addTextBody("aitisi_phone", phone);
        mpEntity.addTextBody("aitisi_send_method", sendmethod);
        mpEntity.addTextBody("aitisi_send_address", send_address);
        mpEntity.addTextBody("prints_no[]", papers[0]);
        mpEntity.addTextBody("prints_no[]", papers[1]);
        mpEntity.addTextBody("prints_no[]", papers[2]);
        mpEntity.addTextBody("prints_no[]", papers[3]);
        mpEntity.addTextBody("prints_no[]", papers[4]);
        mpEntity.addTextBody("prints_no[]", papers[5]);
        mpEntity.addTextBody("prints_no[]", papers[6]);
        mpEntity.addTextBody("prints_no[]", papers[7]);
        mpEntity.addTextBody("prints_no[]", papers[8]);
        mpEntity.addTextBody("prints_no[]", papers[9]);
        mpEntity.addTextBody("aitisi_allo", papers[10]);
        mpEntity.addTextBody("send", "");
        HttpEntity entity = mpEntity.build();

        //We send the request
        HttpPost post = new HttpPost("https://icarus-icsd.aegean.gr/student_aitisi.php");
        post.setEntity(entity);
        HttpClient client = HttpClients.custom().build();

        //Gets new/old cookies and set them in store and store to CTX
        CookieStore Store = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", Cookies.get("PHPSESSID"));
        cookie.setPath("//");
        cookie.setDomain("icarus-icsd.aegean.gr");
        Store.addCookie(cookie);

        HttpContext CTX = new BasicHttpContext();
        CTX.setAttribute(ClientContext.COOKIE_STORE, Store);
        HttpResponse response = null;
        try {
            response = client.execute(post, CTX);
        } catch (IOException ex) {
            Logger.getLogger(Icarus.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Check if user credentials are ok
        if (response == null) {
            return false;
        }

        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode != 200) {
            return false;
        }

        try {
            String html = EntityUtils.toString(response.getEntity(), "ISO-8859-7");
            Document res = Jsoup.parse(html);

            if (res.getElementsByClass("success").isEmpty()) {
                return false;
            }

        } catch (IOException | org.apache.http.ParseException ex) {
            return false;
        }

        return true;
    }

    public void LoadMarks(Document response) {

        All_Lessons = new ArrayList<>();
        Succeed_Lessons = new ArrayList<>();
        Exams_Lessons = new ArrayList<>();
        if(response==null) {
            try {
                //We send the request
                response = Jsoup
                        .connect("https://icarus-icsd.aegean.gr/student_main.php")
                        .cookies(Cookies)
                        .get();
            } catch (IOException ex) {
                Logger.getLogger(Icarus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Start Catching Lessons
        Elements sGrades = response.getElementById("succeeded_grades").select("tr");
        Elements aGrades = response.getElementById("analytic_grades").select("tr");
        Elements eGrades = response.getElementById("exetastiki_grades").select("tr");

        for (Element a : sGrades) {
            if (!a.select("td").isEmpty()) {
                Succeed_Lessons.add(getLesson(a));
            }
        }

        for (Element a : aGrades) {
            if (!a.select("td").isEmpty()) {
                All_Lessons.add(getLesson(a));
            }
        }

        for (Element a : eGrades) {
            if (!a.select("td").isEmpty()) {
                Exams_Lessons.add(getLesson(a));
            }
        }

    }

    private Lesson getLesson(Element a) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yy");

        String ID = a.select("td").get(1).text();
        String Title = a.select("td").get(2).text();
        Double Mark = 0.0;
        try {
            Mark = Double.valueOf(a.select("td").get(3).text());
        } catch (Exception ex) {
        }
        String Cemester = a.select("td").get(4).text();
        Date Statement = null, Exam = null;
        try {
            Statement = formatter.parse(a.select("td").get(5).text().trim());
            Exam = formatter.parse(a.select("td").get(6).text().trim());
        } catch (ParseException ex) {
        }
        Lesson.LessonStatus Status;

        switch (a.select("td").get(7).text().trim()) {
            case "Επιτυχία":
                Status = Lesson.LessonStatus.PASSED;
                break;
            case "Αποτυχία":
                Status = Lesson.LessonStatus.FAILED;
                break;
            case "Ακύρωση":
                Status = Lesson.LessonStatus.CANCELLED;
                break;
            default:
                Status = Lesson.LessonStatus.NOT_GIVEN;
                break;
        }
        return new Lesson(ID, Title, Mark, Cemester, Statement, Exam, Status);
    }

    public ArrayList<Lesson> getSucceed_Lessons() {
        return Succeed_Lessons;
    }

    public ArrayList<Lesson> getAll_Lessons() {
        return All_Lessons;
    }

    public ArrayList<Lesson> getExams_Lessons() {
        return Exams_Lessons;
    }

    public String getStudentFullName() {
        return StudentFullName;
    }

    public String getID() {
        return ID;
    }

    public String getStudentName() {
        return StudentFullName.split(" ")[0];
    }

    public String getSurname() {
        return StudentFullName.split(" ")[0];
    }

    private void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        //HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> true);

        SSLContext context;
        context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

}
