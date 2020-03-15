package com.example.transport;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.Cache;
import ru.evotor.devices.commons.ConnectionWrapper;
import ru.evotor.devices.commons.DeviceServiceConnector;
import ru.evotor.devices.commons.exception.DeviceServiceException;
import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.printer.printable.IPrintable;
import ru.evotor.devices.commons.printer.printable.PrintableBarcode;
import ru.evotor.devices.commons.printer.printable.PrintableImage;
import ru.evotor.devices.commons.printer.printable.PrintableText;
import ru.evotor.devices.commons.services.IPrinterServiceWrapper;
import ru.evotor.devices.commons.services.IScalesServiceWrapper;
import ru.evotor.framework.component.PaymentPerformer;
import ru.evotor.framework.core.IntegrationAppCompatActivity;
import ru.evotor.framework.core.IntegrationException;
import ru.evotor.framework.core.IntegrationManagerCallback;
import ru.evotor.framework.core.IntegrationManagerFuture;
import ru.evotor.framework.core.action.command.open_receipt_command.OpenPaybackReceiptCommand;
import ru.evotor.framework.core.action.command.open_receipt_command.OpenSellReceiptCommand;
import ru.evotor.framework.core.action.command.print_receipt_command.PrintPaybackReceiptCommand;
import ru.evotor.framework.core.action.command.print_receipt_command.PrintReceiptCommandResult;
import ru.evotor.framework.core.action.command.print_receipt_command.PrintSellReceiptCommand;
import ru.evotor.framework.core.action.command.print_z_report_command.PrintZReportCommand;
import ru.evotor.framework.core.action.event.receipt.changes.position.PositionAdd;
import ru.evotor.framework.core.action.event.receipt.changes.position.SetExtra;
import ru.evotor.framework.payment.PaymentSystem;
import ru.evotor.framework.payment.PaymentType;
import ru.evotor.framework.receipt.ExtraKey;
import ru.evotor.framework.receipt.Payment;
import ru.evotor.framework.receipt.Position;
import ru.evotor.framework.receipt.PrintGroup;
import ru.evotor.framework.receipt.Receipt;
import ru.evotor.framework.receipt.ReceiptApi;
import ru.evotor.framework.receipt.TaxNumber;

import static com.example.transport.R.drawable.tab_background;
import static com.example.transport.R.drawable.tab_icon_selector;

