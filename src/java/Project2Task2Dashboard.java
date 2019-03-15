import com.mongodb.client.FindIterable;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;

/**
 *
 * @author eason
 */
@WebServlet(name = "Project2Task2dashboard", urlPatterns = {"/dashboard"})
public class Project2Task2Dashboard extends HttpServlet {

    private static GetDB gDB;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        gDB = new GetDB();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // instantiate a docs iterator.
        FindIterable<Document> docs = gDB.mc.find();
        
        // my collection size.
        int mcSize = 0;
        // most search age by users.
        int mostSearchAge = -1;
        Map<Integer, Integer> ageMap = new HashMap<>(); // key: age, value: times.
        double totalResponseTime = 0;
        StringBuilder sb = new StringBuilder();
        // iterator the collections.
        for (Document d : docs) {
            sb.append(d.toString()).append("<br>").append("<br>");
            mcSize++;
            int age = Integer.parseInt(d.getString("age"));
            if (ageMap.containsKey(age)) {
                Integer tempAge = ageMap.get(age);
                ageMap.put(age, ++tempAge);
            } else {
                ageMap.put(age, 1);
            }
            totalResponseTime += d.getLong("responseTime");
        }

        // count average response time.
        double avgrspTime = totalResponseTime/mcSize;
        
        // Define frequent search age.
        int tmp = 0;
        for (Integer i : ageMap.keySet()) {
            if (ageMap.get(i) > tmp) {
                mostSearchAge = i;
                tmp = ageMap.get(i);
            }
        }
        
        // handle digits after decimal.
        DecimalFormat df = new DecimalFormat("0.000");
        
        // Set attribute to result.jsp.
        request.setAttribute("mcSize",mcSize);
        request.setAttribute("mostSearchAge",mostSearchAge);
        request.setAttribute("avgrspTime",df.format(avgrspTime));
        request.setAttribute("fullLog",sb.toString());
        
        String nextView = "result.jsp";
        // Transfer control over the the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
}
