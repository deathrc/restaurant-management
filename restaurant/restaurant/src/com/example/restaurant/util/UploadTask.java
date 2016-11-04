package com.example.restaurant.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class UploadTask extends Thread {
	private String upImageName;
	private String upUrl;
	private Handler handler;
	private int count;
	private String aimName;

	/**
	 * 初始化图片上传任务
	 * 
	 * @param handler
	 * @param upImageName
	 *            手机中待上传图片名称
	 * @param count
	 * @param aimName
	 *            保存到服务器中的图片名称
	 */
	public UploadTask(Handler handler, String upImageName, int count, String aimName) {
		this.handler = handler;
		this.upImageName = upImageName;
		this.upUrl = "http://" + IP.IP + ":8080/restaurantweb/uploadservlet";
		this.count = count;
		this.aimName = aimName;
	}

	public void run() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost request = new HttpPost(upUrl);
		File upImage = new File(upImageName);
		Message msg = this.handler.obtainMessage();

		try {
			MultipartEntity entity = new MultipartEntity();
			FileBody fileBody = new FileBody(upImage);
			entity.addPart("upimg", fileBody);

			request.setEntity(entity);
			request.setHeader("imgName", aimName);// 设置目标图片名称

			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				msg.obj = result.toString();
				msg.what = count;
			} else {
				msg.obj = "上传失败";
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.obj = "1";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.obj = "2";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.obj = "3";
			msg.obj = e.getCause().toString();
		}
		this.handler.sendMessage(msg);
	}
}
