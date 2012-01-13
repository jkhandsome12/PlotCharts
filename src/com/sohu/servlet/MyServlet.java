package com.sohu.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sohu.fusioncharts.DataGenerator;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public MyServlet()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String netType = request.getParameter("netType");
            String captionType = request.getParameter("captionType");
            String dirPath = request.getParameter("dirPath");
            String xmlStr = DataGenerator.getXmlStr(netType, Integer.valueOf(captionType), dirPath);
            StringReader sr = new StringReader(xmlStr);
            Writer w = new OutputStreamWriter(response.getOutputStream(), "utf-8");
            int i = 0;
            while((i = sr.read()) != -1)
            {
                w.write(i);
            }
            w.flush();
            sr.close();
            w.close();
        }
        catch(Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String dirPath = request.getParameter("dirPath");
            Set<String> set = DataGenerator.getNetList(dirPath);
            request.setAttribute("netList", set);
            request.setAttribute("dirPath", dirPath);
            request.getRequestDispatcher("test.jsp").forward(request, response);
        }
        catch(Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
