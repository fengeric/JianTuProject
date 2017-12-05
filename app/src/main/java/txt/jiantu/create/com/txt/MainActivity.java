package txt.jiantu.create.com.txt;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import txt.jiantu.create.com.txt.per.PermissionHelper;
import txt.jiantu.create.com.txt.util.LogUtil;
import txt.jiantu.create.com.txt.util.OnPermissionCallback;
import txt.jiantu.create.com.txt.util.ToastManager;

public class MainActivity extends AppCompatActivity {
    private EditText et_book_title;//书名
    private EditText et_book_chapter_num;//多少章/回
    private EditText et_book_chapter_unit;//章/回单位
    private EditText et_book_content_line_num;//1回中多少行概要
    private EditText et_book_good_sentence_line_num;//1回中多少行概好句
    public final String sdPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/AAmyJianTu";
    private PermissionHelper permissionHelper;
    private String book_title = "", book_unit = "";// 书名；章/回
    private int book_chapter_num = 0;// 书的章节数量
    private int content_line_num = 0;// 1回中多少行概要
    private int good_sentence_line_num = 0;//1回中多少行概好句

    private int[] arr;//创建一个输入的章节数目长度的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        try {
            et_book_title = (EditText) findViewById(R.id.et_book_title);
            et_book_chapter_num = (EditText) findViewById(R.id.et_book_chapter_num);
            et_book_chapter_unit = (EditText) findViewById(R.id.et_book_chapter_unit);
            et_book_content_line_num = (EditText) findViewById(R.id.et_book_content_line_num);
            et_book_good_sentence_line_num = (EditText) findViewById(R.id.et_book_good_sentence_line_num);
        } catch (Exception e) {
          LogUtil.e(getClass(), "initView", e);
        }
    }

    public void btnToMyJianTu(View v){
        try {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent,1);
            File file = new File(sdPath);
            if(null==file || !file.exists()){
                return;
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "file/*");
            try {
                startActivity(intent);
                startActivity(Intent.createChooser(intent,"选择浏览工具"));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
          LogUtil.e(getClass(), "btnToMyJianTu", e);
        }
    }

    public void btnCreate(View v){
        try {
            if (!judgeTextRightful())
                return;
            book_chapter_num = Integer.parseInt(et_book_chapter_num.getText().toString());
            book_title = et_book_title.getText().toString();
            book_unit = et_book_chapter_unit.getText().toString();
            content_line_num = Integer.parseInt(et_book_content_line_num.getText().toString());
            good_sentence_line_num = Integer.parseInt(et_book_good_sentence_line_num.getText().toString());
            arr = new int[book_chapter_num];//创建一个输入的章节数目长度的数组
            // 下载文件
            permissionHelper = PermissionHelper.getInstance(this, new OnPermissionCallback() {
                @Override
                public void onPermissionGranted(@NonNull String permissionName) {
                    //权限允许
                    if (permissionName.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //afterWork(permissionName);


                        //创建简图整个文件夹
                        createFileFolder(sdPath);


                        //创建书的整个文件夹
                        String book_path = sdPath + "/" + book_title;
                        createFileFolder(book_path);

                        //删除书文件夹下面的所有文件，防止原有旧文件覆盖不了
                        delAllFile(book_path);

                        //创建txt人物关系图
                        String people_relation_txt_content = "!" + book_title + "\r\n" + "!!.";
                        createTextFile(book_path, book_title + "人物关系", people_relation_txt_content);

                        //创建内容概要txt文件
                        for (int i = 0; i < arr.length; i++) {
                            arr[i] = i;
                        }

                        Object[] subAry = splitAry(arr, 10);//分割后的子块数组
                        for (int i = 0; i < subAry.length; i++) {
                            int[] aryItem = (int[]) subAry[i];
                            String content = "";
                            for (int j = 0; j < aryItem.length; j++) {
                                /*if (TextUtils.isEmpty(content)) {
                                    content = "!!第" + (aryItem[j] + 1) + book_unit + " " + "\r\n" +
                                            getString(content_line_num, good_sentence_line_num);
                                } else {
                                    content = content + "\r\n" +
                                            "!!第" + (aryItem[j] + 1) + book_unit + " " + "\r\n" +
                                            getString(content_line_num, good_sentence_line_num);
                                }*/
                                String s = "!!第" + (aryItem[j] + 1) + book_unit + " " + "\r\n" +
                                        getString(content_line_num, good_sentence_line_num);
                                content = (TextUtils.isEmpty(content)) ? s : content + "\r\n" + s;
                            }
                            content = "!概要" + "\r\n" + content;
                            createTextFile(book_path, book_title + "内容概要" + (aryItem[0] + 1) + "到" + (aryItem[aryItem.length - 1] + 1) + book_unit, content);
                        }
                    }
                }

                @Override
                public void onNoPermissionNeeded() {
                    ToastManager.showToast(MainActivity.this, "读写权限已允许");/*
                    //执行操作
                    afterWork(permissionName);
                    */

                }
            });
            permissionHelper.request(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } catch (Exception e) {
          LogUtil.e(getClass(), "btnCreate", e);
        }
    }

    /**@title 根据写入的内容的行数、好句的行数，返回一串字符串
     * @params 
     * @return type 
     **/
    private String getString(int contentLineNum, int goodSentenceLineNum){
        String s = "";
        try {
            s = "!!!概况" + "\r\n" +
                    returnContentString(contentLineNum) + "\r\n" +
                    "!!!好句" + "\r\n" +
                    returnContentString(goodSentenceLineNum);
        } catch (Exception e) {
          LogUtil.e(getClass(), "getString", e);
        }
        return s;
    }

    private String returnContentString(int lineNum) {
        String s1 = "";
        for (int i = 0; i < lineNum; i++) {
            if (TextUtils.isEmpty(s1)) {
                s1 = "!!!!.";
            } else {
                s1 = s1 + "\r\n" + "!!!!.";
            }
        }
        return s1;
    }

    /**@title 拆分数组
     * @params [1,2,3,4,5,6,7],每组3个>>>[1,2,3]、[4,5,6]、[7]
     * @return type 
     **/
    private static Object[] splitAry(int[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;

        List<List<Integer>> subAryList = new ArrayList<List<Integer>>();

        for (int i = 0; i < count; i++) {
            int index = i * subSize;

            List<Integer> list = new ArrayList<Integer>();
            int j = 0;
            while (j < subSize && index < ary.length) {
                list.add(ary[index++]);
                j++;
            }

            subAryList.add(list);
        }

        Object[] subAry = new Object[subAryList.size()];

        for(int i = 0; i < subAryList.size(); i++){
            List<Integer> subList = subAryList.get(i);

            int[] subAryItem = new int[subList.size()];
            for(int j = 0; j < subList.size(); j++){
                subAryItem[j] = subList.get(j).intValue();
            }

            subAry[i] = subAryItem;
        }

        return subAry;
    }

    //删除文件夹下所有文件
    public void delAllFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            if (!file.isDirectory()) {
                return;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                    //delFolder(path + "/" + tempList[i]);//再删除空文件夹
                }
            }
        } catch (Exception e) {
          LogUtil.e(getClass(), "delAllFile", e);
        }
    }

    /**@title 创建文件夹
     * @params 
     * @return type 
     **/
    private void createFileFolder(String path){
        try {
            File director = new File(path);
            if (!director.exists()) {
                director.mkdir();
            }
        } catch (Exception e) {
          LogUtil.e(getClass(), "createFileFolder", e);
        }
    }
    
    /**@title 创建txt文件
     * @params 
     * @return type 
     **/
    private void createTextFile(String textPath, String textName, String textContent){
        try {
            File fileName = new File(textPath, "/" + textName + ".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件

            if(fileName.exists())
                fileName.delete();

            fileName.createNewFile();

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
            out.write(textContent); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
          LogUtil.e(getClass(), "createTextFile", e);
        }
    }

    private boolean judgeTextRightful(){
        boolean flag = true;
        try {
            if (et_book_title == null || TextUtils.isEmpty(et_book_title.getText().toString())) {
                ToastManager.showToast(this, "请填写书名!");
                flag = false;
                return flag;
            }
            if (et_book_chapter_num == null || TextUtils.isEmpty(et_book_chapter_num.getText().toString())) {
                ToastManager.showToast(this, "请填写总共有多少章/回");
                flag = false;
                return flag;
            }
            if (et_book_chapter_unit == null || TextUtils.isEmpty(et_book_chapter_unit.getText().toString())) {
                ToastManager.showToast(this, "请填写章/回单位");
                flag = false;
                return flag;
            }
        } catch (Exception e) {
          LogUtil.e(getClass(), "judgeTextRightful", e);
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionHelper.onActivityForResult(requestCode);
    }
}