public class MyGalery extends IntegrationAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    public static final String APP_PREFERENCES = "mysettings";
    public static final String LOG_TAG = "MYGALERY = ";

    private static final int LONG_DELAY = 31500; // 3.5 seconds


    private List<Item> mItems = new ArrayList<>();
    private RecyclerView rw;
    SharedPreferences sharedPreferencesInfo;
    SharedPreferences sharedPreferencesId;
    SharedPreferences sharedPreferencesJsonArrString;

    Timer timer;
    TimerTask mTimerTask;
    ProgressBar progressBar;
    String save;
    String saveFix;
    String saveCost;
    private static Context mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Boolean returStatus;
    String idRout;
    Integer indexButton;
    ArrayList<JSONObject> arrJSon = new ArrayList<>();

    String jsStr;
    TabHost tabHost;
    Boolean tabsIndex;

    Button btnNal;
    Button btnBagaz;

    Button btnLgot;
    TextView textSumm;

    Spinner spinnerOne;
    Spinner spinnerTwo;
    String jsonCost;


    Button infobtn;

    Integer nds;

    ArrayList<String> routOne = new ArrayList<>();
    ArrayList<String> routTwo = new ArrayList<>();
    ArrayList<String> id_rout = new ArrayList<>();

    ArrayList<String> stop_point_to_id = new ArrayList<>();
    ArrayList<Float> price = new ArrayList<>();
    ArrayList<Float> privilege_price = new ArrayList<>();
    ArrayList<Float> bagPrice = new ArrayList<>();

    TextView textErorr;
    String idRouteStr;
    Float priceCost;
    Float privelagePrice;
    Float priceBag;
    String nameRoutsSave;
    String finalNameString;
    Boolean statusBuy;
    String idFrom;
    String idTo;
    String id_rout_btn;
    Float coast_rout;
    TabHost.TabSpec tabSpec;
    Integer idTabhost;
    ImageView imgViewGalery;
    Button btnBeznalBeznal;
    Button btnLgotBeznal;
    Button btnpolnbeznal;
    String statusSettings;
    Bitmap bitmap;
    String curentUnixTime;

    public static int white = 0xFFFFFFFF;
    public static int black = 0xFF000000;
    public final static int WIDTH = 500;

    private TableLayout tabLayout;

    List<PrintData> mList = new ArrayList<>();
    PrintableBarcode.BarcodeType barType = PrintableBarcode.BarcodeType.EAN8;
    //Тип печати: BARCODE, IMAGE, TEXT
    PrintData.PrintType printType = PrintData.PrintType.TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_galery);


        rw = (RecyclerView)findViewById(R.id.rw);
        rw.setLayoutManager(new GridLayoutManager(this,2));
        returStatus = false;
        sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
        save = sharedPreferencesInfo.getString("json","");

        sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
        saveFix = sharedPreferencesInfo.getString("jsonitemRoutsFix","");
        Log.d("asd" ,saveFix);

        infobtn = findViewById(R.id.infobtn);

        sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
        saveCost = sharedPreferencesInfo.getString("saveCost","");
        Log.d("asd" ,saveCost);

        sharedPreferencesInfo = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        nds = sharedPreferencesInfo.getInt("nds", -1);


        sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
        save = sharedPreferencesInfo.getString("json","");

        spinnerOne = findViewById(R.id.spinnerOne);
        spinnerTwo = findViewById(R.id.spinerTwo);
        btnNal = findViewById(R.id.btnNal);
        btnBagaz = findViewById(R.id.btnBeznal);
        btnLgot = findViewById(R.id.btnLgotNal);
        textSumm = findViewById(R.id.textSumm);
        textErorr = findViewById(R.id.textWarning);
        imgViewGalery = findViewById(R.id.imgViewGalery);
        btnpolnbeznal = findViewById(R.id.btnpolnbeznal);
        btnLgotBeznal = findViewById(R.id.btnLgotBeznal);
        btnBeznalBeznal = findViewById(R.id.btnBeznalBeznal);


        clearTabs2();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));


        sharedPreferencesId = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        idRout = sharedPreferencesId.getString("route","");

        statusSettings = sharedPreferencesId.getString("statusSwitch","");

        Log.d(LOG_TAG, "id rout = " + idRout);
        Log.d("save nds = " , "nds - " +nds);



        Log.d(LOG_TAG, " KJINBVINDINVNDININGININIFNI INBIDNINBBINIBNIDNVIN = " +statusSettings);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */


        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyGalery.this, InfoActivity.class);
                intent.putExtra("ativityid", "galary");

                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if(mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                // TODO Fetching data from server

            }
        });


        if (idRout.equals("")||idRout == null){

            idRout = "false";

            Intent intent = new Intent(MyGalery.this, StartActivity.class);
            startActivity(intent);
        }




        btnNal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              if (statusBuy == false){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите начальную и конечную остановку",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }else {

                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    Log.d(LOG_TAG, priceCost + " price cost");
                    id_rout_btn = "1";
                    coast_rout = priceCost;

                //  openReceiptAndEmail(priceCost);



                    openReceipt(priceCost , finalNameString + (" Полная"), nds, 0,true, getUnixTime());
                //
                }



            }
        });






        DeviceServiceConnector.startInitConnections(getApplicationContext());
        DeviceServiceConnector.addConnectionWrapper(new ConnectionWrapper() {
            @Override
            public void onPrinterServiceConnected(IPrinterServiceWrapper printerService) {
                Log.e(getClass().getSimpleName(), "onPrinterServiceConnected");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            //Печать сообщения об успешной инициализации принтера
                            DeviceServiceConnector.getPrinterService().printDocument(
                                    //В настоящий момент печать возможна только на ККМ, встроенной в смарт-терминал,
                                    //поэтому вместо номера устройства всегда следует передавать константу
                                    ru.evotor.devices.commons.Constants.DEFAULT_DEVICE_INDEX,
                                    new PrinterDocument(
                                            new PrintableText("PRINTER INIT OK")));
                        } catch (DeviceServiceException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }

            @Override
            public void onPrinterServiceDisconnected() {
                Log.e(getClass().getSimpleName(), "onPrinterServiceDisconnected");
            }

            @Override
            public void onScalesServiceConnected(IScalesServiceWrapper scalesService) {
                Log.e(getClass().getSimpleName(), "onScalesServiceConnected");
            }

            @Override
            public void onScalesServiceDisconnected() {
                Log.e(getClass().getSimpleName(), "onScalesServiceDisconnected");
            }
        });





        mList.add(new PrintData(barType, printType, "hellow"));
        mList.add(new PrintData(barType, printType, "Короче Макс очень сильно любит сосать члены!< Главный любитель членов ин зе ворлд"));
        mList.add(new PrintData(barType, PrintData.PrintType.IMAGE, ""));




        btnLgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        final List<IPrintable> pList = new ArrayList<>();
                 for (PrintData item : mList) {
        if (item.getPrintType().equals(PrintData.PrintType.TEXT)) {
        //Печать текста
        pList.add(new PrintableText(item.getData()));
        } else if (item.getPrintType().equals(PrintData.PrintType.BARCODE)) {
        //Печать штрихкода
        //Штрихкод должен быть с верной контрольной суммой
        pList.add(new PrintableBarcode(item.getData(), item.getBarType()));
        } else if (item.getPrintType().equals(PrintData.PrintType.IMAGE)) {
        //Печать картинки


//            @SuppressLint("ResourceType") InputStream inputStream = getContext().getResources().openRawResource(R.drawable.icon_app);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


            try {

                long unixTime = System.currentTimeMillis() / 1000L;
                String strLong = Long.toString(unixTime);

                bitmap = encodeAsBitmap("https://avto.infosaver.ru/showCheck/"+sharedPreferencesId.getString("device_id","")+"&"+getUnixTime());
                pList.add(new PrintableImage(bitmap));

            } catch (WriterException e) {
                e.printStackTrace();
            }

            try {

                File cachePath = new File(MyGalery.this.getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/qr.png"); // overwrites this image every time
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (IOException e) {
                e.printStackTrace();

            }


        }
        }
        new Thread() {
@Override
public void run() {
        try {

        DeviceServiceConnector.getPrinterService().printDocument(
        //В настоящий момент печать возможна только на ККМ, встроенной в смарт-терминал,
        //поэтому вместо номера устройства всегда следует передавать константу
        ru.evotor.devices.commons.Constants.DEFAULT_DEVICE_INDEX,
        new PrinterDocument(pList.toArray(new IPrintable[pList.size()])));
        } catch (DeviceServiceException e) {
        e.printStackTrace();
        }

        }
        }.start();




//                if (priceCost == null){
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Выберите начальную и конечную остановку",
//                            Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, nds, 0);
//                    toast.show();
//
//
//
//                }else {
//                    progressBar.setVisibility(ProgressBar.VISIBLE);
//                    id_rout_btn = "2";
//                    coast_rout = privelagePrice;
//
//                    openReceipt(privelagePrice, finalNameString + (" Льготная"), nds, 0,true);
//
//                }
//
            }
        });

        btnBagaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceCost == null) {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите начальную и конечную остановку",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }else {
                    id_rout_btn = "3";
                    coast_rout = priceBag;

                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    openReceipt(priceBag, finalNameString + (" Багаж"), nds, 0,true,getUnixTime());

                    }
            }
        });


        btnpolnbeznal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (statusBuy == false){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите начальную и конечную остановку",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }else {


                    if (statusSettings.equals("true")){

                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        Log.d(LOG_TAG, priceCost + " price cost");
                        id_rout_btn = "1";
                        coast_rout = priceCost;
                        openReceipts(priceCost , finalNameString + (" Полная"), nds, 1,true);

                    }else {

                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        Log.d(LOG_TAG, priceCost + " price cost");
                        id_rout_btn = "1";
                        coast_rout = priceCost;
                        openReceipt(priceCost , finalNameString + (" Полная"), nds, 1,true,getUnixTime());
                        //

                    }


                }



            }
        });


        btnLgotBeznal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (priceCost == null){

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите начальную и конечную остановку",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, nds, 0);
                    toast.show();



                }else {

                    if (statusSettings.equals("true")){


                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        id_rout_btn = "2";
                        coast_rout = privelagePrice;

                        openReceipts(privelagePrice, finalNameString + (" Льготная"), nds, 1,true);

                    }else {

                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        id_rout_btn = "2";
                        coast_rout = privelagePrice;

                        openReceipt(privelagePrice, finalNameString + (" Льготная"), nds, 1,true,getUnixTime());

                    }


                }

            }
        });



        btnBeznalBeznal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceCost == null) {

                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Выберите начальную и конечную остановку",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }else {


                    if (statusSettings.equals("true")){
                        id_rout_btn = "3";
                        coast_rout = priceBag;

                        progressBar.setVisibility(ProgressBar.VISIBLE);

                        openReceipts(priceBag, finalNameString + (" Багаж"), nds, 1,true);
                    }else {


                        id_rout_btn = "3";
                        coast_rout = priceBag;

                        progressBar.setVisibility(ProgressBar.VISIBLE);

                        openReceipt(priceBag, finalNameString + (" Багаж"), nds, 1,true,getUnixTime());
                    }



                }
            }
        });


// TODO mi timer false
//        if(timer != null){
//            timer.cancel();
//        }
//

        timer = new Timer();
        mTimerTask = new MyTimerTask();
        Integer t = 100000;
        timer.schedule(mTimerTask, t, t);




        mContext = getApplicationContext();


// TODO mi tab layout


        tabHost  = (TabHost) findViewById(R.id.tabHost);


        tabHost.setup();




//        public void setupTabView(){
//            for (int i = 0; i < tabLayout.getTabCount(); i++) {
//                tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
//                TextView tab_name = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.txt_tab_name);
//                tab_name.setText("" + tabNames[i]);
//            }
//        }





        tabSpec  = tabHost.newTabSpec("tag1");

