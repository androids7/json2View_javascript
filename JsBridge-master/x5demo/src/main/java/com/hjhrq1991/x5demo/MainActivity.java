package com.hjhrq1991.x5demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hjhrq1991.library.tbs.BridgeHandler;
import com.hjhrq1991.library.tbs.CallBackFunction;
import com.hjhrq1991.library.tbs.DefaultHandler;
import com.hjhrq1991.library.tbs.SimpleBridgeWebViewClientListener;
import com.hjhrq1991.library.tbs.TbsBridgeWebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import java.io.*;
import android.content.*;
import org.json.*;
import com.avocarrot.json2view.*;
import android.support.v4.app.*;
import android.content.pm.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;

/**
 * @author hjhrq1991 created at 4/28/16 14:33.
 * @Description: jsBridge for tbs_x5 core.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    private String url;

    TbsBridgeWebView webView;
	
	View sampleView;
	HashMap<String,String> ondown;
	
	DynamicView dyview;
/*
    Button backBtn;
    Button btn1;
    Button btn2;
    Button btn3;
*/

    ByteArrayOutputStream bao;
	PrintStream ps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //=======================使用时请替换成自己url==========================
//        url = "file:///android_asset/demo2.html";
      //  url = "file:///android_asset/testJavascriptBridge.html";
	  url="file:///sdcard/0/index.html";
		verifyStoragePermissions(this);
		bao=new ByteArrayOutputStream();
		ps=new PrintStream(bao);
		
		System.setErr(ps);
		System.setOut(ps);
		
		ondown=new HashMap<String,String>();
        initView();
		
		try
		{
			FileWriter fw=new FileWriter("/sdcard/0/log.txt");
			fw.write(new String(bao.toByteArray()));
			fw.close();
		}
		catch (IOException e)
		{

		}
		
		
    }

	
	
	
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
		"android.permission.READ_EXTERNAL_STORAGE",
		"android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {
			//检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
																"android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
				// 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    private void initView() {
		
		
		
		
		
        webView =new TbsBridgeWebView(this);
		//(TbsBridgeWebView) findViewById(R.id.webView);

		
		
		JSONObject jsonObject;

        try {
			//sho(readFile("sdcard/0/sample.json"));
			
            jsonObject = new JSONObject(readFile("sdcard/0/sample.json"));

        } catch (JSONException je) {
            je.printStackTrace();
			sho(je.toString());
            jsonObject = null;
        }

        if (jsonObject != null) {

			
			dyview=new DynamicView();
            /* create dynamic view and return the view with the holder class attached as tag */
             sampleView =dyview.createView(this, jsonObject, WeViewHolder.class);
            /* get the view with id "testClick" and attach the onClickListener */
      //      ((WeViewHolder) sampleView.getTag()).clickableView.setOnClickListener(this);

            /* add Layout Parameters in just created view and set as the contentView of the activity */
            sampleView.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
          
			setContentView(R.layout.test);
			LinearLayout l=(LinearLayout)findViewById(R.id.l);
			//l.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
			
			l.addView(webView);
			l.addView(sampleView);
			

        } else {
            sho("Could not load valid json file");
        }
		
		
		
		
		
		
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setSavePassword(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        //设定支持h5viewport
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕.
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWwebViewebView.getSettings().setUserAgentString(Constant.useragent);

		/*
        backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
*/
        //=======================js桥使用改方法替换原有setWebViewClient()方法==========================
        webView.setBridgeWebViewClientListener(new SimpleBridgeWebViewClientListener() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "超链接：" + url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {
            
				/*
				if (btn1 != null) {
                    btn1.setVisibility(View.GONE);
                }

                if (btn2 != null) {
                    btn2.setVisibility(View.GONE);
                }
				*/
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @Override
            public boolean onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                String message;
                switch (sslError.getPrimaryError()) {
                    case android.net.http.SslError.SSL_UNTRUSTED:
                        message = "证书颁发机构不受信任";
                        break;
                    case android.net.http.SslError.SSL_EXPIRED:
                        message = "证书过期";
                        break;
                    case android.net.http.SslError.SSL_IDMISMATCH:
                        message = "网站名称与证书不一致";
                        break;
                    case android.net.http.SslError.SSL_NOTYETVALID:
                        message = "证书无效";
                        break;
                    case android.net.http.SslError.SSL_DATE_INVALID:
                        message = "证书日期无效";
                        break;
                    case android.net.http.SslError.SSL_INVALID:
                    default:
                        message = "证书错误";
                        break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示").setMessage(message + "，是否继续").setCancelable(true)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                sslErrorHandler.proceed();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                sslErrorHandler.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        //=======================此方法必须调用==========================
        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.loadUrl(url);

        //description：如需使用自定义桥名，调用以下方法即可，
        // 传空或不调用setCustom方法即使用默认桥名。
        // 默认桥名：WebViewJavascriptBridge
        //=======================使用自定义桥名时调用以下代码即可==========================
//        webView.setCustom("桥名");
        webView.setCustom("TestJavascriptBridge");

        //=======================以下4个web调用native示例方法==========================
        webView.registerHandler("bindWidget", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "回传结果：" + data);
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
				
				try
				{
					JSONObject obj=new JSONObject(data);
					
					if(obj.get("type").equals("method")){
					//String id =	obj.get("id").toString();
						String fun=obj.get("method").toString();
						if(fun.equals("setOnClick")){
							
							String idvalue=obj.get("id").toString();
							
						final	String callMethod=obj.get("call").toString();
							ondown.put(idvalue,callMethod);
							
							
					final TextView tv=	(TextView)((WeViewHolder)sampleView.getTag()).view.get(idvalue);
							
						
						
						
							tv.setOnClickListener(new OnClickListener(){
								public void onClick(View v){
									tv.setText("hello");
									
									//=======================这里是native调用web==========================
									webView.callHandler(callMethod, "haha", new CallBackFunction() {
											@Override
											public void onCallBack(String data) {
												Log.i(TAG, "回传结果：" + data);
												Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
											}
										});
									
								}
							});
							//MainActivity.this);
							
							
						}
					}
				}
				catch (JSONException e)
				{}
			//	((TextView )((sampleView.findViewWithTag("testClick"))))   .setText("hello");
				
            }
        });

        webView.registerHandler("initSignNetShare", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "回传结果：" + data);
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

        webView.registerHandler("jsHandler1", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "回传结果：" + data);
				
				/*
                if (btn1 != null)
                    btn1.setVisibility(View.VISIBLE);
					*/
            }
        });

        webView.registerHandler("jsHandler2", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "回传结果：" + data);
				
				/*
                if (btn2 != null)
                    btn2.setVisibility(View.VISIBLE);
					*/
            }
        });


        //=======================招行一网通js桥回调==========================
        webView.registerHandler("initCmbSignNetPay", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                //在这里解析回调数据并执行处理
                Log.i(TAG, "回传结果：" + data);
            }
        });
    }

    @Override
    public void onClick(View v) {
		
	
		sho("你好");
		
        switch (v.getId()) {
            case R.id.back:
                if (webView.canGoBack())
                    webView.goBack();
                else
                    finish();
                break;
            case R.id.btn1:
                //=======================这里是native调用web==========================
                webView.callHandler("click1", "pic", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "回传结果：" + data);
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn2:
                //=======================这里是native调用web==========================
                webView.callHandler("click2", "success", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "回传结果：" + data);
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn3:
                //=======================这里是native调用web==========================
                webView.callHandler("click3", "success", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "回传结果：" + data);
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
				
				
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }
	
	
	
	public void sho(Object o){
		Toast.makeText(this,o.toString(),1).show();
	}
	private String readFile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null) isr.close();
                if (fIn != null) fIn.close();
                if (input != null) input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
	
	
	
	private String readFile(String fileName) {
      //  StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
      
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            fIn = new FileInputStream(new File(fileName));
            byte[] b=new byte[1024];
            int p=0;
            while ((p=fIn.read(b))!=-1) {
                out.write(b,0,p);
            }
        } catch (Exception e) {
			sho(e.toString());
            e.getMessage();
        } finally {
            try {
                
                if (fIn != null) fIn.close();
             
            } catch (Exception e2) {
                e2.getMessage();
				sho(e2.toString());
            }
        }
        return new String( out.toByteArray());
		//returnString.toString();
    }

	@Override
	protected void onDestroy()
	 {
	 // TODO: Implement this method
	 
		 try
		 {
			 FileWriter fw=new FileWriter("/sdcard/0/log.txt");
			 fw.write(new String(bao.toByteArray()));
			 fw.close();
		 }
		 catch (IOException e)
		 {

		 }
		 
	 
	 super.onDestroy();
	 }
	
	
	/*
	
	@Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.avocarrot.com/")));
    }
	*/
	
	/*
	static public class WeViewHolder {
       @DynamicViewId(id = "testClick")
        public View clickableView;

        public WeViewHolder() {
        }
    }
	
	*/
	
	
	
}
