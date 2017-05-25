package com.gzr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/common")
public class CommonController {
	
	/*
	 *  图片命名格式
	 */
	private static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMddHHmmss";

	/*
	 * 上传的绝对路径
	 */
	private static String PROJECT_PATH="";
	/*
	 * 上传图片文件夹
	 */
	private static String UPLOAD_PATH="upload/img/";
	
	@RequestMapping("/test1")
	@ResponseBody
	public String testCkEditor1(HttpServletRequest request) {
		System.out.println(request.getParameter("content"));
		return "index";
	}

	@RequestMapping("/upload/uploadImg")
	public void uploadImg(@RequestParam("upload")MultipartFile file,//
			HttpServletRequest request, //
			HttpServletResponse response,//
			@RequestParam("CKEditorFuncNum")String CKEditorFuncNum)//
			throws IllegalStateException, IOException{
		//定义上传路径，暂时本项目下
		PROJECT_PATH=request.getSession().getServletContext().getRealPath("/");
		File filePath=new File(PROJECT_PATH+UPLOAD_PATH);
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		System.out.println(PROJECT_PATH);
		
		PrintWriter out =response.getWriter();
		
		String fileName=file.getOriginalFilename();
		System.out.println("name:"+fileName);
		String uploadContentType =file.getContentType();
		String expandedName ="";
		if (uploadContentType.equals("image/pjpeg")  
                || uploadContentType.equals("image/jpeg")) {  
            // IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg  
            expandedName = ".jpg";  
        } else if (uploadContentType.equals("image/png")  
                || uploadContentType.equals("image/x-png")) {  
            // IE6上传的png图片的headimageContentType是"image/x-png"  
            expandedName = ".png";  
        } else if (uploadContentType.equals("image/gif")) {  
            expandedName = ".gif";  
        } else if (uploadContentType.equals("image/bmp")) {  
            expandedName = ".bmp";  
        } else {  
        	
            out.println("<script type=\"text/javascript\">");  
            out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum  
                    + ",''," + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");  
            out.println("</script>");  
            return ;  
        }  
		if (file.getSize()> 600 * 1024) {  
            out.println("<script type=\"text/javascript\">");  
            out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum  
                    + ",''," + "'文件大小不得大于600k');");  
            out.println("</script>");  
            return ;  
		}
		DateFormat df = new SimpleDateFormat(DEFAULT_SUB_FOLDER_FORMAT_AUTO);
		//当前时间加上文件原名
		fileName = df.format(new Date())+expandedName;
		//保存图片到制定路径,SpringMVC给的图片保存方法
		file.transferTo(new File(PROJECT_PATH+UPLOAD_PATH +fileName));
        // 返回"图像"选项卡并显示图片  request.getContextPath()为web项目名   
        out.println("<script type=\"text/javascript\">");  
        out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum  
                + ",'" + "common/upload/img/" + fileName + "','')");  
        out.println("</script>");  
        return ;  
	}
	
	//@RequestMapping("upload/img/{fileName}")
	public String download(@PathVariable String fileName,HttpServletRequest request,  
            HttpServletResponse response){
		String requestUrl=request.getRequestURI();
		String suffix=requestUrl.substring(requestUrl.lastIndexOf("."),requestUrl.length());
		System.out.println(suffix+"xxxxx");
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="
				+ fileName);
		try {
			String path = request.getSession().getServletContext().getRealPath("/")
					+ UPLOAD_PATH;//这个download目录为啥建立在classes下的
			InputStream inputStream = new FileInputStream(new File(path+ fileName+suffix));

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			 // 这里主要关闭。
			os.close();

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
            //  返回值要注意，要不然就出现下面这句错误！
            //java+getOutputStream() has already been called for this response
		return null;
	}
	
	public void test(){
		
	}
	
}
