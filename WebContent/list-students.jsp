<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!Doctype html>
<html>
<head>
	<title>Student Management System</title>
	<link type = "text/css" rel = "stylesheet" href = "css/style.css">
</head>

<body>
	<div id = "wrapper">
		<div id = "header">
			<h2>My University</h2>
		</div>
	</div>
	
	<div id = "container">
		<div id = "content">
		<!-- put new button Add Student here -->
		<input type="button" value = "Add Student" 
				onclick ="window.location.href= 'add-student-form.jsp'; return false;"
				class ="add-student-button"/>
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
					<!-- set up a link for each student -->
					<c:url var="templink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
					<!-- set up delete link for student -->
					<c:url var="deletelink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"/>
						<c:param name="studentId" value="${tempStudent.id}"/>
					</c:url>
					<tr>
						<td> ${tempStudent.firstname} </td>
						<td> ${tempStudent.lastname} </td>
						<td> ${tempStudent.email} </td>
						<td> <a href="${templink}">Update</a>
							|
						 	<a href="${deletelink}" 
						 		onclick="if(!(confirm('Are you sure you want to delete this student?')))return false">
						 		Delete
						 	</a>
						</td>
						
					</tr>
				</c:forEach>				
			</table>
		</div>
	</div>

</body>

</html>
