package com.sachidanand.me;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/student_management_system")
	private DataSource dataSource;
	
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//create our student db util ... and pass in the connection pool/data source
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
			}
		catch(Exception e) {
			throw new ServletException(e);
		}
		
		
	}

	/**
     * Default constructor. 
     */
    public StudentControllerServlet() {
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			//read the command parameter
			String thecommand = request.getParameter("command");
			//if the command is missing then default to listing students
			if(thecommand==null) {
				thecommand ="LIST";
			}
			
			//route to the appropriate method
			switch(thecommand) {
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			
			}	
			
		}
		catch(Exception e){
			throw new ServletException(e);	
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from data
		String theStudentId = request.getParameter("studentId");
		//delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		//return to student-list
		listStudents(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
				// read student info from form data
				int id = Integer.parseInt(request.getParameter("studentId"));
				String firstName = request.getParameter("firstname");
				String lastName = request.getParameter("lastname");
				String email = request.getParameter("email");
				
				// create a new student object
				Student theStudent = new Student(id, firstName, lastName, email);
				
				// perform update on database
				studentDbUtil.updateStudent(theStudent);
				
				// send them back to the "list students" page
				listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student id from Form data
		String theStudentId = request.getParameter("studentId");
		//get student from database(db util)
		Student theStudent = studentDbUtil.getstudent(theStudentId);
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student info from data
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		//create a new student object
		Student theStudent = new Student(firstname, lastname, email);
		//add the student to the database		
		studentDbUtil.addStudent(theStudent);		
		//send back to main page(the student list)
		listStudents(request, response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//get students from db util
		List<Student> students = studentDbUtil.getStudents();
		//add students to the request
		request.setAttribute("STUDENT_LIST", students);
		//send to JSP page view
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

}
