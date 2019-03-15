import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.JSONObject;

/**
 *
 * @author eason
 */
@WebServlet(name = "Project4Task2Servlet", urlPatterns = {"/Project4Task2Servlet/*"})
public class Project4Task2Servlet extends HttpServlet {

    private static GetDB gDB;
    
    @Override
    public void init() throws ServletException {
        super.init();
        gDB = new GetDB();
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        
        long start = System.currentTimeMillis();
        JSONObject result = null;
        
        // The name is on the path /name so skip over the '/'
        String searchAge = (request.getPathInfo()).substring(1);
        
        // return 401 if name not provided
        if(searchAge.equals("")) {
            response.setStatus(401);
            return;      
        }
               
        // make call to third party, process data and return a JSONObject.
        GetPlayers gp = new GetPlayers();
        result = gp.makeCallToAPI(searchAge);
        
        // return 404 if name not in map
        if(result == null) {
            // no variable name found in map
            response.setStatus(404);
            return;    
        }
        
        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("text/plain;charset=UTF-8");
        
        // return the value from a GET request
        PrintWriter out = response.getWriter();
        out.println(result.toString());   
        long end = System.currentTimeMillis();
        
        // Add search log into mongo DB.
        Document doc = Document.parse(result.toString());
        doc.append("responseTime", (end-start)/1000);
        doc.append("ts", new Date());
        gDB.mc.insertOne(doc);
    }
}
