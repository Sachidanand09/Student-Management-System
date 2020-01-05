package com.sachidanand.me;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


public class StudentDbUtil {
	private DataSource datasource;
	public StudentDbUtil(DataSource thedatasource) {
		this.datasource = thedatasource;
	}
	
	public List<Student> getStudents() throws Exception{
			List<Student> students = new ArrayList<>();
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;

			try {
				//get connection
				myConn = datasource.getConnection();
				//create sql statement
				String sql = "Select * from student order by last_name";
				myStmt = myConn.createStatement();
				//execute query
				myRs = myStmt.executeQuery(sql);
				//process resultSet
				while(myRs.next()) {
					//retrieve data from resultset
					int id = myRs.getInt("id");
					String firstname = myRs.getString("first_name");
					String lastname = myRs.getString("last_name");
					String email = myRs.getString("email");
					//create new student object
					
					Student tempStudent = new Student(id, firstname, lastname, email);
					//add it to the list of students
					
				students.add(tempStudent);
					
				}
				
			}
			finally {
				//close JDBC connection
				close(myConn, myStmt, myRs);
				
			}
			
			return students;
			
			
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			
			if(myRs != null) {
				myRs.close();
			}
			if(myStmt != null) {
				myStmt.close();
			}
			if(myConn != null) {
				myConn.close();
			}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			//get database connection
			myConn = datasource.getConnection();
			//create sql query for insert
			String sql = "insert into student"+"(first_name,last_name,email)"+"values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);
			//set the param values for the student
			myStmt.setString(1, theStudent.getFirstname());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail());
			//execute sql insert
			myStmt.execute();
		}
		finally {
			//clean up JDBC objects
			close(myConn,myStmt,null);
		}
	}

	public Student getstudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		
		try {
			//convert Student id to int
			studentId = Integer.parseInt(theStudentId);
			//get connection to the database
			myConn = datasource.getConnection();
			//create sql to get selected the student 
			String sql = "select * from student where id=?";
			//create prepared statment
			myStmt = myConn.prepareStatement(sql);
			//set params
			myStmt.setInt(1, studentId);
			//execute statement
			myRs = myStmt.executeQuery();
			//retrieve data from resultset row
			if(myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("could not find student id :"+ studentId);
			}
			return theStudent;
		}
		finally {
			close(myConn,myStmt,myRs);
		}
	}	
public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = datasource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstname());
			myStmt.setString(2, theStudent.getLastname());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

public void deleteStudent(String theStudentId) throws Exception {
	Connection myConn = null;
	PreparedStatement myStmt = null;
	try {
		//convert student id to Integer
		int studentId = Integer.parseInt(theStudentId);
		//make connection to db
		myConn = datasource.getConnection();
		//create sql query
		String sql = "delete from student where id=?";
		//create prepared statment
		myStmt = myConn.prepareStatement(sql);
		//set params
		myStmt.setInt(1, studentId);
		//execute statement
		myStmt.execute();
	}
	finally {
		//cleanup jdbc code
		close(myConn, myStmt, null);
	}
}
	
}
