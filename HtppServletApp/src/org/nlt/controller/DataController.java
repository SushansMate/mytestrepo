package org.nlt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DataController extends HttpServlet implements ParentIntreface {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service(req, resp);
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession ses = req.getSession();
		String clientIp = req.getRemoteAddr();

		resp.setContentType("text/html");

		PrintWriter out = resp.getWriter();
		try {
			String pageName = req.getParameter("page");

			if (pageName.equals("LoginPage")) {
				String email = req.getParameter("email");
				String password = req.getParameter("password");

				Statement smt = con.createStatement();
				ResultSet rs = smt.executeQuery(
						"select * from users where email='" + email + "' and password='" + password + "' and status=1");

				if (rs.next()) {
					ses.setAttribute("Ip", req.getRemoteAddr());
					ses.setAttribute("LoginName", rs.getObject("name"));
					RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
					rd.include(req, resp);
				} else {
					RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
					out.println("<h3>Invalid Login! Please Try Again.....</h3>");
					rd.include(req, resp);
				}
				rs.close();
				smt.close();

			} else if (pageName.equals("PersonPage")) {
				String fName = req.getParameter("fname");
				String Age = req.getParameter("age");
				String Phone = req.getParameter("phone");
				String Gender = req.getParameter("gender");

				Statement smt = con.createStatement();
				smt.execute("insert into persons(name,age,phone,gender,status) values ('" + fName + "'," + Age + ",'"
						+ Phone + "','" + Gender + "',1)");

				smt.close();

				RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
				out.println("<h1>Record Submitted Successfully </h1>");
				rd.include(req, resp);
			}

			else if (pageName.equals("PersonUpdatePage")) {
				String id = req.getParameter("id");
				String fName = req.getParameter("fname");
				String Age = req.getParameter("age");
				String Phone = req.getParameter("phone");
				String Gender = req.getParameter("gender");

				Statement smt = con.createStatement();
				smt.execute("update persons set name='" + fName + "',age=" + Age + ",phone='" + Phone + "',gender='"
						+ Gender + "' where id=" + id);

				smt.close();

				RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
				out.println("<h3>Record Updated Successfully </h3>");
				rd.include(req, resp);
			}

			else if (pageName.equals("PersonDeletePage")) {
				String id = req.getParameter("id");

				Statement smt = con.createStatement();
				smt.execute("update persons set status=0 where id=" + id);

				smt.close();

				RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
				out.println("<h1>Record Deleted Successfully </h1>");
				rd.include(req, resp);
			}

		} catch (Exception e) {
			out.println("<h2>" + e + "</h2>");

		}
	}
}