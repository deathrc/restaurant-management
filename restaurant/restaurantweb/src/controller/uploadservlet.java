package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.SmartFile;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

/**
 * Servlet implementation class uploadservlet
 */

public class uploadservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public uploadservlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String imgName=request.getHeader("imgName"); //获取指定图片名称
		PrintWriter out = response.getWriter();
		String path = this.getServletContext().getRealPath("dish");
		File fpath = new File(path);
		if (!fpath.exists()) {
			fpath.mkdirs();
		}
		SmartUpload su = new SmartUpload("utf-8");
		su.initialize(getServletConfig(), request, response);

		try {
			su.setAllowedFilesList("jpg,png,gif");
			su.setMaxFileSize(1 * 1024 * 1024);
			su.upload();
			SmartFile file = su.getFiles().getFile(0);
			//String fname = new java.util.Date().getTime() + "." + file.getFileExt();
			file.saveAs(path + '/' + imgName);

			out.print(imgName);
		} catch (SmartUploadException e) {
			e.printStackTrace();
			out.print("文件上传失败");
		} catch (SecurityException e) {
			e.printStackTrace();
			out.print("只能上传jpg,png,gif文件，并且不能超过1M!");
		}
	
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request,response);
		
	}
}