//        tabSpec.setContent(R.id.tab1);
//
       tabSpec.setIndicator("ФИКС ОПЛАТА");

   //    tabSpec.setIndicator("", ContextCompat.getDrawable(MyGalery.this, R.drawable.tab_icon_selector));

        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);


        // и устанавливаем его, как заголовок




        tabSpec = tabHost.newTabSpec("tag2");


        tabSpec.setIndicator("МАРШРУТ");

        //tabSpec.setIndicator("Вкладка 2", ContextCompat.getDrawable(MyGalery.this, R.drawable.tab_background));

        tabSpec.setContent(R.id.tab2);
        // и устанавливаем его, как заголовок
        tabHost.addTab(tabSpec);



        idTabhost = 1;

        tabHost.setCurrentTab(0);

        tabsIndex = false;


        if (tabHost.getCurrentTab()== 0){

            clearTabs2();
            rw.setAlpha(1);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            new ItemTask().execute();
            tabsIndex = false;

        }

        statusBuy = false;
        //rw.setEnabled(false);

        //обработчик переключения табов
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {

                if (tabId == "tag1") {

                    idTabhost = 1;

                    clearTabs2();
                    rw.setAlpha(1);

                    Log.d(LOG_TAG, "tag1");
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    new ItemTask().execute();

                    tabsIndex = false;
                    rw.setClickable(true);




                } else if (tabId == "tag2") {

                    idTabhost = 2;

                    rw.setClickable(false);


                    rw.setAlpha(0);
                    Log.d(LOG_TAG, "idRouteStr = " + idRout);
                    routOne.clear();
                    routTwo.clear();

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyGalery.this,android.R.layout.simple_spinner_item,routTwo );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTwo.setAdapter(adapter);
                    visibleTab();

                    textSumm.setText("Выберите начальную и конечную остановку");

                    if (idRout.equals("false")){

                        textErorr.setAlpha(1);
                        textErorr.setText("Добавьте маршрут в настройках");

                    }else {
                        progressBar.setVisibility(ProgressBar.VISIBLE);

                        spinnerOne.setEnabled(true);
                        spinnerTwo.setEnabled(true);

                        reconectApirout();
                        reconectApiCost();

                        spinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                textSumm.setText("Выберите начальную и конечную остановку");
                                btnBagaz.setAlpha(0);
                                btnLgot.setAlpha(0);
                                privilege_price.clear();
                                price.clear();
                                stop_point_to_id.clear();
                                bagPrice.clear();
                                routTwo.clear();
                                routTwo.add("Конечная остановка");
                                Log.d(LOG_TAG,"json cost ssss  = " + jsonCost);

                                if (position == 0){

                                    textSumm.setText("Выберите начальную и конечную остановку");
                                    statusBuy = false;
                                }else {


                                    idRouteStr = id_rout.get(position-1);
                                    nameRoutsSave = routOne.get(position);

                                    idFrom = idRouteStr;

                                    Log.d(LOG_TAG,"id rout = " + idRouteStr + "position - " + position + "nameRoutSave " + nameRoutsSave);



                                    if (jsonCost == null){

                                        Log.d(LOG_TAG,"clear");

                                    }else {

                                        try {
                                            JSONObject jsonBody = null;
                                            jsonBody = new JSONObject(jsonCost);
                                            JSONArray jsonArr = null;
                                            jsonArr = jsonBody.getJSONArray(idRouteStr);
                                            Log.d(LOG_TAG,"json cost  = " + jsonCost);


                                            for (int i = 0; i < jsonArr.length()  ; i++) {

                                                JSONObject costJsonObject = jsonArr.getJSONObject(i);
                                                routTwo.add(costJsonObject.getString("stop_point_to_name"));
                                                privilege_price.add((float) costJsonObject.getDouble("privilege_price"));
                                                price.add((float) costJsonObject.getDouble("price"));
                                                bagPrice.add((float) costJsonObject.getDouble("bag_price"));
                                                stop_point_to_id.add(costJsonObject.getString("stop_point_to_id"));

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }




                                    ArrayAdapter<String> adapters = new ArrayAdapter<String>(MyGalery.this,android.R.layout.simple_spinner_item,routTwo);
                                    adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerTwo.setAdapter(adapters);

                                    Log.d(LOG_TAG, "rout two  = " + routTwo);


                                }

                                spinnerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


//
                                        btnBeznalBeznal.setAlpha(0);
                                        btnBeznalBeznal.setEnabled(false);

                                        btnLgotBeznal.setAlpha(0);
                                        btnLgotBeznal.setEnabled(false);

                                        btnpolnbeznal.setAlpha(0);
                                        btnpolnbeznal.setEnabled(false);

                                        btnNal.setAlpha(0);
                                        btnNal.setEnabled(false);
                                        textSumm.setText("Выберите начальную и конечную остановку");
                                        btnBagaz.setAlpha(0);
                                        btnBagaz.setEnabled(false);
                                        btnLgot.setAlpha(0);
                                        btnLgot.setEnabled(false);


                                        if (position == 0){
                                            statusBuy = false;
                                            textSumm.setText("Выберите начальную и конечную остановку");

                                        }else {



                                            finalNameString = nameRoutsSave + "-" + routTwo.get(position);
                                            priceCost = price.get(position - 1);
                                            privelagePrice = privilege_price.get(position - 1);
                                            priceBag = bagPrice.get(position - 1);
                                            idTo = stop_point_to_id.get(position -1);

                                            String privelPrice;


                                            imgViewGalery.setAlpha(1.f);

                                            btnNal.setAlpha(1);
                                            btnNal.setEnabled(true);
                                            btnpolnbeznal.setAlpha(1);
                                            btnpolnbeznal.setEnabled(true);

                                            if (privelagePrice != 0 && priceBag == 0) {
                                                btnLgot.setEnabled(true);
                                                btnLgot.setAlpha(1);
                                                btnLgotBeznal.setEnabled(true);
                                                btnLgotBeznal.setAlpha(1);

                                                btnNal.setText("  Полный");
                                                btnpolnbeznal.setText(priceCost +" руб.");
                                                btnLgot.setText("    Льготный");
                                                btnLgotBeznal.setText(privelagePrice +" руб.");

                                          //      textSumm.setText(finalNameString + "\n" + "Полная стоимость: " + priceCost + "\nЛьготная стоимость: " + privelagePrice);
                                            } else if (priceBag != 0 && privelagePrice == 0){
                                                btnBagaz.setEnabled(true);
                                                btnBagaz.setAlpha(1);
                                                btnBeznalBeznal.setEnabled(true);
                                                btnBeznalBeznal.setAlpha(1);

                                                btnNal.setText("  Полный");
                                                btnpolnbeznal.setText(priceCost +" руб.");
                                                btnBagaz.setText(" Багаж");
                                                btnBeznalBeznal.setText(priceBag + " руб.");
                                                textSumm.setText(finalNameString + "\n" + "Полная стоимость: " + priceCost +"\nСтоимость проезда с багажом: " + priceBag);

                                              }else if (privelagePrice != 0 && priceBag != 0){

                                                btnNal.setText("  Полный");
                                                btnpolnbeznal.setText(priceCost +" руб.");

                                                btnLgot.setText("    Льготный");
                                                btnLgotBeznal.setText(privelagePrice +" руб.");

                                                btnBagaz.setText(" Багаж");
                                                btnBeznalBeznal.setText(priceBag + " руб.");

                                                //     textSumm.setText(finalNameString + "\n" +"Полная стоимость: " + priceCost + "\nЛьготная стоимость: " + privelagePrice + "\nБагаж: " + priceBag);
                                                btnLgot.setEnabled(true);
                                                btnBagaz.setEnabled(true);
                                                btnBeznalBeznal.setEnabled(true);
                                                btnBeznalBeznal.setAlpha(1);
                                                btnLgotBeznal.setEnabled(true);
                                                btnLgotBeznal.setAlpha(1);
                                                btnBagaz.setAlpha(1);
                                                btnLgot.setAlpha(1);

                                            }else {
                                                btnNal.setText("  Полный");
                                                btnpolnbeznal.setText(priceCost +" руб.");

                                             //   textSumm.setText(finalNameString + "\n" + "Полная стоимость: " + priceCost);

                                                btnBagaz.setAlpha(0);
                                                btnLgot.setAlpha(0);

                                                btnLgot.setEnabled(false);
                                                btnBagaz.setEnabled(false);


                                                btnBeznalBeznal.setEnabled(false);
                                                btnBeznalBeznal.setAlpha(0);
                                                btnLgotBeznal.setEnabled(false);
                                                btnLgotBeznal.setAlpha(0);

                                            }

                                            statusBuy = true;

                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }



                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });
                    }
                    
                }


            }
        });


        rw.addOnItemTouchListener(new RecyclerItemClickListener(this, rw, new RecyclerItemClickListener.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(View view, int position) {
                Log.d("ads", "position = " + position);

                indexButton = position;


                if (tabsIndex == false){
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    if (mItems.get(position).getSum() == null || mItems.get(position).getSum() == 0 ){

                        LayoutInflater li = LayoutInflater.from(MyGalery.this);
                        View promptsView = li.inflate(R.layout.prompt, null);

                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MyGalery.this);
                        mDialogBuilder.setView(promptsView);



                        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                        final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
                        tv.setText("Введите сумму: ");

                        mDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {

                                                String  str = userInput.getText().toString();

                                                if (str.equals("") || str == null){
                                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                    dialog.cancel();

                                                }else {
                                                    Float decimal = Float.parseFloat(str);
                                                    openReceipt(decimal, mItems.get(position).getName(), mItems.get(position).getNds(), mItems.get(position).getType(),false,getUnixTime());

                                                }


                                            }
                                        })
                                .setNegativeButton("ОТМЕНА",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                dialog.cancel();

                                            }
                                        });

                        AlertDialog alertDialog = mDialogBuilder.create();

                        alertDialog.show();

                        Button bn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                        if(bn != null) {
                            bn.setTextColor(R.color.black);
                        }

                        Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                        if(b != null) {
                            b.setTextColor(R.color.black);
                        }


                    }else {

                     //   openReceipts();

                        openReceipt(mItems.get(position).getSum(), mItems.get(position).getName(), mItems.get(position).getNds(), mItems.get(position).getType(),false,getUnixTime());

                    }
                }else {

                }





            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));




    }



    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? black : white;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }


    private Bitmap getBitmapFromAsset(String fileName) {
        AssetManager assetManager = getAssets();
        InputStream stream = null;
        try {
            stream = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(stream);
    }


    //TODO clear spinner

    void  clearTabs2(){

        btnLgot.setAlpha(0);
        btnNal.setAlpha(0);
        spinnerOne.setAlpha(0);
        spinnerTwo.setAlpha(0);

        btnBeznalBeznal.setAlpha(0);
        btnLgotBeznal.setAlpha(0);
        btnpolnbeznal.setAlpha(0);

        imgViewGalery.setAlpha(0.f);

        btnNal.setEnabled(false);
        spinnerOne.setEnabled(false);
        spinnerTwo.setEnabled(false);
        btnBagaz.setEnabled(false);
        btnLgot.setEnabled(false);
        btnBagaz.setAlpha(0);
        textSumm.setAlpha(0);
        routOne.clear();
        routTwo.clear();
        textErorr.setAlpha(0);


    }

    void  visibleTab(){

       // btnNal.setAlpha(1);
       // spinnerOne.setAlpha(1);
      //  spinnerTwo.setAlpha(1);
        textSumm.setAlpha(1);

        textSumm.setText("Выберите начальную и конечную остановку");


    }



    public  String getUnixTime(){

        long unixTime = System.currentTimeMillis() / 1000L;
        String strLong = Long.toString(unixTime);

        return strLong;

    }


//TODO my timer task

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {

                    Log.d("LOG_TAG", "timer + ");
                    reconectFunc();
                }});
        }
    }


    void bufer(Boolean typeBufer){

        sharedPreferencesId = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        if (idRout == null|| idRout.equals("")){

        }else {


            String id_route = sharedPreferencesId.getString("route", "");
            String id_car = sharedPreferencesId.getString("car", "");
            String id_driver = sharedPreferencesId.getString("driver", "");

            if (typeBufer == false){


                Log.d(LOG_TAG, "id route = " + id_route + "\n id car = " + id_car + "\n id drive = " + id_driver + " \n id button = " + mItems.get(indexButton).getId() + "\n idFrom = " + idFrom + "\n Idto = " + idTo);


                JSONObject clientList = new JSONObject();
                try {

                    clientList.put("id_route", id_route);
                    clientList.put("id_car", id_car);
                    clientList.put("id_driver", id_driver);
                    clientList.put("id_button", mItems.get(indexButton).getId());
                    clientList.put("unix_time",curentUnixTime);
                    clientList.put("qr_code",sharedPreferencesId.getString("device_id","")+"&"+curentUnixTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrJSon.add(clientList);
                jsStr = arrJSon.toString();

                sharedPreferencesJsonArrString = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesJsonArrString.edit();
                ed.putString("jsStr", jsStr);
                ed.commit();


            }else if (typeBufer == true){

                Log.d(LOG_TAG, "type bufer");


                JSONObject clientList = new JSONObject();

                Integer x = Integer.valueOf(idFrom);
               // int y = Integer.parseInt(str);

                try {

                    clientList.put("id_route", id_route);
                    clientList.put("id_car", id_car);
                    clientList.put("id_driver", id_driver);
                    clientList.put("id_from",x);
                    clientList.put("id_to",idTo);
                    clientList.put("cost_type", id_rout_btn);
                    clientList.put("cost", coast_rout);
                    clientList.put("unix_time",curentUnixTime);
                    clientList.put("qr_code",sharedPreferencesId.getString("device_id","")+"&"+curentUnixTime);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrJSon.add(clientList);
                jsStr = arrJSon.toString();

                sharedPreferencesJsonArrString = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesJsonArrString.edit();
                ed.putString("jsStr", jsStr);
                ed.commit();

            }



            Log.d("LOG_TAG ", "timer = " + jsStr);



        }

    }



    void reconectApirout() {



        if ((Boolean) StartActivity.NetworkManager.isNetworkAvailable(MyGalery.this)) {

            spinnerOne.setEnabled(true);
            spinnerTwo.setEnabled(true);
            spinnerTwo.setAlpha(1);
            spinnerOne.setAlpha(1);
            textErorr.setAlpha(0);
            new RoutTask().execute();


        } else {


            spinnerOne.setEnabled(true);
            spinnerTwo.setEnabled(true);
            spinnerTwo.setAlpha(1);
            spinnerOne.setAlpha(1);

            new RoutTask().execute();


            progressBar.setVisibility(ProgressBar.INVISIBLE);
            textErorr.setAlpha(1);
            textErorr.setText("Нет подключения к сети интернет!\nДанные будт загружены из буфера");
//            progressBar.setVisibility(ProgressBar.INVISIBLE);
//
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "Нет подключения к сети интернет! Проверьте свое подключение rout!",
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }


    }


    void reconectApiCost() {



        if ((Boolean) StartActivity.NetworkManager.isNetworkAvailable(MyGalery.this)) {

            textErorr.setAlpha(0);

            new CostTask().execute();


        } else {

            new CostTask().execute();
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            textErorr.setAlpha(1);
            textErorr.setText("Нет подключения к сети интернет!\nДанные будт загружены из буфера");
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    "Нет подключения к сети интернет! Проверьте свое подключение cost!",
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }


    }



    public  class CostTask extends AsyncTask<Void, Void, List<ItemCost>> {


        @Override
        protected List<ItemCost> doInBackground(Void... voids) {

            return new APICost().itemCosts(saveCost,idRout);

        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(List<ItemCost> itemCosts) {
            super.onPostExecute(itemCosts);

            progressBar.setVisibility(ProgressBar.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);

            if (itemCosts.size() == 0){

                textErorr.setText("Нет подключения к сети интернет!\nПодключитесь к интернету для загрузки данных в буфер");

                Log.d(LOG_TAG, "пусто");

            }else{

                routTwo.clear();
                routTwo.add("Конечная остановка");

                sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesInfo.edit();
                ed.putString("saveCost", itemCosts.get(0).cost);
                ed.commit();

                jsonCost = itemCosts.get(0).cost;

            }



        }
    }



    public  class RoutTask extends AsyncTask<Void, Void, List<ItemRout>> {


        @Override
        protected List<ItemRout> doInBackground(Void... voids) {

            return new APIRout().itemRouts(saveFix,idRout);

        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(List<ItemRout> itemRouts) {
            super.onPostExecute(itemRouts);
            mSwipeRefreshLayout.setRefreshing(false);


            if (itemRouts.size() == 0){

                textErorr.setText("Нет подключения к сети интернет!\nПодключитесь к интернету для загрузки данных в буфер");

                Log.d(LOG_TAG, "пустоccc");

            }else {

                routOne.clear();
                routOne.add("Начальная остановка");

                sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesInfo.edit();
                ed.putString("jsonitemRoutsFix", itemRouts.get(0).getJsonString());
                ed.commit();


                for (int i= 0; i<itemRouts.size(); i++){

                    routOne.add(itemRouts.get(i).getName());
                    id_rout.add(itemRouts.get(i).getId());
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyGalery.this,android.R.layout.simple_spinner_item,routOne );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOne.setAdapter(adapter);

            }



        }
    }


    public  class JsonTask extends AsyncTask<Void, Void, List<Item>> {


        @Override
        protected List<Item> doInBackground(Void... voids) {

            return new APIBufer().itemsStart(jsStr);

        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);

            Log.d("LOG" , "onPostExecute");


                arrJSon.clear();
                jsStr = null;
                sharedPreferencesJsonArrString = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesJsonArrString.edit();
                ed.putString("jsStr", jsStr);
                ed.commit();




        }
    }

    void reconectFunc() {

        if ((Boolean) StartActivity.NetworkManager.isNetworkAvailable(MyGalery.this)) {


            sharedPreferencesJsonArrString = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            jsStr = sharedPreferencesJsonArrString.getString("jsStr","");
            Log.d("LOG" , "jsonStr = " +jsStr);

            if (jsStr == null||jsStr.equals("")){

                Log.d("LOG" , "nulll");

            }else {


                new JsonTask().execute();
            }


        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "У вас отсутствуют заданные маршруты! Задайте в настройках",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.item1:
                progressBar.setVisibility(ProgressBar.VISIBLE);
                zzzz();
                return true;
            case R.id.item2:
                Log.d("item", "item = " + item.getTitle().toString());


                LayoutInflater li = LayoutInflater.from(MyGalery.this);
                View promptsView = li.inflate(R.layout.prompt, null);

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MyGalery.this);
                mDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
                final TextView tv = (TextView) promptsView.findViewById(R.id.tv);

                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("БАНКОВСКАЯ КАРТА",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        String  str = userInput.getText().toString();

                                        if (str.equals("") || str == null){
                                            dialog.cancel();
                                        }else {
                                            progressBar.setVisibility(ProgressBar.VISIBLE);
                                            Integer decimal = Integer.parseInt(str);
                                            checkReturn(decimal,1);

                                        }


                                    }
                                })
                        .setNegativeButton("НАЛИЧНЫЕ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        String  str = userInput.getText().toString();

                                        if (str.equals("") || str == null){
                                            dialog.cancel();

                                        }else {
                                            progressBar.setVisibility(ProgressBar.VISIBLE);

                                            Integer decimal = Integer.parseInt(str);
                                            checkReturn(decimal,0);

                                        }

                                    }
                                });

                AlertDialog alertDialog = mDialogBuilder.create();

                alertDialog.show();

                Button bn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                if(bn != null) {
                    bn.setTextColor(R.color.black);
                }

                Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                if(b != null) {
                    b.setTextColor(R.color.black);
                }
                // openPayback();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

            case R.id.item3:

                Intent intent = new Intent(MyGalery.this, StartActivity.class);
                startActivity(intent);

                return true;

        }

    }

    @Override
    public void onRefresh() {

        Log.d(LOG_TAG,"ID TAB HOST -----"+idTabhost );


        if (idTabhost == 1){

            new ItemTask().execute();


        }else {
            reconectApirout();
            reconectApiCost();

        }



    }

    public static Context getContext() {

        return mContext;
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener mListener;

        private GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null && mListener != null) {
                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());

            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    private static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imgBackground;
        final TextView name, coast, type, textFalse;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgBackground = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.names);
            coast = (TextView) itemView.findViewById(R.id.coast);
            type = (TextView) itemView.findViewById(R.id.type);
            textFalse = (TextView)itemView.findViewById(R.id.textFalse);

            itemView.setOnClickListener(this);



        }


        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "naem = " + name);

        }
    }



    private class Adapter extends RecyclerView.Adapter<Holder>{

        private List<Item> itemList;
        public Adapter(List<Item> items){

            itemList = items;

        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(MyGalery.this);
            View v;
            if (itemList.size() == 0){
                v = inflater.inflate(R.layout.item_fale, viewGroup, false);
            }else {
                v = inflater.inflate(R.layout.buy_item, viewGroup, false);
            }

            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int i) {


                if (itemList.size() == 0) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    holder.textFalse.setText("Нет данных!");

                }else {


                        mSwipeRefreshLayout.setRefreshing(false);


                        Item item = itemList.get(i);
                        holder.itemView.setBackgroundColor(Color.parseColor(item.getHexColor()));
                        holder.name.setText(item.getName());

                        if (item.getSum() == 0 || item.getSum() == null){
                            holder.coast.setText("Поьзовательская цена");

                        }else {
                            holder.coast.setText("Цена: " + item.getSum().toString() + " рублей");

                        }


                        String type_coast = null;

                        if (item.getType() == 0){

                            type_coast = "Наличный";

                        }else if (item.getType() == 1){
                            type_coast = "Безналичный";

                        }

                        holder.type.setText(type_coast + " расчет");





                }









        }

        @Override
        public int getItemCount() {

            if (itemList.size() == 0){
                return 1;
            }else {
                return itemList.size();
            }

        }
    }


    public  class ItemTask extends AsyncTask<Void, Void, List<Item>> {


        @Override
        protected List<Item> doInBackground(Void... voids) {

            return new API().items(save, idRout);

        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);

            progressBar.setVisibility(ProgressBar.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);




            if (items.size() == 0){
                Log.d("asd", "falseeeee");
                tabHost.setCurrentTab(1);

                mItems = items;
                rw.setLayoutManager(new GridLayoutManager(MyGalery.this,1));
                setupAdapter();

            }else{

                sharedPreferencesInfo = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = sharedPreferencesInfo.edit();
                ed.putString("json", items.get(0).getJsonString());
                ed.commit();
                mItems = items;
                rw.setLayoutManager(new GridLayoutManager(MyGalery.this,2));

                setupAdapter();
            }



        }
    }




    private  void setupAdapter(){
        rw.setAdapter(new Adapter(mItems));

    }


    public void openReceipts(Float decimal, String names, Integer tax, Integer type, Boolean typeBufer) {


        Log.d("Приход " , "цена = "+ decimal.toString() + " name =" + names +" tax = " + tax + " type = " + type.toString());




        BigDecimal bd = BigDecimal.valueOf(decimal);



        //Дополнительное поле для позиции. В списке наименований расположено под количеством и выделяется синим цветом
        Set<ExtraKey> set = new HashSet<>();
        set.add(new ExtraKey(null, null, "Тест Зубочистки"));
        //Создание списка товаров чека
        List<PositionAdd> positionAddList = new ArrayList<>();
        positionAddList.add(
                new PositionAdd(
                        Position.Builder.newInstance(
                                //UUID позиции
                                UUID.randomUUID().toString(),
                                //UUID товара
                                null,

                                names,
                                //Наименование единицы измерения
                                "1",
                                //Точность единицы измерения
                                0,
                                //Цена без скидок
                                bd,
                                BigDecimal.ONE
                                //Количество
                                //Добавление цены с учетом скидки на позицию. Итог = price - priceWithDiscountPosition
                        ).setPriceWithDiscountPosition(new BigDecimal(decimal))
                                .setExtraKeys(set).build()
                )
        );

        //Дополнительные поля в чеке для использования в приложении
        JSONObject object = new JSONObject();
        try {
            object.put("someSuperKey", "AWESOME RECEIPT OPEN");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SetExtra extra = new SetExtra(object);

        //Открытие чека продажи. Передаются: список наименований, дополнительные поля для приложения
        new OpenSellReceiptCommand(positionAddList, extra).process(MyGalery.this, new IntegrationManagerCallback() {




            @Override
            public void run(IntegrationManagerFuture future) {


                progressBar.setVisibility(ProgressBar.INVISIBLE);

                mSwipeRefreshLayout.setRefreshing(false);


                try {


                    bufer(typeBufer);

                    IntegrationManagerFuture.Result result = future.getResult();
                    if (result.getType() == IntegrationManagerFuture.Result.Type.OK) {
                        startActivity(new Intent("evotor.intent.action.payment.SELL"));
                    }


                    switch (result.getType()) {
                        case OK:

                            break;
                        case ERROR:


                            switch (result.getError().getCode()) {

                                case PrintReceiptCommandResult.ERROR_CODE_DATETIME_SYNC_REQUIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нужна синхронизация даты/времени ККМ и терминала", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_SESSION_TIME_EXPIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Время сессии превысило 24 часа, закройте смену открыв меню в правом верхнем углу", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_EMAIL_AND_PHONE_ARE_NULL:

                                    Toast.makeText(MyGalery.this, "Ошибка: В интерет чеках поля 'эл.почта' и/или 'телефон' клиента должны быть заполнены", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_KKM_IS_BUSY:

                                    Toast.makeText(MyGalery.this, "Ошибка: ККМ в данный момент выполняет другую операцию", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_AUTHENTICATED_USER:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет авторизованного пользователя на терминале", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_PRINT_DOCUMENT_CREATION_FAILED:

                                    Toast.makeText(MyGalery.this, "Ошибка создания документа для печати", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_PERMISSION:

                                    Toast.makeText(MyGalery.this, "Ошибка: У приложения нет необходимого разрешения (permission)", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_POSITIONS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_PAYMENTS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_KKM_IS_NOT_AVAILABLE:

                                    Toast.makeText(MyGalery.this, "Ккм не доступна", Toast.LENGTH_LONG).show();

                                    break;


                                default:
                                    break;
                            }


                            break;
                    }



                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void openReceipt(Float decimal, String names, Integer tax, Integer type, Boolean typeBufer,String unix) {


        Log.d("Приход " , "цена = "+ decimal.toString() + " name =" + names +" tax = " + tax + " type = " + type.toString());

        curentUnixTime = unix;


        PaymentSystem paymentSystem = null;

        if (type == 0){
            paymentSystem = new PaymentSystem(PaymentType.CASH, "Internet", "12424");
        } else if (type == 1){
            paymentSystem = new PaymentSystem(PaymentType.ELECTRON, "Internet", "12424");
        }


        BigDecimal bd = BigDecimal.valueOf(decimal);

        // Этот код написал Олежа Ясно солнышко , следующий день после негритянок эхеххех

        Position.Builder pos = Position.Builder.newInstance(
                UUID.randomUUID().toString(),
                null,
                names,
                "1",
                0,
                bd,
                BigDecimal.ONE
        );
        switch (tax){
            case 0:
                pos.setTaxNumber(TaxNumber.NO_VAT);
                //pos.build();
                break;
            case 1:
                pos.setTaxNumber(TaxNumber.VAT_0);
                //pos.build();
                break;
            case 2:
                pos.setTaxNumber(TaxNumber.VAT_10);
                //pos.build();
                break;
            case 3:
                pos.setTaxNumber(TaxNumber.VAT_10_110);
                //pos.build();
                break;
            case 4:
                pos.setTaxNumber(TaxNumber.VAT_18);
                //pos.build();
                break;
            case 5:
                pos.setTaxNumber(TaxNumber.VAT_18_118);
                //pos.build();
                break;
                default:break;
        }




        List<Position> list = new ArrayList<>();
//        list.add(
//                Position.Builder.newInstance(
//                        UUID.randomUUID().toString(),
//                        null,
//                            names,
//                        "1",
//                        0,
//                        new BigDecimal(decimal),
//                        BigDecimal.ONE
//                ).setTaxNumber(TaxNumber.VAT_10).build()
//        );
        list.add(pos.build());


        //Position.Builder.newInstance()

        HashMap payments = new HashMap<Payment, BigDecimal>();
        //PaymentSystem paymentSystem = new PaymentSystem(PaymentType.CASH, "Card", "Cashless");
      //  PaymentSystem paymentSystem = new PaymentSystem(PaymentType.CASH, "Internet", "12424");
        PaymentPerformer paymentPerformer = new PaymentPerformer(paymentSystem, getPackageName()
                ,MyGalery.class.getName()
                ,"f98ed05f-0d1e-4524-b1b1-4800ce69e998"
                , getApplicationName(MyGalery.this));


        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                bd,
                paymentSystem,
                paymentPerformer,
                "purposeIdentifier",
                "accountId",
                "accountUserDescription"
        );

        payments.put(payment, bd);


        PrintGroup printGroup = new PrintGroup(UUID.randomUUID().toString(),
                PrintGroup.Type.CASH_RECEIPT, null, null, null, null, true);
        Receipt.PrintReceipt printReceipt = new Receipt.PrintReceipt(
                printGroup,
                list,
                payments,
                new HashMap<Payment, BigDecimal>(), new HashMap<String, BigDecimal>()
        );

        ArrayList<Receipt.PrintReceipt> listDocs = new ArrayList<>();
        listDocs.add(printReceipt);

        Receipt slip = ReceiptApi.getReceipt(MyGalery.this,  Receipt.Type.SELL);

       // Toast.makeText(MyGalery.this, "get header = " + slip.getHeader().toString(), Toast.LENGTH_LONG).show();

      //  final Toast toast = Toast.makeText(getBaseContext(), "YOUR MESSAGE + " + slip.getHeader().toString(),Toast.LENGTH_SHORT); toast.show(); new
        //        CountDownTimer(100000, 1000) { public void onTick(long millisUntilFinished) {toast.show();} public void onFinish() {toast.cancel();} }.start();

//        JSONObject object = new JSONObject();
//        String di =  sharedPreferencesId.getString("device_id","");
//
//        String extraString = di+"&"+unix;
//        Toast.makeText(MyGalery.this, extraString, Toast.LENGTH_LONG).show();
//
//        try {
//            object.put("qr_code", extraString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        SetExtra extra = new SetExtra(object);



        new PrintSellReceiptCommand(listDocs,null, null, "infosaverapp@yandex.ru",null,null,null).process(MyGalery.this, new IntegrationManagerCallback() {
            @Override
            public void run(IntegrationManagerFuture integrationManagerFuture) {


                progressBar.setVisibility(ProgressBar.INVISIBLE);
                ////
                mSwipeRefreshLayout.setRefreshing(false);

                try {

                    bufer(typeBufer);

                    IntegrationManagerFuture.Result result = integrationManagerFuture.getResult();



                  //  PrintReceiptCommandResult printSellReceiptResult = PrintReceiptCommandResult.create(result.getData());



                    switch (result.getType()) {
                        case OK:

                            break;
                        case ERROR:


                            switch (result.getError().getCode()){

                                case PrintReceiptCommandResult.ERROR_CODE_DATETIME_SYNC_REQUIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нужна синхронизация даты/времени ККМ и терминала", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_SESSION_TIME_EXPIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Время сессии превысило 24 часа, закройте смену открыв меню в правом верхнем углу", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_EMAIL_AND_PHONE_ARE_NULL:

                                    Toast.makeText(MyGalery.this, "Ошибка: В интерет чеках поля 'эл.почта' и/или 'телефон' клиента должны быть заполнены", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_KKM_IS_BUSY:

                                    Toast.makeText(MyGalery.this, "Ошибка: ККМ в данный момент выполняет другую операцию", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_AUTHENTICATED_USER:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет авторизованного пользователя на терминале", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_PRINT_DOCUMENT_CREATION_FAILED:

                                    Toast.makeText(MyGalery.this, "Ошибка создания документа для печати", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_PERMISSION:

                                    Toast.makeText(MyGalery.this, "Ошибка: У приложения нет необходимого разрешения (permission)", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_POSITIONS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_PAYMENTS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_KKM_IS_NOT_AVAILABLE:

                                    Toast.makeText(MyGalery.this, "Ккм не доступна", Toast.LENGTH_LONG).show();

                                    break;


                                    default:break;
                            }


                            break;
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public void zzzz() {

        //new PrintZReportCommand().process(context, callback);

        new PrintZReportCommand().process(MyGalery.this, new IntegrationManagerCallback(){

            public void run(IntegrationManagerFuture integrationManagerFuture) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);

                try {
                    IntegrationManagerFuture.Result result = integrationManagerFuture.getResult();
                    switch (result.getType()) {
                        case OK:
                            break;
                        case ERROR:


                            switch (result.getError().getCode()){

                                case PrintReceiptCommandResult.ERROR_CODE_DATETIME_SYNC_REQUIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нужна синхронизация даты/времени ККМ и терминала", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_SESSION_TIME_EXPIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Время сессии превысило 24 часа, закройте смену открыв меню в правом верхнем углу", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_EMAIL_AND_PHONE_ARE_NULL:

                                    Toast.makeText(MyGalery.this, "Ошибка: В интерет чеках поля 'эл.почта' и/или 'телефон' клиента должны быть заполнены", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_KKM_IS_BUSY:

                                    Toast.makeText(MyGalery.this, "Ошибка: ККМ в данный момент выполняет другую операцию", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_AUTHENTICATED_USER:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет авторизованного пользователя на терминале", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_PRINT_DOCUMENT_CREATION_FAILED:

                                    Toast.makeText(MyGalery.this, "Ошибка создания документа для печати", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_PERMISSION:

                                    Toast.makeText(MyGalery.this, "Ошибка: У приложения нет необходимого разрешения (permission)", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_POSITIONS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_PAYMENTS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет платежей в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_KKM_IS_NOT_AVAILABLE:

                                    Toast.makeText(MyGalery.this, "Ккм не доступна", Toast.LENGTH_LONG).show();

                                    break;


                                default:break;
                            }
                            break;
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }

        });


    }


    public void checkReturn(Integer decimal, Integer type) {


        Log.d("13", "decimal = " + decimal);


        PaymentSystem paymentSystem = null;

        if (type == 0){
            paymentSystem = new PaymentSystem(PaymentType.CASH, "Internet", "12424");
        } else if (type == 1){
            paymentSystem = new PaymentSystem(PaymentType.ELECTRON, "Internet", "12424");
        }


        Position.Builder pos = Position.Builder.newInstance(
                UUID.randomUUID().toString(),
                null,
                "Возврат " ,
                "1",
                0,

        new BigDecimal(decimal),
                BigDecimal.ONE
        );

        pos.setTaxNumber(TaxNumber.NO_VAT);


        List<Position> list = new ArrayList<>();
//        list.add(
//                Position.Builder.newInstance(
//                        UUID.randomUUID().toString(),
//                        null,
//                            names,
//                        "1",
//                        0,
//                        new BigDecimal(decimal),
//                        BigDecimal.ONE
//                ).setTaxNumber(TaxNumber.VAT_10).build()
//        );
        list.add(pos.build());


        //Position.Builder.newInstance()

        HashMap payments = new HashMap<Payment, BigDecimal>();
        //PaymentSystem paymentSystem = new PaymentSystem(PaymentType.CASH, "Card", "Cashless");
        //  PaymentSystem paymentSystem = new PaymentSystem(PaymentType.CASH, "Internet", "12424");
        PaymentPerformer paymentPerformer = new PaymentPerformer(paymentSystem, getPackageName()
                ,MyGalery.class.getName()
                ,"f98ed05f-0d1e-4524-b1b1-4800ce69e998"
                , getApplicationName(MyGalery.this));


        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                new BigDecimal(decimal),
                paymentSystem,
                paymentPerformer,
                "purposeIdentifier",
                "accountId",
                "accountUserDescription"
        );

        payments.put(payment, new BigDecimal(decimal));


        PrintGroup printGroup = new PrintGroup(UUID.randomUUID().toString(),
                PrintGroup.Type.CASH_RECEIPT, null, null, null, null, true);
        Receipt.PrintReceipt printReceipt = new Receipt.PrintReceipt(
                printGroup,
                list,
                payments,
                new HashMap<Payment, BigDecimal>(), new HashMap<String, BigDecimal>()
        );

        ArrayList<Receipt.PrintReceipt> listDocs = new ArrayList<>();
        listDocs.add(printReceipt);




        new PrintPaybackReceiptCommand(listDocs, null, null, "infosaverapp@yandex.ru",null,null,null,null).process(MyGalery.this, new IntegrationManagerCallback() {
            @Override
            public void run(IntegrationManagerFuture integrationManagerFuture) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                returStatus = false;


                try {
                    IntegrationManagerFuture.Result result = integrationManagerFuture.getResult();

                    switch (result.getType()) {
                        case OK:

                           PrintReceiptCommandResult printSellReceiptResult = PrintReceiptCommandResult.create(result.getData());
                            //Toast.makeText(MyGalery.this, "OK", Toast.LENGTH_LONG).show();

                            break;
                        case ERROR:

                            switch (result.getError().getCode()){

                                case PrintReceiptCommandResult.ERROR_CODE_DATETIME_SYNC_REQUIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нужна синхронизация даты/времени ККМ и терминала", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_SESSION_TIME_EXPIRED:

                                    Toast.makeText(MyGalery.this, "Ошибка: Время сессии превысило 24 часа, закройте смену открыв меню в правом верхнем углу", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_EMAIL_AND_PHONE_ARE_NULL:

                                    Toast.makeText(MyGalery.this, "Ошибка: В интерет чеках поля 'эл.почта' и/или 'телефон' клиента должны быть заполнены", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_KKM_IS_BUSY:

                                    Toast.makeText(MyGalery.this, "Ошибка: ККМ в данный момент выполняет другую операцию", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_AUTHENTICATED_USER:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет авторизованного пользователя на терминале", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_PRINT_DOCUMENT_CREATION_FAILED:

                                    Toast.makeText(MyGalery.this, "Ошибка создания документа для печати", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_PERMISSION:

                                    Toast.makeText(MyGalery.this, "Ошибка: У приложения нет необходимого разрешения (permission)", Toast.LENGTH_LONG).show();

                                    break;
                                case PrintReceiptCommandResult.ERROR_CODE_NO_POSITIONS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_CODE_NO_PAYMENTS:

                                    Toast.makeText(MyGalery.this, "Ошибка: Нет позиций в чеке", Toast.LENGTH_LONG).show();

                                    break;

                                case PrintReceiptCommandResult.ERROR_KKM_IS_NOT_AVAILABLE:

                                    Toast.makeText(MyGalery.this, "Ккм не доступна", Toast.LENGTH_LONG).show();

                                    break;


                                default:break;
                            }

                            break;
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });

    }



    //    //TODO ELECTRON
    public void openReceiptAndEmail(Float decimal) {



        BigDecimal bd = BigDecimal.valueOf(decimal);



        //Создание списка товаров чека
        List<Position> list = new ArrayList<>();
        list.add(
                Position.Builder.newInstance(
                        //UUID позиции
                        UUID.randomUUID().toString(),
                        //UUID товара
                        null,
                        //Наименование
                        "1234",
                        //Наименование единицы измерения
                        "12",
                        //Точность единицы измерения
                        0,
                        //Цена без скидок
                        bd,                        //Количество
                        BigDecimal.ONE
                ).build()
        );
        list.add(
                Position.Builder.newInstance(
                        UUID.randomUUID().toString(),
                        null,
                        "1234",
                        "12",
                        0,
                        bd,
                        BigDecimal.ONE)
                        //Добавление цены с учетом скидки на позицию. Итог = price - priceWithDiscountPosition
                        .setPriceWithDiscountPosition(new BigDecimal(1)).build()
        );
        //Способ оплаты
        HashMap payments = new HashMap<Payment, BigDecimal>();
        payments.put(new Payment(
                UUID.randomUUID().toString(),
                bd,
                null,
                new PaymentPerformer(
                        new PaymentSystem(PaymentType.ELECTRON, "Internet", "12424"),
                        "имя пакета",
                        "название компонента",
                        "app_uuid",
                        "appName"
                ),
                null,
                null,
                null
        ), bd);
        PrintGroup printGroup = new PrintGroup(UUID.randomUUID().toString(),
                PrintGroup.Type.CASH_RECEIPT, null, null, null, null, false, null,null);
        Receipt.PrintReceipt printReceipt = new Receipt.PrintReceipt(
                printGroup,
                list,
                payments,
                new HashMap<Payment, BigDecimal>(), new HashMap<String, BigDecimal>()
        );

        ArrayList<Receipt.PrintReceipt> listDocs = new ArrayList<>();
        listDocs.add(printReceipt);
        //Добавление скидки на чек
        BigDecimal receiptDiscount = new BigDecimal(1);
        new PrintSellReceiptCommand(listDocs, null, "79531979617", "kanzakisan1@gmail.com", receiptDiscount,null,null).process(MyGalery.this, new IntegrationManagerCallback() {
            @Override
            public void run(IntegrationManagerFuture integrationManagerFuture) {
                try {
                    IntegrationManagerFuture.Result result = integrationManagerFuture.getResult();
                    switch (result.getType()) {
                        case OK:
                            PrintReceiptCommandResult printSellReceiptResult = PrintReceiptCommandResult.create(result.getData());
                            Toast.makeText(MyGalery.this, "OK", Toast.LENGTH_LONG).show();
                            break;
                        case ERROR:
                            Toast.makeText(MyGalery.this, result.getError().getMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (IntegrationException e) {
                    e.printStackTrace();
                }
            }
        });
    }





}
