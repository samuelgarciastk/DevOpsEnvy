package servlet;

import model.ProjectListBean;
import service.ProjectService;
import service.ProjectServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class AllProjectServlet
 */
public class AllProjectsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProjectService projectService;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllProjectsServlet() {
        super();
        projectService = new ProjectServiceImpl();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            if (session.getAttribute("username") == null) {
                session.invalidate();
                session = null;
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                String userName = String.valueOf(session.getAttribute("username"));
                ProjectListBean projectBean = projectService.getProjectList(userName);
                request.setAttribute("projectList", projectBean);
                RequestDispatcher rd = request.getRequestDispatcher("/allProjects");
                rd.forward(request, response);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
